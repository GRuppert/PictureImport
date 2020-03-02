package org.nyusziful.pictureorganizer.Service;

import org.nyusziful.pictureorganizer.DAL.DAO.MediafileDAO;
import org.nyusziful.pictureorganizer.DAL.DAO.MediafileDAOImplHib;
import org.nyusziful.pictureorganizer.DAL.Entity.*;
import org.nyusziful.pictureorganizer.DTO.ImageDTO;
import org.nyusziful.pictureorganizer.DTO.MediafileDTO;
import org.nyusziful.pictureorganizer.DTO.Meta;
import org.nyusziful.pictureorganizer.Service.ExifUtils.ExifService;
import org.nyusziful.pictureorganizer.Service.Rename.RenameService;
import org.nyusziful.pictureorganizer.UI.Model.TableViewMediaFile;
import org.nyusziful.pictureorganizer.UI.Progress;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static org.nyusziful.pictureorganizer.Service.ExifUtils.ExifService.createXmp;
import static org.nyusziful.pictureorganizer.Service.FolderService.dataToWinPath;
import static org.nyusziful.pictureorganizer.Service.Hash.MediaFileHash.getFullHash;
import static org.nyusziful.pictureorganizer.Service.Hash.MediaFileHash.getHash;
import static org.nyusziful.pictureorganizer.Service.Rename.FileNameFactory.getV;
import static org.nyusziful.pictureorganizer.UI.StaticTools.*;

public class MediafileService {
    private MediafileDAO mediafileDAO;
    private DriveService driveService;
    private FolderService folderService;
    private ImageService imageService;

    public MediafileService() {
        driveService = new DriveService();
        folderService = new FolderService();
        imageService = new ImageService();
        mediafileDAO = new MediafileDAOImplHib();
    }

    public List<MediaFile> getMediafiles() {
        List<MediaFile> getMediafiles = mediafileDAO.getAll();
        return getMediafiles;
    }

    /*
        public MediafileDTO getMediafile(String name) {
            openConnection();
            MediafileDTO getMediafile = mediafileDAO.getByName(name);
            closeConnection();
            return getMediafile;
        }
    */
    public MediaFile getMediafile(Drive drive, Path path) {
        MediaFile getMediafile = mediafileDAO.getByFile(drive, path);
        return getMediafile;
    }

    public MediafileDTO getMediafileDTO(MediaFile mediafile) {
        MediafileDTO mediafileDTO = new MediafileDTO();
        if (mediafile != null) {
            if (mediafile.getImage() != null) mediafileDTO.driveId = mediafile.getImage().getId();
            if (mediafile.getFolder() != null)
                mediafileDTO.path = FolderService.winToDataPath(mediafile.getFolder().getJavaPath());
            mediafileDTO.filename = mediafile.getFilename();
            mediafileDTO.dateMod = mediafile.getDateMod();
            mediafileDTO.filehash = mediafile.getFilehash();
            mediafileDTO.size = mediafile.getSize();
        }
        return mediafileDTO;
    }

    public File getFile(MediafileDTO mediafile) {
        return new File(mediafile.letter + ":" + dataToWinPath(mediafile.path) + "\\" + mediafile.filename);
    }

    public void persistMediaFiles(MediaFile mediafile) {
        persistMediaFiles(Collections.singleton(mediafile));
    }

    public void persistMediaFiles(Collection<? extends MediaFile> mediaFiles) {
        for (MediaFile file : mediaFiles) {
            if (file.getId() > -1)
                mediafileDAO.merge(file);
            else
                mediafileDAO.persist(file);
        }
    }

    public void deleteMediaFiles(Collection<? extends MediaFile> mediaFiles) {
        for (MediaFile file : mediaFiles) {
            if (file.getId() > -1)
                mediafileDAO.delete(file);
        }
    }

    public void updateMediafile(MediaFile Mediafile) {
        mediafileDAO.merge(Mediafile);
    }

    public static void main(String[] args) {
        final MediafileService mediafileService = new MediafileService();
        MediaFile mediafileDTO = new MediaFile(

        );
        final Drive driveDTO = new Drive();
        driveDTO.setId(100);
/*        MediafileDTO mediafile = mediafileService.getMediafile("001ccb41c7eb77075051f3febdcafe71");
        System.out.println(mediafile);
        mediafileService.updateMediafile(mediafile);*/
    }

