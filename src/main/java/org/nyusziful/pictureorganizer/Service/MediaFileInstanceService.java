package org.nyusziful.pictureorganizer.Service;

import org.nyusziful.pictureorganizer.DAL.DAO.MediaFileInstanceDAO;
import org.nyusziful.pictureorganizer.DAL.DAO.MediaFileInstanceDAOImplHib;
import org.nyusziful.pictureorganizer.DAL.Entity.*;
import org.nyusziful.pictureorganizer.DTO.ImageDTO;
import org.nyusziful.pictureorganizer.DTO.MediafileInstanceDTO;
import org.nyusziful.pictureorganizer.DTO.Meta;
import org.nyusziful.pictureorganizer.Main.CommonProperties;
import org.nyusziful.pictureorganizer.Service.ExifUtils.ExifService;
import org.nyusziful.pictureorganizer.Service.Rename.RenameService;
import org.nyusziful.pictureorganizer.UI.Contoller.MainController;
import org.nyusziful.pictureorganizer.UI.Model.TableViewMediaFileInstance;
import org.nyusziful.pictureorganizer.UI.ProgressLeakingTask;

import java.io.File;
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
import static org.nyusziful.pictureorganizer.UI.StaticTools.*;

public class MediaFileInstanceService {
    private final MediaFileInstanceDAO mediaFileInstanceDAO;
    private final DriveService driveService;
    private final FolderService folderService;
    private final ImageService imageService;
    private static MediaFileInstanceService instance;

    private MediaFileInstanceService() {
        driveService = new DriveService();
        folderService = new FolderService();
        imageService = new ImageService();
        mediaFileInstanceDAO = new MediaFileInstanceDAOImplHib();
    }

    public static MediaFileInstanceService getInstance() {
        if (instance == null) {
            instance = new MediaFileInstanceService();
        }
        return instance;
    }

    public MediaFileInstance getMediaFileInstance(Drive drive, Path path, boolean withImage) {
        return mediaFileInstanceDAO.getByFile(drive, path, withImage);
    }

    public MediaFileInstance getMediaFileInstance(Path path, boolean withImage) {
        Drive localDrive = driveService.getLocalDrive(path);
        return getMediaFileInstance(localDrive, path, withImage);
    }

    public MediaFileInstance getMediaFileInstance(MediafileInstanceDTO mediafileDTO, boolean withImage) {
        Path path = Paths.get(mediafileDTO.abolutePath);
        return getMediaFileInstance(path, withImage);
    }

    public MediafileInstanceDTO getMediafileDTO(MediaFileInstance mediaFileInstance) {
        MediafileInstanceDTO mediafileDTO = new MediafileInstanceDTO();
        if (mediaFileInstance != null) {
            mediafileDTO.abolutePath = mediaFileInstance.getFilePath().toString();
            mediafileDTO.filename = mediaFileInstance.getFilename();
            mediafileDTO.dateMod = mediaFileInstance.getDateMod();
            mediafileDTO.fileHash = mediaFileInstance.getMediaFileVersion().getFilehash();
            mediafileDTO.size = mediaFileInstance.getMediaFileVersion().getSize();
            mediafileDTO.isOriginal = TRUE.equals(mediaFileInstance.getMediaFileVersion().isOriginal());
        }
        return mediafileDTO;
    }

    public static MediafileInstanceDTO getMediafileDTO(File file) {
        MediafileInstanceDTO mediafileDTO = new MediafileInstanceDTO();
        if (file != null) {
            mediafileDTO.abolutePath = file.getAbsolutePath();
            mediafileDTO.filename = file.getName();
        }
        return mediafileDTO;
    }

    public void saveMediaFileInstance(MediaFileInstance mediaFileInstance) {
        saveMediaFileInstance(mediaFileInstance, false);
    }

    private void saveMediaFileInstance(MediaFileInstance mediaFileInstance, boolean batch) {
        saveMediaFiles(Collections.singleton(mediaFileInstance), batch);
    }

