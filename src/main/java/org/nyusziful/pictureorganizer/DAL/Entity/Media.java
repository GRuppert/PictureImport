package org.nyusziful.pictureorganizer.DAL.Entity;

import javax.persistence.*;

@Entity
@Table(name = "media_image")
public class Media implements Cloneable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    protected int id = -1;

    @ManyToOne
    @JoinColumn(name= "media_file_version", referencedColumnName="id")
    private MediaFileVersion mediaFileVersion;

    @ManyToOne
    @JoinColumn(name= "image_id", referencedColumnName="id")
    private Image image;

    @ManyToOne
    @JoinColumn(name= "meta_data_id", referencedColumnName="id")
    private MetaData metaData;

    public Media() {
    }

    public Media(MediaFileVersion mediaFileVersion, Image image, MetaData metaData) {
        this.mediaFileVersion = mediaFileVersion;
        this.image = image;
        this.metaData = metaData;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        Media media = new Media();
        media.mediaFileVersion = this.mediaFileVersion;
        media.image = this.image;
        media.metaData = this.metaData;
        return media;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public MetaData getMetaData() {
        return metaData;
    }

    public void setMetaData(MetaData metaData) {
        this.metaData = metaData;
    }

    public MediaFileVersion getMediaFileVersion() {
        return mediaFileVersion;
    }
}
