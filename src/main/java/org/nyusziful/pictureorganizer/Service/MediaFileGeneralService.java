package org.nyusziful.pictureorganizer.Service;

import com.sun.javafx.scene.control.GlobalMenuAdapter;
import org.nyusziful.pictureorganizer.DAL.Entity.*;

import java.io.File;

public class MediaFileGeneralService {
    final MediaDirectoryService mds = MediaDirectoryService.getInstance();
    final MediaFileService mfs = MediaFileService.getInstance();
    final MediaFileVersionService mfvs = MediaFileVersionService.getInstance();
    final MediaFileInstanceService mfis = MediaFileInstanceService.getInstance();

    private static MediaFileGeneralService instance;

    private MediaFileGeneralService() {}

    public static MediaFileGeneralService getInstance() {
        if (instance == null) {
            instance = new MediaFileGeneralService();
        }
        return instance;
    }

    public void loadStatus(int mediaDirectoryId) {
        final MediaDirectory mediaDirectory = mds.getMediaDirectoryById(mediaDirectoryId);
        for (MediaFile mediaFile : mfs.getgetMediaFileByMediaDirectory(mediaDirectory)) {
            for (MediaFileInstance mediaFileInstance : mfis.getMediaFilesInstancesByMediaFile(mediaFile)) {
                final Folder folder = mediaFileInstance.getFolder();
            }
        }

    }



    public static void main(String[] args) {
        new MediaFileGeneralService().loadStatus(888);
    }

}
