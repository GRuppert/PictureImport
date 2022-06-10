package org.nyusziful.pictureorganizer.DAL.Entity;

import org.apache.commons.io.FilenameUtils;
import org.nyusziful.pictureorganizer.DTO.Meta;
import org.nyusziful.pictureorganizer.Service.Rename.FileNameFactory;

import javax.persistence.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(
    name = "media_file_instance",
    uniqueConstraints = {@UniqueConstraint(columnNames = {"folder_id", "filename"})}
)
public class MediaFileInstance extends TrackingEntity implements Cloneable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    protected int id = -1;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="folder_id", referencedColumnName="id", nullable=false)
    private Folder folder;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="media_file_id", referencedColumnName="id", nullable=false)
    private MediaFile mediaFile;

    private String filename;

    @Column(name = "name_version")
    private int nameVersion;
    @Column(name = "date_mod")
    private Timestamp dateMod;

    @Transient
    protected Path filePath;


    private void loadPath() {
        if (folder != null && folder.getJavaPath() != null && filename != null) {
            filePath = Paths.get(folder.getJavaPath().toString() + "\\" + filename);
        }
    }

    public MediaFileInstance() {
        // this form used by Hibernate
    }

    public MediaFileInstance(Folder folder, Path path, long size, Timestamp dateMod, Boolean original) {
        moveFile(folder, path);
        this.dateMod = dateMod;
        this.dateMod.setNanos(0);
    }

    public MediaFileInstance(MediaFileInstance mediaFile) {
        moveFile(mediaFile.getFolder(), mediaFile.getFilePath());
        this.dateMod = mediaFile.getDateMod();
        this.dateMod.setNanos(0);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        MediaFileInstance mediaFile = (MediaFileInstance)super.clone();
        mediaFile.id = -1;
        return mediaFile;
    }

    public void moveFile(Folder folder, Path filePath) {
        this.filePath = filePath;
        this.filename = filePath.getFileName().toString();
        updateVersion();
        this.folder = folder;
    }

    public void updateVersion() {
        Meta result = FileNameFactory.getV(filename);
        if (result != null) {
            this.nameVersion = result.nameVersion;
        } else {
            this.nameVersion = 0;
        }
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
        loadPath();
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
        if (anObject instanceof MediaFileInstance) {
            MediaFileInstance anotherFile = (MediaFileInstance)anObject;
            if (id > -1) {
                if (id == anotherFile.id) return true;
                else return false;
            }
            if ((this.filename != null && this.filename.equals(anotherFile.filename)) &&
                (this.folder != null && this.folder.equals(anotherFile.folder)) &&
                (this.dateMod != null && this.dateMod.toInstant().toEpochMilli() == anotherFile.dateMod.toInstant().toEpochMilli())
            ) return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(filename, folder);
    }

    public int getId() {
        return id;
    }

    public Folder getFolder() {
        return folder;
    }

    public Drive getDrive() {
        return folder.getDrive();
    }

    public String getType() {
        return FilenameUtils.getExtension(filename);
    }

    public Path getFilePath() {
        if (filePath == null) loadPath();
        return filePath;
    }

    public void updateFolder(Folder folder) {
        this.folder = folder;
    }

    @Override
    public String toString() {
        return folder + "\\" + filename;
    }

    public int getNameVersion() {
        return nameVersion;
    }

    public void setNameVersion(int nameVersion) {
        this.nameVersion = nameVersion;
    }

    public MediaFile getMediaFile() {
        return mediaFile;
    }

    public void setMediaFile(MediaFile mediaFile) {
        this.mediaFile = mediaFile;
    }
}