    public void saveMediaFiles(Collection<? extends MediaFileInstance> mediaFileInstances) {
        saveMediaFiles(mediaFileInstances, false);
    }

    public void saveMediaFiles(Collection<? extends MediaFileInstance> mediaFileInstances, boolean batch) {
        for (MediaFileInstance file : mediaFileInstances) {
            if (file.getId() > -1)
                mediaFileInstanceDAO.merge(file, batch);
            else
                mediaFileInstanceDAO.persist(file, batch);
        }
    }

    public void deleteMediaFiles(Collection<? extends MediaFileInstance> mediaFileInstances) {
        for (MediaFileInstance file : mediaFileInstances) {
            if (file.getId() > -1)
                mediaFileInstanceDAO.delete(file);
        }
    }

    public void updateMediafile(MediaFileInstance Mediafile) {
        mediaFileInstanceDAO.merge(Mediafile);
    }

    public static void main(String[] args) {
        final MediaFileInstanceService mediafileService = new MediaFileInstanceService();
//        mediafileService.syncFolder(Paths.get("g:\\Pictures\\Photos\\DBSaved\\"), Paths.get("h:\\Képek\\Photos\\Processed"));
    }

    public List<MediaFileInstance> getMediaFilesInstancesFromPath(Path path, boolean recursive) {
        final Drive localDrive = driveService.getLocalDrive(path.toString().substring(0, 1));
        return recursive ? mediaFileInstanceDAO.getByPathRec(localDrive, path, true): mediaFileInstanceDAO.getByPath(localDrive, path, true);
    }

    public void flush() {
        mediaFileInstanceDAO.flush();
    }

    public void close() {
        mediaFileInstanceDAO.close();
    }

    public <V> Boolean syncFolder(Path source, Path target, ProgressLeakingTask<V> progress) {
/*        final Drive targetDrive = driveService.getLocalDrive(target.toString().substring(0, 1));
        if (targetDrive == null) return false;
        List<MediaFileInstance> filesInFolderFromDB = getMediaFilesInstancesFromPath(source, true);
        int progressing = 0;
        progress.updateProgress(progressing, filesInFolderFromDB.size());
        inputLoop:
        for (MediaFileInstance sourceMediaFile : filesInFolderFromDB) {
            progress.updateProgress(progressing++, filesInFolderFromDB.size());
            List<MediaFileInstance> filesInTarget = mediaFileInstanceDAO.getMediaFilesFromPathOfImage(sourceMediaFile.getImage(), targetDrive, target);
            if (filesInTarget.size() == 0) continue;
            final String subFolder = sourceMediaFile.getFilePath().toString().substring(source.toString().length());
            HashMap<String, Set<MediaFileInstance>> versions = new HashMap<>();
            MediaFileInstance atPlace = null;
            boolean atPlaceMatch = false;
            for (MediaFileInstance mediaFileToMove : filesInTarget) {
                Set<MediaFileInstance> mediaFiles = versions.get(mediaFileToMove.getFilehash());
                if (mediaFiles == null) {
                    mediaFiles = new HashSet<>();
                }
                mediaFiles.add(mediaFileToMove);
                for (MediaFileInstance mediaFile : mediaFiles) {
                    if (mediaFile.getFilePath().getParent().toString().equals(target.toString() + subFolder)) {
                        atPlace = mediaFile;
                        atPlaceMatch = mediaFile.getFilehash().equals(sourceMediaFile.getFilehash());
                        break;
                    }
                }
                versions.put(mediaFileToMove.getFilehash(), mediaFiles);
            }
            Set<MediaFileInstance> exactMatch = versions.get(sourceMediaFile.getFilehash());
            boolean originalPlace = true;
            if (atPlace != null) {
                if (atPlaceMatch || (exactMatch == null && versions.size() == 1)) {
                    originalPlace = false;
                    Set<MediaFileInstance> mediaFiles = versions.get(atPlace.getFilehash());
                    mediaFiles.remove(atPlace);
                    versions.put(atPlace.getFilehash(), mediaFiles);
                }
            }
            if (exactMatch != null) {
                versions.remove(sourceMediaFile.getFilehash());
                int j = 1;
                for (Set<MediaFileInstance> value : versions.values()) {
                    for (MediaFileInstance mediaFileToMove : value) {
                        renameMediaFile(mediaFileToMove, Paths.get(getFreeDir(target.toString()+"\\conflict\\", subFolder) + subFolder), TableViewMediaFile.WriteMethod.MOVE, false);
                        j++;
                    }
                }
                for (MediaFileInstance mediaFileToMove : exactMatch) {
                    renameMediaFile(mediaFileToMove, Paths.get((originalPlace ? target.toString() : getFreeDir(target.toString()+"\\duplicate\\", subFolder)) + subFolder), TableViewMediaFile.WriteMethod.MOVE, false);
                    originalPlace = false;
                }
            } else if (versions.size() == 1) {
                for (Set<MediaFileInstance> value : versions.values()) {
                    for (MediaFileInstance mediaFileToMove : value) {
                        renameMediaFile(mediaFileToMove, Paths.get((originalPlace ? target.toString() : getFreeDir(target.toString()+"\\duplicate\\", subFolder)) + subFolder), TableViewMediaFile.WriteMethod.MOVE, false);
                        originalPlace = false;
                    }
                }
            } else {
                for (Set<MediaFileInstance> value : versions.values()) {
                    for (MediaFileInstance mediaFileToMove : value) {
                        renameMediaFile(mediaFileToMove, Paths.get(getFreeDir(target.toString()+"\\conflict\\", subFolder) + subFolder), TableViewMediaFile.WriteMethod.MOVE, false);
                    }
                }
            }
        }*/
        return true;
    }
    private String getFreeDir(String input, String subFolder) {
        int i  = 1;
        while (true) {
            if (!new File(input+i+subFolder).exists()) return input+i;
            i++;
        }
    }

