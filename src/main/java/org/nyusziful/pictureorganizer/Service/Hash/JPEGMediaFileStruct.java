package org.nyusziful.pictureorganizer.Service.Hash;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class JPEGMediaFileStruct implements MediaFileStruct<JPEGSegment> {
    private boolean backup = false;
    private JPEGSegment mainImage;
    private ExifSegment exifSegment;
    private List<JPEGSegment> segments;
    private String terminationMessage;
    private List<String> warningMessages;
    private byte[] exif;
    private String name;

    public JPEGMediaFileStruct() {
        segments = new ArrayList<>();
        warningMessages = new ArrayList<>();
    }

    @Override
    public void addSegment(JPEGSegment segment) {
        if ("Backup".equals(segment.getId())) backup = true;
        if (218 == segment.getMarker() && (mainImage == null || mainImage.getLength() < segment.getLength())) {mainImage = segment;}
        if (segment instanceof ExifSegment && segment.getId().equals("Exif\0\0") && (getExifSegment() == null || getExifSegment().getLength() < segment.getLength())) {
            exifSegment = (ExifSegment)segment;}
        segments.add(segment);
    }

    @Override
    public void drawMap() {
        long readedBytes = 0;
        for (JPEGSegment segment : segments) {
            System.out.format("%,10d / 0x%8X  " + segment.getMarkerText() + "%n%,10d / 0x%8X " + segment.getId() + "%n", segment.getStartAddress(), segment.getStartAddress(), segment.getLength(), segment.getLength());
            readedBytes += segment.getLength();
            if (segment instanceof ExifSegment) {
                MediaFileStruct data = ((ExifSegment) segment).getData();
                if (data != null) data.drawMap();
            }
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
    public JPEGSegment getLastSegmentIgnorePadding() {
        for (int i = 0; i < segments.size(); i++) {
            JPEGSegment segment = segments.get(segments.size()-1-i);
            if (segment.getMarker() != 0) return segment;
        }
        return null;
    }

    @Override
    public JPEGSegment getLastSegment() {
        return segments.get(segments.size()-1);
    }

    @Override
    public JPEGSegment getSegment(int i) {
        return segments.get(i);
    }

    @Override
    public int getSegmentSize() {
        return getSegments().size();
    }

    public List<JPEGSegment> getSegments() {
        return segments;
    }

    public boolean isBackup() {
        return backup;
    }

    public JPEGSegment getMainImage() {
        return mainImage;
    }

    public ExifSegment getExifSegment() {
        return exifSegment;
    }

    public byte[] getExif() {
        return exif;
    }

    public void setExif(byte[] exif) {
        this.exif = exif;
    }
}
