package org.nyusziful.Hash;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TIFFMediaFileStruct implements MediaFileStruct<ImageFileDirectory> {
    private File file;
    private byte[] digestBytes;
    private Boolean endian;
    private ArrayList<ImageFileDirectory> segments;
    private long startAddress;
    private String terminationMessage;
    private List<String> warningMessages;

    public TIFFMediaFileStruct(File file, long startAddress) {
        this.file = file;
        this.startAddress = startAddress;
        segments = new ArrayList<>();
    }

    @Override
    public String getTerminationMessage() {
        return terminationMessage;
    }

    @Override
    public void setTerminationMessage(String terminationMessage) {
        this.terminationMessage = terminationMessage;
    }

    @Override
    public void addWarningMessage(String warningMessage) {
        warningMessages.add(warningMessage);
    }

    @Override
    public void addSegment(ImageFileDirectory segment) {
        segments.add(segment);
    }

    @Override
    public void drawMap() {

    }

    @Override
    public ImageFileDirectory getLastSegment() {
        return null;
    }

    @Override
    public ImageFileDirectory getSegment(int i) {
        return null;
    }

    public byte[] getDigestBytes() {
        return digestBytes;
    }

    public void setDigestBytes(byte[] digestBytes) {
        if (this.digestBytes == null) this.digestBytes = digestBytes; else addWarningMessage("Multiple Hash");
    }

    public void setEndian(boolean endian) {
        if (this.endian == null) this.endian = endian; else addWarningMessage("Multiple Endian");
    }

    public Boolean getEndian() {
        return endian;
    }

    public File getFile() {
        return file;
    }
}
