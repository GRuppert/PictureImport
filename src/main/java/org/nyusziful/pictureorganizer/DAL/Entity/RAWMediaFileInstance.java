package org.nyusziful.pictureorganizer.DAL.Entity;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;

@Entity
@DiscriminatorValue("RAW")
public class RAWMediaFileInstance extends MediaFileInstance {
    private boolean XMPattached;

    public RAWMediaFileInstance() {
        // this form used by Hibernate
    }

    public RAWMediaFileInstance(Folder folder, Path path, Timestamp dateMod, MediaFileVersion mediaFileVersion) {
        super(folder, path, dateMod, mediaFileVersion);
        this.XMPattached = Files.exists(Paths.get(path.toString()+".xmp"));
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        RAWMediaFileInstance rawMediaFile = (RAWMediaFileInstance)super.clone();
        rawMediaFile.XMPattached = this.isXMPattached();
        return rawMediaFile;
    }


    public boolean isXMPattached() {
        return XMPattached;
    }

    public void setXMPattached(boolean XMPattached) {
        this.XMPattached = XMPattached;
    }


}
