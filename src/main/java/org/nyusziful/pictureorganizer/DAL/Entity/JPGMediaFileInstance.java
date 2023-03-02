package org.nyusziful.pictureorganizer.DAL.Entity;

import org.nyusziful.pictureorganizer.Service.Hash.JPGHash;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.nio.file.Path;
import java.sql.Timestamp;

@Entity
@DiscriminatorValue("JPG")
public class JPGMediaFileInstance extends MediaFileInstance {
    public JPGMediaFileInstance() {
        // this form used by Hibernate
    }

    public JPGMediaFileInstance(Folder folder, Path path, Timestamp dateMod, MediaFileVersion mediaFileVersion) {
        super(folder, path, dateMod, mediaFileVersion);
/*        exifbackup = checkBackupExif();

        if (original) {
            exifbackup = addExifbackup();
        } else {
            exifbackup = checkBackupExif();
        }
*/
    }

    public boolean addExifbackup(boolean orig) {
        boolean exifbackup = JPGHash.addBackupExif(filePath.toFile(), orig);
        JPGMediaFileVersion jpgMediaFileVersion = ((JPGMediaFileVersion)getMediaFileVersion());
        if (jpgMediaFileVersion != null)
            ((JPGMediaFileVersion)getMediaFileVersion()).setExifbackup(exifbackup);
        return exifbackup;
    }

    public boolean checkBackupExif() {
        boolean exifbackup = JPGHash.checkBackupExif(filePath.toFile());
        JPGMediaFileVersion jpgMediaFileVersion = ((JPGMediaFileVersion)getMediaFileVersion());
        if (jpgMediaFileVersion != null)
            ((JPGMediaFileVersion)getMediaFileVersion()).setExifbackup(exifbackup);
        return exifbackup;
    }

/*
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
*/
/*
        try {
            Metadata md = JpegMetadataReader.readMetadata(stream);
            System.out.println("");
        } catch (JpegProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
*//*

        setMeta(ExifService.readMeta(stream, getFilename(), getDateStored().getZone() == null ? ZoneId.systemDefault() : getDateStored().getZone()));
    }
*/

}
