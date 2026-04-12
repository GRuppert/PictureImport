package org.nyusziful.pictureorganizer.DAL.Entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import org.nyusziful.pictureorganizer.DTO.Meta;

@Entity
@DiscriminatorValue("JPG")
public class JPGMedia extends Media {
    private Boolean exifbackup;

    public JPGMedia() {
        // this form used by Hibernate
    }

    public JPGMedia(MediaFileVersion mediaFileVersion, Image image, Meta meta, MediaType media_type) {
        super(mediaFileVersion, image, meta, media_type);
        this.exifbackup = ((JPGMediaFileVersion)mediaFileVersion).getExifbackup();
    }

    public boolean isExifbackup() {
        return exifbackup;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        JPGMedia jpgMediaFile = (JPGMedia)super.clone();
        jpgMediaFile.exifbackup = this.isExifbackup();
        return jpgMediaFile;
    }
}
