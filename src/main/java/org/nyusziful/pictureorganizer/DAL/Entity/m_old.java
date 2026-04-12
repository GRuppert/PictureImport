package org.nyusziful.pictureorganizer.DAL.Entity;

import jakarta.persistence.*;
/*
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "media_image")
@DiscriminatorColumn(name = "file_type")
@DiscriminatorValue("DEF")
 */
public class m_old implements Cloneable {
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

    @ManyToOne
    @JoinColumn(name= "meta_data_id", referencedColumnName="id")
    private MetaData metaData;

    public m_old() {
    }

    public m_old(MediaFileVersion mediaFileVersion, Image image, MetaData metaData) {
        this.mediaFileVersion = mediaFileVersion;
        this.image = image;
        this.metaData = metaData;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        m_old media = new m_old();
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
