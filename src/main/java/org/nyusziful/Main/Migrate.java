package org.nyusziful.Main;

import org.nyusziful.Rename.AnalyzingMediaFile;
import org.nyusziful.Rename.Meta;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.ZoneId;
import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.nyusziful.ExifUtils.ExifReadWrite.readFileMeta;
import static org.nyusziful.Hash.MediaFileHash.getHash;
import static org.nyusziful.Main.StaticTools.getDirectoryElementsNonRecursive;
import static org.nyusziful.Main.StaticTools.supportedFileType;
import static org.nyusziful.Main.StaticTools.supportedMediaFileType;

public class Migrate {
    private long fileSizeCountTotal = 0;
    private long fileSizeCount = 0;
    private long fileCountTotal = 0;
    private long fileCount = 0;

    public static void main(String[] args) {

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
        writer.close();
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
            writer.close();
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
     * Orig Hash
     *
     * @param path
     * @param start
     */

    private void listFiles(Path path, int start) {
        fileSizeCountTotal = 0;
        fileSizeCount = 0;
        fileCountTotal = 0;
        fileCount = 0;
        String filename = "";
        try
        {
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
            PrintWriter pw = new PrintWriter(new File("e:\\test2.csv"));
            long startTime = System.nanoTime();
            String delimiter = "\t";
            Files.walkFileTree (path, new SimpleFileVisitor<Path>() {
                @Override public FileVisitResult
                visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    if (!attrs.isDirectory() && attrs.isRegularFile() && supportedFileType(file.getFileName().toString())) {
                        StringBuilder sb = new StringBuilder();
                        String hash = getHash(file.toFile());
                        sb.append(start + fileCount).append(delimiter);
                        sb.append(hash).append(delimiter);
                        sb.append(hash).append(delimiter);
                        sb.append(delimiter);
                        sb.append(file.getParent().toString().substring(2)).append(delimiter);
                        sb.append(file.getFileName()).append(delimiter);
                        sb.append(attrs.size());
                        sb.append('\n');
                        pw.write(sb.toString());
                        fileSizeCount += attrs.size();
                        fileCount++;
                        if ((fileCount % 10000) == 0) {
                            long toSeconds = TimeUnit.NANOSECONDS.toMillis(System.nanoTime()-startTime);
                            System.out.print("Time elasped: " + toSeconds + "s Data processed: " + fileSizeCount/1048576 + "MB from " + fileCount + " files" );
                            System.out.println(" Avg. speed " + fileSizeCount/1048576/toSeconds + "MB/s ETA: " + toSeconds*(fileSizeCountTotal - fileSizeCount)/fileSizeCount + "s Data left: " + (fileSizeCountTotal-fileSizeCount)/1048576 + "MB from " + (fileCountTotal-fileCount) + " files");
                            pw.flush();
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
            pw.close();
            progressDialog.dispose();
        }
        catch (IOException e)
        {
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
                String hash = getHash(content[i]);
                str.append(hash + "\t" + content[i] + "\n");
                content[i].renameTo(new File(content[i].getParentFile() + "\\" + hash + content[i].getName()));
            }
        }
        return str;
    }
}
