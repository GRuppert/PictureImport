package org.nyusziful.pictureorganizer.DTO;

import org.nyusziful.pictureorganizer.Service.Hash.MediaFileHash;

import java.time.ZonedDateTime;

import static org.nyusziful.pictureorganizer.Service.Hash.MediaFileHash.EMPTYHASH;


public class ImageDTO {
    public String hash = EMPTYHASH;
    public String exifHash = EMPTYHASH;
    public ZonedDateTime dateTaken = null;
    public ZonedDateTime dateCorrected = null;
    public String originalFilename = "";
    public String type = MediaFileHash.Type.UNKNOWN.name();
    public String latitude = "";
    public String longitude = "";
    public String altitude = "";
    public String parentHash = "";
    public byte[] exif = null;
    public Boolean exifBackup = null;
    public String fullhash = EMPTYHASH;

}
