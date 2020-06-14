package org.nyusziful.pictureorganizer.Service;

import org.nyusziful.pictureorganizer.DAL.DAO.MediafileDAO;
import org.nyusziful.pictureorganizer.DAL.DAO.MediafileDAOImplHib;
import org.nyusziful.pictureorganizer.DAL.Entity.*;
import org.nyusziful.pictureorganizer.DTO.ImageDTO;
import org.nyusziful.pictureorganizer.DTO.MediafileDTO;
import org.nyusziful.pictureorganizer.DTO.Meta;
import org.nyusziful.pictureorganizer.Main.CommonProperties;
import org.nyusziful.pictureorganizer.Service.ExifUtils.ExifService;
import org.nyusziful.pictureorganizer.Service.Rename.RenameService;
import org.nyusziful.pictureorganizer.UI.Contoller.MainController;
import org.nyusziful.pictureorganizer.UI.Model.TableViewMediaFile;
import org.nyusziful.pictureorganizer.UI.ProgressLeakingTask;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.util.*;

import static org.nyusziful.pictureorganizer.Service.ExifUtils.ExifService.createXmp;
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

/*    public List<MediaFile> getMediafiles() {
        List<MediaFile> getMediafiles = mediafileDAO.getAll();
        return getMediafiles;
    }*/

    /*
        public MediafileDTO getMediafile(String name) {
            openConnection();
            MediafileDTO getMediafile = mediafileDAO.getByName(name);
            closeConnection();
            return getMediafile;
        }
    */
    public MediaFile getMediafile(Drive drive, Path path, boolean withImega) {
        return mediafileDAO.getByFile(drive, path, withImega);
    }

    public MediaFile getMediaFile(Path path, boolean withImega) {
        Drive localDrive = driveService.getLocalDrive(path);
        return getMediafile(localDrive, path, withImega);
    }

    public MediaFile getMediaFile(MediafileDTO mediafileDTO, boolean withImega) {
        Path path = Paths.get(mediafileDTO.abolutePath);
        final MediaFile mediaFile = getMediaFile(path, withImega);
        mediaFile.setOriginal(mediafileDTO.isOriginal);
        return mediaFile;
    }

    public MediafileDTO getMediafileDTO(MediaFile mediafile) {
        MediafileDTO mediafileDTO = new MediafileDTO();
        if (mediafile != null) {
            if (mediafile.getImage() != null) mediafileDTO.fileHash = mediafile.getImage().getHash();
            mediafileDTO.abolutePath = mediafile.getFilePath().toString();
            mediafileDTO.filename = mediafile.getFilename();
            mediafileDTO.dateMod = mediafile.getDateMod();
            mediafileDTO.fileHash = mediafile.getFilehash();
            mediafileDTO.size = mediafile.getSize();
            mediafileDTO.isOriginal = Boolean.TRUE.equals(mediafile.isOriginal());
        }
        return mediafileDTO;
    }

    public static MediafileDTO getMediafileDTO(File file) {
        MediafileDTO mediafileDTO = new MediafileDTO();
        if (file != null) {
            mediafileDTO.abolutePath = file.getAbsolutePath();
            mediafileDTO.filename = file.getName();
        }
        return mediafileDTO;
    }

    public void saveMediaFile(MediaFile mediafile) {
        saveMediaFile(mediafile, false);
    }

    private void saveMediaFile(MediaFile mediafile, boolean batch) {
        saveMediaFiles(Collections.singleton(mediafile), batch);
    }

    public void saveMediaFiles(Collection<? extends MediaFile> mediaFiles) {
        saveMediaFiles(mediaFiles, false);
    }

    public void saveMediaFiles(Collection<? extends MediaFile> mediaFiles, boolean batch) {
        for (MediaFile file : mediaFiles) {
            if (file.getId() > -1)
                mediafileDAO.merge(file, batch);
            else
                mediafileDAO.persist(file, batch);
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

    public List<MediaFile> getMediaFilesFromPath(Path path, boolean recursive) {
        final Drive localDrive = driveService.getLocalDrive(path.toString().substring(0, 1));
        return recursive ?  mediafileDAO.getByPathRec(localDrive, path): mediafileDAO.getByPath(localDrive, path);
    }

    public int getVersionNumber(Image image) {
        return 0; //TODO implement properly
/*
        if (image == null) return -1;
        if (image.getParent() == null) return 0;
        return getVersionNumber(image.getParent()) + 1;
 */
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
            if (mediaFile instanceof VideoMediaFile)
                image.setDuration(((VideoMediaFile)mediaFile).getDuration());
            return true;
        } else {
            if (!origFileName.equals(image.getOriginalFilename()) || !mediaFile.getFilehash().equals(image.getOriginalFileHash()) || (!mediaFile.getDateStored().isEqual(image.getDateTaken()) && !mediaFile.getDateStored().isEqual(image.getActualDate()))) {
                throw new Exception("Mismatch");
            }
        }
        return false;
    }

/*    public Set<MediaFile> readMediaFilesFromFolderRecursive(Path path, boolean original, boolean force, ZoneId zone, Set<MediaFile> filesFailing) {
        ProgressDTO progress = new ProgressDTO();
        progress.reset();
        Set<MediaFile> mediaFiles = new HashSet<>();
        for (Path subpath : FolderService.getMediaFoldersRec(path, progress)) {
            mediaFiles.addAll(readMediaFilesFromFolder(subpath, original, force, zone, filesFailing, progress));
        }
        System.out.println("Failed Files:");
        filesFailing.forEach(file -> System.out.println(file.getFilePath()));
        return mediaFiles;
    }
 */
    public void syncFolder(Path target, Path source) {
        final Drive targetDrive = driveService.getLocalDrive(target.toString().substring(0, 1));
        if (targetDrive == null) return;
        List<MediaFile> filesInFolderFromDB = getMediaFilesFromPath(source, true);
        Set<MediaFile> toSave = new HashSet<>();
        for (MediaFile sourceMediaFile : filesInFolderFromDB) {
            List<MediaFile> filesInTarget = mediafileDAO.getMediaFilesFromPathOfImage(sourceMediaFile.getImage(), targetDrive, target);
            if (filesInTarget.size() == 0) continue;
            final String subFolder = sourceMediaFile.getFilePath().toString().substring(source.toString().length());
            if (filesInTarget.size() == 1) {
                Path newPath = Paths.get(target.toString()+subFolder);
                MediaFile mediaFileToMove = filesInTarget.get(0);
                renameMediaFile(getMediafileDTO(mediaFileToMove), newPath, TableViewMediaFile.WriteMethod.MOVE, false);
            } else if (filesInTarget.size() > 1) {
                Path newPath = Paths.get(target.toString()+"\\conflict"+subFolder);
                for (MediaFile mediaFileToMove : filesInTarget) {
                    renameMediaFile(getMediafileDTO(mediaFileToMove), newPath, TableViewMediaFile.WriteMethod.MOVE, false);
                }
            }
        }
    }

    public Set<MediafileDTO> reOrganizeFilesInSubFolders(Path path, ProgressLeakingTask progress) {
        Set<MediafileDTO> result = new HashSet<>();
        if (driveService.getLocalDrive(path.toString().substring(0, 1)) == null) return result;
        List<MediaFile> filesInFolderFromDB = getMediaFilesFromPath(path, true);
        HashMap<String, MediaFile> pathToMediaFile = new HashMap<>();
        filesInFolderFromDB.forEach(f -> pathToMediaFile.put(f.getFilePath().toString().toLowerCase(), f));
        HashSet<Path> paths = new HashSet<>();
        try {
            Files.find(path, Integer.MAX_VALUE,
                    (filePath, fileAttr) -> fileAttr.isRegularFile() && supportedFileType(filePath.toFile()))
                    .forEach(paths::add);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int progressing = 0;
        progress.updateProgress(progressing, paths.size());
        HashSet<File> unknownFiles = new HashSet<>();
        //remove exact matches
        for (Path path1 : paths) {
             MediaFile found = null;
/*            for (MediaFile mediaFile : filesInFolderFromDB) {
                if (mediaFile.getFilePath().equals(path1)) {
                    found = mediaFile;
                    break;
                }
            }
*/
            found = pathToMediaFile.get(path1.toString().toLowerCase());
            if (found != null) {
                filesInFolderFromDB.remove(found);
                pathToMediaFile.remove(path1.toString().toLowerCase());
            } else {
                unknownFiles.add(path1.toFile());
            }
            progressing++;
            progress.updateProgress(progressing, paths.size() + unknownFiles.size());
        }
        //pick the first matching
        Set<MediaFile> toSave = new HashSet<>();
        for (File unknownFile : unknownFiles) {
            System.out.println(unknownFile);
            progressing++;
            progress.updateProgress(progressing, paths.size() + unknownFiles.size());
            MediaFile mediaFile = null;
            String fileName = unknownFile.getName().toLowerCase();
            BasicFileAttributes attrs = org.nyusziful.pictureorganizer.Service.Rename.StaticTools.getFileAttributes(unknownFile);
            final Timestamp dateMod = new Timestamp(attrs.lastModifiedTime().toMillis());
            for (MediaFile potentialMediaFileFromList : filesInFolderFromDB) {
                if (fileName.equals(potentialMediaFileFromList.getFilename().toLowerCase()) && potentialMediaFileFromList.getSize() == attrs.size() && potentialMediaFileFromList.getDateMod().toInstant().toEpochMilli() == dateMod.toInstant().toEpochMilli()) {
                    mediaFile = potentialMediaFileFromList;
                    break;
                }
            }
            if (mediaFile == null) {
                result.add(getMediafileDTO(readMediaFile(unknownFile)));
            } else {
                mediaFile.updateFolder(folderService.getFolder(unknownFile.getParentFile().toPath()));
                toSave.add(mediaFile);
                filesInFolderFromDB.remove(mediaFile);
            }
        }
        saveMediaFiles(toSave);
        deleteMediaFiles(filesInFolderFromDB);
        return result;
    }

    public Set<MediafileDTO> readMediaFilesFromFolder(Path path, boolean original, boolean force, ZoneId zone, String notes, MainController.ReadTask progress) {
        Set<MediaFile> mediaFiles = new HashSet<>();
        Set<MediafileDTO> result = new HashSet<>();
        Drive drive = driveService.getLocalDrive(path.toString().substring(0, 1));
        if (drive == null) return result;
        Folder folder = folderService.getFolder(path);
        List<MediaFile> filesInFolderFromDB = getMediaFilesFromPath(path, false);
        HashMap<String, MediaFile> fileSet = new HashMap<>();
        for (MediaFile file : filesInFolderFromDB) {
            fileSet.put(file.getFilePath().toString().toLowerCase(), file);
        }
        final File[] files = path.toFile().listFiles();
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            MediaFile mediaFile = readMediaFile(file, fileSet, folder, original, force, zone, notes);
            if (mediaFile != null) {
                mediaFiles.add(mediaFile);
                if (progress != null) progress.increaseProgress();
            }
        }
        filesInFolderFromDB.removeAll(mediaFiles);
        deleteMediaFiles(filesInFolderFromDB);
        mediaFiles.remove(null);
        mediaFiles.forEach(mf -> {result.add(getMediafileDTO(mf));});
        return result;
    }

    private MediaFile readMediaFile(File file) {
        MediaFile result = mediafileDAO.getByFile(driveService.getLocalDrive(file.toPath()), file.toPath(), false);
        return result != null ? result : readMediaFile(file, null, folderService.getFolder(file.getParentFile().toPath()), false, false, CommonProperties.getInstance().getZone(), "");
    }

    private MediaFile readMediaFile(File file, HashMap<String, MediaFile> filesInFolderMap, Folder folder, boolean original, boolean force, ZoneId zone, String notes) {
        if (filesInFolderMap == null) filesInFolderMap = new HashMap<>();
        if (notes == null) notes = "";
        Path filePath = file.toPath();
        BasicFileAttributes attrs = org.nyusziful.pictureorganizer.Service.Rename.StaticTools.getFileAttributes(file);
        MediaFile actFile = null;
        if (!attrs.isDirectory() && attrs.isRegularFile() && supportedFileType(filePath.getFileName().toString())) {
            Boolean fileOriginal = original;
            Set<String> whatToSave = new HashSet<>();
            actFile = filesInFolderMap.get(filePath.toString().toLowerCase());
            assert folder != null;
            final Timestamp dateMod = new Timestamp(attrs.lastModifiedTime().toMillis());
            dateMod.setNanos(0);
            final long fileSize = attrs.size();
            if (actFile == null) {
                final String name = filePath.getFileName().toString();
                if (supportedRAWFileType(name)) {
                    actFile = new RAWMediaFile(folder, filePath, fileSize, dateMod, original);
                } else if (supportedJPGFileType(name)) {
                    actFile = new JPGMediaFile(folder, filePath, fileSize, dateMod, original);
                } else if (supportedVideoFileType(name)) {
                    actFile = new VideoMediaFile(folder, filePath, fileSize, dateMod, original);
                } else {
                    actFile = new MediaFile(folder, filePath, fileSize, dateMod, original);
                }
            } else {
                fileOriginal = !Boolean.FALSE.equals(fileOriginal) && !Boolean.FALSE.equals(actFile.isOriginal()) && !(fileOriginal == null && actFile.isOriginal() == null);
                if (!fileOriginal.equals(actFile.isOriginal())) {
                    actFile.setOriginal(fileOriginal);
                    whatToSave.add("file");
                }
            }
            if (force || actFile.getId() < 0 || actFile.getSize() != fileSize || actFile.getDateMod().compareTo(dateMod) != 0 || (actFile instanceof VideoMediaFile && ((VideoMediaFile)actFile).getDuration() == 0)) {
                actFile.setDateMod(dateMod);
                actFile.setSize(fileSize);
                ImageDTO imageDTO = getHash(filePath.toFile());
                Image image;
                image = imageService.getImage(imageDTO, true);
                if (image == null) {
                    image = new Image(imageDTO.hash, imageDTO.type);
                    whatToSave.add("image");
                }
                final String fullHash = getFullHash(filePath.toFile());
                Meta meta = ExifService.readMeta(filePath.toFile(), zone);
                if (actFile instanceof JPGMediaFile) {
                    ((JPGMediaFile) actFile).setWithQuality(meta.quality);
                }
                if (actFile instanceof VideoMediaFile) {
                    ((VideoMediaFile) actFile).setDuration(meta.duration);
                }
                actFile.setDateStored(meta.date);
                actFile.setImage(image);
                actFile.setFilehash(fullHash);
                whatToSave.add("file");
            }
            if (Boolean.TRUE.equals(fileOriginal)) {
                try {
                    final boolean updated = updateOriginalImage(actFile);
                    if (updated) {
                        whatToSave.add("file");
                        whatToSave.add("image");
                    }
                } catch (Exception e) {
                    notes += actFile + "\n";
                }
            }
            if (whatToSave.contains("image")) {
                imageService.saveImage(actFile.getImage(), true);
            }
            if (whatToSave.contains("file")) {
                saveMediaFile(actFile, true);
                filesInFolderMap.put(actFile.getFilePath().toString().toLowerCase(), actFile);
            }
            mediafileDAO.close();
        }
        return actFile;
    }


    public String getMediaFileName(MediafileDTO mediafileDTO, String nameVersion) {
        MediaFile mediaFile = getMediaFile(mediafileDTO, true);
        if (mediaFile == null) return mediafileDTO.filename;
        return RenameService.getName(mediaFile, nameVersion, Integer.toString(getVersionNumber(mediaFile.getImage())));
    }

    public boolean renameMediaFile(MediafileDTO mediafileDTO, Path newPath, TableViewMediaFile.WriteMethod writeMethod, boolean overwrite) {
        MediaFile mediaFile = getMediaFile(mediafileDTO, true);
        final boolean rename = RenameService.write(mediaFile.getFilePath(), newPath, writeMethod, overwrite);
        if (overwrite || rename) {
            Folder folder = folderService.getFolder(newPath.getParent());
            if (TableViewMediaFile.WriteMethod.COPY.equals(writeMethod)) {
                try {
                    mediaFile = (MediaFile) mediaFile.clone();
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
//                mediaFile = readMediaFile(newPath.toFile(), null, folder, mediaFile.isOriginal(), false, mediaFile.getDateStored().getZone(), null);
            }
            mediaFile.moveFile(folder, newPath);

            if (mediaFile instanceof JPGMediaFile && mediaFile.isOriginal() && !((JPGMediaFile) mediaFile).isExifbackup()) {
                ((JPGMediaFile) mediaFile).addExifbackup();
            }
            if (mediaFile instanceof RAWMediaFile && !((RAWMediaFile) mediaFile).isXMPattached()) {
                ((RAWMediaFile) mediaFile).setXMPattached(createXmp(mediaFile.getFilePath().toFile()) != null);
            }

            saveMediaFile(mediaFile);
        }

        return rename;
    }
}