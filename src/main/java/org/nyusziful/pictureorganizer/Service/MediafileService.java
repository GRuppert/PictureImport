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
import java.util.concurrent.TimeUnit;

import static java.lang.Boolean.TRUE;
import static org.nyusziful.pictureorganizer.Service.ExifUtils.ExifService.createXmp;
import static org.nyusziful.pictureorganizer.Service.Hash.MediaFileHash.getHash;
import static org.nyusziful.pictureorganizer.Service.Rename.FileNameFactory.getV;
import static org.nyusziful.pictureorganizer.UI.StaticTools.*;

public class MediafileService {
    private MediafileDAO mediafileDAO;
    private DriveService driveService;
    private FolderService folderService;
    private ImageService imageService;
    private static MediafileService instance;

    private MediafileService() {
        driveService = new DriveService();
        folderService = new FolderService();
        imageService = new ImageService();
        mediafileDAO = new MediafileDAOImplHib();
    }

    public static MediafileService getInstance() {
        if (instance == null) {
            instance = new MediafileService();
        }
        return instance;
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

    public MediaFile getMediaFile(Path path, boolean withImage) {
        Drive localDrive = driveService.getLocalDrive(path);
        return getMediafile(localDrive, path, withImage);
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
            mediafileDTO.isOriginal = TRUE.equals(mediafile.isOriginal());
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
//        mediafileService.syncFolder(Paths.get("g:\\Pictures\\Photos\\DBSaved\\"), Paths.get("h:\\Képek\\Photos\\Processed"));
    }

    public List<MediaFile> getMediaFilesFromPath(Path path, boolean recursive) {
        final Drive localDrive = driveService.getLocalDrive(path.toString().substring(0, 1));
        return recursive ?  mediafileDAO.getByPathRec(localDrive, path, true): mediafileDAO.getByPath(localDrive, path, true);
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

    public void close() {
        mediafileDAO.close();
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
            if (mediaFile instanceof JPGMediaFile)
                image.setExif(((JPGMediaFile)mediaFile).getExif());
            if (mediaFile instanceof VideoMediaFile)
                image.setDuration(((VideoMediaFile)mediaFile).getDuration());
            return true;
        } else {
            if (!origFileName.equals(image.getOriginalFilename()) || !mediaFile.getFilehash().equals(image.getOriginalFileHash()) || ((image.getDateTaken() == null || mediaFile.getDateStored().compareTo(image.getDateTaken()) != 0) && (image.getActualDate() == null || mediaFile.getDateStored().compareTo(image.getActualDate()) != 0 ))) {
                throw new Exception("Mismatch");
            }
        }
        return false;
    }

    public boolean updateBestimateImage(MediaFile mediaFile) throws Exception {
/*        final Image image = mediaFile.getImage();
        final Meta metaOrig = getV(mediaFile.getFilename());
        String origFileName = (metaOrig != null && metaOrig.originalFilename != null) ? metaOrig.originalFilename : mediaFile.getFilename();
        if (image.getOriginalFileHash() == null && image.getDateTaken() == null && image.getOriginalFilename() == null
        && image.getBestimateFileHash() == null && image.getDateCorrected() == null && image.getBestimateFilename() == null) {
            image.setBestimateFileHash(mediaFile.getFilehash());
            image.setDateCorrected(mediaFile.getDateStored());
            image.setBestimateFilename(origFileName);
            return true;
        } else {
            if (!(origFileName.equals(image.getBestimateFilename()) || origFileName.equals(image.getOriginalFilename()))
                    || !(mediaFile.getFilehash().equals(image.getOriginalFileHash()) || mediaFile.getFilehash().equals(image.getBestimateFileHash()))
                    || ((image.getDateTaken() == null || mediaFile.getDateStored().compareTo(image.getDateTaken()) != 0) && (image.getActualDate() == null || mediaFile.getDateStored().compareTo(image.getActualDate()) != 0 ))) {
                throw new Exception("Mismatch");
            }
        }*/
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
    public Boolean syncFolder(Path source, Path target, ProgressLeakingTask progress) {
        final Drive targetDrive = driveService.getLocalDrive(target.toString().substring(0, 1));
        if (targetDrive == null) return false;
        List<MediaFile> filesInFolderFromDB = getMediaFilesFromPath(source, true);
        int progressing = 0;
        progress.updateProgress(progressing, filesInFolderFromDB.size());
        inputLoop:
        for (MediaFile sourceMediaFile : filesInFolderFromDB) {
            progress.updateProgress(progressing++, filesInFolderFromDB.size());
            List<MediaFile> filesInTarget = mediafileDAO.getMediaFilesFromPathOfImage(sourceMediaFile.getImage(), targetDrive, target);
            if (filesInTarget.size() == 0) continue;
            final String subFolder = sourceMediaFile.getFilePath().toString().substring(source.toString().length());
            HashMap<String, Set<MediaFile>> versions = new HashMap<>();
            MediaFile atPlace = null;
            boolean atPlaceMatch = false;
            for (MediaFile mediaFileToMove : filesInTarget) {
                Set<MediaFile> mediaFiles = versions.get(mediaFileToMove.getFilehash());
                if (mediaFiles == null) {
                    mediaFiles = new HashSet<>();
                }
                mediaFiles.add(mediaFileToMove);
                for (MediaFile mediaFile : mediaFiles) {
                    if (mediaFile.getFilePath().getParent().toString().equals(target.toString() + subFolder)) {
                        atPlace = mediaFile;
                        atPlaceMatch = mediaFile.getFilehash().equals(sourceMediaFile.getFilehash());
                        break;
                    }
                }
                versions.put(mediaFileToMove.getFilehash(), mediaFiles);
            }
            Set<MediaFile> exactMatch = versions.get(sourceMediaFile.getFilehash());
            boolean originalPlace = true;
            if (atPlace != null) {
                if (atPlaceMatch || (exactMatch == null && versions.size() == 1)) {
                    originalPlace = false;
                    Set<MediaFile> mediaFiles = versions.get(atPlace.getFilehash());
                    mediaFiles.remove(atPlace);
                    versions.put(atPlace.getFilehash(), mediaFiles);
                }
            }
            if (exactMatch != null) {
                versions.remove(sourceMediaFile.getFilehash());
                int j = 1;
                for (Set<MediaFile> value : versions.values()) {
                    for (MediaFile mediaFileToMove : value) {
                        renameMediaFile(mediaFileToMove, Paths.get(getFreeDir(target.toString()+"\\conflict\\", subFolder) + subFolder), TableViewMediaFile.WriteMethod.MOVE, false);
                        j++;
                    }
                }
                for (MediaFile mediaFileToMove : exactMatch) {
                    renameMediaFile(mediaFileToMove, Paths.get((originalPlace ? target.toString() : getFreeDir(target.toString()+"\\duplicate\\", subFolder)) + subFolder), TableViewMediaFile.WriteMethod.MOVE, false);
                    originalPlace = false;
                }
            } else if (versions.size() == 1) {
                for (Set<MediaFile> value : versions.values()) {
                    for (MediaFile mediaFileToMove : value) {
                        renameMediaFile(mediaFileToMove, Paths.get((originalPlace ? target.toString() : getFreeDir(target.toString()+"\\duplicate\\", subFolder)) + subFolder), TableViewMediaFile.WriteMethod.MOVE, false);
                        originalPlace = false;
                    }
                }
            } else {
                for (Set<MediaFile> value : versions.values()) {
                    for (MediaFile mediaFileToMove : value) {
                        renameMediaFile(mediaFileToMove, Paths.get(getFreeDir(target.toString()+"\\conflict\\", subFolder) + subFolder), TableViewMediaFile.WriteMethod.MOVE, false);
                    }
                }
            }
        }
        return true;
    }

    private String getFreeDir(String input, String subFolder) {
        int i  = 1;
        while (true) {
            if (!new File(input+i+subFolder).exists()) return input+i;
            i++;
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

    public Set<MediafileDTO> readMediaFilesFromFolder(Path path, Boolean original, boolean force, ZoneId zone, String notes, MainController.ReadTask progress) {
        long start = System.nanoTime();
        Set<MediaFile> mediaFiles = new HashSet<>();
        Set<MediafileDTO> result = new HashSet<>();
        long getDrive = System.nanoTime();
        Drive drive = driveService.getLocalDrive(path.toString().substring(0, 1));
        if (drive == null) return result;
        long getFolder = System.nanoTime();
        Folder folder = folderService.getFolder(path);
        long getFiles = System.nanoTime();
        List<MediaFile> filesInFolderFromDB = getMediaFilesFromPath(path, false);
        long lists = System.nanoTime();
        HashMap<String, MediaFile> fileSet = new HashMap<>();
        for (MediaFile file : filesInFolderFromDB) {
            fileSet.put(file.getFilePath().toString().toLowerCase(), file);
        }
        final File[] files = path.toFile().listFiles();
        long readfiles = System.nanoTime();
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            MediaFile mediaFile = readMediaFile(file, fileSet, folder, original, force, zone, notes);
            if (mediaFile != null) {
                mediaFiles.add(mediaFile);
                if (progress != null) progress.increaseProgress();
            }
        }
        long writeDB = System.nanoTime();
        mediafileDAO.close();
        long removeFiles = System.nanoTime();
        filesInFolderFromDB.removeAll(mediaFiles);
        deleteMediaFiles(filesInFolderFromDB);
        long listupdate = System.nanoTime();
        mediaFiles.remove(null);
        mediaFiles.forEach(mf -> {result.add(getMediafileDTO(mf));});
        long end = System.nanoTime();
/*
        System.out.println(
                "Init  " + TimeUnit.NANOSECONDS.toMillis(getDrive - start)
                        + "\nDrive " + TimeUnit.NANOSECONDS.toMillis(getFolder - getDrive)
                        + "\nFolde " + TimeUnit.NANOSECONDS.toMillis(getFiles - getFolder)
                        + "\nFiles  " + TimeUnit.NANOSECONDS.toMillis(lists - getFiles)
                        + "\nLists " + TimeUnit.NANOSECONDS.toMillis(readfiles - lists)
                        + "\nRead   " + TimeUnit.NANOSECONDS.toMillis(writeDB - readfiles)
                        + "\nComit   " + TimeUnit.NANOSECONDS.toMillis(removeFiles - writeDB)
                        + "\nDelet  " + TimeUnit.NANOSECONDS.toMillis(listupdate - removeFiles)
                        + "\nWhole " + TimeUnit.NANOSECONDS.toMillis(end - start)

        );
*/
        System.out.println(path + " done in " + TimeUnit.NANOSECONDS.toMillis(end - start));
        return result;
    }

    public MediaFile readMediaFile(File file) {
        MediaFile result = mediafileDAO.getByFile(driveService.getLocalDrive(file.toPath()), file.toPath(), false);
        if (result == null) result = readMediaFile(file, null, folderService.getFolder(file.getParentFile().toPath()), null, false, CommonProperties.getInstance().getZone(), "");
        mediafileDAO.close();
        return result;
    }

    private MediaFile readMediaFile(File file, HashMap<String, MediaFile> filesInFolderMap, Folder folder, Boolean original, boolean force, ZoneId zone, String notes) {
        if (filesInFolderMap == null) filesInFolderMap = new HashMap<>();
        if (notes == null) notes = "";
        Path filePath = file.toPath();
        BasicFileAttributes attrs = org.nyusziful.pictureorganizer.Service.Rename.StaticTools.getFileAttributes(file);
        MediaFile actFile = null;
        long strat = System.nanoTime();
        if (!attrs.isDirectory() && attrs.isRegularFile() && supportedFileType(filePath.getFileName().toString())) {
            Set<String> whatToSave = new HashSet<>();
            actFile = filesInFolderMap.get(filePath.toString().toLowerCase());
            assert folder != null;
            final Timestamp dateMod = new Timestamp(attrs.lastModifiedTime().toMillis());
            dateMod.setNanos(0);
            final long fileSize = attrs.size();
            long fileCreateStart = System.nanoTime();
            if (actFile == null) {
                final String name = filePath.getFileName().toString();
                if (supportedRAWFileType(name)) {
                    actFile = new RAWMediaFileInstance(folder, filePath, fileSize, dateMod, original);
                } else if (supportedJPGFileType(name)) {
                    actFile = new JPGMediaFile(folder, filePath, fileSize, dateMod, original);
                } else if (supportedVideoFileType(name)) {
                    actFile = new VideoMediaFile(folder, filePath, fileSize, dateMod, original);
                } else {
                    actFile = new MediaFile(folder, filePath, fileSize, dateMod, original);
                }
            }
            long hashStart = System.nanoTime();
            long hashFinishStart = hashStart;
            long exifReadStart = hashStart;
            long exifReadEnd = hashStart;
            if (force || actFile.getId() < 0 || actFile.getSize() != fileSize || actFile.getDateMod().compareTo(dateMod) != 0 || (actFile instanceof VideoMediaFile && !file.getName().endsWith(".MTS") && ((VideoMediaFile)actFile).getDuration() == 0)) {
                System.out.println(file.toPath());
                actFile.setDateMod(dateMod);
                actFile.setSize(fileSize);
                ImageDTO imageDTO = getHash(filePath.toFile());
                hashFinishStart = System.nanoTime();
                Image image;
                image = imageService.getImage(imageDTO, true);
                exifReadStart = System.nanoTime();
                Meta meta = ExifService.readMeta(filePath.toFile(), zone);
                exifReadEnd = System.nanoTime();
                if (image == null) {
                    image = new Image(imageDTO.hash, imageDTO.type, meta.make, meta.model);
                    whatToSave.add("image");
                } else {
                    if (image.getCamera_model() == null) image.setCamera_model(meta.model);
                    if (image.getCamera_make() == null) image.setCamera_make(meta.make);
                }
                actFile.setMeta(meta);
                actFile.setImage(image);
                actFile.setFilehash(imageDTO.fullhash);
                actFile.setExif(imageDTO.exif);
                whatToSave.add("file");
            }
            if (TRUE.equals(original)) {
                if (!actFile.isOriginal()) {
                    try {
                        final boolean updated = updateOriginalImage(actFile);
                        if (updated) {
                            whatToSave.add("image");
                        }
                    } catch (Exception e) {
                        notes += actFile + "\n";
                    }
                }
            } else {
                try {
                    final boolean updated = updateBestimateImage(actFile);
                    if (updated) {
                        whatToSave.add("file");
                        whatToSave.add("image");
                    }
                } catch (Exception e) {
                    notes += actFile + "\n";
                }
            }
            long WriteToDBStart = System.nanoTime();
            if (whatToSave.contains("image")) {
                imageService.saveImage(actFile.getImage(), true);
            }
            if (whatToSave.contains("file")) {
                saveMediaFile(actFile, true);
                filesInFolderMap.put(actFile.getFilePath().toString().toLowerCase(), actFile);
            }
            long WriteToDBEnd = System.nanoTime();
/*
            System.out.println(
                    "Init  " + TimeUnit.NANOSECONDS.toMillis(fileCreateStart - start)
                        + "\nMedia " + TimeUnit.NANOSECONDS.toMillis(hashStart - fileCreateStart)
                            + "\nHash  " + TimeUnit.NANOSECONDS.toMillis(hashFinishStart - hashStart)
                                + "\nImage " + TimeUnit.NANOSECONDS.toMillis(exifReadStart - hashFinishStart)
                                    + "\nExif  " + TimeUnit.NANOSECONDS.toMillis(exifReadEnd - exifReadStart)
                                        + "\nSet   " + TimeUnit.NANOSECONDS.toMillis(WriteToDBStart - exifReadEnd)
                                            + "\nDB w  " + TimeUnit.NANOSECONDS.toMillis(WriteToDBEnd - WriteToDBStart)
                + "\nWhole " + TimeUnit.NANOSECONDS.toMillis(WriteToDBEnd - start)

            );
*/
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
        return renameMediaFile(mediaFile, newPath, writeMethod, overwrite);
    }

    public boolean renameMediaFile(MediaFile mediaFile, Path newPath, TableViewMediaFile.WriteMethod writeMethod, boolean overwrite) {
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

            if (mediaFile instanceof JPGMediaFile && !((JPGMediaFile) mediaFile).isExifbackup()) {
                ((JPGMediaFile) mediaFile).addExifbackup(TRUE.equals(mediaFile.isOriginal()));
            }
            if (mediaFile instanceof RAWMediaFileInstance && !((RAWMediaFileInstance) mediaFile).isXMPattached()) {
                ((RAWMediaFileInstance) mediaFile).setXMPattached(createXmp(mediaFile.getFilePath().toFile()) != null);
            }

            saveMediaFile(mediaFile);
        }

        return rename;
    }

}