    public MediaFile getMediaFile(Path path) {
        final Drive localDrive = driveService.getLocalDrive(path.toString().substring(0, 1));
        return mediafileDAO.getByFile(localDrive, path);
    }

    public List<MediaFile> getMediaFilesFromPath(Path path) {
        final Drive localDrive = driveService.getLocalDrive(path.toString().substring(0, 1));
        return mediafileDAO.getByPath(localDrive, path);
    }

    public int getVersionNumber(Image image) {
        if (image == null) return -1;
        if (image.getParent() == null) return 0;
        return getVersionNumber(image.getParent()) + 1;
    }

    public void flush() {
        mediafileDAO.flush();
    }

    /**
     * @param mediaFile
     * @return true if data has been written into the image entity, which has to be persisted
     * @throws Exception if the data in the "original" media file does not match what was already saved into the image
     */
    public boolean updateOriginalImage(MediaFile mediaFile) throws Exception {
        final Image image = mediaFile.getImage();
        final Meta metaOrig = getV(mediaFile.getFilename());
        String origFileName = (metaOrig != null && metaOrig.originalFilename != null) ? metaOrig.originalFilename : mediaFile.getFilename();
        if (image.getOriginalFileHash() == null && image.getDateTaken() == null && image.getOriginalFilename() == null) {
            image.setOriginalFileHash(mediaFile.getFilehash());
            image.setDateTaken(mediaFile.getDateStored());
            image.setOriginalFilename(origFileName);
            mediaFile.setOriginal(true);
            return true;
        } else {
            if (!origFileName.equals(image.getOriginalFilename()) || !mediaFile.getFilehash().equals(image.getOriginalFileHash()) || (!mediaFile.getDateStored().isEqual(image.getDateTaken()) && !mediaFile.getDateStored().isEqual(image.getDateCorrected()))) {
                throw new Exception("Mismatch");
            }
        }
        return false;
    }

    public Set<MediaFile> readMediaFilesFromFolderRecursive(Path path, boolean original, boolean force, ZoneId zone, Set<MediaFile> filesFailing) {
        Progress progress = Progress.getInstance();
        progress.reset();
        Set<MediaFile> mediaFiles = new HashSet<>();
        for (Path subpath : FolderService.getMediaFoldersRec(path, progress)) {
            mediaFiles.addAll(readMediaFilesFromFolder(subpath, original, force, zone, filesFailing));
            progress.increaseProgress();
        }
        System.out.println("Failed Files:");
        filesFailing.forEach(file -> System.out.println(file.getFilePath()));
        return mediaFiles;
    }

    public Set<MediaFile> readMediaFilesFromFolder(Path path, boolean original, boolean force, ZoneId zone, Set<MediaFile> filesFailing) {
        Set<MediaFile> mediaFiles = new HashSet<>();
        Drive drive = driveService.getLocalDrive(path.toString().substring(0, 1));
        Folder folder = folderService.getFolder(path);
        List<MediaFile> filesInFolderFromDB = getMediaFilesFromPath(path);
        HashMap<String, MediaFile> fileSet = new HashMap<>();
        for (MediaFile file : filesInFolderFromDB) {
            fileSet.put(file.getFilePath().toString().toLowerCase(), file);
        }
        final File[] files = path.toFile().listFiles();
        for (File file : files) {
            MediaFile mediaFile = readMediaFile(file, fileSet, folder, original, force, zone, filesFailing);
            mediaFiles.add(mediaFile);
        }
        filesInFolderFromDB.removeAll(mediaFiles);
        deleteMediaFiles(filesInFolderFromDB);
        mediaFiles.remove(null);
        return mediaFiles;
    }

