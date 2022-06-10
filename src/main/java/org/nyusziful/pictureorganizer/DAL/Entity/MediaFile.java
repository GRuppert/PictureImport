package org.nyusziful.pictureorganizer.DAL.Entity;

import org.nyusziful.pictureorganizer.DTO.Meta;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Objects;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "media_file_instance")
@DiscriminatorColumn(name = "type")
@DiscriminatorValue("DEF")
public class MediaFile extends TrackingEntity implements Cloneable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    protected int id = -1;

    @Column(name = "filehash")
    private String filehash;

    @Column(name = "commit")
    private String commit;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="previous_id", referencedColumnName="id", nullable=false)
    private MediaFile previous;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="main_id", referencedColumnName="id", nullable=false)
    private MediaFile main;

    private Long size;

    //????
    @Column(name = "original_filename")
    private String originalFilename;

/*
    @ManyToMany(
            fetch = FetchType.LAZY,
            cascade = {CascadeType.REFRESH})
*/

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="image_id", referencedColumnName="id")
    private Image image;


    @Column(name = "date_stored")
    private String dateStoredString;

    @Column(name = "date_stored_local")
    private Timestamp dateStoredLocal;

    @Column(name = "date_stored_utc")
    private Timestamp dateStoredUTC;

    @Column(name = "date_stored_tz")
    private String dateStoredTZString;

    @Column(name = "invalid")
    private Boolean invalid;
    @Transient
    private ZonedDateTime dateStored;

    @Transient
    private Boolean original;

    private void stringToDate() {
//        if (dateStoredString != null)
//            dateStored = ZonedDateTime.parse(dateStoredString, XmpDateFormatTZ);
        String prefix = "";
        if (dateStoredTZString.startsWith("+") || dateStoredTZString.startsWith("-"))
            prefix = "UTC";
        if (dateStoredLocal != null && dateStoredTZString != null)
            dateStored = dateStoredLocal.toLocalDateTime().atZone(ZoneId.of(prefix + dateStoredTZString));
    }

    private void loadOriginal() {
        if (image != null && image.getOriginalFileHash() != null) {
            original = filehash.equals(image.getOriginalFileHash());
        }
    }

    public MediaFile() {
        // this form used by Hibernate
    }

    public MediaFile(String filehash, long size, Boolean original) {
        this.filehash = filehash;
        this.size = size;
        this.size = size;
        this.original = original;
    }

    public MediaFile(MediaFile mediaFile) {
        this.size = mediaFile.getSize();
        this.original = mediaFile.isOriginal();
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        MediaFile mediaFile = (MediaFile)super.clone();
        mediaFile.id = -1;
        return mediaFile;
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
            if (
                (this.image != null && this.image.equals(anotherFile.image)) &&
                (this.filehash != null && this.filehash.equals(anotherFile.filehash)) &&
                this.size == anotherFile.size
            ) return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(filehash);
    }

    public int getId() {
        return id;
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
        if (dateStored != null) {
            dateStoredLocal = Timestamp.valueOf(dateStored.toLocalDateTime());
            dateStoredUTC = Timestamp.valueOf(dateStored.withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime());
            dateStoredTZString = dateStored.getZone().getId();
        } else {
            dateStoredLocal = null;
            dateStoredUTC = null;
            dateStoredTZString = null;
        }
    }

    public String getOriginalFilename() {
        return originalFilename;
    }

    public void setOriginalFilename(String originalFilename) {
        this.originalFilename = originalFilename;
    }

    public void setMeta(Meta meta) {
        setDateStored(meta.date);
    }
}
