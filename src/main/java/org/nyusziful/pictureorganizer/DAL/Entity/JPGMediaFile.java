package org.nyusziful.pictureorganizer.DAL.Entity;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("JPG")
public class JPGMediaFile extends MediaFile {
    private boolean standalone = true;

    public JPGMediaFile() {
    }

    public JPGMediaFile(MediaFileVersion originalVersion, String originalFilename, Integer shotnumber, boolean standalone, MediaFile mainMediaFile) {
        super(originalVersion, originalFilename, shotnumber);
        super.setMainMediaFile(mainMediaFile);
        setStandalone(standalone);
    }
    public void setStandalone(boolean standalone) {
        this.standalone = standalone;
    }

    public boolean isStandalone() {
        return standalone;
    }

    public static boolean setWithQuality(String quality) {
        return (quality != null && quality.equalsIgnoreCase("RAW + JPEG"));
    }
}
