package org.nyusziful.pictureorganizer.DTO;


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
    public int nameVersion;
    public String originalFilename;
    public ZonedDateTime date;
    public String model;
    public String odID;
    public String dID;
    public String iID;
    public String note = "";
    public String orig;
    public Boolean dateFormat;
    public String quality;
    public long duration;
    public Integer orientation;
    public Integer rating;
    public String title;
    public String keyword;
    public String make;
    public Integer shotnumber;

    public Meta() {
    }

    public Meta(int nameVersion, String originalFilename, ZonedDateTime date, Boolean dateFormat, String model, String iID, String dID, String odID, String note, String orig, String quality, Integer orientation, String make, Integer rating, Long duration, Integer shotnumber) {
        this.nameVersion = nameVersion;
        this.originalFilename = originalFilename;
        this.date = date;
        this.dateFormat = dateFormat;
        this.model = model;
        this.odID = odID;
        this.dID = dID;
        this.iID = iID;
        this.orig = orig;
        this.quality = quality;
        this.duration = duration;
        this.orientation = orientation;
        this.make = make;
        this.rating = rating;
        this.shotnumber = shotnumber;
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
            .append(" ")
            .append(quality)
            .append(" ")
            .append(duration)
            .append(" ")
            .append(orientation)
            .append(" ")
            .append(rating)
            .append(" ")
            .append(title)
            .append(" ")
            .append(keyword)
            .append(" ")
            .append(make)
            .append(" ")
            .append(shotnumber)
            ;
        return sb.toString();
    }

}