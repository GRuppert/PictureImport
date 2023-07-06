package org.nyusziful.pictureorganizer.DAL.Entity;

import javax.persistence.*;
import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static java.lang.Integer.parseInt;

@Entity
@Table(name = "media_directory")
public class MediaDirectory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    protected int id = -1;
    @Column(name = "from_date")
    private LocalDate firstDate = null;
    @Column(name = "to_date")
    private LocalDate lastDate = null;
    @Column(name = "title")
    private String label;
    public static DateTimeFormatter FolderFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");//2018-06-14
    @Transient
    private boolean conflicting = false;
    public MediaDirectory() {

    }
    @Override
    public String toString() {
        return firstDate + " - " + lastDate + (label.isEmpty() ? "" : " " + label);
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
}
