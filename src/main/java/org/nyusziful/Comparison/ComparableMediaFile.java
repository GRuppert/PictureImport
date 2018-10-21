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
public class ComparableMediaFile extends Meta {
    public File file;

    public ComparableMediaFile(File fileIn, Meta metaIn) {
        super(metaIn);
        this.file = fileIn;
    }
}              

