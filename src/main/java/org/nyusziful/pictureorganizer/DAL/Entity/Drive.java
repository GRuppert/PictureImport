package org.nyusziful.pictureorganizer.DAL.Entity;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "drive")
public class Drive {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private int id= -1;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Drive other)) return false;
        return getId() == other.getId() || ((getId() == -1 || other.getId() == -1) && Objects.equals(getVolumeSN(), other.getVolumeSN()));
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}
