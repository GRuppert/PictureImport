package org.nyusziful.pictureorganizer.DB;

import java.sql.Timestamp;
import java.util.Objects;

public class filePOJO {
    private int id;
    private String filename;
    private String path;
    private int driveId;
    private String fullhash;
    private String hash;
    private long size;
    private Timestamp dateMod;

    public filePOJO(String filename, String path, int driveId, String fullhash, String hash, long size, Timestamp dateMod) {
        this(-1, filename, path, driveId, fullhash, hash, size, dateMod);
    }

    public filePOJO(String filename, String path, int driveId, long size, Timestamp dateMod) {
        this(-1, filename, path, driveId, "", "", size, dateMod);
    }

    public filePOJO(int id, String filename, String path, int driveId, String fullhash, String hash, long size, Timestamp dateMod) {
        this.id = id;
        this.filename = filename;
        this.path = path;
        this.driveId = driveId;
        this.fullhash = fullhash;
        this.hash = hash;
        this.size = size;
        this.dateMod = dateMod;
        this.dateMod.setNanos(0);

    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getDriveId() {
        return driveId;
    }

    public void setDriveId(int driveId) {
        this.driveId = driveId;
    }

    public String getFullhash() {
        return fullhash;
    }

    public void setFullhash(String fullhash) {
        this.fullhash = fullhash;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public Timestamp getDateMod() {
        return dateMod;
    }

    public void setDateMod(Timestamp dateMod) {
        this.dateMod = dateMod;
        this.dateMod.setNanos(0);
    }

    @Override
    public boolean equals(Object anObject){
        if (this == anObject) {
            return true;
        }
        if (anObject instanceof filePOJO) {
            filePOJO anotherFile = (filePOJO)anObject;
            if (id > -1 && id == anotherFile.id) return true;
            if (this.filename.equals(anotherFile.filename) &&
                this.path.equals(anotherFile.path) &&
                this.driveId == anotherFile.driveId &&
                this.fullhash.equals(anotherFile.fullhash) &&
                this.hash.equals(anotherFile.hash) &&
                this.size == anotherFile.size &&
                this.dateMod.toInstant().toEpochMilli() == anotherFile.dateMod.toInstant().toEpochMilli())
                return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(filename, path, driveId);
    }

}
