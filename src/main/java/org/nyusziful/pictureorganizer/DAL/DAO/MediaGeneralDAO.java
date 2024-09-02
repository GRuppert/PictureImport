package org.nyusziful.pictureorganizer.DAL.DAO;

import org.nyusziful.pictureorganizer.DTO.DirectorySummaryDTO;
import org.nyusziful.pictureorganizer.DTO.FolderSummaryDTO;
import org.nyusziful.pictureorganizer.DTO.VersionDTO;

import java.util.Collection;

public interface MediaGeneralDAO {
    Collection<DirectorySummaryDTO> loadDirectoryBackupStatus();
    Collection<DirectorySummaryDTO> loadDirectoryVersionStatus();
    Collection<FolderSummaryDTO> loadDirectoryVersionStatus(Collection<Integer> mediaFileVersionIds);
    Collection<VersionDTO> loadDirectoryVersions(int mediaDirectoryId);
}
