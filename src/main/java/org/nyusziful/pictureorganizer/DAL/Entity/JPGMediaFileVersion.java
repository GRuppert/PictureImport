package org.nyusziful.pictureorganizer.DAL.Entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import java.time.ZonedDateTime;

@Entity
@DiscriminatorValue("JPG")
public class JPGMediaFileVersion extends MediaFileVersion {
    private Boolean exifbackup;

    public JPGMediaFileVersion() {
        // this form used by Hibernate
    }

    public JPGMediaFileVersion(String filehash, JPGMediaFileVersion parent, Long size, JPGMediaFile mediaFile, ZonedDateTime dateStored) {
        super(filehash, parent, size, mediaFile, dateStored);
    }

    public Boolean getExifbackup() {
        return exifbackup;
    }

    public void setExifbackup(Boolean exifbackup) {
        this.exifbackup = exifbackup;
    }
}