    public Set<MediafileInstanceDTO> reOrganizeFilesInSubFolders(Path path, ProgressLeakingTask progress) {
        Set<MediafileInstanceDTO> result = new HashSet<>();
/*        if (driveService.getLocalDrive(path.toString().substring(0, 1)) == null) return result;
        List<MediaFileInstance> filesInFolderFromDB = getMediaFilesInstancesFromPath(path, true);
        HashMap<String, MediaFileInstance> pathToMediaFile = new HashMap<>();
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
             MediaFileInstance found = null;
/*            for (MediaFileInstance mediaFile : filesInFolderFromDB) {
                if (mediaFile.getFilePath().equals(path1)) {
                    found = mediaFile;
                    break;
                }
            }
* /
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
        Set<MediaFileInstance> toSave = new HashSet<>();
        for (File unknownFile : unknownFiles) {
            System.out.println(unknownFile);
            progressing++;
            progress.updateProgress(progressing, paths.size() + unknownFiles.size());
            MediaFileInstance mediaFile = null;
            String fileName = unknownFile.getName().toLowerCase();
            BasicFileAttributes attrs = org.nyusziful.pictureorganizer.Service.Rename.StaticTools.getFileAttributes(unknownFile);
            final Timestamp dateMod = new Timestamp(attrs.lastModifiedTime().toMillis());
            for (MediaFileInstance potentialMediaFileFromList : filesInFolderFromDB) {
                if (fileName.equals(potentialMediaFileFromList.getFilename().toLowerCase()) && potentialMediaFileFromList.getSize() == attrs.size() && potentialMediaFileFromList.getDateMod().toInstant().toEpochMilli() == dateMod.toInstant().toEpochMilli()) {
                    mediaFile = potentialMediaFileFromList;
                    break;
                }
            }
            if (mediaFile == null) {
                result.add(getMediafileDTO(readMediaFileInstance(unknownFile)));
            } else {
                mediaFile.updateFolder(folderService.getFolder(unknownFile.getParentFile().toPath()));
                toSave.add(mediaFile);
                filesInFolderFromDB.remove(mediaFile);
            }
        }
        saveMediaFiles(toSave);
        deleteMediaFiles(filesInFolderFromDB);*/
        return result;
    }

