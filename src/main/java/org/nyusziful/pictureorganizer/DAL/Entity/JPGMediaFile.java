package org.nyusziful.pictureorganizer.DAL.Entity;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("JPG")
public class JPGMediaFile extends Mediafile {
    private boolean exifbackup;


    public boolean isExifbackup() {
        return exifbackup;
    }
}
