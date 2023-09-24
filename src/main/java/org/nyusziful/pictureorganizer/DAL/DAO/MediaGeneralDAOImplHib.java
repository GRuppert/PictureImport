package org.nyusziful.pictureorganizer.DAL.DAO;

import org.nyusziful.pictureorganizer.DAL.Entity.Folder;
import org.nyusziful.pictureorganizer.DAL.Entity.MediaDirectory;
import org.nyusziful.pictureorganizer.DAL.JPAConnection;
import org.nyusziful.pictureorganizer.DTO.DirectorySummaryDTO;
import org.nyusziful.pictureorganizer.DTO.FolderSummaryDTO;
import org.nyusziful.pictureorganizer.Service.FolderService;

import jakarta.persistence.Query;
import java.math.BigInteger;
import java.util.*;

public class MediaGeneralDAOImplHib implements MediaGeneralDAO {
    private MediaDirectoryDAO mediaDirectoryDAO = new MediaDirectoryDAOImplHib();
    public Collection<DirectorySummaryDTO> loadDirectoryBackupStatus() {
        HashMap<Integer, MediaDirectory> mediaDirectoryHashMap = new HashMap<>();
        for (MediaDirectory mediaDirectory : mediaDirectoryDAO.getAll()) {
            mediaDirectoryHashMap.put(mediaDirectory.getId(), mediaDirectory);
        }
        Query query = JPAConnection.getInstance().getEntityManager().createNativeQuery("SELECT mfv.id, mf.media_directory_id, count(distinct(f.drive_id)) FROM media_file_version mfv LEFT JOIN media_file mf ON mf.id = mfv.media_file_id LEFT JOIN media_file_instance mfi ON mfi.media_file_version_id = mfv.id LEFT JOIN folder f ON f.id = mfi.folder_id GROUP BY 1,2 ORDER BY 2,1");
        List<Object[]> instances = query.getResultList();
        HashMap<MediaDirectory, DirectorySummaryDTO> directorySummaries = new HashMap<>();
        for (Object[] a : instances) {
            MediaDirectory mediaDirectory = mediaDirectoryHashMap.get((Integer) a[1]);
            DirectorySummaryDTO directorySummaryDTO = directorySummaries.get(mediaDirectory);
            if (directorySummaryDTO == null) {
                directorySummaryDTO = new DirectorySummaryDTO(mediaDirectory);
                directorySummaries.put(mediaDirectory, directorySummaryDTO);
            }
            if (((BigInteger) a[2]).intValue() > 2) {
                directorySummaryDTO.put("Backup", (Integer) a[0]);
            } else {
                directorySummaryDTO.put("NoBackup", (Integer) a[0]);
            }
        }
        ArrayList<DirectorySummaryDTO> values = new ArrayList<>(directorySummaries.values());
        Collections.sort(values);
        return values;
    }

    public Collection<DirectorySummaryDTO> loadDirectoryVersionStatus() {
        HashMap<Integer, MediaDirectory> mediaDirectoryHashMap = new HashMap<>();
        for (MediaDirectory mediaDirectory : mediaDirectoryDAO.getAll()) {
            mediaDirectoryHashMap.put(mediaDirectory.getId(), mediaDirectory);
        }
        Query query = JPAConnection.getInstance().getEntityManager().createNativeQuery("SELECT mf.id, mf.media_directory_id, count(distinct(mfv.id)) FROM media_file_version mfv LEFT JOIN media_file mf ON mf.id = mfv.media_file_id WHERE mfv.parent_id IS NULL GROUP BY 1,2 ORDER BY 2,1");
        List<Object[]> instances = query.getResultList();
        HashMap<MediaDirectory, DirectorySummaryDTO> directorySummaries = new HashMap<>();
        for (Object[] a : instances) {
            MediaDirectory mediaDirectory = mediaDirectoryHashMap.get((Integer) a[1]);
            DirectorySummaryDTO directorySummaryDTO = directorySummaries.get(mediaDirectory);
            if (directorySummaryDTO == null) {
                directorySummaryDTO = new DirectorySummaryDTO(mediaDirectory);
                directorySummaries.put(mediaDirectory, directorySummaryDTO);
            }
            if (((Long) a[2]).intValue() == 1) {
                directorySummaryDTO.put("NoCollision", (Integer) a[0]);
            } else {
                directorySummaryDTO.put("Collision", (Integer) a[0]);
            }
        }
        ArrayList<DirectorySummaryDTO> values = new ArrayList<>(directorySummaries.values());
        Collections.sort(values);
        return values;
    }

    public Collection<FolderSummaryDTO> loadDirectoryVersionStatus(Integer[] mediaFileVersionIds) {
        HashMap<Integer, Folder> folderHashMap = new HashMap<>();
        Set<Integer> mediaFiles = new HashSet<>();
        Query query = JPAConnection.getInstance().getEntityManager().createNativeQuery("SELECT mf.id mfid, mfv.id mfvid, f.drive_id, f.id fid FROM media_file_version mfv LEFT JOIN media_file mf ON mf.id = mfv.media_file_id LEFT JOIN media_file_instance mfi ON mfi.media_file_version_id = mfv.id LEFT JOIN folder f ON f.id = mfi.folder_id WHERE mfv.id IN :ids ORDER BY 3,4");
        query.setParameter("ids", mediaFileVersionIds);
        List<Object[]> instances = query.getResultList();
        HashMap<Folder, FolderSummaryDTO> folderSummaries = new HashMap<>();
        FolderService folderService = new FolderService();
        for (Object[] a : instances) {
            Integer folderId = (Integer) a[4];
            Folder folder = folderHashMap.get(folderId);
            if (folder == null) {
                folder = folderService.getFolder(folderId);
                folderHashMap.put(folderId, folder);
            }
            FolderSummaryDTO folderSummaryDTO = folderSummaries.get(folder);
            if (folderSummaryDTO == null) {
                folderSummaryDTO = new FolderSummaryDTO(folder, mediaFiles);
                folderSummaries.put(folder, folderSummaryDTO);
            }
            Integer mediaFileId = (Integer) a[0];
            mediaFiles.add(mediaFileId);
            folderSummaryDTO.put(mediaFileId, (Integer) a[1]);
        }
        ArrayList<FolderSummaryDTO> values = new ArrayList<>(folderSummaries.values());
        Collections.sort(values);
        return values;
    }



}
