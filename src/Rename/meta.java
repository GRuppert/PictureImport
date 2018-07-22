package Rename;


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
public class meta{
    public String originalFilename;
    public ZonedDateTime date;
    public String model;
    public String odID;
    public String dID;
    public String iID;
    public String note = "";
    public String orig;
    public Boolean dateFormat;

    public meta(String originalFilename, ZonedDateTime date, Boolean dateFormat, String model, String iID, String dID, String odID, String note, String orig) {
        this.originalFilename = originalFilename;
        this.date = date;
        this.dateFormat = dateFormat;
        this.model = model;
        this.odID = odID;
        this.dID = dID;
        this.iID = iID;
        this.orig = orig;
    }
    
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
            .append(odID)
            .append(" ")
            .append(dID)
            .append(" ")
            .append(iID)
            .append(" ")
            .append(orig)
            .append(" ")
            .append(note)
            ;
        return sb.toString();
    }

/*    public meta(String originalFilename, String date, String model, String iID, String dID, String odID) {
        this.originalFilename = originalFilename;
        this.date = date;
        this.model = model;
        this.odID = odID;
        this.dID = dID;
        this.iID = iID;
    }*/
}