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

    public RAWMediaFileInstance(Folder folder, Path path, long size, Timestamp dateMod, Boolean original) {
        super(folder, path, size, dateMod, original);
        this.XMPattached = Files.exists(Paths.get(path.toString()+".xmp"));
    }

    public RAWMediaFileInstance(RAWMediaFileInstance rawMediaFile) {
        super(rawMediaFile);
        this.XMPattached = rawMediaFile.isXMPattached();
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
