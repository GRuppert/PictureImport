package org.nyusziful.pictureorganizer.Service.Backend;

import jakarta.persistence.EntityManager;
import org.apache.logging.log4j.core.util.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.nyusziful.pictureorganizer.DAL.Entity.JPGMediaFile;
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
import java.util.*;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.junit.Assert.*;

public class MediaTest {
    Path tmpdir;
    @Before
    public void switchToTestDB() throws IOException {
        JPAConnection.setMode(JPAConnection.DBMode.TEST);
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
            if (!"JPG1ROTATE.JPG".equals(testFile.getName()))
                Files.copy(testFile.toPath(), Paths.get(tmpdir.toAbsolutePath().toString(), testFile.getName()));
        }
        Files.copy(new File(this.getClass().getClassLoader().getResource("20160627_183440_GT-I9195I-20160627_173440.jpg").getFile()).toPath(), Paths.get(tmpdir.toAbsolutePath().toString(), "BROKEN1.JPG"));
        DriveService driveService = new DriveService();
        if (null == driveService.getLocalDrive(tmpdir.toString().substring(0, 1))
        ) {
            driveService.addLocalDrive(tmpdir.toString().substring(0, 1));
        }
    }

    @After
    public void checkReadOnly() throws IOException {
        File file = new File(this.getClass().getClassLoader().getResource("RAWJPG").getFile());
        for (File testFile : file.listFiles(File::isFile)) {
            if (!"JPG1ROTATE.JPG".equals(testFile.getName()) && !"JPG1.JPG".equals(testFile.getName()))
                assertEquals(testFile.getName() + " mismatch from original data", -1L, Files.mismatch(testFile.toPath(), Paths.get(tmpdir.toAbsolutePath().toString(), testFile.getName())));
        }
    }

    @Test
    public void writeToDB() throws IOException {
        MediaFileInstanceService mediafileInstanceService = MediaFileInstanceService.getInstance();
        final Collection<Integer> mediaFileInstanceIDs = new HashSet<>();
        for (MediaFileInstance mediaFileInstance : mediafileInstanceService.readMediaFilesFromFolderInternal(tmpdir, true, false, CommonProperties.getInstance().getZone(), "", null)) {
            mediaFileInstanceIDs.add(mediaFileInstance.getId());
        }
        final Collection<MediaFileInstance> mediaFileInstances = mediafileInstanceService.getMediaFileInstances(mediaFileInstanceIDs);
        Assert.isNonEmpty(mediaFileInstances);
        Set<Integer> wb1Ids = new HashSet<>();
        Set<Integer> wb2Ids = new HashSet<>();
        Set<Integer> brokenIds = new HashSet<>();
        Map<String, Integer> beforeInstanceIds = new HashMap<>();
        int beforeVersionId = -1;
        int beforeMFId = -1;
        for (MediaFileInstance mediaFileInstance : mediaFileInstances) {
            assertEquals(true, mediaFileInstance.getMediaFileVersion().isOriginal());
            beforeInstanceIds.put(mediaFileInstance.getFilename(), mediaFileInstance.getId());
            //TODO media_image type
            switch (mediaFileInstance.getFilename()) {
                case "ARWJPG1.JPG":
                    assertEquals(false, ((JPGMediaFile)mediaFileInstance.getMediaFileVersion().getMediaFile()).isStandalone());
                    assertEquals("ARWJPG1.ARW", mediaFileInstance.getMediaFileVersion().getMediaFile().getMainMediaFile().getOriginalFilename());
                    break;
                case "WB1_1.ARW":
                    assertEquals(1, mediaFileInstance.getMediaFileVersion().getMediaFile().getShotnumber().intValue());
                    wb1Ids.add(mediaFileInstance.getMediaFileVersion().getMediaFile().getId());
                    break;
                case "WB1_2.ARW":
                    assertEquals(2, mediaFileInstance.getMediaFileVersion().getMediaFile().getShotnumber().intValue());
                    wb1Ids.add(mediaFileInstance.getMediaFileVersion().getMediaFile().getId());
                    break;
                case "WB1_3.ARW":
                    assertEquals(3, mediaFileInstance.getMediaFileVersion().getMediaFile().getShotnumber().intValue());
                    wb1Ids.add(mediaFileInstance.getMediaFileVersion().getMediaFile().getId());
                    break;
                case "WB2_1.ARW":
                    assertEquals(1, mediaFileInstance.getMediaFileVersion().getMediaFile().getShotnumber().intValue());
                    wb2Ids.add(mediaFileInstance.getMediaFileVersion().getMediaFile().getId());
                    break;
                case "WB2_2.ARW":
                    assertEquals(2, mediaFileInstance.getMediaFileVersion().getMediaFile().getShotnumber().intValue());
                    wb2Ids.add(mediaFileInstance.getMediaFileVersion().getMediaFile().getId());
                    break;
                case "WB2_3.ARW":
                    assertEquals(3, mediaFileInstance.getMediaFileVersion().getMediaFile().getShotnumber().intValue());
                    wb2Ids.add(mediaFileInstance.getMediaFileVersion().getMediaFile().getId());
                    break;
                case "BROKEN1.JPG":
                    brokenIds.add(mediaFileInstance.getMediaFileVersion().getMediaFile().getId());
                    assertEquals(true, mediaFileInstance.getMediaFileVersion().isInvalid());
                    assertNull(mediaFileInstance.getMediaFileVersion().getDateStored()); //TODO even with a broken image the metadata should be accessible
                    break;
                case "JPG1.JPG":
                    assertEquals(true, ((JPGMediaFile)mediaFileInstance.getMediaFileVersion().getMediaFile()).isStandalone());
                    beforeVersionId = mediaFileInstance.getMediaFileVersion().getId();
                    beforeMFId = mediaFileInstance.getMediaFileVersion().getMediaFile().getId();
                    assertEquals(1, mediaFileInstance.getMediaFileVersion().getMainMedia().getOrientation().intValue());
                    break;
            }

        }
        assertEquals(3, wb1Ids.size());
        assertEquals(3, wb2Ids.size());
        rotate(new File(tmpdir+"\\JPG1.JPG"));
        Files.copy(new File(this.getClass().getClassLoader().getResource("20160627_173442_GT-I9195I-20160627_173443.jpg").getFile()).toPath(), Paths.get(tmpdir.toAbsolutePath().toString(), "BROKEN2.JPG"));
        final Collection<MediaFileInstance> afterMediaFileInstances = mediafileInstanceService.readMediaFilesFromFolderInternal(tmpdir, false, false, CommonProperties.getInstance().getZone(), "", null);
        for (MediaFileInstance mediaFileInstance : afterMediaFileInstances) {
            switch (mediaFileInstance.getFilename()) {
                case "JPG1.JPG":
                    assertNotEquals(beforeVersionId, mediaFileInstance.getMediaFileVersion().getId());
                    assertEquals(beforeMFId, mediaFileInstance.getMediaFileVersion().getMediaFile().getId());
                    assertEquals(6, mediaFileInstance.getMediaFileVersion().getMainMedia().getOrientation().intValue());
                    break;
                default:
                    assertEquals(mediaFileInstance.getId(), (int) beforeInstanceIds.get(mediaFileInstance.getFilename()));
                    break;
                case "BROKEN2.JPG":
                    brokenIds.add(mediaFileInstance.getMediaFileVersion().getMediaFile().getId());
                    break;
            }
        }
        assertEquals(2, brokenIds.size());
    }

    private void rotate(File file) throws IOException {
        Files.copy(new File(this.getClass().getClassLoader().getResource("RAWJPG/rotated/JPG1.JPG").getFile()).toPath(), file.toPath(), REPLACE_EXISTING);
    }


}
