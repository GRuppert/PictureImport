package org.nyusziful.Hash;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class JPEGFileStruct {
    private File file;
    private List<JPEGSegment> segments;
    private long size;
    private String terminationMessage;
    private List<String> warningMessages;

    public JPEGFileStruct(File file) {
        this.file = file;
        this.size = file.length();
        segments = new ArrayList<>();
        warningMessages = new ArrayList<>();
    }

    public void addSegment(JPEGSegment segment) {
        segments.add(segment);
    }

    public void drawMap() {
        System.out.format(file.getName() + " size: %,8d bytes %n", size);
        long readedBytes = 0;
        for (JPEGSegment segment : segments) {
            System.out.format("%,8d / 0x%8X  " + segment.getMarker() + " " + segment.getId() + "%n", segment.getStartAddress(), segment.getStartAddress());
            System.out.format("%,8d length + marker " + segment.getMarker() + "%n", segment.getLength());
            readedBytes += segment.getLength();
        }
        System.out.format("Recognized size: %,8d bytes %n", readedBytes);
        System.out.println("\n");
        for (String warningMessage : warningMessages) {
            System.out.println(warningMessage);
        }
        System.out.println("Stopped because: " + terminationMessage);
    }

    public String getTerminationMessage() {
        return terminationMessage;
    }

    public void setTerminationMessage(String terminationMessage) {
        this.terminationMessage = terminationMessage;
    }

    public void addWarningMessage(String warningMessage) {
        warningMessages.add(warningMessage);
    }

    public JPEGSegment getLastSegment() {
        return segments.get(segments.size()-1);
    }

    public JPEGSegment getSegment(int i) {
        return segments.get(i);
    }
}
