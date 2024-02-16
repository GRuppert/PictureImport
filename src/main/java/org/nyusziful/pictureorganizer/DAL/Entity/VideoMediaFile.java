package org.nyusziful.pictureorganizer.DAL.Entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("VID")
public class VideoMediaFile extends MediaFile {
    public VideoMediaFile() {
        // this form used by Hibernate
    }

    public VideoMediaFile(String originalFilename, Integer shotnumber) {
        super(originalFilename, shotnumber);
    }

}
