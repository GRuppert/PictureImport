package org.nyusziful.pictureorganizer.Model;

import org.nyusziful.pictureorganizer.Main.CommonProperties;

import java.time.ZonedDateTime;

import static java.lang.Integer.parseInt;

public class MediaDirectory {
    public ZonedDateTime from = null;
    public ZonedDateTime to = null;

    public MediaDirectory(String directoryName) {
        try {
            from = ZonedDateTime.of(
                    parseInt(directoryName.substring(0, 4)),
                    parseInt(directoryName.substring(5, 7)) - 1,
                    parseInt(directoryName.substring(8, 10)),
                    0, 0, 0, 0, CommonProperties.getInstance().getZone());
            to = ZonedDateTime.of(
                    parseInt(directoryName.substring(13, 17)),
                    parseInt(directoryName.substring(18, 20)) - 1,
                    parseInt(directoryName.substring(21, 23)),
                    23, 59, 59, 999999999, CommonProperties.getInstance().getZone());
        } catch (Exception e) {

        }
    }

}
