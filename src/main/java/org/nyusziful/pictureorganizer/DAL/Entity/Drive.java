package org.nyusziful.pictureorganizer.DAL.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "drive")
public class Drive {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private int id;
    private String description;
    private String volumeSN;
    private boolean backup;

    public Drive() {
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isBackup() {
        return backup;
    }

    public void setBackup(boolean backup) {
        this.backup = backup;
    }

    @Override
    public String toString() {
        return "DriveDTO{" +
                "id=" + id +
                ", volumeSN='" + volumeSN + '\'' +
                ", description='" + description + '\'' +
                ", backup=" + backup +
                '}';
    }

    public String getVolumeSN() {
        return volumeSN;
    }

    public void setVolumeSN(String volumeSN) {
        this.volumeSN = volumeSN;
    }
}
