package Rename;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

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
