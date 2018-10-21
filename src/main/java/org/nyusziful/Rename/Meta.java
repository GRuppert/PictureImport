package org.nyusziful.Rename;


import java.time.ZonedDateTime;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author gabor
 */
public class Meta {
    public String originalFilename;
    public ZonedDateTime date;
    public String model;
    public String odID;
    public String dID;
    public String iID;
    public String note = "";
    public String orig;
    public Boolean dateFormat;

    /**
     *
     * @param originalFilename
     * @param date
     * @param dateFormat
     * @param model
     * @param iID
     * @param dID
     * @param odID
     * @param note
     * @param orig
     */
    public Meta(String originalFilename, ZonedDateTime date, Boolean dateFormat, String model, String iID, String dID, String odID, String note, String orig) {
        this.originalFilename = originalFilename;
        this.date = date;
        this.dateFormat = dateFormat;
        this.model = model;
        this.odID = odID;
        this.dID = dID;
        this.iID = iID;
        this.orig = orig;
    }

    public Meta(Meta meta) {
        this.originalFilename = meta.originalFilename;
        this.date = meta.date;
        this.dateFormat = meta.dateFormat;
        this.model = meta.model;
        this.odID = meta.odID;
        this.dID = meta.dID;
        this.iID = meta.iID;
        this.orig = meta.orig;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(originalFilename)
            .append(" ")
            .append(date)
            .append(" ")
            .append(dateFormat)
            .append(" ")
            .append(model)
            .append(" ")
            .append(iID)
            .append(" ")
            .append(dID)
            .append(" ")
            .append(odID)
            .append(" ")
            .append(note)
            .append(" ")
            .append(orig)
            ;
        return sb.toString();
    }
    
/*    public Meta(String originalFilename, String date, String model, String iID, String dID, String odID) {
        this.originalFilename = originalFilename;
        this.date = date;
        this.model = model;
        this.odID = odID;
        this.dID = dID;
        this.iID = iID;
    }*/
}