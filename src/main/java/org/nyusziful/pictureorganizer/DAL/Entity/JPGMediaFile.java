package org.nyusziful.pictureorganizer.DAL.Entity;

import org.nyusziful.pictureorganizer.Service.Hash.JPGHash;

import javax.persistence.*;
import java.nio.file.Path;
import java.sql.Timestamp;

@Entity
@DiscriminatorValue("JPG")
public class JPGMediaFile extends MediaFile {
    private boolean exifbackup = false;
    private boolean standalone = true;

    public JPGMediaFile() {
        // this form used by Hibernate
    }

    public JPGMediaFile(Drive drive, Folder folder, Path path, long size, Timestamp dateMod, boolean original) {
        super(drive, folder, path, size, dateMod, original);
        if (original) {
            exifbackup = addExifbackup();
        } else {
            exifbackup = checkBackupExif();
        }
    }

    public boolean addExifbackup() {
        exifbackup = JPGHash.addBackupExif(filePath.toFile());
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

}
