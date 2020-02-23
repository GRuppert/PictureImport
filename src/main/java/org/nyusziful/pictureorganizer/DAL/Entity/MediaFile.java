package org.nyusziful.pictureorganizer.DAL.Entity;

import org.apache.commons.io.FilenameUtils;

import javax.persistence.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.Objects;

import static org.nyusziful.pictureorganizer.UI.StaticTools.XmpDateFormatTZ;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(
    name = "media_file",
    uniqueConstraints = {@UniqueConstraint(columnNames = {"drive_id", "folder_id", "filename"})}
)
@DiscriminatorColumn(name = "type")
@DiscriminatorValue("DEF")
public class MediaFile extends TrackingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    protected int id = -1;

    @ManyToOne
    @JoinColumn(name="drive_id", referencedColumnName="id", nullable=false)
    private Drive drive;

    @ManyToOne
    @JoinColumn(name="folder_id", referencedColumnName="id", nullable=false)
    private Folder folder;

    private String filename;

    @ManyToOne(cascade = {CascadeType.REFRESH})
    @JoinColumn(name="image_id", referencedColumnName="id")
    private Image image;

    private String filehash;

    private Long size;

    @Column(name = "date_mod")
    private Timestamp dateMod;

    @Column(name = "date_stored", updatable = false)
    private String dateStoredString;

    @Transient
    private ZonedDateTime dateStored;

    private String latitude;
    private String longitude;
    private String altitude;


    @Transient
    protected Path filePath;

    @Transient
    private Boolean original;

    @PrePersist
    private void dateToString() {
        if (dateStored != null)
            dateStoredString = XmpDateFormatTZ.format(dateStored);
    }

    @PostLoad
    private void fillTransients() {
        stringToDate();
        loadPath();
    }

    private void stringToDate() {
        if (dateStoredString != null)
            dateStored = ZonedDateTime.parse(dateStoredString, XmpDateFormatTZ);

    }

    private void loadPath() {
        filePath = Paths.get(folder.getJavaPath().toString() + "\\" + filename);
        if (image != null && image.getOriginalFileHash() != null) {
            original = filehash.equals(image.getOriginalFileHash());
        }
    }

    public MediaFile() {
        // this form used by Hibernate
    }

    public MediaFile(Drive drive, Folder folder, Path path, long size, Timestamp dateMod, boolean original) {
        this.filePath = path;
        this.filename = path.getFileName().toString();
        this.drive = drive;
        this.folder = folder;
        this.size = size;
        this.dateMod = dateMod;
        this.dateMod.setNanos(0);
        this.original = original;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
        loadPath();
    }

    public String getFilehash() {
        return filehash;
    }

    public void setFilehash(String filehash) {
        this.filehash = filehash;
        if (image != null && image.getOriginalFileHash() != null) {
            original = filehash.equals(image.getOriginalFileHash());
        }
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public Timestamp getDateMod() {
        return dateMod;
    }

    public void setDateMod(Timestamp dateMod) {
        this.dateMod = dateMod;
        this.dateMod.setNanos(0);
    }

    @Override
    public boolean equals(Object anObject){
        if (this == anObject) {
            return true;
        }
        if (anObject instanceof MediaFile) {
            MediaFile anotherFile = (MediaFile)anObject;
            if (id > -1 && id == anotherFile.id) return true;
            if (this.filename.equals(anotherFile.filename) &&
                this.folder.equals(anotherFile.folder) &&
                this.drive == anotherFile.drive &&
                this.image.equals(anotherFile.image) &&
                this.filehash.equals(anotherFile.filehash) &&
                this.size == anotherFile.size &&
                this.dateMod.toInstant().toEpochMilli() == anotherFile.dateMod.toInstant().toEpochMilli())
                return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(filename, folder, drive);
    }

    public int getId() {
        return id;
    }

    public Folder getFolder() {
        return folder;
    }

    public Drive getDrive() {
        return drive;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        setImage(image, false);
    }

    public void setImage(Image image, boolean cross) {
        //prevent endless loop
        if (sameAsFormer(image))
            return;
        //set new owner
        if (this.image!=null && !(cross && image == null))
            this.image.removeMediaFile(this, true);
        this.image = image;
        //remove from the old owner
        //set myself to new owner
        if (image!=null && !cross)
            image.addMediaFile(this, true);
    }

    private boolean sameAsFormer(Image newImage) {
        return image==null? newImage == null : image.equals(newImage);
    }

    public String getType() {
        return FilenameUtils.getExtension(filename);
    }

    public Path getFilePath() {
        return filePath;
    }

    public Boolean isOriginal() {
        return original;
    }

    public void setOriginal(Boolean original) {
        this.original = original;
    }

    public ZonedDateTime getDateStored() {
        return dateStored;
    }

    public void setDateStored(ZonedDateTime dateCorrected) {
        this.dateStored = dateCorrected;
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

    public void remove() {
        setImage(null);
    }
}
