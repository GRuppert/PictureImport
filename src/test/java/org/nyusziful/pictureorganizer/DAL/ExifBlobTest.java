package org.nyusziful.pictureorganizer.DAL;

import org.junit.Test;
import org.nyusziful.pictureorganizer.Service.MediaFileService;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ExifBlobTest {
    @Test
    public void testImageService() {
        JPAConnection.setMode(JPAConnection.DBMode.TEST);
        MediaFileService mediafileService = MediaFileService.getInstance();
        String fileLoc = "E:\\Work\\Testfiles\\conflictHandleResult\\WP_20150711_15_59_03_Pro__highres.jpg";
        //TODO fix it
/*        MediaFile mediaFile = mediafileService.readMediaFile(new File(fileLoc));
        if (mediaFile instanceof JPGMediaFile) {
            byte[] exif = mediaFile.getExif();
            mediafileService.saveMediaFile(mediaFile);
            MediaFile mediaFileReRead = mediafileService.getMediaFile(Paths.get(fileLoc), false);
            assertEquals(new String(mediaFileReRead.getExif()), new String(exif));
        }*/
    }
}
