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
    private List<ReadRange> excludeRanges;

    public TIFFMediaFileStruct(File file, long startAddress) {
        this.file = file;
        this.startAddress = startAddress;
        segments = new ArrayList<>();
        warningMessages = new ArrayList<>();
        this.excludeRanges = new ArrayList<>();
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
        System.out.format(file.getName() + " size: %,8d bytes %n", file.length());
        long readedBytes = 0;
        for (ImageFileDirectory segment : segments) {
            drawSegmentMap(segment, 1);
        }
        System.out.format("Recognized size: %,10d bytes %n", readedBytes);
        System.out.println("\n");
        for (String warningMessage : warningMessages) {
            System.out.println(warningMessage);
        }
        System.out.println("Stopped because: " + terminationMessage);

    }

    public void drawSegmentMap(ImageFileDirectory segment, int depth) {
        for (IfdTag tag : segment.getTags()) {
            for (int i = 0; i < depth; i++) System.out.print("  ");
            System.out.println(IfdNames.getTag(tag.tagId) + " " + (tag.getCount() < 4 ? "value: " + tag.getValue() : "pointer: " + tag.getPointer()));
        }
        for (ImageFileDirectory subSegment : segment.getSubDirs()) {
            drawSegmentMap(subSegment, depth++);
        }
        System.out.println();
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

    public List<ReadRange> getExcludeRanges() {
        return excludeRanges;
    }

    public void addExcludeRange(ReadRange excludeRange) {
        this.excludeRanges.add(excludeRange);
    }
}
