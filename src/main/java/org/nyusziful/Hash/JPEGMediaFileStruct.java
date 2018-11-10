package org.nyusziful.Hash;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class JPEGMediaFileStruct implements MediaFileStruct<JPEGSegment> {
    private File file;
    private List<JPEGSegment> segments;
    private String terminationMessage;
    private List<String> warningMessages;

    public JPEGMediaFileStruct(File file) {
        this.file = file;
        segments = new ArrayList<>();
        warningMessages = new ArrayList<>();
    }

    @Override
    public void addSegment(JPEGSegment segment) {
        segments.add(segment);
    }

    @Override
    public void drawMap() {
        System.out.format(file.getName() + " size: %,8d bytes %n", file.length());
        long readedBytes = 0;
        for (JPEGSegment segment : segments) {
            System.out.format("%,10d / 0x%8X  " + segment.getMarker() + "%n%,10d / 0x%8X " + segment.getId() + "%n", segment.getStartAddress(), segment.getStartAddress(), segment.getLength(), segment.getLength());
            readedBytes += segment.getLength();
            if (segment.getData() != null) segment.getData().drawMap();
        }
        System.out.format("Recognized size: %,10d bytes %n", readedBytes);
        System.out.println("\n");
        for (String warningMessage : warningMessages) {
            System.out.println(warningMessage);
        }
        System.out.println("Stopped because: " + terminationMessage);
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
    public JPEGSegment getLastSegment() {
        return segments.get(segments.size()-1);
    }

    @Override
    public JPEGSegment getSegment(int i) {
        return segments.get(i);
    }
}
