package org.nyusziful.pictureorganizer.DAL.Entity;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.sql.Timestamp;

public class TrackingEntity {
    private Timestamp credate;
    private String creator;
    private Timestamp upddate;
    private String updater;

    @PrePersist
    private void creator() {
        credate = new java.sql.Timestamp(System.nanoTime());
        creator = System.getenv("COMPUTERNAME");
        upddate = new java.sql.Timestamp(System.nanoTime());
        updater = System.getenv("COMPUTERNAME");
    }

    @PreUpdate
    private void updater() {
        upddate = new java.sql.Timestamp(System.nanoTime());
        updater = System.getenv("COMPUTERNAME");
    }
}
