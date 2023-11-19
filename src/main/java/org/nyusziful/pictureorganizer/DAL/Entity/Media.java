package org.nyusziful.pictureorganizer.DAL.Entity;

import jakarta.persistence.*;
import org.nyusziful.pictureorganizer.DTO.Meta;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "media_image")
@DiscriminatorColumn(name = "file_type")
@DiscriminatorValue("DEF")
public class Media implements Cloneable {
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

    public Media(MediaFileVersion mediaFileVersion, Image image, Meta meta) {
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
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        Media media = new Media();
        media.mediaFileVersion = this.mediaFileVersion;
        media.image = this.image;
        media.duration = this.duration;
        media.latitude = this.latitude;
        media.longitude = this.longitude;
        media.altitude = this.altitude;
        media.orientation = this.orientation;
        media.keyword = this.keyword;
        media.rating = this.rating;
        media.title = this.title;
        return media;
    }
    public Image getImage() {
        return image;
    }
    public void setImage(Image image) {
        this.image = image;
    }
    public MediaFileVersion getMediaFileVersion() {
        return mediaFileVersion;
    }
}
