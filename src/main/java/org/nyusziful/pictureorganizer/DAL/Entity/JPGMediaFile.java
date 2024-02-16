package org.nyusziful.pictureorganizer.DAL.Entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("JPG")
public class JPGMediaFile extends MediaFile {
    private boolean standalone = true;

    public JPGMediaFile() {
    }

    public JPGMediaFile(String originalFilename, Integer shotnumber, boolean standalone, MediaFile mainMediaFile) {
        super(originalFilename, shotnumber);
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
        return !(quality != null && quality.equalsIgnoreCase("RAW + JPEG"));
    }
}
