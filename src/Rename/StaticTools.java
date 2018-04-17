package Rename;


import Main.PicOrganizes;
import com.adobe.xmp.XMPException;
import com.adobe.xmp.XMPIterator;
import com.adobe.xmp.XMPMeta;
import com.adobe.xmp.XMPMetaFactory;
import com.adobe.xmp.properties.XMPPropertyInfo;
import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.drew.metadata.xmp.XmpDirectory;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import org.apache.commons.io.FilenameUtils;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author gabor
 */
public class StaticTools {
    

    /** 
     * Copy the source to two locations
     * @param source the original file
     * @param dest the primary output file
     * @param backup the secondary output file
     * @returns true if no Exception was raised
     */
    public static boolean copyAndBackup(File source, File dest, File backup) {
        FileInputStream input = null;
        FileOutputStream output = null;
        FileOutputStream outputII = null;
        try {
            input = new FileInputStream(source);
            output = new FileOutputStream(dest);
            outputII = new FileOutputStream(backup);
            byte[] bufR = new byte[4096];
            byte[] bufW;
            int bytesRead;
            while ((bytesRead = input.read(bufR)) > 0)
            {
                bufW = bufR;
                output.write(bufW, 0, bytesRead);
                outputII.write(bufW, 0, bytesRead);
            } 
            
        } catch (FileNotFoundException ex) {
            return false;
        } catch (IOException ex) {
            return false;
        } finally {
            try {
                input.close();
                output.close();
                outputII.close();
            } catch (IOException ex) {
                return false;
            }
        }
        return true;
    }
    

    
}
