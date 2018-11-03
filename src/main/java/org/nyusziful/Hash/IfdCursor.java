package org.nyusziful.Hash;


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
public class IfdCursor {
    private final File file;
    private final Boolean endian;
    private List<Long> pointers;
    
    public IfdCursor(File file, Boolean endian, long pointer) {
        this.file = file;
        this.endian = endian;
        this.pointers = new ArrayList();
        pointers.add(pointer);
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
        return pointers.get(pointers.size() - 1);
    }

    /**
     * @param pointer the pointer to set
     */
    public void setPointer(long pointer) {
        pointers.add(pointer);
    }

    public List<Long> getPointers() {
        return pointers;
    }
}
