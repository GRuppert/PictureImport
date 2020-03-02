package org.nyusziful.pictureorganizer.Main;

import org.nyusziful.pictureorganizer.DAL.DAO.MediafileDAOImplHib;
import org.nyusziful.pictureorganizer.DAL.Entity.MediaFile;
import org.nyusziful.pictureorganizer.DTO.ImageDTO;
import org.nyusziful.pictureorganizer.Service.MediafileService;
import org.nyusziful.pictureorganizer.Service.Rename.RenameService;
import org.nyusziful.pictureorganizer.UI.Model.TableViewMediaFile;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.nyusziful.pictureorganizer.Service.Hash.MediaFileHash.getHash;
import static org.nyusziful.pictureorganizer.UI.StaticTools.*;

public class PresetUseCases {
    private static long fileSizeCountTotal = 0;
    private static long fileSizeCount = 0;
    private static long fileCountTotal = 0;
    private static long fileCount = 0;
    private static long prevTime = System.nanoTime();

    public static void main(String[] args) {
//        updateFolder(Paths.get("G:\\Pictures\\Photos"), 1, 0);
//        updateFolder(Paths.get("D:\\Képek"), drives.get(7), 0, false, "arw", ZoneId.systemDefault());

//        updateFolder(Paths.get("E:\\temp"), 2, 0);

//        recover();
//        updateFolder(Paths.get("E:\\Képek\\ExifBackupTest\\try1"), true,null, ZoneId.systemDefault());
        Set<MediaFile> filesFailing = new HashSet<>();
        MediafileService mfs = new MediafileService();
        final Set<MediaFile> mediaFiles = mfs.readMediaFilesFromFolderRecursive(Paths.get("E:\\Képek\\ExifBackupTest\\try1"), true, false, ZoneId.systemDefault(), filesFailing);
        HashMap<String, MediaFile> renaming = new HashMap<>();
        mediaFiles.forEach(mf -> renaming.put(mfs.getMediaFileName(mf, "6"), mf));
        renaming.forEach((newName,mf) -> mfs.renameMediaFile(mf, Paths.get(mf.getFilePath().getParent()+newName), TableViewMediaFile.WriteMethod.MOVE, false));
//        updateFolder(Paths.get("e:\\Képek\\PreImportTest\\Run"), true,null, ZoneId.systemDefault());
    }

    private void osNev() {
/*        ZoneId zone = ZoneId.systemDefault();
        int copyOrMove = MOVE;
        File dirRoot = new File("G:\\Pictures\\Photos\\Régi képek\\Dupla\\Képek");
        Path toDir = Paths.get("G:\\Pictures\\Photos\\V5");
//        File dirRoot = new File("G:\\Pictures\\Photos\\Új");
        System.out.println("---- " + dirRoot.getName() + " ----\n\n");

        File[] dirs = dirRoot.updateFolder(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return (new File(dir + "\\" + name).isDirectory());
            }});
        Collection<DirectoryElement> directoryElements = getDirectoryElementsNonRecursive(Arrays.asList(dirs), (File dir, String name) -> supportedFileType(name));

        JProgressBar progressBar;
        JDialog progressDialog;
        for(int k = 0; k < dirs.length; k++) {
            File dir1 = dirs[k];
            if(dir1.isDirectory()) {
                System.out.println(dir1.getName());
                File[] content = dir1.updateFolder((File dir, String name) -> supportedFileType(name));
                int chunkSize = 100;//At least 2, exiftool has a different output format for single files
                progressBar = new JProgressBar(0, content.length);
                progressDialog = progressDiag(progressBar);
                for (int j = 0; j*chunkSize < content.length; j++) {
                    ArrayList<File> files = new ArrayList<>();
                    for (int f = 0; (f < chunkSize) && (j*chunkSize + f < content.length); f++) {
                        files.add(content[j*chunkSize + f].getName());
                    }
                    List<Meta> exifToMeta = readFileMeta(files, dir1, zone);
                    Iterator<Meta> iterator = exifToMeta.iterator();
                    int i = 0;
                    while (iterator.hasNext()) {
                        Meta next = iterator.next();
                        //TODO null!
                        AnalyzingMediaFile media = null;
//                        AnalyzingMediaFile media = new AnalyzingMediaFile(next);
                        if (media.getProcessing()) {
                            media.write(copyOrMove);
                        } else {
                            System.out.println(media.getNewName() + ": " + media.getNote());
                        }
                        i++;
                        progressBar.setValue(i + j*chunkSize);
                    }
                }
                progressDialog.dispose();
            }
        }*/
    }

