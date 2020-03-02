package org.nyusziful.pictureorganizer.Service;

import org.nyusziful.pictureorganizer.DAL.DAO.FolderDAO;
import org.nyusziful.pictureorganizer.DAL.DAO.FolderDAOImplHib;
import org.nyusziful.pictureorganizer.DAL.Entity.Drive;
import org.nyusziful.pictureorganizer.DAL.Entity.Folder;
import org.nyusziful.pictureorganizer.DTO.FolderDTO;
import org.nyusziful.pictureorganizer.DTO.MediafileDTO;
import org.nyusziful.pictureorganizer.UI.Progress;
import org.nyusziful.pictureorganizer.UI.StaticTools;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

import static org.nyusziful.pictureorganizer.UI.StaticTools.supportedFileType;

public class FolderService {
    private FolderDAO folderDAO;
    private DriveService driveService;

    public FolderService() {
        driveService = new DriveService();
        folderDAO = new FolderDAOImplHib();
    }


    public static String winToDataPath(String path) {
        return path.replaceAll("\\\\", "/");
    }

    public static String winToDataPath(Path path) {
        return path.toString().substring(2).replaceAll("\\\\", "/");
    }

    public static String dataToWinPath(String path) {
        return path.replaceAll("/", "\\\\");
    }

    private Folder getFolder(Drive drive, Path path) {
        return folderDAO.getFolderByPath(drive, path);
    }

    public Folder getFolder(Path path) {
        Drive drive = driveService.getLocalDrive(path.toString().substring(0, 1));
        Folder folder = getFolder(drive, path);
        if (folder == null) {
            folder = new Folder(drive, path);
            persistFolder(folder);
        }
        return folder;
    }

    public FolderDTO getFolderDTO(Drive drive, Path folder) {
        final FolderDTO folderDTO = new FolderDTO();
        folderDTO.driveId = drive.getId();
        folderDTO.letter = driveService.getLocalLetter(drive);
        folderDTO.path = winToDataPath(folder.toString().substring(3));
        return folderDTO;
    }

    public void persistFolder(Folder folder) {
        persistFolder(Collections.singleton(folder));
    }

    public void persistFolder(Collection<Folder> folders) {
        for (Folder folder: folders) {
            folderDAO.persist(folder);
        }
    }

    public static Set<Path> getMediaFoldersRec(Path path, Progress progress) {
        Set<Path> mediafolders = new HashSet<>();
        int fileSizeCountTotal = 0;
        try {
            Files.walkFileTree (path, new SimpleFileVisitor<Path>() {
                @Override public FileVisitResult
                visitFile(Path filePath, BasicFileAttributes attrs) throws IOException {
                    if (!attrs.isDirectory() && attrs.isRegularFile() && supportedFileType(filePath.getFileName().toString())) {
                        progress.setGoal(progress.getGoal()+1);
//                        fileSizeCountTotal += attrs.size();
//                        fileCountTotal++;
                        mediafolders.add(filePath.getParent());
                    }
                    return FileVisitResult.CONTINUE;
                }

                @Override public FileVisitResult
                visitFileFailed(Path file, IOException exc) {
                    StaticTools.errorOut(file.toString(), exc);
                    // Skip folders that can't be traversed
                    return FileVisitResult.CONTINUE;
                }

                @Override public FileVisitResult
                postVisitDirectory (Path dir, IOException exc) {
                    if (exc != null)
                        StaticTools.errorOut(dir.toString(), exc);
                    // Ignore errors traversing a folder
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            throw new AssertionError ("walkFileTree will not throw IOException if the FileVisitor does not");
        }
        return mediafolders;
    }


}
