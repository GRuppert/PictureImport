package org.nyusziful.pictureorganizer.DAL.Entity;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("JPG")
public class JPGMediaFile extends MediaFile {
    private boolean exifbackup;

    public JPGMediaFile() {
        // this form used by Hibernate
    }


    public boolean isExifbackup() {
        return exifbackup;
    }
}
