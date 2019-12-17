package org.nyusziful.pictureorganizer.DAL.Entity;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.Collection;

@Entity
@Table(name = "image")
public class Image extends TrackingEntity {
    @Id
    @Column(name = "ODID", updatable = false, nullable = false)
    private String hash;
    @Column(name = "Date_taken", updatable = false)
    private ZonedDateTime dateTaken;
    @Column(name = "Original_Filename")
    private String oringinalFilename;
    @Column(name = "type")
    private String type;
    @ManyToOne
    private Image parent;
    @OneToMany(mappedBy = "parent")
    private Collection<Image> children;
    @OneToMany(mappedBy = "image")
    private Collection<Mediafile> mediaFiles;

    public Image() {

    }

    public Image(String hash, String oringinalFilename, String type) {
        this.hash = hash;
        this.dateTaken = dateTaken;
        this.oringinalFilename = oringinalFilename;
        this.type = type;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public ZonedDateTime getDateTaken() {
        return dateTaken;
    }

    public void setDateTaken(ZonedDateTime dateTaken) {
        this.dateTaken = dateTaken;
    }

    public String getOringinalFilename() {
        return oringinalFilename;
    }

    public void setOringinalFilename(String oringinalFilename) {
        this.oringinalFilename = oringinalFilename;
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
                ", oringinalFilename='" + oringinalFilename + '\'' +
                ", type='" + type + '\'' +
                '}';
    }

    public Collection<Mediafile> getMediaFiles() {
        return mediaFiles;
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

    public void setChildren(Collection<Image> children) {
        this.children = children;
    }
}
