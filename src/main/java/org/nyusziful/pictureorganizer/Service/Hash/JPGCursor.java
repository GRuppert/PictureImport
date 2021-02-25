package org.nyusziful.pictureorganizer.Service.Hash;


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
    private int marker;
    long position = -1;
    private BufferedInputStream bufferedInStream;

    public JPGCursor() { }

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
