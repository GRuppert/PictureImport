package org.nyusziful.pictureorganizer.DAL;

import org.junit.Test;
import org.nyusziful.pictureorganizer.DAL.Entity.Folder;
import org.nyusziful.pictureorganizer.DAL.Entity.Image;
import org.nyusziful.pictureorganizer.DAL.Entity.MediaFile;
import org.nyusziful.pictureorganizer.DTO.ImageDTO;
import org.nyusziful.pictureorganizer.Service.FolderService;
import org.nyusziful.pictureorganizer.Service.ImageService;
import org.nyusziful.pictureorganizer.Service.MediafileService;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.sql.Timestamp;

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

    @Test
    public void testBiDirectional() {
        final ImageService imageService = new ImageService();
        final FolderService folderService = new FolderService();
        final MediafileService mediafileService = new MediafileService();
        ImageDTO imageFirstDTO = new ImageDTO();
        String testHash = "TESTFIRST";
        String testType = "type";
        imageFirstDTO.hash = testHash;
        imageFirstDTO.type = testType;
        Image image = imageService.getImage(imageFirstDTO);
        assertTrue(image == null);
        image = new Image(testHash, testType);
        imageService.persistImage(image);
        image = imageService.getImage(imageFirstDTO);
        assertTrue(image.getId() > -1);

        final Path path = Paths.get("DSC08806.JPG");
        Folder folder = folderService.getFolder(path.getParent());
        BasicFileAttributes attrs = org.nyusziful.pictureorganizer.Service.Rename.StaticTools.getFileAttributes(path.toFile());
        final Timestamp dateMod = new Timestamp(attrs.lastModifiedTime().toMillis());
        final MediaFile mediaFile = new MediaFile(folder, path, attrs.size(), dateMod, false);
        mediaFile.setImage(image);
        mediafileService.saveMediaFile(mediaFile);

        ImageDTO imageSecondDTO = new ImageDTO();
        String testHash2 = "TESTSECOND";
        imageSecondDTO.hash = testHash2;
        imageSecondDTO.type = testType;
        Image image2 = imageService.getImage(imageSecondDTO);
        assertTrue(image2 == null);
        image2 = new Image(testHash, testType);
        imageService.persistImage(image2);
        image2 = imageService.getImage(imageSecondDTO);
        assertTrue(image2.getId() > -1);

        mediaFile.setImage(image2);
        imageService.saveImage(image);
        imageService.saveImage(image2);

        mediafileService.saveMediaFile(mediaFile);

    }

}
