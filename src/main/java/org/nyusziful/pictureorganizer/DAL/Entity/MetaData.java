package org.nyusziful.pictureorganizer.DAL.Entity;

import javax.persistence.*;

@Entity
@Table(name = "meta_data")
public class MetaData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private int id;
    private Integer duration;
    private String latitude;
    private String longitude;
    private String altitude;
    private Integer orientation;
    private String keyword;
    private Integer rating;
    private String title;

    public MetaData() {
    }

}
