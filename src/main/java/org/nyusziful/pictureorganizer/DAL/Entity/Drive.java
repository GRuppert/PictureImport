package org.nyusziful.pictureorganizer.DAL.Entity;


import javax.persistence.*;

@Entity
@Table(name = "drive")
public class Drive {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private int id;
    private String letter;
    private String description;
    private boolean backup;

    public Drive() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
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
                ", letter='" + letter + '\'' +
                ", description='" + description + '\'' +
                ", backup=" + backup +
                '}';
    }
}
