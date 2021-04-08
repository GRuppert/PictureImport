package org.nyusziful.pictureorganizer.Service.Hash;

import java.util.ArrayList;
import java.util.Collections;

public class ExifSegment extends JPEGSegment {
    private MediaFileStruct data;
    private ArrayList<Long> orientationAddress;

    public ExifSegment(long startAddress, long length, int marker, String id) {
        super(startAddress, length, marker, id);
    }

    public ExifSegment(long startAddress, long length, int marker) {
        super(startAddress, length, marker);
    }

    public MediaFileStruct getData() {
        return data;
    }

    public void setData(MediaFileStruct data) {
        this.data = data;
        orientationAddress = new ArrayList<>();
        for (int i = 0; i < data.getSegmentSize(); i++) {
            MediaFileSegment segment = data.getSegment(i);
            if (segment instanceof ImageFileDirectory) {
                checkTags((ImageFileDirectory)segment);
            }
        }
    }

    private void checkTags(ImageFileDirectory ifd) {
        for (IfdTag tag : ifd.getTags()) {
            if (tag.tagId == 274) {
                orientationAddress.add(tag.address + 6 + 12);//header + 12th byte of the tag
            }
        }
        for (ImageFileDirectory subDir : ifd.getSubDirs()) {
            checkTags(subDir);
        }
    }

    public ArrayList<Long> getOrientationAddress() {
        Collections.sort(orientationAddress);
        return orientationAddress;
    }

}
