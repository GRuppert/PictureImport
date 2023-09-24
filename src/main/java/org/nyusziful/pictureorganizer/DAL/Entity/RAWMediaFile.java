package org.nyusziful.pictureorganizer.DAL.Entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("RAW")
public class RAWMediaFile extends MediaFile {
    public RAWMediaFile() {
        // this form used by Hibernate
    }

    public RAWMediaFile(RAWMediaFileVersion originalVersion, String originalFilename, Integer shotnumber) {
        super(originalVersion, originalFilename, shotnumber);
    }

}
