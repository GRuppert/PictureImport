package org.nyusziful.Hash;

import java.util.ArrayList;

public class ImageFileDirectory implements MediaFileSegment {
    private long startAddress;
    private ArrayList<ImageFileDirectory> subDirs;
    private ArrayList<ImageFileDirectory> tags;
    private long length = 0;

    public ImageFileDirectory(long startAddress) {
        this.startAddress = startAddress;
        subDirs = new ArrayList<>();
    }

    @Override
    public long getStartAddress() {
        return startAddress;
    }

    @Override
    public long getLength() {
        return length;
    }

    public void addTIFFDirectory(ImageFileDirectory directory) {
        subDirs.add(directory);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && this.getClass().equals(obj.getClass())) {
            ImageFileDirectory otherSegment = (ImageFileDirectory) obj;
                if (this.getLength() == otherSegment.getLength())
                    if (this.getStartAddress() == otherSegment.getStartAddress())
                            return true;
        }
        return false;

    }

    @Override
    public String toString() {
        String header = "";
        return header;
    }

    public void addTag(IfdTag field) {

    }
}
