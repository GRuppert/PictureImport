package org.nyusziful.pictureorganizer.DAL.Entity;

import com.drew.imaging.jpeg.JpegMetadataReader;
import com.drew.imaging.jpeg.JpegProcessingException;
import com.drew.metadata.Metadata;
import org.nyusziful.pictureorganizer.DTO.Meta;
import org.nyusziful.pictureorganizer.Service.ExifUtils.ExifReadWriteIMR;
import org.nyusziful.pictureorganizer.Service.ExifUtils.ExifService;
import org.nyusziful.pictureorganizer.Service.Hash.JPGHash;

import javax.persistence.*;
import java.io.*;
import java.nio.file.Path;
import java.sql.Timestamp;
import java.time.ZoneId;

import static javax.xml.bind.DatatypeConverter.parseHexBinary;

@Entity
@DiscriminatorValue("JPG")
public class JPGMediaFile extends MediaFile {
    private Boolean exifbackup;
    private boolean standalone = true;


    public JPGMediaFile() {
        // this form used by Hibernate
    }

    public JPGMediaFile(Folder folder, Path path, long size, Timestamp dateMod, Boolean original) {
        super(folder, path, size, dateMod, original);
/*        exifbackup = checkBackupExif();

        if (original) {
            exifbackup = addExifbackup();
        } else {
            exifbackup = checkBackupExif();
        }
*/
    }

    public JPGMediaFile(JPGMediaFile jpgMediaFile) {
        super(jpgMediaFile);
        this.exifbackup = jpgMediaFile.isExifbackup();
        this.standalone = jpgMediaFile.isStandalone();

    }

    public boolean addExifbackup(boolean orig) {
        exifbackup = JPGHash.addBackupExif(filePath.toFile(), orig);
        return exifbackup;
    }

    public boolean checkBackupExif() {
        exifbackup = JPGHash.checkBackupExif(filePath.toFile());
        return exifbackup;
    }

    public boolean isExifbackup() {
        return exifbackup;
    }

    public void setStandalone(boolean standalone) {
        this.standalone = standalone;
    }

    public boolean isStandalone() {
        return standalone;
    }

    @Override
    public void setMeta(Meta meta) {
        super.setMeta(meta);
        setWithQuality(meta.quality);
    }

    public void setWithQuality(String quality) {
        if (quality == null) return;
        switch (quality) {
            case "RAW + JPEG":
                setStandalone(false);
                break;
            default:
                setStandalone(true);
        }
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        JPGMediaFile jpgMediaFile = (JPGMediaFile)super.clone();
        jpgMediaFile.exifbackup = this.isExifbackup();
        jpgMediaFile.standalone = this.isStandalone();
        return jpgMediaFile;
    }

    public void readDBExif() {
        byte[] jpgHeader =  parseHexBinary("ffd8");//ffe000124A46494600010200000100010000");
        byte[] exif = getExif();
        byte[] jpgClose = parseHexBinary("ffdaffd9");
        int length = exif.length;
        byte[] fileBytes = new byte[2 + length + 4];  //resultant array of size first array and second array
        System.arraycopy(jpgHeader, 0, fileBytes, 0, 2);
        System.arraycopy(exif, 0, fileBytes, 2, length);
        System.arraycopy(jpgClose, 0, fileBytes, length+2, 4);
        InputStream stream = new ByteArrayInputStream(fileBytes);
        String filename = "e:\\temp2.jpg";
        File file = new File(filename);
        file.delete();
        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            outputStream.write(jpgHeader);
            outputStream.write(exif);
            outputStream.write(jpgClose);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            ExifReadWriteIMR.readMeta(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
/*
        try {
            Metadata md = JpegMetadataReader.readMetadata(stream);
            System.out.println("");
        } catch (JpegProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
*/
        setMeta(ExifService.readMeta(stream, getFilename(), getDateStored().getZone() == null ? ZoneId.systemDefault() : getDateStored().getZone()));
    }

}
