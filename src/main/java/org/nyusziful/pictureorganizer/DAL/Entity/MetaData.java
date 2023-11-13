package org.nyusziful.pictureorganizer.DAL.Entity;

import jakarta.persistence.*;
import org.nyusziful.pictureorganizer.DTO.Meta;

@Entity
@Table(name = "meta_data")
public class MetaData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private int id;
    private Long duration;
    private String latitude;
    private String longitude;
    private String altitude;
    private Integer orientation;
    private String keyword;
    private Integer rating;
    private String title;

    public MetaData() {
    }

    public MetaData(Meta meta) {
        this.duration = meta.duration;
        this.latitude = meta.latitude;
        this.longitude = meta.longitude;
        this.altitude = meta.altitude;
        this.orientation = meta.orientation;
        this.keyword = meta.keyword;
        this.rating = meta.rating;
        this.title = meta.title;
    }

}
