package org.nyusziful.pictureorganizer.Service.Hash;

public interface MediaFileStruct<T extends MediaFileSegment> {

    public void addSegment(T segment);

    public void drawMap();

    public String getTerminationMessage();

    public void setTerminationMessage(String terminationMessage);

    public void addWarningMessage(String warningMessage);

    public  T getLastSegmentIgnorePadding();

    public  T getLastSegment();

    public T getSegment(int i);
}
