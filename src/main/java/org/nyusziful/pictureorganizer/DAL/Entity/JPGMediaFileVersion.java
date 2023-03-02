package org.nyusziful.pictureorganizer.DAL.Entity;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("JPG")
public class JPGMediaFileVersion extends MediaFileVersion {
    private Boolean exifbackup;

    public JPGMediaFileVersion() {
        // this form used by Hibernate
    }

    public JPGMediaFileVersion(String filehash, MediaFileVersion parent, Long size) {
        super(filehash, parent, size);
    }

    public Boolean getExifbackup() {
        return exifbackup;
    }

    public void setExifbackup(Boolean exifbackup) {
        this.exifbackup = exifbackup;
    }
}