    public Set<MediafileInstanceDTO> readMediaFilesFromFolder(Path path, Boolean original, boolean force, ZoneId zone, String notes, MainController.ReadTask progress) {
        long start = System.nanoTime();
        Set<MediaFileInstance> mediaFiles = new HashSet<>();
        Set<MediafileInstanceDTO> result = new HashSet<>();
        long getDrive = System.nanoTime();
        Drive drive = driveService.getLocalDrive(path.toString().substring(0, 1));
        if (drive == null) return result;
        long getFolder = System.nanoTime();
        Folder folder = folderService.getFolder(path);
        long getFiles = System.nanoTime();
        List<MediaFileInstance> filesInFolderFromDB = getMediaFilesInstancesFromPath(path, false);
        long lists = System.nanoTime();
        HashMap<String, MediaFileInstance> fileSet = new HashMap<>();
        for (MediaFileInstance file : filesInFolderFromDB) {
            fileSet.put(file.getFilePath().toString().toLowerCase(), file);
        }
        long readfiles = System.nanoTime();
        for (File file : path.toFile().listFiles()) {
            MediaFileInstance mediaFile = readMediaFileInstance(file, fileSet, folder, original, force, zone, notes);
            if (mediaFile != null) {
                mediaFiles.add(mediaFile);
                if (progress != null) progress.increaseProgress();
            }
        }
        long writeDB = System.nanoTime();
        mediaFileInstanceDAO.close();
        long removeFiles = System.nanoTime();
        filesInFolderFromDB.removeAll(mediaFiles);
        deleteMediaFiles(filesInFolderFromDB);
        long listupdate = System.nanoTime();
        mediaFiles.remove(null);
        mediaFiles.forEach(mf -> result.add(getMediafileDTO(mf)));
        long end = System.nanoTime();
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
        System.out.println(path + " done in " + TimeUnit.NANOSECONDS.toMillis(end - start));
        return result;
    }

    public MediaFileInstance readMediaFileInstance(File file) {
        MediaFileInstance result = mediaFileInstanceDAO.getByFile(driveService.getLocalDrive(file.toPath()), file.toPath(), false);
        if (result == null) result = readMediaFileInstance(file, new HashMap<>(), folderService.getFolder(file.getParentFile().toPath()), null, false, CommonProperties.getInstance().getZone(), "");
        mediaFileInstanceDAO.close();
        return result;
    }

