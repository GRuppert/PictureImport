package org.nyusziful.pictureorganizer.Service.Comparison;


import org.nyusziful.pictureorganizer.DTO.Meta;

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

    public ComparableMediaFile(File fileIn, Meta meta) {
        this.file = fileIn;
        this.originalFilename = meta.originalFilename;
        this.date = meta.date;
        this.dateFormat = meta.dateFormat;
        this.model = meta.model;
        this.odID = meta.odID;
        this.dID = meta.dID;
        this.iID = meta.iID;
        this.orig = meta.orig;
        this.quality = meta.quality;
        this.duration = duration;
    }
}              

