package org.nyusziful.Main;

import org.nyusziful.pictureorganizer.Service.Hash.JPGHash;
import org.nyusziful.pictureorganizer.Service.Hash.MediaFileHash;
import org.nyusziful.pictureorganizer.DTO.ImageDTO;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.EnumSet;
import java.util.Set;

/**
 * Watches a directory for newly created JPEG files and checks their import status.
 *
 * <p>For each new JPEG detected the service computes its canonical hash and checks:
 * <ol>
 *   <li>Whether the file is already recorded in the database.</li>
 *   <li>Whether an EXIF backup (.bak) file exists next to it.</li>
 *   <li>Whether a canonical hash is already embedded inside the file.</li>
 * </ol>
 * The result is reported to the registered {@link FileEventListener}.
 *
 * <p>Watching can be temporarily suspended with {@link #pause()} and restarted with
 * {@link #resume()} without stopping the background thread.
 */
public class FolderWatchService {

    /** Flags describing what is still missing / already done for a detected file. */
    public enum FileStatus {
        NOT_IN_DB,
        EXIF_BACKUP_MISSING,
        HASH_MISSING
    }

    public interface FileEventListener {
        /**
         * Called (from the watch thread) when a new JPEG is detected.
         *
         * @param file   the detected file
         * @param status set of conditions that still need to be fulfilled (may be empty)
         */
        void onNewFile(File file, Set<FileStatus> status);
    }

    private final Path watchDir;
    private final HashChecker hashChecker;

    private volatile FileEventListener listener;
    private volatile boolean paused = false;
    private volatile boolean running = false;

    private WatchService watchService;
    private Thread watchThread;

    public FolderWatchService(Path watchDir, HashChecker hashChecker) {
        this.watchDir = watchDir;
        this.hashChecker = hashChecker;
    }

    public void setFileEventListener(FileEventListener listener) {
        this.listener = listener;
    }

    public void start() throws IOException {
        if (running) return;
        watchService = FileSystems.getDefault().newWatchService();
        watchDir.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);
        running = true;
        watchThread = new Thread(this::watchLoop, "FolderWatchService");
        watchThread.setDaemon(true);
        watchThread.start();
    }

    public void stop() {
        running = false;
        try {
            if (watchService != null) watchService.close();
        } catch (IOException ignored) {
        }
        if (watchThread != null) {
            watchThread.interrupt();
        }
    }

    /** Temporarily ignore file-system events without stopping the background thread. */
    public void pause() {
        paused = true;
    }

    /** Re-enable event processing after a {@link #pause()}. */
    public void resume() {
        paused = false;
    }

    public boolean isPaused() {
        return paused;
    }

    // -------------------------------------------------------------------------

    private void watchLoop() {
        while (running) {
            WatchKey key;
            try {
                key = watchService.take();
            } catch (InterruptedException | ClosedWatchServiceException e) {
                break;
            }
            for (WatchEvent<?> event : key.pollEvents()) {
                if (!paused && event.kind() == StandardWatchEventKinds.ENTRY_CREATE) {
                    @SuppressWarnings("unchecked")
                    Path created = watchDir.resolve(((WatchEvent<Path>) event).context());
                    File file = created.toFile();
                    if (isJpeg(file)) {
                        handleNewFile(file);
                    }
                }
            }
            if (!key.reset()) break;
        }
    }

    private void handleNewFile(File file) {
        // Small delay to let the OS finish writing before we read the file
        try { Thread.sleep(200); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }

        Set<FileStatus> status = EnumSet.noneOf(FileStatus.class);

        ImageDTO dto = MediaFileHash.getHash(file);
        String canonicalHash = (dto != null) ? dto.hash : MediaFileHash.EMPTYHASH;

        // 1. Check database
        if (!hashChecker.isInDatabase(canonicalHash)) {
            status.add(FileStatus.NOT_IN_DB);
        }

        // 2. Check EXIF backup
        File bakFile = new File(file.getPath() + ".bak");
        if (!bakFile.exists()) {
            status.add(FileStatus.EXIF_BACKUP_MISSING);
        }

        // 3. Check canonical hash embedded in file
        if (!JPGHash.hasCanonicalHash(file)) {
            status.add(FileStatus.HASH_MISSING);
        }

        FileEventListener l = listener;
        if (l != null) {
            l.onNewFile(file, status);
        }
    }

    private static boolean isJpeg(File file) {
        if (!file.isFile()) return false;
        String name = file.getName().toLowerCase();
        return name.endsWith(".jpg") || name.endsWith(".jpeg");
    }
}
