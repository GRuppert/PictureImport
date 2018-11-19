package org.nyusziful.Hash;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static org.nyusziful.Hash.BasicFileReader.skipBytes;

public class TIFFMediaFileStruct implements MediaFileStruct<ImageFileDirectory>, AutoCloseable {
    public static int HASHING = 0;
    public static int MAPPING = 1;
    private File file;
    private byte[] digestBytes;
    private Boolean endian;
    private ArrayList<ImageFileDirectory> segments;
    private long startAddress;
    private String terminationMessage;
    private List<String> warningMessages;
    private List<ReadRange> excludeRanges;
//    private RandomAccessFile randomAccessFile;
//    private FileInputStream fis;
//    private BufferedInputStream bufferedInputStream;
    private BufferedRandomAccessFile bufferedRandomAccessFile;
    private int mode;

    public boolean jumpTo(long position) throws IOException {
        bufferedRandomAccessFile.seek(position+startAddress);
        return (bufferedRandomAccessFile.getFilePointer() == position + startAddress);
/*        try {
            bufferedInputStream.reset();
            return skipBytes(bufferedInputStream, position);
        } catch (IOException e) {
            randomAccessFile.seek(position + startAddress);
           openNewBufferStream();
            return (randomAccessFile.getFilePointer() == position + startAddress);
        }*/
    }

    public TIFFMediaFileStruct(File file, long startAddress, int mode) throws IOException {
        this.file = file;
        this.startAddress = startAddress;
        this.mode = mode;
        segments = new ArrayList<>();
        warningMessages = new ArrayList<>();
        this.excludeRanges = new ArrayList<>();
        bufferedRandomAccessFile = new BufferedRandomAccessFile(file, "r", 65536);
/*        randomAccessFile = new RandomAccessFile(file.getAbsolutePath(), "r");
        fis = new FileInputStream(randomAccessFile.getFD());
        openNewBufferStream();*/
    }

/*    private void openNewBufferStream() throws IOException {
//        if (bufferedInputStream != null) bufferedInputStream.close();
        bufferedInputStream = new BufferedInputStream(fis);
    }*/

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
        System.out.println();
        if (100 < segment.getTags().size()) {
            for (int i = 0; i < depth; i++) System.out.print("\t");
            System.out.println("MakerNotes");
        } else {
            for (IfdTag tag : segment.getTags()) {
                for (int i = 0; i < depth; i++) System.out.print("\t");
                System.out.format("%,10d / 0x%8X  " + IfdNames.getTag(tag.tagId) + " " + (tag.getCount() < 4 ? "value: " + tag.getValue() : "pointer from the TIFF beginning: " + tag.getPointer()) + "%n", tag.address, tag.address);
            }
        }
        for (ImageFileDirectory subSegment : segment.getSubDirs()) {
            drawSegmentMap(subSegment, depth++);
        }
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
/*
    public RandomAccessFile getRandomAccessFile() {
        return randomAccessFile;
    }

    public void setRandomAccessFile(RandomAccessFile randomAccessFile) {
        this.randomAccessFile = randomAccessFile;
    }

    public BufferedInputStream getBufferedInputStream() {
        return bufferedInputStream;
    }

    public void setBufferedInputStream(BufferedInputStream bufferedInputStream) {
        this.bufferedInputStream = bufferedInputStream;
    }
*/
    @Override
    public void close() throws IOException {
/*        bufferedInputStream.close();
        randomAccessFile.close();*/
        bufferedRandomAccessFile.close();
    }

    public int getMode() {
        return mode;
    }

    public BufferedRandomAccessFile getBufferedRandomAccessFile() {
        return bufferedRandomAccessFile;
    }
}
