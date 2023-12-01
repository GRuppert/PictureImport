package org.nyusziful.pictureorganizer.Service.Backend;

import jakarta.persistence.EntityManager;
import org.apache.logging.log4j.core.util.Assert;
import org.junit.Before;
import org.junit.Test;
import org.nyusziful.pictureorganizer.DAL.Entity.MediaFileInstance;
import org.nyusziful.pictureorganizer.DAL.JPAConnection;
import org.nyusziful.pictureorganizer.Main.CommonProperties;
import org.nyusziful.pictureorganizer.Service.DriveService;
import org.nyusziful.pictureorganizer.Service.MediaFileInstanceService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class MediaTest {
    Path tmpdir;
    @Before
    public void switchToTestDB() throws IOException {
        JPAConnection.setTest(true);
        EntityManager entityManager = JPAConnection.getInstance().getEntityManager();
        entityManager.createNativeQuery("TRUNCATE TABLE testpictureorganizer.media_file_instance").executeUpdate();
        entityManager.createNativeQuery("TRUNCATE TABLE testpictureorganizer.media_image").executeUpdate();
        entityManager.createNativeQuery("TRUNCATE TABLE testpictureorganizer.media_file_version").executeUpdate();
        entityManager.createNativeQuery("TRUNCATE TABLE testpictureorganizer.media_file").executeUpdate();
        entityManager.createNativeQuery("TRUNCATE TABLE testpictureorganizer.image").executeUpdate();
        entityManager.createNativeQuery("TRUNCATE TABLE testpictureorganizer.folder").executeUpdate();
        tmpdir = Files.createTempDirectory("JavaUnitTest");
        File file = new File(this.getClass().getClassLoader().getResource("RAWJPG").getFile());
        for (File testFile : file.listFiles()) {
            Files.copy(testFile.toPath(), Paths.get(tmpdir.toAbsolutePath().toString(), testFile.getName()));
        }

        DriveService driveService = new DriveService();
        if (null == driveService.getLocalDrive(tmpdir.toString().substring(0, 1))
        ) {
            driveService.addLocalDrive(tmpdir.toString().substring(0, 1));
        }
    }

    @Test
    public void writeToDB() throws IOException {
        MediaFileInstanceService mediafileInstanceService = MediaFileInstanceService.getInstance();
        final Collection<MediaFileInstance> mediaFileInstances = mediafileInstanceService.readMediaFilesFromFolderInternal(tmpdir, true, false, CommonProperties.getInstance().getZone(), "", null);
        Assert.isNonEmpty(mediaFileInstances);
        for (MediaFileInstance mediaFileInstance : mediaFileInstances) {
            switch (mediaFileInstance.getFilename()) {
                case "ARWJPG1.ARW":
                    assertEquals(true, mediaFileInstance.getMediaFileVersion().isOriginal());
                    assertNotNull(mediaFileInstance.getMediaFileVersion().getMediaFile().getMainMediaFile());
                    break;
                case "WB1_3.ARW":
                    assertEquals(true, mediaFileInstance.getMediaFileVersion().isOriginal());
                    break;

            }

        }

    }
}
