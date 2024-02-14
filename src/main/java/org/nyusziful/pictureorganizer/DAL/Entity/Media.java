package org.nyusziful.pictureorganizer.DAL.Entity;

import jakarta.persistence.*;
import org.nyusziful.pictureorganizer.DTO.Meta;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "media_image")
@DiscriminatorColumn(name = "file_type")
@DiscriminatorValue("DEF")
public class Media implements Cloneable {
    public enum MediaType {
        MAIN, THMB
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    protected int id = -1;

    @ManyToOne
    @JoinColumn(name= "media_file_version_id", referencedColumnName="id")
    private MediaFileVersion mediaFileVersion;

    @ManyToOne
    @JoinColumn(name= "image_id", referencedColumnName="id")
    private Image image;
    @Enumerated(EnumType.STRING)
    @Column(length = 5, name = "media_type")
    private MediaType mediaType;
    private Long duration;
    private String latitude;
    private String longitude;
    private String altitude;
    private Integer orientation;
    private String keyword;
    private Integer rating;
    private String title;

    public Media() {
    }

    public Media(MediaFileVersion mediaFileVersion, Image image, Meta meta, MediaType mediaType) {
        this.mediaFileVersion = mediaFileVersion;
        this.image = image;
        this.duration = meta.duration;
        this.latitude = meta.latitude;
        this.longitude = meta.longitude;
        this.altitude = meta.altitude;
        this.orientation = meta.orientation;
        this.keyword = meta.keyword;
        this.rating = meta.rating;
        this.title = meta.title;
        this.title = meta.title;
        this.mediaType = mediaType;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        Media media = new Media();
        media.mediaFileVersion = this.mediaFileVersion;
        media.image = this.image;
        media.duration = this.getDuration();
        media.latitude = this.getLatitude();
        media.longitude = this.getLongitude();
        media.altitude = this.getAltitude();
        media.orientation = this.getOrientation();
        media.keyword = this.getKeyword();
        media.rating = this.getRating();
        media.title = this.getTitle();
        media.mediaType = this.getMediaType();
        return media;
    }
    public Image getImage() {
        return image;
    }
    public MediaFileVersion getMediaFileVersion() {
        return mediaFileVersion;
    }

    public MediaType getMediaType() {
        return mediaType;
    }

    public Long getDuration() {
        return duration;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getAltitude() {
        return altitude;
    }

    public Integer getOrientation() {
        return orientation;
    }

    public String getKeyword() {
        return keyword;
    }

    public Integer getRating() {
        return rating;
    }

    public String getTitle() {
        return title;
    }
}
