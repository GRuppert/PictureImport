/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nyusziful.Main;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import javax.swing.filechooser.FileSystemView;

/**
 *
 * @author gabor
 */
public class Services {
    public static Path backupMounted() {
        ArrayList<File> drives = new ArrayList<>();
        File[] paths;
        FileSystemView fsv = FileSystemView.getFileSystemView();
        paths = File.listRoots();
        for(File path:paths)
        {
            String desc = fsv.getSystemDisplayName(path);
            if (desc.startsWith("test")) return path.toPath();
        }
        return null;
    }

    public void importFiles(List<String> directories, Path backupdrive) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
}
