/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nyusziful.pictureorganizer.UI;

import java.awt.Toolkit;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileSystemView;

import org.apache.commons.io.FilenameUtils;

/**
 *
 * @author gabor
 */
public class StaticTools {
    public static DateTimeFormatter ExifDateFormat = DateTimeFormatter.ofPattern("yyyy:MM:dd HH:mm:ss");//2016:11:24 20:05:46
    public static DateTimeFormatter ExifDateFormatTZ = DateTimeFormatter.ofPattern("yyyy:MM:dd HH:mm:ssXXX");//2016:11:24 20:05:46+02:00
    public static DateTimeFormatter XmpDateFormat = DateTimeFormatter.ISO_DATE_TIME;//2016-11-24T20:05:46
    public static DateTimeFormatter XmpDateFormatTZ = DateTimeFormatter.ISO_OFFSET_DATE_TIME;//2016-11-24T20:05:46+02:00
    public static DateTimeFormatter MP4DateFormatTZ = DateTimeFormatter.ofPattern("E MMM dd HH:mm:ss XXX yyyy");//Do Sep 06 20:36:46 +02:00 2018

    public static void main(String[] args) {
        final Collection<String> strings = importDirectories(new File("E:\\work\\DATA\\FolderTest"));
        DateTimeFormatter MP4DateFormatTZ = DateTimeFormatter.ofPattern("E MMM dd HH:mm:ss XXX yyyy");//Do Sep 06 20:36:46 +02:00 2018
        ZonedDateTime zdt = ZonedDateTime.of ( LocalDate.of ( 2018 , 9 , 6 ) , LocalTime.of ( 20 , 36, 46 ) , ZoneId.systemDefault() );
        String zdtString = MP4DateFormatTZ.format(zdt);
        System.out.println(zdtString);
    }

    static String[] Sony = {
            "\\DCIM\\",
            "\\PRIVATE\\AVCHD\\BDMV\\STREAM\\",
            "\\PRIVATE\\M4ROOT\\CLIP\\"
    };
    static String[] Android = {
            "\\DCIM\\Camera",
            "\\WhatsApp\\Media\\WhatsApp Images"
    };

    static String[] imageFiles = {
        "jpg",
        "jpeg",
        "png",
        "gif",
        "tif",
        "arw",
        "nef",
        "dng",
        "nar"            
    };
    static String[] JPGFiles = {
        "jpg",
        "jpeg"
    };
    static String[] RAWFiles = {
        "tif",
        "arw",
        "nef",
        "dng"
    };
    static String[] videoFiles = {
        "avi",
        "mpg",
        "mp4",
        "mts",
        "3gp",
        "mov"
    };
    static String[] metaFiles = {
        "gpx",
        "sfv",
        "pdf",
        "doc",
        "xls",
        "xlsx"
    };

    public static Boolean supportedFileType(String name) {
        if (supportedMetaFileType(name)) return true;
        if (supportedMediaFileType(name)) return true;
        return false;
    }
    public static Boolean supportedMetaFileType(String name) {
        String ext = FilenameUtils.getExtension(name.toLowerCase());
        for (String extSupported : metaFiles) {
            if (ext.equals(extSupported)) return true;
        }
        return false;

    }
    public static Boolean supportedMediaFileType(String name) {
        String ext = FilenameUtils.getExtension(name.toLowerCase());
        for (String extSupported : imageFiles) {
            if (ext.equals(extSupported)) return true;
        }
        return supportedVideoFileType(name);
    }
    public static Boolean supportedVideoFileType(String name) {
        String ext = FilenameUtils.getExtension(name.toLowerCase());
        for (String extSupported : videoFiles) {
            if (ext.equals(extSupported)) return true;
        }
        return false;
    }
    public static Boolean supportedRAWFileType(String name) {
        String ext = FilenameUtils.getExtension(name.toLowerCase());
        for (String extSupported : RAWFiles) {
            if (ext.equals(extSupported)) return true;
        }
        return false;

    }
    public static Boolean supportedJPGFileType(String name) {
        String ext = FilenameUtils.getExtension(name.toLowerCase());
        for (String extSupported : JPGFiles) {
            if (ext.equals(extSupported)) return true;
        }
        return false;

    }



    /**
     * Creates a List with the predefined standard directories on recognized volumes
     * @return a List of String which are the default on the recognized media
     */
    public static Collection<String> defaultImportDirectories() {
        File[] paths;
        FileSystemView fsv = FileSystemView.getFileSystemView();
        paths = File.listRoots();
        Set<String> results = new HashSet<>();
        for(File path:paths) {
            String desc = fsv.getSystemTypeDescription(path);
            if (desc.startsWith("USB") || desc.startsWith("SD")) results.addAll(importDirectories(path));
        }
        return results;
    }

    public static Collection<String> importDirectories(File folder) {
        boolean valid = true;
        Set<String> resultDirs = new HashSet<>();
        if (folder == null || !folder.isDirectory()) return resultDirs;
        String absolutePath = folder.getAbsolutePath();
        if (absolutePath.endsWith("\\")) absolutePath = absolutePath.substring(0, absolutePath.length()-1);
        for(String criteria:Sony) {
            File probe = new File(absolutePath+criteria);
            if(probe.exists() && probe.isDirectory()) {
                continue;
            }
            valid = false;
            break;
        }
        if (valid) {
            for (File subdir:new File(absolutePath+Sony[0]).listFiles((File dir, String name) -> dir.isDirectory())) {
                resultDirs.add(subdir.toString());
            }
            resultDirs.add(absolutePath+Sony[1]);
            resultDirs.add(absolutePath+Sony[2]);
        }
        return resultDirs;
    }

