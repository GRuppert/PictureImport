package org.nyusziful.pictureorganizer.DAL.Entity;

import org.nyusziful.pictureorganizer.DTO.Meta;
import org.nyusziful.pictureorganizer.Service.ExifUtils.ExifService;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;

@Entity
@DiscriminatorValue("VID")
public class VideoMediaFile extends MediaFile {
    private long duration;

    public VideoMediaFile() {
        // this form used by Hibernate
    }

    public VideoMediaFile(Folder folder, Path path, long size, Timestamp dateMod, boolean original) {
        super(folder, path, size, dateMod, original);
     }

    public VideoMediaFile(MediaFile videoMediaFile) {
        super(videoMediaFile);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        VideoMediaFile rawMediaFile = (VideoMediaFile)super.clone();
        rawMediaFile.duration = this.getDuration();
        return rawMediaFile;
    }

    @Override
    public void setMeta(Meta meta) {
        super.setMeta(meta);
        setDuration(meta.duration);
    }


    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getDuration() {
        return duration;
    }
}
