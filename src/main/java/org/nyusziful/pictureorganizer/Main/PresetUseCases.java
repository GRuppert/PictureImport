package org.nyusziful.pictureorganizer.Main;

import org.nyusziful.pictureorganizer.DAL.Entity.Drive;
import org.nyusziful.pictureorganizer.DAL.Entity.Image;
import org.nyusziful.pictureorganizer.DTO.ImageDTO;
import org.nyusziful.pictureorganizer.DTO.Meta;
import org.nyusziful.pictureorganizer.Service.DriveService;
import org.nyusziful.pictureorganizer.Service.ExifUtils.ExifService;
import org.nyusziful.pictureorganizer.Service.ImageService;
import org.nyusziful.pictureorganizer.Service.Rename.RenameService;
import org.nyusziful.pictureorganizer.UI.StaticTools;
import org.nyusziful.pictureorganizer.DAL.Entity.Mediafile;
import org.nyusziful.pictureorganizer.Service.MediafileService;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static org.nyusziful.pictureorganizer.Service.Hash.MediaFileHash.getFullHash;
import static org.nyusziful.pictureorganizer.Service.Hash.MediaFileHash.getHash;
import static org.nyusziful.pictureorganizer.UI.StaticTools.getDirectoryElementsNonRecursive;
import static org.nyusziful.pictureorganizer.UI.StaticTools.supportedFileType;
import static org.nyusziful.pictureorganizer.UI.StaticTools.supportedMediaFileType;

public class PresetUseCases {
    private static long fileSizeCountTotal = 0;
    private static long fileSizeCount = 0;
    private static long fileCountTotal = 0;
    private static long fileCount = 0;
    private static long prevTime = System.nanoTime();

    public static void main(String[] args) {
//        listFiles(Paths.get("G:\\Pictures\\Photos"), 1, 0);
        final DriveService driveService = new DriveService();
        final List<Drive> drives = driveService.getDrives();
        listFiles(Paths.get("D:\\Képek"), drives.get(7), 0, false, "arw", ZoneId.systemDefault());

//        listFiles(Paths.get("E:\\temp"), 2, 0);
//        listFiles(Paths.get("E:\\Képek"), 7, 0);
    }

    private void osNev() {
/*        ZoneId zone = ZoneId.systemDefault();
        int copyOrMove = MOVE;
        File dirRoot = new File("G:\\Pictures\\Photos\\Régi képek\\Dupla\\Képek");
        Path toDir = Paths.get("G:\\Pictures\\Photos\\V5");
//        File dirRoot = new File("G:\\Pictures\\Photos\\Új");
        System.out.println("---- " + dirRoot.getName() + " ----\n\n");

        File[] dirs = dirRoot.listFiles(new FilenameFilter() {
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
                File[] content = dir1.listFiles((File dir, String name) -> supportedFileType(name));
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


    /**
     * Media Hash
     * Hash
     * File
     *
     * @param path
     * @param start
     */
    private static void listFiles(Path path, Drive drive, int start, boolean original, String extension, ZoneId zone) {
        fileSizeCountTotal = 0;
        fileSizeCount = 0;
        fileCountTotal = 0;
        fileCount = 0;
        String filename = "";
        int blockSize = 1000;
        boolean force = false;
        final MediafileService mediafileService = new MediafileService();
        final ImageService imageService = new ImageService();
        final RenameService renameService = new RenameService();
        List<Mediafile> mediafiles = mediafileService.getMediafiles();
        try {
            Files.walkFileTree (path, new SimpleFileVisitor<Path>() {
                @Override public FileVisitResult
                visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    if (!attrs.isDirectory() && attrs.isRegularFile() && supportedFileType(file.getFileName().toString())) {
                        fileSizeCountTotal += attrs.size();
                        fileCountTotal++;
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
            JProgressBar progressBar = new JProgressBar(0, (int)(fileSizeCountTotal/1000000));
            JDialog progressDialog = progressDiag(progressBar);
            Set<Mediafile> files = new HashSet<>();
            long startTime = System.nanoTime();
            HashMap<String,Image> images = new HashMap<String, Image>();
            Files.walkFileTree (path, new SimpleFileVisitor<Path>() {
                @Override public FileVisitResult
                visitFile(Path filePath, BasicFileAttributes attrs) {
                    if (!attrs.isDirectory() && attrs.isRegularFile() && supportedFileType(filePath.getFileName().toString()) && (extension != null && filePath.getFileName().endsWith(extension)) ) {
                        long startTime = System.nanoTime();
                        long toSeconds = 0;
                        boolean fileToSave = false;
                        Mediafile actFile = mediafileService.getMediaFile(new Mediafile(drive, filePath, attrs.size(), new Timestamp(attrs.lastModifiedTime().toMillis())));
                        if (force || actFile.getId() < 0) {
                            System.out.println("Hashing: " + path + "/" + filename);
                            ImageDTO imageDTO = getHash(filePath.toFile());
                            Image image;
                            if (images.containsKey(imageDTO.hash+imageDTO.type)) {
                                image = images.get(imageDTO.hash+imageDTO.type);
                            } else {
                                image = imageService.getImage(imageDTO);
                                if (image.getId() < 0) {
                                    Meta meta = ExifService.readMeta(filePath.toFile(), zone);
                                    image.setDateTaken(meta.date);
                                    image.setOriginalFilename(actFile.getFilename());
                                    images.put(image.getHash()+image.getType(), image);
                                }
                            }
                            actFile.setImage(image);

                            actFile.setFilehash(getFullHash(filePath.toFile()));
                            toSeconds = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime);
                            if (toSeconds > 0)
                                System.out.println(toSeconds + "s. Hash speed " + attrs.size() * 2 / 1048576 / toSeconds + "MB/s");
                            fileToSave = true;
                            fileSizeCount += attrs.size();
                            fileCount++;
                        } else {
                            fileSizeCountTotal -= attrs.size();
                            fileCountTotal--;
                        }
                        if (renameService.rename(actFile)) fileToSave = true;
/*
                        long toSeconds2 = TimeUnit.NANOSECONDS.toMillis(System.nanoTime()-startTime);
                        System.out.println((toSeconds2-toSeconds) + "s to DAL.");
                        long secondsBetween = TimeUnit.NANOSECONDS.toMillis(System.nanoTime()-prevTime);
                        System.out.println(secondsBetween + "s since the last iteration.");
*/
                        prevTime = System.nanoTime();
                        if (fileToSave) {
                            files.add(actFile);
                        }
                        if (files.size() > 0 && (files.size() % blockSize) == 0) {
                            System.out.println("Writting files to DAL");
                            long insertTime = System.nanoTime();
                            mediafileService.saveMediafile(files);
                            files.clear();
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
//                            pw.flush();
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
            mediafileService.saveMediafile(files);
            System.out.println("Rédi");
            progressDialog.dispose();
        } catch (IOException e) {
            throw new AssertionError ("walkFileTree will not throw IOException if the FileVisitor does not");
        }
    }

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
