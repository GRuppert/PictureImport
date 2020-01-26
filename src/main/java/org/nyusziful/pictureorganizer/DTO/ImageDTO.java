package org.nyusziful.pictureorganizer.DTO;

import org.nyusziful.pictureorganizer.Service.Hash.MediaFileHash;

import java.time.ZonedDateTime;

import static org.nyusziful.pictureorganizer.Service.Hash.MediaFileHash.EMPTYHASH;
import static org.nyusziful.pictureorganizer.Service.Hash.MediaFileHash.Type;

public class ImageDTO {
    public String hash = EMPTYHASH;
    public ZonedDateTime dateTaken = null;
    public ZonedDateTime dateCorrected = null;
    public String originalFilename = "";
    public String type = Type.UNKNOWN.name();
    public String latitude = "";
    public String longitude = "";
    public String altitude = "";
    public String parentHash = "";

}
