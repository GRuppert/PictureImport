package org.nyusziful.pictureorganizer.Hash;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;

public class Ifd {
    private ArrayList<IfdTag> tagList;
    private byte[] nextIfd;

    public byte[] getNumberOfTags() {
        return ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(tagList.size()).array();
    }

}
