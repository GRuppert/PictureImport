package org.nyusziful.pictureorganizer.DAL.Entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import java.time.ZonedDateTime;

@Entity
@DiscriminatorValue("RAW")
public class RAWMediaFileVersion extends MediaFileVersion {
    public RAWMediaFileVersion() {
        // this form used by Hibernate
    }

    public RAWMediaFileVersion(String filehash, RAWMediaFileVersion parent, Long size, RAWMediaFile mediaFile, ZonedDateTime dateStored, Boolean invalid) {
        super(filehash, parent, size, mediaFile, dateStored, invalid);
    }

}
