package org.nyusziful.pictureorganizer.DAL.DAO;

import org.nyusziful.pictureorganizer.DTO.DirectorySummaryDTO;
import org.nyusziful.pictureorganizer.DTO.FolderSummaryDTO;

import java.util.Collection;

public interface MediaGeneralDAO {
    public Collection<DirectorySummaryDTO> loadDirectoryBackupStatus();
    public Collection<DirectorySummaryDTO> loadDirectoryVersionStatus();
    public Collection<FolderSummaryDTO> loadDirectoryVersionStatus(Integer[] mediaFileVersionIds);
}
