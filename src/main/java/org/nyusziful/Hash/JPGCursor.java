package org.nyusziful.Hash;


import java.io.BufferedInputStream;
import java.io.File;

/*
 * To change this license header, choose License Headers bufferedInStream Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template bufferedInStream the editor.
 */

/**
 *
 * @author gabor
 */
public class JPGCursor {
    private final File file;
    private int marker;
    long position;
    private BufferedInputStream bufferedInStream;

    public JPGCursor(File file) {
        this.file = file;
    }

    public int getMarker() {
        return marker;
    }

    public void setMarker(int marker) {
        this.marker = marker;
    }

    public BufferedInputStream getBufferedInStream() {
        return bufferedInStream;
    }

    public void setBufferedInStream(BufferedInputStream bufferedInStream) {
        this.bufferedInStream = bufferedInStream;
    }
}
