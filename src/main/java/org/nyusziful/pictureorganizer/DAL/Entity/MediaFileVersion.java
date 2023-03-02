package org.nyusziful.pictureorganizer.DAL.Entity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.Set;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "media_file_version")
@DiscriminatorColumn(name = "file_type")
@DiscriminatorValue("DEF")
public class MediaFileVersion extends TrackingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    protected int id = -1;

    @Column(name = "filehash")
    private String filehash;

    @Column(name = "commit")
    private String commit;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="parent_id", referencedColumnName="id", nullable=true)
    private MediaFileVersion parent;

    @Column(name = "size")
    private Long size;

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

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH})
    @JoinColumn(name="image_id", referencedColumnName="id")
    private Set<Media> media;

    @Transient
    private ZonedDateTime dateStored;

    private void stringToDate() {
//        if (dateStoredString != null)
//            dateStored = ZonedDateTime.parse(dateStoredString, XmpDateFormatTZ);
        String prefix = "";
        if (dateStoredTZString.startsWith("+") || dateStoredTZString.startsWith("-"))
            prefix = "UTC";
        if (dateStoredLocal != null && dateStoredTZString != null)
            dateStored = dateStoredLocal.toLocalDateTime().atZone(ZoneId.of(prefix + dateStoredTZString));
    }

    public MediaFileVersion() {
        // this form used by Hibernate
    }

    public MediaFileVersion(String filehash, MediaFileVersion parent, Long size) {
        this.filehash = filehash;
        this.parent = parent;
        this.size = size;
    }

    public String getFilehash() {
        return filehash;
    }

    public void setFilehash(String filehash) {
        this.filehash = filehash;
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
        if (anObject instanceof MediaFileVersion) {
            MediaFileVersion anotherFile = (MediaFileVersion)anObject;
            if (id > -1) {
                if (id == anotherFile.id) return true;
                else return false;
            }
            if (
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

    public Boolean isOriginal() {
        return parent == null;
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

    public String getCommit() {
        return commit;
    }

    public void setCommit(String commit) {
        this.commit = commit;
    }

    public Boolean getInvalid() {
        return invalid;
    }

    public void setInvalid(Boolean invalid) {
        this.invalid = invalid;
    }

    public MediaFileVersion getParent() {
        return parent;
    }

    public String getDateStoredString() {
        return dateStoredString;
    }

    public Timestamp getDateStoredLocal() {
        return dateStoredLocal;
    }

    public Timestamp getDateStoredUTC() {
        return dateStoredUTC;
    }

    public String getDateStoredTZString() {
        return dateStoredTZString;
    }

    public Set<Media> getMedia() {
        return media;
    }
}