    private MediaFileInstance readMediaFileInstance(File file, HashMap<String, MediaFileInstance> filesInFolderMap, Folder folder, Boolean original, boolean force, ZoneId zone, String notes) {
        if (notes == null) notes = "";
        Path filePath = file.toPath();
        BasicFileAttributes attrs = org.nyusziful.pictureorganizer.Service.Rename.StaticTools.getFileAttributes(file);
        MediaFileInstance actFileInstance = null;
        long start = System.nanoTime();
        if (!attrs.isDirectory() && attrs.isRegularFile() && supportedFileType(filePath.getFileName().toString())) {
            Set<String> whatToSave = new HashSet<>();
            actFileInstance = filesInFolderMap.get(filePath.toString().toLowerCase());
            assert folder != null;
            final Timestamp dateMod = new Timestamp(attrs.lastModifiedTime().toMillis());
            dateMod.setNanos(0);
            final long fileSize = attrs.size();
            long hashStart = System.nanoTime();
            long hashEnd = hashStart;
            long exifReadStart = hashStart;
            long exifReadEnd = hashStart;
            long fileCreateEnd = hashStart;
            if (force
                    || actFileInstance == null
                    || actFileInstance.getMediaFileVersion().getSize() != fileSize
                    || actFileInstance.getDateMod().compareTo(dateMod) != 0
//                    || (actFileInstance instanceof VideoMediaFileInstance && !file.getName().endsWith(".MTS") && ((VideoMediaFileVersion)actFileInstance.getMediaFileVersion().getMedia().)..getDuration() == 0))
            ) {
                whatToSave.add("instance");
                System.out.println(file.toPath());
                ImageDTO imageDTO = getHash(filePath.toFile());
                hashEnd = System.nanoTime();
                Meta meta = ExifService.readMeta(filePath.toFile(), zone);
                exifReadEnd = System.nanoTime();
                if (actFileInstance == null) {
                    final String name = filePath.getFileName().toString();
                    MediaFileVersion mediafileVersion = MediaFileVersionService.getInstance().getMediafileVersion(imageDTO.fullhash); //if we have the exact same version
                    MediaFile mediaFile = null;
                    if (mediafileVersion == null) {
                        whatToSave.add("version");
                        for (MediaFileVersion actMediaFileVersion : MediaFileVersionService.getInstance().getMediafileVersionsByImageHash(imageDTO.hash)) {
                            if (mediafileVersion.getMedia().size() == 1) {
                                if (mediaFile == null) mediaFile = actMediaFileVersion.getMediaFile();
                                else if (!mediaFile.equals(actMediaFileVersion)) {
                                    //this shouldn't be
                                }
                            }
                        }
                    }
                    if (supportedRAWFileType(name)) {
                        if (mediafileVersion == null && mediaFile == null) mediaFile = new RAWMediaFile(null, meta.originalFilename, meta.shotnumber);
                        if (mediaFile != null) mediafileVersion = new RAWMediaFileVersion(imageDTO.fullhash, null, fileSize, (RAWMediaFile)mediaFile, meta.date);
                        actFileInstance = new RAWMediaFileInstance(folder, filePath, dateMod, (RAWMediaFileVersion)mediafileVersion);
                    } else if (supportedJPGFileType(name)) {
                        if (mediafileVersion == null && mediaFile == null) mediaFile = new JPGMediaFile(null, meta.originalFilename, meta.shotnumber, JPGMediaFile.setWithQuality(meta.quality), null);
                        if (mediaFile != null) mediafileVersion = new JPGMediaFileVersion(imageDTO.fullhash, null, fileSize, (JPGMediaFile)mediaFile, meta.date);
                        actFileInstance = new JPGMediaFileInstance(folder, filePath, dateMod, (JPGMediaFileVersion)mediafileVersion);
                    } else if (supportedVideoFileType(name)) {
                        if (mediafileVersion == null && mediaFile == null) mediaFile = new VideoMediaFile(null, meta.originalFilename, meta.shotnumber);
                        if (mediaFile != null) mediafileVersion = new VideoMediaFileVersion(imageDTO.fullhash, null, fileSize, (VideoMediaFile)mediaFile, meta.date);
                        actFileInstance = new VideoMediaFileInstance(folder, filePath, dateMod, (VideoMediaFileVersion)mediafileVersion);
                    } else {
                        if (mediafileVersion == null && mediaFile == null) mediaFile = new MediaFile(null, meta.originalFilename, meta.shotnumber);
                        if (mediaFile != null) mediafileVersion = new MediaFileVersion(imageDTO.fullhash, null, fileSize, mediaFile, meta.date);
                        actFileInstance = new MediaFileInstance(folder, filePath, dateMod, mediafileVersion);
                    }
                }
                fileCreateEnd = System.nanoTime();
                Image image;
                image = imageService.getImage(imageDTO, true);
                exifReadStart = System.nanoTime();
                if (image == null) {
                    image = new Image(imageDTO.hash, imageDTO.type, meta.make, meta.model);
                    whatToSave.add("image");
                } else {
                    if (image.getCamera_model() == null) image.setCamera_model(meta.model);
                    if (image.getCamera_make() == null) image.setCamera_make(meta.make);
                }
            }
            long WriteToDBStart = System.nanoTime();
            if (whatToSave.contains("version")) {
                MediaFileVersionService.getInstance().saveMediaFileVersion(actFileInstance.getMediaFileVersion(),true);
            }
            if (whatToSave.contains("instance")) {
                saveMediaFileInstance(actFileInstance);
                filesInFolderMap.put(actFileInstance.getFilePath().toString().toLowerCase(), actFileInstance);
            }
            long WriteToDBEnd = System.nanoTime();
            System.out.println(
                    "Init  " + TimeUnit.NANOSECONDS.toMillis(hashStart - start)
                        + "\nHash  " + TimeUnit.NANOSECONDS.toMillis(hashEnd - hashStart)
                            + "\nMedia " + TimeUnit.NANOSECONDS.toMillis(fileCreateEnd - hashEnd)
                                + "\nImage " + TimeUnit.NANOSECONDS.toMillis(exifReadStart - fileCreateEnd)
                                    + "\nExif  " + TimeUnit.NANOSECONDS.toMillis(exifReadEnd - exifReadStart)
                                        + "\nSet   " + TimeUnit.NANOSECONDS.toMillis(WriteToDBStart - exifReadEnd)
                                            + "\nDB w  " + TimeUnit.NANOSECONDS.toMillis(WriteToDBEnd - WriteToDBStart)
                + "\nWhole " + TimeUnit.NANOSECONDS.toMillis(WriteToDBEnd - start)

            );
        }
        return actFileInstance;
    }

