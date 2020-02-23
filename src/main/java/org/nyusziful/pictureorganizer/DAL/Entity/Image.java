package org.nyusziful.pictureorganizer.DAL.Entity;

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
    @Column(name = "odid", updatable = false, nullable = false)
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
    @ManyToOne
    @JoinColumn(name="parent_id", referencedColumnName="id")
    private Image parent;
    @OneToMany(mappedBy = "parent")
    private Set<Image> children = new HashSet<>();
    @OneToMany(mappedBy = "image")
    private Set<MediaFile> mediaFiles = new HashSet<>();


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

    public Collection<MediaFile> getMediaFiles() {
        return mediaFiles;
    }

    public void addMediaFile(MediaFile mediaFile) {
        addMediaFile(mediaFile, false);
    }

    public void addMediaFile(MediaFile mediaFile, boolean cross) {
        //prevent endless loop
        if (mediaFiles.contains(mediaFile))
            return ;
        //add new account
        mediaFiles.add(mediaFile);
        //update child if request is not from it
        if (!cross) mediaFile.setImage(this, true);
    }

    /**
     * Removes the account from the person. The method keeps
     * relationships consistency:
     * * the account will no longer reference this person as its owner
     */
    public void removeMediaFile(MediaFile mediaFile) {
        removeMediaFile(mediaFile, false);
    }

    public void removeMediaFile(MediaFile mediaFile, boolean cross) {
        //prevent endless loop
        if (!mediaFiles.contains(mediaFile))
            return ;
        //remove the account
        mediaFiles.remove(mediaFile);
        //remove myself from the twitter account
        if (!cross) mediaFile.setImage(null, true);
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
}
