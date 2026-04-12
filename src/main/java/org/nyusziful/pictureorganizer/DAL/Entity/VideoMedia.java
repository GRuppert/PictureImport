package org.nyusziful.pictureorganizer.DAL.Entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import org.nyusziful.pictureorganizer.DTO.Meta;

@Entity
@DiscriminatorValue("VID")
public class VideoMedia extends Media {
    private int duration;

    public VideoMedia() {
    }

    public VideoMedia(MediaFileVersion mediaFileVersion, Image image, Meta meta, MediaType media_type) {
        super(mediaFileVersion, image, meta, media_type);
        this.duration = meta.duration;
    }


    @Override
    public Object clone() throws CloneNotSupportedException {
        VideoMedia jpgMediaFile = (VideoMedia)super.clone();
        jpgMediaFile.duration = this.getDuration();
        return jpgMediaFile;
    }

    public int getDuration() {
        return duration;
    }
}
