package org.nyusziful.pictureorganizer.DAL;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.nyusziful.pictureorganizer.DAL.Entity.MediaDirectory;
import org.nyusziful.pictureorganizer.Service.MediaDirectoryService;

import java.util.List;

public class MediaDirectoryServiceTest {
    private static MediaDirectoryService mediaDirectoryService;

    @Before
    public void setup() {
        mediaDirectoryService = MediaDirectoryService.getInstance();
    }

    @Test
    public void getAllTest() {
        List<MediaDirectory> mediaDirectoryList = mediaDirectoryService.getAll();
        Assert.assertTrue("At least one directory should exist", mediaDirectoryList != null && !mediaDirectoryList.isEmpty());
        for (MediaDirectory mediaDirectory : mediaDirectoryList) {
            System.out.println(mediaDirectory);
        }

    }
}