    public void test() {
/*        List<String> directories = defaultImportDirectories(new File("G:\\Pictures\\Photos\\Új\\Umag"));
        listOnScreen(createMediafileTable(fileRenameList(directories)));
/*        zone = ZoneId.of("America/Lima");
        /*
        try {
            ArrayList<String[]> readMeta = ExifReadWrite.readMeta(new File("e:\\Képek\\Dev\\ExifDamage\\orig\\DSC07620.JPG"));
            ArrayList<String[]> readMeta1 = ExifReadWrite.readMeta(new File("e:\\Képek\\Dev\\ExifDamage\\digi\\DSC07620.JPG"));
            readMeta.stream().forEach((Meta) -> System.out.println(Meta[0] + " : " + Meta[1]));
            System.out.println("-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-");
            readMeta1.stream().forEach((Meta) -> System.out.println(Meta[0] + " : " + Meta[1]));
        } catch (ImageProcessingException | IOException ex) {
            Logger.getLogger(PicOrganizes.class.getName()).log(Level.SEVERE, null, ex);
        }
        /*        ArrayList<ComparableMediaFile> readDirectoryContent = readDirectoryContent(Paths.get("E:\\temp\\compare\\"));
        for (ComparableMediaFile mFile : readDirectoryContent) {
        System.out.println(mFile.file.getName());
        System.out.println("-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-");
        ArrayList<String[]> readMeta = StaticTools.readMeta(mFile.file);
        for (String[] tagValue : readMeta) {
        System.out.println(tagValue[0] + " : " + tagValue[1]);
        }
        }
        /*        String fullHash = "";
        StringBuilder builder = new StringBuilder();
        File input = new File("K:\\Képek\\Photos\\Nagyok\\Japán\\Nyers");
        long startTime = System.nanoTime();
        try {
        Files.walkFileTree (input.toPath(), new SimpleFileVisitor<Path>() {
        @Override public FileVisitResult
        visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        if (!attrs.isDirectory() && attrs.isRegularFile() && supportedFileType(file.getFileName().toString())) {
        builder.append(StaticTools.getFullHash(file.toFile())).append(" ").append(file.getFileName()).append("\n");
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
        FileUtils.writeStringToFile(new File("e:\\java.md5"), builder.toString(), "ISO-8859-1");
        } catch (Exception ex) {
        Logger.getLogger(PicOrganizes.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println(fullHash + " : " + TimeUnit.NANOSECONDS.toMillis(System.nanoTime()-startTime));
        startTime = System.nanoTime();
        try {
        fullHash = StaticTools.getFullHashPS(input);
        } catch (IOException ex) {
        Logger.getLogger(PicOrganizes.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println(fullHash + " : " + TimeUnit.NANOSECONDS.toMillis(System.nanoTime()-startTime));
        /*
        osNev();
        try {
        Path move = Files.move(Paths.get("E:\\temp\\V5\\K2016-05-2_6@19-3_4-36(+0200)(Thu)-9e56374d932984f18cdf67adfdd5789d-9e56374d932984f18cdf67adfdd5789d-_DSC1920.ARW"), Paths.get("G:\\Pictures\\Photos\\V5\\Közös\\V5\\V5_K2016-05-2_6@19-3_4-36(+0200)(Thu)-null-4078f129a436f88812c97b9ae7500199-_DSC1920.ARW"));
        } catch (IOException e) {
        StaticTools.errorOut("Test", e);
        }
        /*        try {
        ArrayList<String> hash = StaticTools.getHash(Paths.get("E:\\rosszJPG"));
        System.out.println("h");
        } catch (IOException ex) {
        Logger.getLogger(PicOrganizes.class.getName()).log(Level.SEVERE, null, ex);
        }
        /*        StaticTools.copyAndBackup(new File("F:\\proba.arw"), new File("E:\\proba.arw"), new File("G:\\proba.arw"));
        String[] command = new String[]{"exiftool", "-a", "-G1", "20160325_130704_ILCE-5100-DSC05306.JPG"};
        //        String[] command = new String[]{"exiftool", "-overwrite_original", "-n", "-DateTimeOriginal=2017:05:10 21:10:36+02:00", "-DocumentID=\"48f57c56c937f9dfe8ffdf73ee979c56\" ", "-OriginalDocumentID=\"48f57c56c937f9dfe8ffdf73ee979c56\" ", "DSC06063.JPG"};
        ArrayList<String> exifTool = StaticTools.exifTool(command, new File("E:\\jatszoter\\jpg"));
        String[] command2 = new String[]{"exiftool", "-a", "-G1", "20160325_050704_ILCE-5100-DSC05306_2.JPG"};
        ArrayList<String> exifTool2 = StaticTools.exifTool(command2, new File("E:\\jatszoter\\jpg"));
        Iterator<String> iterator = exifTool.iterator();
        while (iterator.hasNext()) {
        String next = iterator.next();
        if (!exifTool2.remove(next)) {
        System.out.println(next);
        }
        }
        System.out.println("----------------------------------");
        Iterator<String> iterator2 = exifTool2.iterator();
        while (iterator2.hasNext()) {
        System.out.println(iterator2.next());
        }
         *       osNev();
        if (true) return;
        /*        StringBuffer str = getDirHash(new File("E:\\KÃƒÂ©pek"), new StringBuffer());
        PrintWriter writer = null;
        try {
        writer = new PrintWriter(new File("E:\\hash.txt"), "UTF-8");
        writer.println(str);
        } catch (FileNotFoundException ex) {
        Logger.getLogger(PicOrganizes.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
        Logger.getLogger(PicOrganizes.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
        writer.flush();
        }
         */
//            readMetaDataTest(new File("e:\\DSC07914.ARW"));
//            removeFiles();
//            compare();


            /*        ArrayList<ComparableMediaFile> readDirectoryContent = readDirectoryContent(Paths.get("E:\\temp\\compare\\"));
            for (ComparableMediaFile mFile : readDirectoryContent) {
            System.out.println(mFile.file.getName());
            System.out.println("-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-");
            ArrayList<String[]> readMeta = StaticTools.readMeta(mFile.file);
            for (String[] tagValue : readMeta) {
            System.out.println(tagValue[0] + " : " + tagValue[1]);
            }
            }

            /*        String fullHash = "";
            StringBuilder builder = new StringBuilder();
            File input = new File("K:\\Képek\\Photos\\Nagyok\\Japán\\Nyers");
            long startTime = System.nanoTime();
            try {
            Files.walkFileTree (input.toPath(), new SimpleFileVisitor<Path>() {
            @Override public FileVisitResult
            visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            if (!attrs.isDirectory() && attrs.isRegularFile() && supportedFileType(file.getFileName().toString())) {
            builder.append(StaticTools.getFullHash(file.toFile())).append(" ").append(file.getFileName()).append("\n");
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
            FileUtils.writeStringToFile(new File("e:\\java.md5"), builder.toString(), "ISO-8859-1");
            } catch (Exception ex) {
            Logger.getLogger(PicOrganizes.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println(fullHash + " : " + TimeUnit.NANOSECONDS.toMillis(System.nanoTime()-startTime));
            startTime = System.nanoTime();
            try {
            fullHash = StaticTools.getFullHashPS(input);
            } catch (IOException ex) {
            Logger.getLogger(PicOrganizes.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println(fullHash + " : " + TimeUnit.NANOSECONDS.toMillis(System.nanoTime()-startTime));
            /*
            osNev();
            try {
            Path move = Files.move(Paths.get("E:\\temp\\V5\\K2016-05-2_6@19-3_4-36(+0200)(Thu)-9e56374d932984f18cdf67adfdd5789d-9e56374d932984f18cdf67adfdd5789d-_DSC1920.ARW"), Paths.get("G:\\Pictures\\Photos\\V5\\Közös\\V5\\V5_K2016-05-2_6@19-3_4-36(+0200)(Thu)-null-4078f129a436f88812c97b9ae7500199-_DSC1920.ARW"));
            } catch (IOException e) {
            StaticTools.errorOut("Test", e);
            }
            /*        try {
            ArrayList<String> hash = StaticTools.getHash(Paths.get("E:\\rosszJPG"));
            System.out.println("h");
            } catch (IOException ex) {
            Logger.getLogger(PicOrganizes.class.getName()).log(Level.SEVERE, null, ex);
            }
            /*        StaticTools.copyAndBackup(new File("F:\\proba.arw"), new File("E:\\proba.arw"), new File("G:\\proba.arw"));
            String[] command = new String[]{"exiftool", "-a", "-G1", "20160325_130704_ILCE-5100-DSC05306.JPG"};
            //        String[] command = new String[]{"exiftool", "-overwrite_original", "-n", "-DateTimeOriginal=2017:05:10 21:10:36+02:00", "-DocumentID=\"48f57c56c937f9dfe8ffdf73ee979c56\" ", "-OriginalDocumentID=\"48f57c56c937f9dfe8ffdf73ee979c56\" ", "DSC06063.JPG"};
            ArrayList<String> exifTool = StaticTools.exifTool(command, new File("E:\\jatszoter\\jpg"));
            String[] command2 = new String[]{"exiftool", "-a", "-G1", "20160325_050704_ILCE-5100-DSC05306_2.JPG"};
            ArrayList<String> exifTool2 = StaticTools.exifTool(command2, new File("E:\\jatszoter\\jpg"));
            Iterator<String> iterator = exifTool.iterator();
            while (iterator.hasNext()) {
            String next = iterator.next();
            if (!exifTool2.remove(next)) {
            System.out.println(next);
            }
            }
            System.out.println("----------------------------------");
            Iterator<String> iterator2 = exifTool2.iterator();
            while (iterator2.hasNext()) {
            System.out.println(iterator2.next());
            }
            *       osNev();
            if (true) return;
            /*        StringBuffer str = getDirHash(new File("E:\\KÃƒÂ©pek"), new StringBuffer());
            PrintWriter writer = null;
            try {
            writer = new PrintWriter(new File("E:\\hash.txt"), "UTF-8");
            writer.println(str);
            } catch (FileNotFoundException ex) {
            Logger.getLogger(PicOrganizes.class.getName()).log(Level.SEVERE, null, ex);
            } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(PicOrganizes.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
            writer.flush();
            }
            */
//            readMetaDataTest(new File("e:\\DSC07914.ARW"));
//            removeFiles();
//            compare();

    }

