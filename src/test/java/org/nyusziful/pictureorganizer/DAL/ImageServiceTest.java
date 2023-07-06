package org.nyusziful.pictureorganizer.DAL;

import org.junit.Before;
import org.junit.Test;
import org.nyusziful.pictureorganizer.DAL.Entity.Folder;
import org.nyusziful.pictureorganizer.DAL.Entity.Image;
import org.nyusziful.pictureorganizer.DAL.Entity.MediaFileInstance;
import org.nyusziful.pictureorganizer.DTO.ImageDTO;
import org.nyusziful.pictureorganizer.Service.ExifUtils.ExifReadWriteIMR;
import org.nyusziful.pictureorganizer.Service.FolderService;
import org.nyusziful.pictureorganizer.Service.ImageService;
import org.nyusziful.pictureorganizer.Service.MediaFileInstanceService;
import org.nyusziful.pictureorganizer.Service.MediaFileVersionService;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.sql.Timestamp;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.nyusziful.pictureorganizer.Service.Hash.MediaFileHash.getHash;

public class ImageServiceTest {
    final FolderService folderService = new FolderService();
    final MediaFileInstanceService mediafileInstanceService = MediaFileInstanceService.getInstance();
    final MediaFileVersionService mediafileVersionService = MediaFileVersionService.getInstance();
    final ImageService imageService = new ImageService();

    @Before
    public void setUp() {
        ImageDTO imageFirstDTO = new ImageDTO();
        String testHash = "TESTFIRST";
        String testType = "type";
        imageFirstDTO.hash = testHash;
        imageFirstDTO.type = testType;
        Image image = imageService.getImage(imageFirstDTO);
        if (image != null)
            imageService.deleteImage(image);
        ImageDTO imageSearch = new ImageDTO();
        testHash = "TEST";
        testType = "type";
        imageSearch.hash = testHash;
        imageSearch.type = testType;
        image = imageService.getImage(imageSearch);
        if (image != null)
            imageService.deleteImage(image);
        ImageDTO imageSecondDTO = new ImageDTO();
        String testHash2 = "TESTSECOND";
        imageSecondDTO.hash = testHash2;
        imageSecondDTO.type = testType;
        Image image2 = imageService.getImage(imageSecondDTO);
        if (image2 != null)
            imageService.deleteImage(image2);
    }

    @Test
    public void testImageService() {
        final ImageService imageService = new ImageService();
        ImageDTO imageSearch = new ImageDTO();
        String testHash = "TEST";
        String testType = "type";
        imageSearch.hash = testHash;
        imageSearch.type = testType;
        Image image = new Image(testHash, testType, "test", "test");
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
        ImageDTO imageFirstDTO = new ImageDTO();
        String testHash = "TESTFIRST";
        String testType = "type";
        imageFirstDTO.hash = testHash;
        imageFirstDTO.type = testType;
        Image image = new Image(testHash, testType, "test", "test");
        imageService.persistImage(image);
        image = imageService.getImage(imageFirstDTO);
        assertTrue(image.getId() > -1);

        String fileName = "DSC08806.JPG";
        ClassLoader classLoader = ExifReadWriteIMR.class.getClassLoader();
        URL resource = classLoader.getResource(fileName);
        File file = new File(resource.getFile());
        Path path = file.toPath();
        Folder folder = folderService.getFolder(path.getParent());
        BasicFileAttributes attrs = org.nyusziful.pictureorganizer.Service.Rename.StaticTools.getFileAttributes(file);
        final Timestamp dateMod = new Timestamp(attrs.lastModifiedTime().toMillis());
        ImageDTO imageDTO = getHash(file);
        mediafileVersionService.getMediafileVersion(imageDTO.fullhash);
        final MediaFileInstance mediaFileInstance = new MediaFileInstance(folder, path, dateMod, null);
        mediaFileInstance.addImage(image);
        mediafileInstanceService.saveMediaFileInstance(mediaFileInstance);

        ImageDTO imageSecondDTO = new ImageDTO();
        String testHash2 = "TESTSECOND";
        imageSecondDTO.hash = testHash2;
        imageSecondDTO.type = testType;
        Image image2 = new Image(testHash, testType, "test", "test");
        imageService.persistImage(image2);
        image2 = imageService.getImage(imageSecondDTO);
        assertTrue(image2.getId() > -1);

        mediaFileInstance.addImage(image2);
        imageService.saveImage(image);
        imageService.saveImage(image2);

        mediafileInstanceService.saveMediaFileInstance(mediaFileInstance);
        mediafileInstanceService.deleteMediaFiles(Collections.singletonList(mediaFileInstance));

    }

}
