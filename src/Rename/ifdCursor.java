package Rename;


import java.io.File;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author gabor
 */
public class ifdCursor {
    private File file;
    private Boolean endian;
    private long pointer;
    
    public ifdCursor(File file, Boolean endian, long pointer) {
        this.file = file;
        this.endian = endian;
        this.pointer = pointer;
    }

    /**
     * @return the file
     */
    public File getFile() {
        return file;
    }

    /**
     * @return the endian
     */
    public Boolean getEndian() {
        return endian;
    }

    /**
     * @return the pointer
     */
    public long getPointer() {
        return pointer;
    }

    /**
     * @param pointer the pointer to set
     */
    public void setPointer(long pointer) {
        this.pointer = pointer;
    }

}
