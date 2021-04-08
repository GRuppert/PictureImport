package org.nyusziful.pictureorganizer.Service.Hash;

import java.io.IOException;

public interface RandomAccessStream {
    void close() throws IOException;
    int read();
    int read(byte b[]);
}
