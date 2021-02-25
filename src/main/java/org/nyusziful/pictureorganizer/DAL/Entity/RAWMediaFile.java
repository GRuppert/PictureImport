package org.nyusziful.pictureorganizer.DAL.Entity;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;

@Entity
@DiscriminatorValue("RAW")
public class RAWMediaFile extends MediaFile {
    private boolean XMPattached;

    public RAWMediaFile() {
        // this form used by Hibernate
    }

    public RAWMediaFile(Folder folder, Path path, long size, Timestamp dateMod, Boolean original) {
        super(folder, path, size, dateMod, original);
        this.XMPattached = Files.exists(Paths.get(path.toString()+".xmp"));
    }

    public RAWMediaFile(RAWMediaFile rawMediaFile) {
        super(rawMediaFile);
        this.XMPattached = rawMediaFile.isXMPattached();
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        RAWMediaFile rawMediaFile = (RAWMediaFile)super.clone();
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
