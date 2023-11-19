package org.nyusziful.pictureorganizer.Service;

import org.apache.commons.io.FilenameUtils;
import org.nyusziful.pictureorganizer.DAL.DAO.MediaDAO;
import org.nyusziful.pictureorganizer.DAL.DAO.MediaDAOImplHib;
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
    private final MediaDAO mediaDAO;
    private final DriveService driveService;
    private final FolderService folderService;
    private final ImageService imageService;
    private static MediaFileInstanceService instance;

    private MediaFileInstanceService() {
        driveService = new DriveService();
        folderService = new FolderService();
        imageService = new ImageService();
        mediaFileInstanceDAO = new MediaFileInstanceDAOImplHib();
        mediaDAO = new MediaDAOImplHib();
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

    public MediafileInstanceDTO getMediafileInstanceDTO(MediaFileInstance mediaFileInstance) {
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

    public static MediafileInstanceDTO getMediafileInstanceDTO(File file) {
        MediafileInstanceDTO mediafileDTO = new MediafileInstanceDTO();
        if (file != null) {
            mediafileDTO.abolutePath = file.getAbsolutePath();
            mediafileDTO.filename = file.getName();
        }
        return mediafileDTO;
    }

    public MediaFileInstance createMediaFileInstance(String name, Folder folder, Path filePath, Timestamp dateMod, MediaFileVersion mediafileVersion) {
        if (supportedRAWFileType(name)) {
            return new RAWMediaFileInstance(folder, filePath, dateMod, (RAWMediaFileVersion)mediafileVersion);
        } else if (supportedJPGFileType(name)) {
            return new JPGMediaFileInstance(folder, filePath, dateMod, (JPGMediaFileVersion)mediafileVersion);
        } else if (supportedVideoFileType(name)) {
            return new VideoMediaFileInstance(folder, filePath, dateMod, (VideoMediaFileVersion)mediafileVersion);
        } else {
            return new MediaFileInstance(folder, filePath, dateMod, mediafileVersion);
        }
    }

    public void saveMediaFileInstance(MediaFileInstance mediaFileInstance) {
        saveMediaFileInstance(mediaFileInstance, false);
    }

    private void saveMediaFileInstance(MediaFileInstance mediaFileInstance, boolean batch) {
        saveMediaFileInstances(Collections.singleton(mediaFileInstance), batch);
    }

    public void saveMediaFileInstances(Collection<? extends MediaFileInstance> mediaFileInstances) {
        saveMediaFileInstances(mediaFileInstances, false);
    }

    public void saveMediaFileInstances(Collection<? extends MediaFileInstance> mediaFileInstances, boolean batch) {
        for (MediaFileInstance file : mediaFileInstances) {
            if (file.getId() > -1)
                mediaFileInstanceDAO.merge(file, batch);
            else
                mediaFileInstanceDAO.persist(file, batch);
        }
    }

    public void deleteMediaFileInstances(Collection<? extends MediaFileInstance> mediaFileInstances) {
        for (MediaFileInstance file : mediaFileInstances) {
            if (file.getId() > -1)
                mediaFileInstanceDAO.delete(file);
        }
    }

    public void updateMediafileInstance(MediaFileInstance Mediafile) {
        mediaFileInstanceDAO.merge(Mediafile);
    }

    public static void main(String[] args) {
        final MediaFileInstanceService mediafileService = new MediaFileInstanceService();
//        mediafileService.syncFolder(Paths.get("g:\\Pictures\\Photos\\DBSaved\\"), Paths.get("h:\\Képek\\Photos\\Processed"));
    }

    public List<MediaFileInstance> getMediaFilesInstancesFromPath(Path path, boolean recursive) {
        final Drive localDrive = driveService.getLocalDrive(path.toString().substring(0, 1));
        return mediaFileInstanceDAO.getByPath(localDrive, path, recursive, true);
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
        HashMap<String, MediaFileInstance> mediaFileInstanceByFilename = new HashMap<>();
        Set<MediaFileInstance> mainMediaFiles = new HashSet<>();
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
            Boolean main = false;
            MediaFileInstance mediaFileInstance = readMediaFileInstance(file, fileSet, folder, original, main, force, zone, notes);
            if (main) mainMediaFiles.add(mediaFileInstance);
            if (mediaFileInstance != null) {
                mediaFileInstanceByFilename.put(file.getName(), mediaFileInstance);
                if (progress != null) progress.increaseProgress();
            }
        }
        long writeDB = System.nanoTime();
        mediaFileInstanceDAO.close();
        long removeFiles = System.nanoTime();
        filesInFolderFromDB.removeAll(mediaFileInstanceByFilename.values());
        deleteMediaFileInstances(filesInFolderFromDB);
        long listupdate = System.nanoTime();
        mediaFileInstanceByFilename.remove(null);
        if (original) mainMediaFiles.forEach(JPGmediaFileInstance -> {
            String filename = JPGmediaFileInstance.getFilename();
            String ext = FilenameUtils.getExtension(filename.toLowerCase());
            filename = filename.replaceFirst(ext+"$", "ARW");
            MediaFileInstance RAWmediaFileInstance = mediaFileInstanceByFilename.get(filename);
            if (RAWmediaFileInstance != null && !JPGmediaFileInstance.getMediaFileVersion().getMediaFile().getMainMediaFile().equals(RAWmediaFileInstance.getMediaFileVersion().getMediaFile())) {
                JPGmediaFileInstance.getMediaFileVersion().getMediaFile().setMainMediaFile(RAWmediaFileInstance.getMediaFileVersion().getMediaFile());
                saveMediaFileInstance(JPGmediaFileInstance);
            }
        });
        mediaFileInstanceByFilename.values().forEach(mf -> result.add(getMediafileInstanceDTO(mf)));
        long end = System.nanoTime();
        if (false) System.out.println(
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
        if (result == null) result = readMediaFileInstance(file, new HashMap<>(), folderService.getFolder(file.getParentFile().toPath()), null, null, false, CommonProperties.getInstance().getZone(), "");
        mediaFileInstanceDAO.close();
        return result;
    }

    private MediaFileInstance readMediaFileInstance(File file, HashMap<String, MediaFileInstance> filesInFolderMap, Folder folder, Boolean original, Boolean main, boolean force, ZoneId zone, String notes) {
        if (notes == null) notes = "";
        Path filePath = file.toPath();
        BasicFileAttributes attrs = org.nyusziful.pictureorganizer.Service.Rename.StaticTools.getFileAttributes(file);
        MediaFileInstance mediaFileInstance = null;
        long start = System.nanoTime();
        if (!attrs.isDirectory() && attrs.isRegularFile() && supportedFileType(filePath.getFileName().toString())) {
            Set<String> whatToSave = new HashSet<>();
            mediaFileInstance = filesInFolderMap.get(filePath.toString().toLowerCase());
            boolean newInstance = mediaFileInstance == null;
            assert folder != null;
            final Timestamp dateMod = new Timestamp(attrs.lastModifiedTime().toMillis());
            dateMod.setNanos(0);
            ArrayList<Long> times = new ArrayList<>();
            final long fileSize = attrs.size();
            if (       force
                    || newInstance
                    || mediaFileInstance.getMediaFileVersion().getSize() != fileSize
                    || mediaFileInstance.getDateMod().compareTo(dateMod) != 0
//                    || (mediaFileInstance instanceof VideoMediaFileInstance && !file.getName().endsWith(".MTS") && ((VideoMediaFileVersion)mediaFileInstance.getMediaFileVersion().getMedia().)..getDuration() == 0))
            ) {
                System.out.println(file.toPath());
                ImageDTO imageDTO = getHash(filePath.toFile());
                times.add(System.nanoTime());
                Meta meta = ExifService.readMeta(filePath.toFile(), zone);
                times.add(System.nanoTime());
                Image image;
                image = imageService.getImage(imageDTO, true);
                if (image == null) {
                    image = new Image(imageDTO.hash, imageDTO.type, meta.make, meta.model);
                    whatToSave.add("image");
                } else if (image.getCamera_model() == null) {
                    image.setCamera_model(meta.model);
                    whatToSave.add("image");
                } else if (image.getCamera_make() == null) {
                    image.setCamera_make(meta.make);
                    whatToSave.add("image");
                }
                times.add(System.nanoTime());
                final String name = filePath.getFileName().toString();

                MediaFile mediaFile = newInstance ? null : mediaFileInstance.getMediaFileVersion().getMediaFile();

                MediaFileVersion mediafileVersion = MediaFileVersionService.getInstance().getMediafileVersion(imageDTO.fullhash);
                if (mediafileVersion == null) {
                    for (MediaFileVersion actMediaFileVersion : MediaFileVersionService.getInstance().getMediafileVersionsByImageHash(imageDTO.hash)) {
                        if (actMediaFileVersion.getMedia().size() == 1) {
                            if (mediaFile == null) mediaFile = actMediaFileVersion.getMediaFile();
                            else if (!mediaFile.equals(actMediaFileVersion)) {
                                //this shouldn't be
                            }
                        }
                    }
                    if (mediaFile == null) {
                        mediaFile = MediaFileService.getInstance().createMediaFile(name, meta);
                        whatToSave.add("mediaFile");
                    }
                    mediafileVersion = MediaFileVersionService.getInstance().createMediaFileVersion(name, meta, imageDTO.fullhash, fileSize, mediaFile, newInstance ? null : mediaFileInstance.getMediaFileVersion());
                    mediafileVersion.getMedia().add(new Media(mediafileVersion, image, meta));
                    whatToSave.add("version");
                } else if (!newInstance) {
                    if (!mediafileVersion.getMediaFile().equals(mediaFile)) notes += " New versions (id="+mediafileVersion.getId()+") MediaFile doesn't match with the old (id=" + mediaFileInstance.getMediaFileVersion().getId() + ") ones.";
                }

                if (newInstance) {
                    mediaFileInstance = createMediaFileInstance(name, folder, filePath, dateMod, mediafileVersion);
                    whatToSave.add("instance");
                } else {
                    if (!mediaFileInstance.getMediaFileVersion().equals(mediafileVersion)) {
                        mediaFileInstance.setMediaFileVersion(mediafileVersion);
                        whatToSave.add("instance");
                    }
                }
                times.add(System.nanoTime());
                if (original && mediaFileInstance instanceof JPGMediaFileInstance && ((JPGMediaFile)mediaFileInstance.getMediaFileVersion().getMediaFile()).isStandalone()) main = true;
            }
            if (original && !mediaFileInstance.getMediaFileVersion().equals(mediaFileInstance.getMediaFileVersion().getMediaFile().getOriginalVersion())) {
                    mediaFileInstance.getMediaFileVersion().getMediaFile().setOriginalVersion(mediaFileInstance.getMediaFileVersion());
                    whatToSave.add("mediaFile");
            }


            long WriteToDBStart = System.nanoTime();
            if (whatToSave.contains("image")) {
                for (Media media : mediaFileInstance.getMediaFileVersion().getMedia()) {
                    imageService.saveImage(media.getImage(),true);
                }
            }
            if (whatToSave.contains("mediaFile")) {
                MediaFileService.getInstance().saveMediaFile(mediaFileInstance.getMediaFileVersion().getMediaFile(),true);
            }
            if (whatToSave.contains("version")) {
                MediaFileVersionService.getInstance().saveMediaFileVersion(mediaFileInstance.getMediaFileVersion(),true);
                for (Media media : mediaFileInstance.getMediaFileVersion().getMedia()) {
                    mediaDAO.persist(media, true);
                }
            }
            if (whatToSave.contains("instance")) {
                saveMediaFileInstance(mediaFileInstance,true);
                filesInFolderMap.put(mediaFileInstance.getFilePath().toString().toLowerCase(), mediaFileInstance);
            }
            mediaDAO.flush();
            long WriteToDBEnd = System.nanoTime();
            if (false) {
                Long prevTime = null;
                for (Long time : times) {
                    if (prevTime != null) System.out.println(TimeUnit.NANOSECONDS.toMillis(time - prevTime));
                    prevTime = time;
                }
            }
        }
        return mediaFileInstance;
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