package org.nyusziful.pictureorganizer.DAL.Entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import java.time.ZonedDateTime;

@Entity
@DiscriminatorValue("VID")
public class VideoMediaFileVersion extends MediaFileVersion {
    public VideoMediaFileVersion() {
        // this form used by Hibernate
    }

    public VideoMediaFileVersion(String filehash, VideoMediaFileVersion parent, Long size, MediaFile mediaFile, ZonedDateTime dateStored) {
        super(filehash, parent, size, mediaFile, dateStored);
    }

}
