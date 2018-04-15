package Rename;


import Rename.meta;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author gabor
 */
public interface ifMetaLink {
    /**
     * Reads the standard metadata from the specified files in the given directory
     * @param filenames String list of the representation of the file names
     * @param dir the directory where the files are
     * @return a list of the <code> meta </code> objects for every file if the read was unsuccessful the note field of the object will contain the error message
     */
    public List<meta> exifToMeta(ArrayList<String> filenames, File dir);
            
    
    
}
