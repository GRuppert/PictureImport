package org.nyusziful.pictureorganizer.DAL.Entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import java.nio.file.Path;
import java.sql.Timestamp;

@Entity
@DiscriminatorValue("VID")
public class VideoMediaFileInstance extends MediaFileInstance {
    public VideoMediaFileInstance() {
        // this form used by Hibernate
    }
    public VideoMediaFileInstance(Folder folder, Path path, Timestamp dateMod, VideoMediaFileVersion mediaFileVersion) {
        super(folder, path, dateMod, mediaFileVersion);
    }

}
