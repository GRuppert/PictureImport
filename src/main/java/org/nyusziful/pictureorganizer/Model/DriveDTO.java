package org.nyusziful.pictureorganizer.Model;


import javax.persistence.*;

@Entity
@Table(name = "drive")
public class DriveDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", updatable = false, nullable = false)
    private Long id;
    @Column(name = "Letter")
    private String letter;
    @Column(name = "Description")
    private String description;
    @Column(name = "Backup")
    private boolean backup;

    public DriveDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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