    public String getMediaFileName(MediafileInstanceDTO mediafileInstanceDTO, String nameVersion) {
        MediaFileInstance mediaFile = getMediaFileInstance(mediafileInstanceDTO, true);
        if (mediaFile == null) return mediafileInstanceDTO.filename;
        return RenameService.getName(mediaFile, nameVersion, Integer.toString(mediaFile.getMediaFileVersion().getVersionNumber()));
    }

    public boolean renameMediaFileInstance(MediafileInstanceDTO mediafileInstanceDTO, Path newPath, TableViewMediaFileInstance.WriteMethod writeMethod, boolean overwrite) {
        MediaFileInstance mediaFileInstance = getMediaFileInstance(mediafileInstanceDTO, true);
        return renameMediaFileInstance(mediaFileInstance, newPath, writeMethod, overwrite);
    }

    public boolean renameMediaFileInstance(MediaFileInstance mediaFileInstance, Path newPath, TableViewMediaFileInstance.WriteMethod writeMethod, boolean overwrite) {
        final boolean rename = RenameService.write(mediaFileInstance.getFilePath(), newPath, writeMethod, overwrite);
        if (overwrite || rename) {
            Folder folder = folderService.getFolder(newPath.getParent());
            if (TableViewMediaFileInstance.WriteMethod.COPY.equals(writeMethod)) {
                try {
                    mediaFileInstance = (MediaFileInstance) mediaFileInstance.clone();
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
//                mediaFileInstance = readMediaFile(newPath.toFile(), null, folder, mediaFileInstance.isOriginal(), false, mediaFileInstance.getDateStored().getZone(), null);
            }
            mediaFileInstance.updatePath(folder, newPath);

            if (mediaFileInstance instanceof RAWMediaFileInstance && !((RAWMediaFileInstance) mediaFileInstance).isXMPattached()) {
                ((RAWMediaFileInstance) mediaFileInstance).setXMPattached(createXmp(mediaFileInstance.getFilePath().toFile()) != null);
            }

            saveMediaFileInstance(mediaFileInstance);
        }

        return rename;
    }

    public List<MediaFileInstance> getMediaFilesInstancesByVersion(MediaFileVersion mediaFileVersion) {
        return mediaFileInstanceDAO.getByVersion(mediaFileVersion);
    }
    public List<MediaFileInstance> getMediaFilesInstancesByMediaFile(MediaFile mediaFile) {
        return mediaFileInstanceDAO.getByMediaFile(mediaFile);
    }
}