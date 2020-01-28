package org.nyusziful.pictureorganizer.DAL.Entity;

import org.apache.commons.io.FilenameUtils;

import javax.persistence.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(
    name = "media_file",
    uniqueConstraints = {@UniqueConstraint(columnNames = {"drive_id", "folder_id", "filename"})}
)
public class Mediafile extends TrackingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private int id = -1;

    @ManyToOne
    @JoinColumn(name="drive_id", referencedColumnName="id", nullable=false)
    private Drive drive;

    @ManyToOne(cascade=CascadeType.PERSIST)
    @JoinColumn(name="folder_id", referencedColumnName="id", nullable=false)
    private Folder folder;

    private String filename;

    @ManyToOne(cascade=CascadeType.PERSIST)
    @JoinColumn(name="image_id", referencedColumnName="id")
    private Image image;

    private boolean XMPattached;

    private String filehash;

    private Long size;

    @Column(name = "date_mod")
    private Timestamp dateMod;

    public Mediafile() {
        // this form used by Hibernate
    }


    public Mediafile(Drive drive, Path path, long size, Timestamp dateMod) {
        this.filename = path.getFileName().toString();
        this.XMPattached = Files.exists(Paths.get(path.toString()+".xmp"));
        this.drive = drive;
        this.folder = new Folder(drive, path.getParent());
        this.size = size;
        this.dateMod = dateMod;
        this.dateMod.setNanos(0);
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
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
        if (anObject instanceof Mediafile) {
            Mediafile anotherFile = (Mediafile)anObject;
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

    public void setId(int id) {
        this.id = id;
    }

    public Folder getFolder() {
        return folder;
    }

    public void setFolder(Folder folder) {
        this.folder = folder;
    }

    public Drive getDrive() {
        return drive;
    }

    public void setDrive(Drive drive) {
        this.drive = drive;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public String getType() {
        return FilenameUtils.getExtension(filename);
    }

    public boolean isXMPattached() {
        return XMPattached;
    }

    public void setXMPattached(boolean XMPattached) {
        this.XMPattached = XMPattached;
    }
}
