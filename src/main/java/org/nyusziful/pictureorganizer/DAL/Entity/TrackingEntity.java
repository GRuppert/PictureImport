package org.nyusziful.pictureorganizer.DAL.Entity;

import org.nyusziful.pictureorganizer.DAL.DAO.HasID;

import javax.persistence.*;
import java.sql.Timestamp;

@MappedSuperclass
public abstract class TrackingEntity {
    @Column
    protected Timestamp credate;
    @Column
    protected String creator;
    @Column
    protected Timestamp upddate;
    @Column
    protected String updater;
/*
    @Version
    private long version;
*/

    @PrePersist
    protected void creator() {
        credate = new java.sql.Timestamp(System.currentTimeMillis());
        creator = System.getenv("COMPUTERNAME");
        updater();
    }

    @PreUpdate
    protected void updater() {
        upddate = new java.sql.Timestamp(System.currentTimeMillis());
        updater = System.getenv("COMPUTERNAME");
    }

    public Timestamp getUpddate() {
        return upddate;
    }
}