    public MediaFile readMediaFile(File file, HashMap<String, MediaFile> filesInFolderMap, Folder folder, boolean original, boolean force, ZoneId zone, Set<MediaFile> filesFailing) {
        if (filesInFolderMap == null) filesInFolderMap = new HashMap<>();
        Path filePath = file.toPath();
        BasicFileAttributes attrs = null;
        try {
            attrs = Files.readAttributes(filePath, BasicFileAttributes.class);
        } catch (IOException e) {
            return null;
        }
        MediaFile actFile = null;
        if (!attrs.isDirectory() && attrs.isRegularFile() && supportedFileType(filePath.getFileName().toString())) {
            Boolean fileOriginal = original;
            boolean fileToSave = false;
            boolean imageToSave = false;
            actFile = filesInFolderMap.get(filePath.toString().toLowerCase());
            assert folder != null;
            final Timestamp dateMod = new Timestamp(attrs.lastModifiedTime().toMillis());
            dateMod.setNanos(0);
            final long fileSize = attrs.size();
            if (actFile == null) {
                final String name = filePath.getFileName().toString();
                if (supportedRAWFileType(name)) {
                    actFile = new RAWMediaFile(folder, filePath, fileSize, dateMod, original);
                    if (!((RAWMediaFile) actFile).isXMPattached()) {
                        ((RAWMediaFile) actFile).setXMPattached(createXmp(filePath.toFile()) != null);
                    }
                } else if (supportedJPGFileType(name)) {
                    actFile = new JPGMediaFile(folder, filePath, fileSize, dateMod, original);
                } else {
                    actFile = new MediaFile(folder, filePath, fileSize, dateMod, original);
                }
            } else {
                fileOriginal = !Boolean.FALSE.equals(fileOriginal) && !Boolean.FALSE.equals(actFile.isOriginal()) && !(fileOriginal == null && actFile.isOriginal() == null);
                if (fileOriginal) {
                    if (actFile instanceof JPGMediaFile && !((JPGMediaFile) actFile).isExifbackup()) {
                        if (((JPGMediaFile) actFile).addExifbackup()) fileToSave = true;
                    }
                }
            }
            if (force || actFile.getId() < 0 || actFile.getSize() != fileSize || actFile.getDateMod().compareTo(dateMod) != 0) {
                actFile.setDateMod(dateMod);
                actFile.setSize(fileSize);
                ImageDTO imageDTO = getHash(filePath.toFile());
                Image image;
                image = imageService.getImage(imageDTO);
                if (image == null) {
                    image = new Image(imageDTO.hash, imageDTO.type);
                    imageToSave = true;
                }
                final String fullHash = getFullHash(filePath.toFile());
                Meta meta = ExifService.readMeta(filePath.toFile(), zone);
                if (actFile instanceof JPGMediaFile) {
                    ((JPGMediaFile) actFile).setWithQuality(meta.quality);
                }
                actFile.setDateStored(meta.date);
                actFile.setImage(image);
                actFile.setFilehash(fullHash);
                fileToSave = true;
            }
            if (Boolean.TRUE.equals(fileOriginal)) {
                try {
                    final boolean updated = updateOriginalImage(actFile);
                    fileToSave = fileToSave || updated;
                    if (updated) imageToSave = true;
                } catch (Exception e) {
                    filesFailing.add(actFile);
                }
            }
            if (actFile instanceof RAWMediaFile && !((RAWMediaFile) actFile).isXMPattached()) {
                createXmp(filePath.toFile());
            }
            if (imageToSave) {
                imageService.persistImage(actFile.getImage());
            }
            if (fileToSave) {
                persistMediaFiles(actFile);
                filesInFolderMap.put(actFile.getFilePath().toString().toLowerCase(), actFile);
            }
        }
        return actFile;
    }

    public String getMediaFileName(MediaFile mediaFile, String nameVersion) {
        return RenameService.getName(mediaFile, nameVersion, getVersionNumber(mediaFile.getImage()));
    }

    public boolean renameMediaFile(MediaFile mediaFile, Path newPath, TableViewMediaFile.WriteMethod writeMethod, boolean overwrite) {
        final boolean rename = RenameService.write(mediaFile.getFilePath(), newPath, writeMethod, overwrite);
        if (rename) {
            mediaFile.setFilename(newPath.getFileName().toString());
            persistMediaFiles(mediaFile);
        }

        return rename;
    }
}