package org.nyusziful.pictureorganizer.DAL;

import org.junit.Test;
import org.nyusziful.pictureorganizer.DAL.Entity.Image;
import org.nyusziful.pictureorganizer.DAL.Entity.JPGMediaFile;
import org.nyusziful.pictureorganizer.DAL.Entity.MediaFile;
import org.nyusziful.pictureorganizer.DTO.ImageDTO;
import org.nyusziful.pictureorganizer.Service.ImageService;
import org.nyusziful.pictureorganizer.Service.MediafileService;

import java.io.File;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ExifBlobTest {
    @Test
    public void testImageService() {
        MediafileService mediafileService = MediafileService.getInstance();
        String fileLoc = "E:\\Work\\Testfiles\\conflictHandleResult\\WP_20150711_15_59_03_Pro__highres.jpg";
        MediaFile mediaFile = mediafileService.readMediaFile(new File(fileLoc));
        if (mediaFile instanceof JPGMediaFile) {
            byte[] exif = ((JPGMediaFile) mediaFile).getExif();
            mediafileService.saveMediaFile(mediaFile);
            MediaFile mediaFileReRead = mediafileService.getMediaFile(Paths.get(fileLoc), false);
            assertEquals(((JPGMediaFile) mediaFileReRead).getExif(), exif);
        }
    }
}
