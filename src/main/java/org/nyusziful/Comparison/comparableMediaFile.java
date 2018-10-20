package org.nyusziful.Comparison;


import org.nyusziful.Rename.Meta;

import java.io.File;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author gabor
 */
public class comparableMediaFile{
    public File file;
    public Meta meta;

    public comparableMediaFile(File fileIn, Meta metaIn) {
        this.file = fileIn;
        this.meta = metaIn;
    }
}              

