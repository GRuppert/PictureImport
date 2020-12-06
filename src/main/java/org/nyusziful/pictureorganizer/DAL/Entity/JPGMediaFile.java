package org.nyusziful.pictureorganizer.DAL.Entity;

import org.nyusziful.pictureorganizer.DTO.Meta;
import org.nyusziful.pictureorganizer.Service.Hash.JPGHash;

import javax.persistence.*;
import java.nio.file.Path;
import java.sql.Timestamp;

@Entity
@DiscriminatorValue("JPG")
public class JPGMediaFile extends MediaFile {
    private boolean exifbackup = false;
    private boolean standalone = true;
    @Column(name = "exif")
    private byte[] exif;


    public JPGMediaFile() {
        // this form used by Hibernate
    }

    public JPGMediaFile(Folder folder, Path path, long size, Timestamp dateMod, boolean original) {
        super(folder, path, size, dateMod, original);
        exifbackup = checkBackupExif();
/*
        if (original) {
            exifbackup = addExifbackup();
        } else {
            exifbackup = checkBackupExif();
        }
*/
    }

    public JPGMediaFile(JPGMediaFile jpgMediaFile) {
        super(jpgMediaFile);
        this.exifbackup = jpgMediaFile.isExifbackup();
        this.standalone = jpgMediaFile.isStandalone();

    }

    public boolean addExifbackup(boolean orig) {
        exifbackup = JPGHash.addBackupExif(filePath.toFile(), orig);
        return exifbackup;
    }

    public boolean checkBackupExif() {
        exifbackup = JPGHash.checkBackupExif(filePath.toFile());
        return exifbackup;
    }

    public boolean isExifbackup() {
        return exifbackup;
    }

    public void setStandalone(boolean standalone) {
        this.standalone = standalone;
    }

    public boolean isStandalone() {
        return standalone;
    }

    @Override
    public void setMeta(Meta meta) {
        super.setMeta(meta);
        setWithQuality(meta.quality);
    }

    public void setWithQuality(String quality) {
        if (quality == null) return;
        switch (quality) {
            case "RAW + JPEG":
                setStandalone(false);
                break;
            default:
                setStandalone(true);
        }
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        JPGMediaFile jpgMediaFile = (JPGMediaFile)super.clone();
        jpgMediaFile.exifbackup = this.isExifbackup();
        jpgMediaFile.standalone = this.isStandalone();
        return jpgMediaFile;
    }

    public byte[] getExif() {
        return exif;
    }
}
