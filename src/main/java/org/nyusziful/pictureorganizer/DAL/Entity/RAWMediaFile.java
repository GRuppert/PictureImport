package org.nyusziful.pictureorganizer.DAL.Entity;

import javax.persistence.Entity;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;

@Entity
public class RAWMediaFile extends Mediafile {
    private boolean XMPattached;

    public RAWMediaFile(Drive drive, Folder folder, Path path, long size, Timestamp dateMod, boolean original) {
        super(drive, folder, path, size, dateMod, original);
        this.XMPattached = Files.exists(Paths.get(path.toString()+".xmp"));
    }

    public boolean isXMPattached() {
        return XMPattached;
    }

    public void setXMPattached(boolean XMPattached) {
        this.XMPattached = XMPattached;
    }


}
