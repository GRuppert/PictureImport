package org.nyusziful.pictureorganizer.Service.Backend;

import jakarta.persistence.EntityManager;
import org.apache.logging.log4j.core.util.Assert;
import org.junit.Before;
import org.junit.Test;
import org.nyusziful.pictureorganizer.DAL.JPAConnection;
import org.nyusziful.pictureorganizer.DTO.MediafileInstanceDTO;
import org.nyusziful.pictureorganizer.Main.CommonProperties;
import org.nyusziful.pictureorganizer.Service.MediaFileInstanceService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Set;

public class MediaTest {
    @Before
    public void switchToTestDB() {
        JPAConnection.setTest(true);
        EntityManager entityManager = JPAConnection.getInstance().getEntityManager();
        entityManager.createNativeQuery("TRUNCATE TABLE testpictureorganizer.media_file_instance").executeUpdate();
        entityManager.createNativeQuery("TRUNCATE TABLE testpictureorganizer.media_image").executeUpdate();
        entityManager.createNativeQuery("TRUNCATE TABLE testpictureorganizer.media_file_version").executeUpdate();
        entityManager.createNativeQuery("TRUNCATE TABLE testpictureorganizer.media_file").executeUpdate();
        entityManager.createNativeQuery("TRUNCATE TABLE testpictureorganizer.image").executeUpdate();
        entityManager.createNativeQuery("TRUNCATE TABLE testpictureorganizer.folder").executeUpdate();
    }

    @Test
    public void writeToDB() {
        File dir = new File("E:\\Work\\Testfiles\\NewDBStructureRW");
        ArrayList<File> directories = new ArrayList<>();
        try {
            Files.walk(dir.toPath())
                    .filter(Files::isDirectory)
                    .forEach(f -> directories.add(f.toFile()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        MediaFileInstanceService mediafileInstanceService = MediaFileInstanceService.getInstance();
        boolean skip = false;
        for (File directory : directories) {
            if (skip) continue;
            final Set<MediafileInstanceDTO> mediaFiles = mediafileInstanceService.readMediaFilesFromFolder(directory.toPath(), true, false, CommonProperties.getInstance().getZone(), "", null);
            Assert.isNonEmpty(mediaFiles);
        }
    }
}
