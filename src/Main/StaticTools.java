/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import java.awt.Toolkit;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javax.swing.JOptionPane;
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
        for (String extSupported : videoFiles) {
            if (ext.equals(extSupported)) return true;
        }
        return false;
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
    

    //remove 5/41MP Nokia "duplicates"
    private static void removeFiles(File file) {
        File[] directories = file.listFiles((File dir, String name) -> dir.isDirectory());
        for (File dir1 : directories) {
            if(dir1.isDirectory()) {
                File[] content = dir1.listFiles(new FilenameFilter() {
                    public boolean accept(File dir, String name) {
                            name = name.toLowerCase();
                        return name.endsWith("__highres.jpg");
                    }});
                if (content.length > 0) {
                    for(int i = 0; i < content.length; i++) {
                        String absolutePath = content[i].getAbsolutePath();
                        try {
                            Files.deleteIfExists(Paths.get(absolutePath.substring(0, absolutePath.length()-13) + ".jpg"));
                        } catch (IOException e) {
                            errorOut(content[i].getName(), e);         
                        }
                    }
                }
            }				
        }

    }
    
}
