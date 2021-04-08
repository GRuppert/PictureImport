package org.nyusziful.pictureorganizer.Service.Hash;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class RandomAccessByteStream extends ByteArrayInputStream implements RandomAccessStream {
    public RandomAccessByteStream(byte[] buf) {
        super(buf);
    }

    public RandomAccessByteStream(byte[] buf, int offset, int length) {
        super(buf, offset, length);
    }

    @Override
    public int read(byte[] b) {
        return read(b, 0, b.length);
    }
}
