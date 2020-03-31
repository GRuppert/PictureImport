package org.nyusziful.pictureorganizer.Model;

import com.sun.javaws.exceptions.InvalidArgumentException;
import org.nyusziful.pictureorganizer.DAL.Entity.Folder;
import org.nyusziful.pictureorganizer.Main.CommonProperties;

import java.io.File;
import java.lang.reflect.Field;
import java.time.ZonedDateTime;

import static java.lang.Integer.parseInt;

public class MediaDirectory {
    public ZonedDateTime from = null;
    public ZonedDateTime to = null;
    private File directory;

    public MediaDirectory(File directory) throws InvalidArgumentException {
        String directoryName = directory.getName();
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
            throw new InvalidArgumentException(new String[]{"Not valid"});
        }
    }

}
