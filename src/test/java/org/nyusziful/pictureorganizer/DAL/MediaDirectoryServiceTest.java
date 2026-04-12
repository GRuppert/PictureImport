package org.nyusziful.pictureorganizer.DAL;

import jakarta.persistence.EntityManager;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.nyusziful.pictureorganizer.DAL.Entity.MediaDirectory;
import org.nyusziful.pictureorganizer.Service.MediaDirectoryService;

import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class MediaDirectoryServiceTest {
    private static MediaDirectoryService mediaDirectoryService;

    @Before
    public void setup() {
        JPAConnection.setMode(JPAConnection.DBMode.TEST);
        EntityManager entityManager = JPAConnection.getInstance().getEntityManager();
        entityManager.createNativeQuery("TRUNCATE TABLE testpictureorganizer.media_directory").executeUpdate();
        mediaDirectoryService = MediaDirectoryService.getInstance();
    }

    @Ignore
    @Test
    public void getAllTest() {
        List<MediaDirectory> mediaDirectoryList = mediaDirectoryService.getAll();
        Assert.assertTrue("At least one directory should exist", mediaDirectoryList != null && !mediaDirectoryList.isEmpty());
        for (MediaDirectory mediaDirectory : mediaDirectoryList) {
            System.out.println(mediaDirectory);
        }

    }

    @Test
    public void writeTest() {
        MediaDirectory test = new MediaDirectory();
        test.setFirstDate(LocalDate.of(1982, 10, 24));
        test.setLastDate(LocalDate.of(1983, 05, 17));
        test.setLabel("Oldies, but goldies");
        mediaDirectoryService.persistMediaDirectory(test);
        assertEquals(test, mediaDirectoryService.getMediaDirectoryById(test.getId()));
        List<MediaDirectory> mediaDirectoryList = mediaDirectoryService.getAll();
        Assert.assertTrue("At least one directory should exist", mediaDirectoryList != null && !mediaDirectoryList.isEmpty());
        for (MediaDirectory mediaDirectory : mediaDirectoryList) {
            System.out.println(mediaDirectory);
        }
        mediaDirectoryService.deleteMediaDirectory(test);
        assertNull(mediaDirectoryService.getMediaDirectoryById(test.getId()));
    }

}
