package org.nyusziful.pictureorganizer.Service;

import org.nyusziful.pictureorganizer.DAL.DAO.MediaGeneralDAO;
import org.nyusziful.pictureorganizer.DAL.DAO.MediaGeneralDAOImplHib;
import org.nyusziful.pictureorganizer.DAL.Entity.*;
import org.nyusziful.pictureorganizer.DTO.DirectorySummaryDTO;
import org.nyusziful.pictureorganizer.DTO.FolderSummaryDTO;

import java.util.Collection;

public class MediaFileGeneralService {
    final MediaDirectoryService mds = MediaDirectoryService.getInstance();
    final MediaFileService mfs = MediaFileService.getInstance();
    final MediaFileVersionService mfvs = MediaFileVersionService.getInstance();
    final MediaFileInstanceService mfis = MediaFileInstanceService.getInstance();

    final MediaGeneralDAO mediaGeneralDAO;

    private static MediaFileGeneralService instance;

    private MediaFileGeneralService() {
        mediaGeneralDAO = new MediaGeneralDAOImplHib();
    }

    public static MediaFileGeneralService getInstance() {
        if (instance == null) {
            instance = new MediaFileGeneralService();
        }
        return instance;
    }

    public void loadStatus(int mediaDirectoryId) {
        final MediaDirectory mediaDirectory = mds.getMediaDirectoryById(mediaDirectoryId);
        for (MediaFile mediaFile : mfs.getMediaFileByMediaDirectory(mediaDirectory)) {
            for (MediaFileInstance mediaFileInstance : mfis.getMediaFilesInstancesByMediaFile(mediaFile)) {
                final Folder folder = mediaFileInstance.getFolder();
            }
        }

    }

    public Collection<DirectorySummaryDTO> loadDirectoryBackupStatus() {
        return mediaGeneralDAO.loadDirectoryBackupStatus();
    }

    public Collection<DirectorySummaryDTO> loadDirectoryVersionStatus() {
        return mediaGeneralDAO.loadDirectoryVersionStatus();
    }

    public Collection<FolderSummaryDTO> loadDirectoryVersionStatus(Integer[] mediaFileVersionIds) {
        return mediaGeneralDAO.loadDirectoryVersionStatus(mediaFileVersionIds);
    }

    public static void main(String[] args) {
        new MediaFileGeneralService().loadStatus(888);
    }

}
