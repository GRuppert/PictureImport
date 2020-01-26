package org.nyusziful.pictureorganizer.Service;

import org.junit.Test;
import org.nyusziful.pictureorganizer.DAL.Entity.Image;
import org.nyusziful.pictureorganizer.DAL.HibConnection;
import org.nyusziful.pictureorganizer.DTO.ImageDTO;
import org.nyusziful.pictureorganizer.Service.Hash.MediaFileHash;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ImageServiceTest {
    @Test
    public void testImageService() {
        final ImageService imageService = new ImageService();
        ImageDTO imageSearch = new ImageDTO();
        imageSearch.hash = "001ccb41c7eb77075051f3febdcafe71";
        imageSearch.type = MediaFileHash.Type.TIFF.name();
        Image image = imageService.getImage(imageSearch);
        assertTrue(image.getId()>-1);
        image.setOriginalFilename("test2");
        try {
            imageService.updateImage(image);
            HibConnection.getInstance().commit();
        } catch (Exception e) {
            HibConnection.getInstance().rollback();
            e.printStackTrace();
        }
        Image rewrittenimage = imageService.getImage(imageSearch);
        assertEquals(rewrittenimage.getOriginalFilename(), "test2");
    }

}
