package org.nyusziful.pictureorganizer.DTO;

import java.util.Date;

public class MediafileDTO {
    public int driveId;
    public String letter;
    // separator is a single slash
    public String path;
    public String filename;
    public boolean hasXMP;
    public String filehash;
    public Long size;
    public Date dateMod;
}
