package org.nyusziful.pictureorganizer.Model;

import com.sun.javaws.exceptions.InvalidArgumentException;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static java.lang.Integer.parseInt;

public class MediaDirectory {
    private LocalDate firstDate = null;
    private LocalDate lastDate = null;
    private String label;
    public static DateTimeFormatter FolderFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");//2018-06-14

    private File directory;
    private boolean conflicting = false;


    public MediaDirectory(File directory) throws InvalidArgumentException {
        this.directory = directory;
        try {
            String directoryName = directory.getName();
            firstDate = LocalDate.of(
                    parseInt(directoryName.substring(0, 4)),
                    parseInt(directoryName.substring(5, 7)),
                    parseInt(directoryName.substring(8, 10)));
            lastDate = LocalDate.of(
                    parseInt(directoryName.substring(13, 17)),
                    parseInt(directoryName.substring(18, 20)),
                    parseInt(directoryName.substring(21, 23)));
            if (directoryName.length() > 24)
                label = directoryName.substring(24);
            else
                label = "";
        } catch (Exception e) {
            throw new InvalidArgumentException(new String[]{"Not valid"});
        }
    }

    @Override
    public String toString() {
        return directory.getName();
    }

    public LocalDate getFirstDate() {
        return firstDate;
    }

    public LocalDate getLastDate() {
        return lastDate;
    }

    public boolean isConflicting() {
        return conflicting;
    }

    public void setConflicting(boolean conflicting) {
        this.conflicting = conflicting;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setFirstDate(LocalDate firstDate) {
        this.firstDate = firstDate;
    }

    public void setLastDate(LocalDate lastDate) {
        this.lastDate = lastDate;
    }

    public File getDirectory() {
        return directory;
    }

    public void setDirectory(File directory) {
        this.directory = directory;
    }

}
