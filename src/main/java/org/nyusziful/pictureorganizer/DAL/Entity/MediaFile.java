package org.nyusziful.pictureorganizer.DAL.Entity;

import org.nyusziful.pictureorganizer.Model.MediaDirectoryInstance;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "media_file")
@DiscriminatorColumn(name = "file_type")
@DiscriminatorValue("DEF")
public class MediaFile extends TrackingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    protected int id = -1;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="main_id", referencedColumnName="id", nullable=true)
    private MediaFile mainMediaFile;

    @Column(name = "shotnumber")
    private Integer shotnumber;

    @Column(name = "original_filename")
    private String originalFilename;

    @OneToOne
    @JoinColumn(name= "original_version_id", referencedColumnName="id")
    private MediaFileVersion originalVersion;

    @ManyToOne
    @JoinColumn(name= "media_directory_id", referencedColumnName="id")
    private MediaDirectory mediaDirectory;

    public MediaFile() {
        // this form used by Hibernate
    }

    public MediaFile(MediaFileVersion originalVersion, String originalFilename, Integer shotnumber) {
        this.originalVersion = originalVersion;
        this.originalFilename = originalFilename;
        this.shotnumber = shotnumber;
    }

    @Override
    public boolean equals(Object anObject){
        if (this == anObject) {
            return true;
        }
        if (anObject instanceof MediaFile) {
            MediaFile anotherFile = (MediaFile)anObject;
            if (id > -1) {
                return id == anotherFile.id;
            }
            return (this.originalVersion != null && this.originalVersion.equals(anotherFile.originalVersion));
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(originalVersion);
    }

    public int getId() {
        return id;
    }

    public MediaFile getMainMediaFile() {
        return mainMediaFile;
    }

    public void setMainMediaFile(MediaFile mainMediaFile) {
        this.mainMediaFile = mainMediaFile;
    }

    public Integer getShotnumber() {
        return shotnumber;
    }

    public void setShotnumber(Integer shotnumber) {
        this.shotnumber = shotnumber;
    }

    public String getOriginalFilename() {
        return originalFilename;
    }

    public void setOriginalFilename(String originalFilename) {
        this.originalFilename = originalFilename;
    }

    public MediaFileVersion getOriginalVersion() {
        return originalVersion;
    }

    public void setOriginalVersion(MediaFileVersion originalVersion) {
        this.originalVersion = originalVersion;
    }

    public MediaDirectory getMediaDirectory() {
        return mediaDirectory;
    }

    public void setMediaDirectory(MediaDirectory mediaDirectory) {
        this.mediaDirectory = mediaDirectory;
    }
}
