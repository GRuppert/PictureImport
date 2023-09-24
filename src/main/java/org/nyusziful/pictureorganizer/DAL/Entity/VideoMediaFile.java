package org.nyusziful.pictureorganizer.DAL.Entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("VID")
public class VideoMediaFile extends MediaFile {
    public VideoMediaFile() {
        // this form used by Hibernate
    }

    public VideoMediaFile(VideoMediaFileVersion originalVersion, String originalFilename, Integer shotnumber) {
        super(originalVersion, originalFilename, shotnumber);
    }

}