    /**
     * Open up a <code> JOptionPane </code> with the given parameters
     * @param source description where the Exception is coming from, used as the header of the Pane
     * @param e the Exception which was thrown, will be prompted as the text of the Pane
     */
    public static void errorOut(String source, Exception e) {
        JOptionPane.showMessageDialog(null, "From :" + source + "\nMessage: " + e.toString(), "Error", JOptionPane.ERROR_MESSAGE);
    }

    /** Generates a windows beep */
    public static void beep() {
        final Runnable runnable = (Runnable) Toolkit.getDefaultToolkit().getDesktopProperty("win.sound.exclamation");
        if (runnable != null) runnable.run();
    }
    
    public static ZonedDateTime getTimeFromStr(String input, ZoneId zone) {
        ZonedDateTime result = null;
        try {
            result = LocalDateTime.parse(input, ExifDateFormat).atZone(zone);
        } catch (DateTimeParseException e) {
            try {
                result = LocalDateTime.parse(input, XmpDateFormat).atZone(zone);
            } catch (DateTimeParseException e1) {
            }
        }
        return result;
    }

    public static ZonedDateTime getZonedTimeFromStr(String input) {
        ZonedDateTime result = null;
        if (input != null)
            try {
                result = OffsetDateTime.parse(input, ExifDateFormatTZ).toZonedDateTime();
            } catch (DateTimeParseException e) {
                try {
                    result = OffsetDateTime.parse(input, XmpDateFormatTZ).toZonedDateTime();
                } catch (DateTimeParseException e1) {
                }
            }
        return result;
    }
   
    /**
     * Opens up a <code> DirectoryChooser </code>
     * @param dir set the home directory if it's not valid "C:\" will be used
     * @return a <code> File </code> object of the chosen path
     */
    public static File getDir(File dir) {
        DirectoryChooser chooser = new DirectoryChooser();
        if (Files.exists(dir.toPath()))
            chooser.setInitialDirectory(dir);
        else
            chooser.setInitialDirectory(new File("C:\\"));
        return chooser.showDialog(null);
    }
    
    public static File getFile(File dir) {
        FileChooser chooser = new FileChooser();
        if (Files.exists(dir.toPath()))
            chooser.setInitialDirectory(dir);
        else
            chooser.setInitialDirectory(new File("C:\\"));
        return chooser.showSaveDialog(null);
    }

    public static Collection<DirectoryElement> getDirectoryElements(Path path, boolean recursive, FilenameFilter filenameFilter) {
        if (recursive) return getDirectoryElementsRecursive(path, filenameFilter);
        else return getDirectoryElementsNonRecursive(path.toFile(), filenameFilter);
    }

    /**
     * Returns all the files excluding directories
     * @param path
     * @return
     */
    public static Collection<DirectoryElement> getDirectoryElementsRecursive(Path path) {
        return getDirectoryElementsRecursive(path, new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return (!new File(dir + "\\" + name).isDirectory());
            }});
    }

    public static Collection<DirectoryElement> getDirectoryElementsRecursive(Path path, FilenameFilter filenameFilter) {
        final ArrayList<DirectoryElement> elements = new ArrayList();
        try
        {
            Files.walkFileTree (path, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult preVisitDirectory(Path file, BasicFileAttributes attrs) {
                    File[] content = file.toFile().listFiles(filenameFilter);
                    if (content != null)
                        Arrays.stream(content).map(DirectoryElement::new).forEach(elements::add);
                    return FileVisitResult.CONTINUE;
                }
            });
        }
        catch (IOException e) {
            StaticTools.errorOut("FileVisitor", e);
            throw new AssertionError ("walkFileTree will not throw IOException if the FileVisitor does not");
        }
        return elements;

    }

    public static Collection<DirectoryElement> getDirectoryElementsNonRecursive(File directory, FilenameFilter filenameFilter) {
        final ArrayList<DirectoryElement> elements = new ArrayList();
        if(directory != null && directory.isDirectory()) {
            File[] content = directory.listFiles(filenameFilter);
            if (content != null) {
                for (File file:content) {
                    elements.add(new DirectoryElement(file));
                }
            }
        }
        return elements;
    }

    public static Collection<DirectoryElement> getDirectoryElementsNonRecursive(Collection<String> directories, FilenameFilter filenameFilter) {
        Iterator<String> iter = directories.iterator();
        ArrayList<DirectoryElement> elements = new ArrayList<>();
        while(iter.hasNext()) {
            elements.addAll(getDirectoryElementsNonRecursive(new File(iter.next()), filenameFilter));
        }
        return elements;
    }



    //remove 5/41MP Nokia "duplicates"
    private static void removeFiles(File file) {
        File[] directories = file.listFiles((File dir, String name) -> dir.isDirectory());
        if (directories != null) {
            for (File dir1 : directories) {
                if(dir1.isDirectory()) {
                    File[] content = dir1.listFiles((dir, name) -> name.toLowerCase().endsWith("__highres.jpg"));
                    if (Objects.requireNonNull(content).length > 0) {
                        for (File aContent : content) {
                            String absolutePath = aContent.getAbsolutePath();
                            try {
                                Files.deleteIfExists(Paths.get(absolutePath.substring(0, absolutePath.length() - 13) + ".jpg"));
                            } catch (IOException e) {
                                errorOut(aContent.getName(), e);
                            }
                        }
                    }
                }
            }
        }

    }


}
