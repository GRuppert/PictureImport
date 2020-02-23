package org.nyusziful.pictureorganizer.DAL.Entity;

import org.nyusziful.pictureorganizer.Service.Hash.JPGHash;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.nio.file.Path;
import java.sql.Timestamp;

@Entity
@DiscriminatorValue("JPG")
public class JPGMediaFile extends MediaFile {
    private boolean exifbackup;

    public JPGMediaFile() {
        // this form used by Hibernate
    }

    public JPGMediaFile(Drive drive, Folder folder, Path path, long size, Timestamp dateMod, boolean original) {
        super(drive, folder, path, size, dateMod, original);
        if (original) {
            exifbackup = JPGHash.addBackupExif(filePath.toFile());
        } else {
            addExifbackup();
        }
        //exifbackup
    }

    public boolean addExifbackup() {
        exifbackup = JPGHash.addBackupExif(filePath.toFile());
        return exifbackup;
    }

    public boolean isExifbackup() {
        return exifbackup;
    }
}
