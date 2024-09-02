package org.nyusziful.pictureorganizer.DAL.Entity;

import jakarta.persistence.*;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.HashSet;
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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="media_file_id", referencedColumnName="id", nullable=true)
    private MediaFile mediaFile;

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

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "mediaFileVersion")
    private Set<Media> media;

    @Transient
    private ZonedDateTime dateStored;

    private void stringToDate() {
//        if (dateStoredString != null)
//            dateStored = ZonedDateTime.parse(dateStoredString, XmpDateFormatTZ);
        String prefix = "";
        if (dateStoredTZString != null && (dateStoredTZString.startsWith("+") || dateStoredTZString.startsWith("-")))
            prefix = "UTC";
        if (dateStoredLocal != null && dateStoredTZString != null)
            dateStored = dateStoredLocal.toLocalDateTime().atZone(ZoneId.of(prefix + dateStoredTZString));
    }

    public MediaFileVersion() {
        // this form used by Hibernate
    }

    public MediaFileVersion(String filehash, MediaFileVersion parent, Long size, MediaFile mediaFile, ZonedDateTime dateStored, Boolean invalid) {
        this.filehash = filehash;
        this.parent = parent;
        this.size = size;
        this.mediaFile = mediaFile;
        this.media = new HashSet<>();
        this.setInvalid(invalid);
        setDateStored(dateStored);
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
    public boolean equals(Object o){
        if (this == o) return true;
        if (!(o instanceof MediaFileVersion other)) return false;
        return getId() == other.getId() || ((getId() == -1 || other.getId() == -1) && (filehash != null && filehash.equals(other.filehash)) && size == other.size);//TODO for broken files this might be not ok
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

    public Boolean isInvalid() {
        return getInvalid();
    }

    public void setParent(MediaFileVersion parent) {
        this.parent = parent;
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

    public Media getMainMedia() {
        if (media != null && !media.isEmpty())
            for (Media media1 : media) {
                if (media1.getMediaType().equals(Media.MediaType.MAIN)) return media1;
            }
        return null;
    }

    public int getVersionNumber() {
        if (getParent() == null) return 0;
        else return getParent().getVersionNumber() + 1;
    }

    public MediaFile getMediaFile() {
        return mediaFile;
    }

    public void setMediaFile(MediaFile mediaFile) {
        this.mediaFile = mediaFile;
    }

    @Override
    public String toString() {
        return "MediaFileVersion{" +
                "id=" + id +
                ", parent=" + parent +
                ", invalid=" + getInvalid() +
                '}';
    }

    public Boolean getInvalid() {
        return invalid;
    }

    public void setInvalid(Boolean invalid) {
        this.invalid = invalid;
    }

    public boolean isAncestor(MediaFileVersion mediaFileVersion) {
        MediaFileVersion parent = getParent();
        while (parent != null) {
            if (parent.equals(mediaFileVersion)) return true;
        }
        return false;
    }
}
