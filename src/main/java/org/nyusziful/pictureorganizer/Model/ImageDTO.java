package org.nyusziful.pictureorganizer.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.ZonedDateTime;

@Entity
@Table(name = "image")
public class ImageDTO {
    @Id
    @Column(name = "ODID", updatable = false, nullable = false)
    private String hash;
    @Column(name = "Date_taken", updatable = false)
    private ZonedDateTime dateTaken;
    @Column(name = "Original_Filename")
    private String oringinalFilename;
    @Column(name = "type")
    private String type;

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
}
