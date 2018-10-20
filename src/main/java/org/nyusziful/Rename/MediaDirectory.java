package org.nyusziful.Rename;

import java.time.ZonedDateTime;

import static java.lang.Integer.parseInt;

public class MediaDirectory {
    public ZonedDateTime from;
    public ZonedDateTime to;

    public MediaDirectory(String directoryName) {
        int sY = parseInt(directoryName.substring(0, 4)), sM = parseInt(directoryName.substring(5, 7)) - 1, sD = parseInt(directoryName.substring(8, 10));
        int eY = parseInt(directoryName.substring(13, 17)), eM = parseInt(directoryName.substring(18, 20)) - 1, eD = parseInt(directoryName.substring(21, 23));

    }

}
