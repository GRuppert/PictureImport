package org.nyusziful.pictureorganizer.DAL.Entity;

import org.nyusziful.pictureorganizer.DTO.ImageDTO;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static org.nyusziful.pictureorganizer.UI.StaticTools.XmpDateFormatTZ;

@Entity
@Table(
        name = "image",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"odid", "type"})}
)
public class Image extends TrackingEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    protected int id = -1;
    @Column(name = "odid", nullable = false)
    private String hash;
    @Column(name = "original_file_hash")
    private String originalFileHash;
    @Column(name = "date_taken")
    private String dateTakenString;
    @Transient
    private ZonedDateTime dateTaken;
    @Column(name = "date_corrected")
    private String dateCorrectedString;
    @Transient
    private ZonedDateTime dateCorrected;
    @Column(name = "original_filename")
    private String originalFilename;
    @Column(name = "type", updatable = false, nullable = false)
    private String type;
    private String latitude;
    private String longitude;
    private String altitude;
    @Column(name = "duration")
    private long duration;
    @ManyToOne
    @JoinColumn(name="parent_id", referencedColumnName="id")
    private Image parent;
    @OneToMany(mappedBy = "parent")
    private Set<Image> children = new HashSet<>();

    private boolean valid = true;

    @PostLoad
    private void stringToDate() {
        if (dateTakenString != null)
            dateTaken = ZonedDateTime.parse(dateTakenString, XmpDateFormatTZ);
        if (dateCorrectedString != null)
            dateCorrected = ZonedDateTime.parse(dateCorrectedString, XmpDateFormatTZ);
    }


    protected Image() {}

    public Image(String hash, String type) {
        this.hash = hash;
        this.type = type;
    }

/*
    public Image(String hash, ZonedDateTime dateTaken, String oringinalFilename, String type) {
        this.hash = hash;
        this.dateTaken = dateTaken;
        this.originalFilename = oringinalFilename;
        this.type = type;
    }
*/

    public String getHash() {
        return hash;
    }

    public ZonedDateTime getDateTaken() {
        return dateTaken;
    }

    public void setDateTaken(ZonedDateTime dateTaken) {
        this.dateTaken = dateTaken;
        if (dateTaken != null)
            dateTakenString = XmpDateFormatTZ.format(dateTaken);
        else
            dateTakenString = null;

    }

    public String getOriginalFilename() {
        return originalFilename;
    }

    public void setOriginalFilename(String oringinalFilename) {
        this.originalFilename = oringinalFilename;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "ImageDTO{" +
                "hash='" + hash + '\'' +
                ", dateTaken=" + dateTaken +
                ", oringinalFilename='" + originalFilename + '\'' +
                ", type='" + type + '\'' +
                '}';
    }

    public Image getParent() {
        return parent;
    }

    public void setParent(Image parent) {
        this.parent = parent;
    }

    public Collection<Image> getChildren() {
        return children;
    }

    public ZonedDateTime getDateCorrected() {
        return dateCorrected;
    }

    public void setDateCorrected(ZonedDateTime dateCorrected) {
        this.dateCorrected = dateCorrected;
        if (dateCorrected != null)
            dateCorrectedString = XmpDateFormatTZ.format(dateCorrected);
        else
            dateCorrectedString = null;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getAltitude() {
        return altitude;
    }

    public void setAltitude(String altitude) {
        this.altitude = altitude;
    }

    public ZonedDateTime getActualDate() {
        return getDateCorrected() != null ? getDateCorrected() : getDateTaken();
    }

    public int getId() {
        return id;
    }

    public String getOriginalFileHash() {
        return originalFileHash;
    }

    public void setOriginalFileHash(String originalFileHash) {
        this.originalFileHash = originalFileHash;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getDuration() {
        return duration;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public void correctHash(ImageDTO hash) {
        if (!valid) {
            this.hash = hash.hash;
            valid = true;
        }
    }
}
