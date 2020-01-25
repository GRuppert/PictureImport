package org.nyusziful.pictureorganizer.DAL.Entity;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.Collection;

@Entity
@Table(name = "image")
public class Image extends TrackingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private int id = -1;
    @Column(name = "odid", updatable = false, nullable = false)
    private String hash;
    @Column(name = "date_taken", updatable = false)
    private ZonedDateTime dateTaken;
    @Column(name = "date_corrected", updatable = false)
    private ZonedDateTime dateCorrected;
    @Column(name = "original_filename")
    private String originalFilename;
    @Column(name = "type")
    private String type;
    private String latitude;
    private String longitude;
    private String altitude;
    @ManyToOne
    @JoinColumn(name="parent_id", referencedColumnName="id")
    private Image parent;
    @OneToMany(mappedBy = "parent")
    private Collection<Image> children;
    @OneToMany(mappedBy = "image")
    private Collection<Mediafile> mediaFiles;

    public Image() {

    }

    public Image(String hash, ZonedDateTime dateTaken, String oringinalFilename, String type) {
        this.hash = hash;
        this.dateTaken = dateTaken;
        this.originalFilename = oringinalFilename;
        this.type = type;
    }

    public String getHash() {
        return hash;
    }

    public ZonedDateTime getDateTaken() {
        return dateTaken;
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

    public Collection<Mediafile> getMediaFiles() {
        return mediaFiles;
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

    public void setChildren(Collection<Image> children) {
        this.children = children;
    }

    public ZonedDateTime getDateCorrected() {
        return dateCorrected;
    }

    public void setDateCorrected(ZonedDateTime dateCorrected) {
        this.dateCorrected = dateCorrected;
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

    public void setId(int id) {
        this.id = id;
    }
}
