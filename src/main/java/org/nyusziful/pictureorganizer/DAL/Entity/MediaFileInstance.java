package org.nyusziful.pictureorganizer.DAL.Entity;

import org.apache.commons.io.FilenameUtils;
import org.nyusziful.pictureorganizer.DTO.Meta;
import org.nyusziful.pictureorganizer.Service.Rename.FileNameFactory;

import jakarta.persistence.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(
    name = "media_file_instance",
    uniqueConstraints = {@UniqueConstraint(columnNames = {"folder_id", "filename"})}
)
@DiscriminatorColumn(name = "file_type")
@DiscriminatorValue("DEF")
public class MediaFileInstance extends TrackingEntity implements Cloneable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    protected int id = -1;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="folder_id", referencedColumnName="id", nullable=false)
    private Folder folder;

    @Column(name = "filename")
    private String filename;

    @Column(name = "name_version")
    private int nameVersion;
    @Column(name = "date_mod")
    private Timestamp dateMod;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="media_file_id", referencedColumnName="id", nullable=false)
    private MediaFile mediaFile;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="media_file_version_id", referencedColumnName="id", nullable=false)
    private MediaFileVersion mediaFileVersion;

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

    public MediaFileInstance(Folder folder, Path path, Timestamp dateMod, MediaFileVersion mediaFileVersion) {
        updatePath(folder, path);
        this.dateMod = dateMod;
        this.dateMod.setNanos(0);
        setMediaFileVersion(mediaFileVersion);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        MediaFileInstance mediaFile = (MediaFileInstance)super.clone();
        mediaFile.id = -1;
        mediaFile.folder = folder;
        mediaFile.filename = filename;
        mediaFile.nameVersion = nameVersion;
        mediaFile.dateMod = dateMod;
        setMediaFileVersion(mediaFileVersion);
        mediaFile.filePath = filePath;
        return mediaFile;
    }

    public void updatePath(Folder folder, Path filePath) {
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
    public boolean equals(Object o){
        if (this == o) return true;
        if (!(o instanceof MediaFileInstance other)) return false;
        return getId() == other.getId() || ((getId() == -1 || other.getId() == -1) && (filename != null && filename.equals(other.filename)) &&
                (folder != null && folder.equals(other.folder)) &&
                (dateMod != null && dateMod.toInstant().toEpochMilli() == other.dateMod.toInstant().toEpochMilli()));
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

    public MediaFileVersion getMediaFileVersion() {
        return mediaFileVersion;
    }

    public void setMediaFileVersion(MediaFileVersion mediaFileVersion) {
        this.mediaFileVersion = mediaFileVersion;
        this.mediaFile = mediaFileVersion.getMediaFile();
    }
}
