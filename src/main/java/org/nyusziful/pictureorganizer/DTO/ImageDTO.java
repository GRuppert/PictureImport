package org.nyusziful.pictureorganizer.DTO;

import java.time.ZonedDateTime;

public class ImageDTO {
    public String hash;
    public ZonedDateTime dateTaken;
    public ZonedDateTime dateCorrected;
    public String originalFilename;
    public String type;
    public String latitude;
    public String longitude;
    public String altitude;
    public String parentHash;

}
