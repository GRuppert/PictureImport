package org.nyusziful.pictureorganizer.DAL.Entity;

import javax.persistence.Entity;

@Entity
public class JPGMediaFile extends Mediafile {
    private boolean exifbackup;


    public boolean isExifbackup() {
        return exifbackup;
    }
}