    public static JDialog progressDiag(JProgressBar bar) {
        JDialog progressDialog = new JDialog(null, Dialog.ModalityType.MODELESS);
        JPanel newContentPane = new JPanel();
        newContentPane.setOpaque(true); //content panes must be opaque
        progressDialog.setContentPane(newContentPane);
        bar.setValue(0);
        bar.setStringPainted(true);
        newContentPane.add(bar);
        progressDialog.pack();
        progressDialog.setVisible(true);
        return progressDialog;
    }

    private static void recover()  {
        Path path = Paths.get("E:\\Képek");
        MediafileDAOImplHib dao = new MediafileDAOImplHib();
        try {
            Files.walkFileTree (path, new SimpleFileVisitor<Path>() {
                @Override public FileVisitResult
                visitFile(Path filePath, BasicFileAttributes attrs) {
                    if (!attrs.isDirectory() && attrs.isRegularFile() && filePath.getFileName().toString().endsWith("-null") ) {
                        String hash = filePath.getFileName().toString().substring(39, 71);
                        List<String> mediaFile = dao.getByHash(hash, 7);
                        if (mediaFile.size()==1) {
                            String oldFilename = mediaFile.get(0);
                            if (oldFilename.matches("D.C[0-9]{5}\\.ARW"))
                                RenameService.write(filePath, Paths.get(filePath.getParent() + "\\" + oldFilename), TableViewMediaFile.WriteMethod.MOVE, false);
                        } else {
                            System.out.println("More filenames");
                        }

                    }
                    return FileVisitResult.CONTINUE;
                }

                @Override public FileVisitResult
                visitFileFailed(Path file, IOException exc) {
    //                        StaticTools.errorOut(file.toString(), exc);
                    // Skip folders that can't be traversed
                    return FileVisitResult.CONTINUE;
                }

                @Override public FileVisitResult
                postVisitDirectory (Path dir, IOException exc) {
    //                        if (exc != null)
    //                        StaticTools.errorOut(dir.toString(), exc);
                    // Ignore errors traversing a folder
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Media Hash
     * Hash
     * File
     *
     * @param path
     */
/*    private static void updateFolder(Path path, boolean original, String extension, ZoneId zone) {
        fileSizeCountTotal = 0;
        fileSizeCount = 0;
        fileCountTotal = 0;
        fileCount = 0;
        String filename = "";
        int blockSize = 100;
//        int blockSize = 1000;
        boolean force = true;
        final DriveService driveService = new DriveService();
        final FolderService folderService = new FolderService();
        final MediafileService mediafileService = new MediafileService();
        final ImageService imageService = new ImageService();
        final RenameService renameService = new RenameService();
        Drive drive = driveService.getLocalDrive(path.toString().substring(0, 1));
        try {
            Set<Path> mediafolders = new HashSet<>();
            Files.walkFileTree (path, new SimpleFileVisitor<Path>() {
                @Override public FileVisitResult
                visitFile(Path filePath, BasicFileAttributes attrs) throws IOException {
                    if (!attrs.isDirectory() && attrs.isRegularFile() && supportedFileType(filePath.getFileName().toString()) && (extension == null || filePath.getFileName().toString().toLowerCase().endsWith(extension)) ) {
                        fileSizeCountTotal += attrs.size();
                        fileCountTotal++;
                        mediafolders.add(filePath.getParent());
//                            System.out.println(file.getFileName());
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
            HashMap<String, Folder> folders = new HashMap<>();
            Set<Folder> folderstosave = new HashSet<>();
            for (Path folder : mediafolders) {
                Folder folderSaved = folderService.getFolder(drive, folder);
                if (folderSaved == null) {
                    folderSaved = new Folder(drive, folder);
                    folderstosave.add(folderSaved);
                }
                folders.put(folderSaved.getJavaPath().toString().toLowerCase(), folderSaved);
            }
            folderService.persistFolder(folderstosave);
            JProgressBar progressBar = new JProgressBar(0, (int)(fileSizeCountTotal/1000000));
            JDialog progressDialog = progressDiag(progressBar);
            Set<MediaFile> filesFailing = new HashSet<>();
            Set<MediaFile> filesToSave = new HashSet<>();
            Set<Image> imagesToSave = new HashSet<>();
            final long startTime = System.nanoTime();
            HashMap<String,Image> images = new HashMap<String, Image>();
            imageService.getImages().forEach(image -> {images.put(image.getHash()+image.getType(), image);});
            HashMap<String, MediaFile> fileSet = new HashMap<>();
            List<MediaFile> byDriveId = mediafileService.getMediaFilesFromPath(path);
            for (MediaFile file : byDriveId) {
                fileSet.put(file.getFilePath().toString().toLowerCase(), file);
            }
            HashMap<String, MediaFile> processed = new HashMap<>();
            Files.walkFileTree (path, new SimpleFileVisitor<Path>() {
                @Override public FileVisitResult
                visitFile(Path filePath, BasicFileAttributes attrs) {
                    if (!attrs.isDirectory() && attrs.isRegularFile() && supportedFileType(filePath.getFileName().toString()) && (extension == null || filePath.getFileName().toString().toLowerCase().endsWith(extension)) && !processed.keySet().contains(filePath.toString().toLowerCase())) {
                        Boolean fileOriginal = original;
                        boolean fileToSave = false;
                        boolean imageToSave = false;
                        MediaFile actFile = fileSet.get(filePath.toString().toLowerCase());
                        final Folder folder = folders.get(filePath.getParent().toString().toLowerCase());
                        assert folder != null;
                        final Timestamp dateMod = new Timestamp(attrs.lastModifiedTime().toMillis());
                        dateMod.setNanos(0);
                        final long fileSize = attrs.size();
                        if (actFile == null) {
                            final String name = filePath.getFileName().toString();
                            if (supportedRAWFileType(name)) {
                                actFile = new RAWMediaFile(drive, folder, filePath, fileSize, dateMod, original);
                                if (!((RAWMediaFile)actFile).isXMPattached()) {
                                    ((RAWMediaFile)actFile).setXMPattached(createXmp(filePath.toFile()) != null);
                                }
                            } else if (supportedJPGFileType(name)) {
                                actFile = new JPGMediaFile(drive, folder, filePath, fileSize, dateMod, original);
                            } else {
                                actFile = new MediaFile(drive, folder, filePath, fileSize, dateMod, original);
                            }
                        } else {
                            fileOriginal = !Boolean.FALSE.equals(fileOriginal) && !Boolean.FALSE.equals(actFile.isOriginal()) && !(fileOriginal == null && actFile.isOriginal() == null);
                            if (fileOriginal) {
                                if (actFile instanceof JPGMediaFile && !((JPGMediaFile)actFile).isExifbackup()) {
                                    if (((JPGMediaFile) actFile).addExifbackup()) fileToSave = true;
                                }
                            }
                        }
                        if (force || actFile.getId() < 0 || actFile.getSize() != fileSize || actFile.getDateMod().compareTo(dateMod) != 0) {
                            System.out.println("Hashing: " + filePath + "/" + filename);
                            actFile.setDateMod(dateMod);
                            actFile.setSize(fileSize);
                            ImageDTO imageDTO = getHash(filePath.toFile());
                            Image image;
                            if (images.containsKey(imageDTO.hash+imageDTO.type)) {
                                image = images.get(imageDTO.hash+imageDTO.type);
                            } else {
                                image = imageService.getImage(imageDTO);
                                if (image == null) {
                                    image = new Image(imageDTO.hash, imageDTO.type);
                                    imageToSave = true;
                                }
                                images.put(image.getHash()+image.getType(), image);
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
                            fileSizeCount += fileSize;
                            fileCount++;
                        } else {
                            fileSizeCountTotal -= fileSize;
                            fileCountTotal--;
                        }
                        if (Boolean.TRUE.equals(fileOriginal)) {
                            try {
                                final boolean updated = mediafileService.updateOriginalImage(actFile);
                                fileToSave = fileToSave || updated;
                                if (updated) imageToSave = true;
                            } catch (Exception e) {
                                filesFailing.add(actFile);
                            }
                        }



                        if (renameService.rename(actFile)) fileToSave = true;

//                        long toSeconds2 = TimeUnit.NANOSECONDS.toMillis(System.nanoTime()-startTime);
//                        System.out.println((toSeconds2-toSeconds) + "s to DAL.");
//                        long secondsBetween = TimeUnit.NANOSECONDS.toMillis(System.nanoTime()-prevTime);
//                        System.out.println(secondsBetween + "s since the last iteration.");

                        if (actFile instanceof RAWMediaFile && !((RAWMediaFile)actFile).isXMPattached()) {
                            createXmp(filePath.toFile());
                        }
                        processed.put(actFile.getFilePath().toString().toLowerCase(), actFile);

                        prevTime = System.nanoTime();
                        if (imageToSave) {
                            imagesToSave.add(actFile.getImage());
                        }
                        if (fileToSave) {
                            filesToSave.add(actFile);
                            fileSet.put(actFile.getFilePath().toString().toLowerCase(), actFile);
                        }

                        if (filesToSave.size() > 0 && (filesToSave.size() % blockSize) == 0) {
                            System.out.println("Writting files to DAL");
                            long insertTime = System.nanoTime();
                            imageService.persistImage(imagesToSave);
                            imagesToSave.clear();
                            mediafileService.persistMediaFiles(filesToSave);
                            filesToSave.clear();
                            mediafileService.flush();
                            System.out.println(blockSize + " files written to DAL in " + TimeUnit.NANOSECONDS.toMillis(System.nanoTime()-insertTime));
                        }
                        if (fileCount > 0 && (fileCount % 10000) == 0) {
                            long toSeconds3 = TimeUnit.NANOSECONDS.toMillis(System.nanoTime()-startTime);
                            System.out.print("Time elasped: " + toSeconds3 + "s Data processed: " + fileSizeCount / 1048576 + "MB from " + fileCount + " files");
                            if (toSeconds3 > 0 && fileSizeCount > 0) {
                                System.out.println(" Avg. speed " + fileSizeCount / 1048576 / toSeconds3 + "MB/s ETA: " + toSeconds3 * (fileSizeCountTotal - fileSizeCount) / fileSizeCount + "s Data left: " + (fileSizeCountTotal - fileSizeCount) / 1048576 + "MB from " + (fileCountTotal - fileCount) + " files");
                            } else {
                                System.out.println();
                            }
                        }
                        progressBar.setValue((int)(fileSizeCount/1000000));
                    }
                    return FileVisitResult.CONTINUE;
                }

                @Override public FileVisitResult
                visitFileFailed(Path file, IOException exc) {
//                        StaticTools.errorOut(file.toString(), exc);
                    // Skip folders that can't be traversed
                    return FileVisitResult.CONTINUE;
                }

                @Override public FileVisitResult
                postVisitDirectory (Path dir, IOException exc) {
//                        if (exc != null)
//                        StaticTools.errorOut(dir.toString(), exc);
                    // Ignore errors traversing a folder
                    return FileVisitResult.CONTINUE;
                }
            });
            imageService.persistImage(imagesToSave);
            mediafileService.persistMediaFiles(filesToSave);
            //Clean up removed files
            processed.keySet().forEach(key -> {fileSet.remove(key);});
            mediafileService.deleteMediaFiles(fileSet.values());
            System.out.println("Failed Files:");
            filesFailing.forEach(file -> System.out.println(file.getFilePath()));

            System.out.println("Rédi");
            progressDialog.dispose();
        } catch (IOException e) {
            throw new AssertionError ("walkFileTree will not throw IOException if the FileVisitor does not");
        }
    }
*/
    /**
     * Renames each @supportedMediaFileType to "hash + name"
     * @param dir
     * @param str
     * @return the string log of "hash + name" data
     */
    private StringBuffer getDirHash(File dir, StringBuffer str) {
        if(dir.isDirectory()) {
            File[] dirs = dir.listFiles(new FilenameFilter() {
                public boolean accept(File dir, String name) {
                    return (new File(dir + "\\" + name).isDirectory());
                }});
            for(int i = 0; i < dirs.length; i++) {
                str = getDirHash(dirs[i], str);
            }
            File[] content = dir.listFiles(new FilenameFilter() {
                public boolean accept(File dir, String name) {
                    return supportedMediaFileType(name);
                }});
            for(int i = 0; i < content.length; i++) {
                ImageDTO image = getHash(content[i]);
                str.append(image.hash + "\t" + content[i] + "\n");
                content[i].renameTo(new File(content[i].getParentFile() + "\\" + image.hash + content[i].getName()));
            }
        }
        return str;
    }
}
