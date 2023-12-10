package org.nyusziful.pictureorganizer.DTO;

import java.time.ZonedDateTime;

import static org.nyusziful.pictureorganizer.Service.Hash.MediaFileHash.UNKNOWN;
import static org.nyusziful.pictureorganizer.Service.Hash.MediaFileHash.Type;

public class ImageDTO {
    public String hash = UNKNOWN;
    public String exifHash = UNKNOWN;
    public ZonedDateTime dateTaken = null;
    public ZonedDateTime dateCorrected = null;
    public String originalFilename = "";
    public String type = Type.UNKNOWN.name();
    public String latitude = "";
    public String longitude = "";
    public String altitude = "";
    public String parentHash = "";
    public byte[] exif = null;
    public Boolean exifBackup = null;
    public String fullhash = UNKNOWN;

}
