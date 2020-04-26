package org.nyusziful.pictureorganizer.DAL.Entity;

import org.apache.commons.io.FilenameUtils;
import org.hibernate.Hibernate;

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
public class MediaFile extends TrackingEntity implements Cloneable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    protected int id = -1;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="drive_id", referencedColumnName="id", nullable=false)
    private Drive drive;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="folder_id", referencedColumnName="id", nullable=false)
    private Folder folder;

    private String filename;

    @ManyToOne(
            fetch = FetchType.LAZY,
            cascade = {CascadeType.REFRESH})
    @JoinColumn(name="image_id", referencedColumnName="id")
    private Image image;

    private String filehash;

    private Long size;

    @Column(name = "date_mod")
    private Timestamp dateMod;

    @Column(name = "date_stored")
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

    private void stringToDate() {
        if (dateStoredString != null)
            dateStored = ZonedDateTime.parse(dateStoredString, XmpDateFormatTZ);

    }

    private void loadPath() {
        if (folder != null && folder.getJavaPath() != null && filename != null) {
            filePath = Paths.get(folder.getJavaPath().toString() + "\\" + filename);
        }
    }

    private void loadOriginal() {
        if (image != null && image.getOriginalFileHash() != null) {
            original = filehash.equals(image.getOriginalFileHash());
        }
    }

    public MediaFile() {
        // this form used by Hibernate
    }

    public MediaFile(Folder folder, Path path, long size, Timestamp dateMod, boolean original) {
        moveFile(folder, path);
        this.size = size;
        this.dateMod = dateMod;
        this.dateMod.setNanos(0);
        this.original = original;
    }

    public MediaFile(MediaFile mediaFile) {
        moveFile(mediaFile.getFolder(), mediaFile.getFilePath());
        this.size = mediaFile.getSize();
        this.dateMod = mediaFile.getDateMod();
        this.dateMod.setNanos(0);
        this.original = mediaFile.isOriginal();
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        MediaFile mediaFile = (MediaFile)super.clone();
        mediaFile.id = -1;
        return mediaFile;
    }

    public void moveFile(Folder folder, Path filePath) {
        this.filePath = filePath;
        this.filename = filePath.getFileName().toString();
        this.drive = folder.getDrive();
        this.folder = folder;
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
            if (id > -1) {
                if (id == anotherFile.id) return true;
                else return false;
            }
            if ((this.filename != null && this.filename.equals(anotherFile.filename)) &&
                (this.folder != null && this.folder.equals(anotherFile.folder)) &&
                this.drive == anotherFile.drive &&
                (this.image != null && this.image.equals(anotherFile.image)) &&
                (this.filehash != null && this.filehash.equals(anotherFile.filehash)) &&
                this.size == anotherFile.size &&
                (this.dateMod != null && this.dateMod.toInstant().toEpochMilli() == anotherFile.dateMod.toInstant().toEpochMilli())
            ) return true;
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
        this.image = image;
    }

    private boolean sameAsFormer(Image newImage) {
        return image==null? newImage == null : image.equals(newImage);
    }

    public String getType() {
        return FilenameUtils.getExtension(filename);
    }

    public Path getFilePath() {
        if (filePath == null) loadPath();
        return filePath;
    }

    public Boolean isOriginal() {
        if (original == null) loadOriginal();
        return original;
    }

    public void setOriginal(Boolean original) {
        this.original = original;
    }

    public ZonedDateTime getDateStored() {
        if (dateStored == null) stringToDate();
        return dateStored;
    }

    public void setDateStored(ZonedDateTime dateCorrected) {
        this.dateStored = dateCorrected;
        if (dateStored != null)
            dateStoredString = XmpDateFormatTZ.format(dateStored);
        else
            dateStoredString = null;
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

    public void updateFolder(Folder folder) {
        this.folder = folder;
    }

    @Override
    public String toString() {
        return folder + "\\" + filename;
    }
}
