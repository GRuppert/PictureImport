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
        String testHash = "TEST";
        String testType = "type";
        imageSearch.hash = testHash;
        imageSearch.type = testType;
        Image image = imageService.getImage(imageSearch);
        assertTrue(image == null);
        image = new Image(testHash, testType);
        imageService.persistImage(image);
        Image imageRetrieved = imageService.getImage(imageSearch);
        assertTrue(imageRetrieved.getId() > -1);
        imageRetrieved.setOriginalFilename("test2");
        imageService.updateImage(imageRetrieved);
/*
        try {
            HibConnection.getInstance().commit();
        } catch (Exception e) {
            HibConnection.getInstance().rollback();
            e.printStackTrace();
        }
*/
        Image rewrittenimage = imageService.getImage(imageSearch);
        assertEquals(rewrittenimage.getOriginalFilename(), "test2");
        imageService.deleteImage(imageRetrieved);
        Image imageDeleted = imageService.getImage(imageSearch);
        assertTrue(imageDeleted == null);
    }

}
