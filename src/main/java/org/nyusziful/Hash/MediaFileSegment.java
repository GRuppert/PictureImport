package org.nyusziful.Hash;

public interface MediaFileSegment {
    public long getStartAddress();

    public long getLength();

    @Override
    public boolean equals(Object obj);

    @Override
    public String toString();
}
