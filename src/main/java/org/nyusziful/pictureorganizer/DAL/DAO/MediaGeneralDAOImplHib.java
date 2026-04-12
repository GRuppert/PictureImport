package org.nyusziful.pictureorganizer.DAL.DAO;

import org.nyusziful.pictureorganizer.DAL.Entity.Folder;
import org.nyusziful.pictureorganizer.DAL.Entity.MediaDirectory;
import org.nyusziful.pictureorganizer.DAL.JPAConnection;
import org.nyusziful.pictureorganizer.DTO.DirectorySummaryDTO;
import org.nyusziful.pictureorganizer.DTO.FolderSummaryDTO;
import org.nyusziful.pictureorganizer.DTO.VersionDTO;
import org.nyusziful.pictureorganizer.Service.FolderService;

import jakarta.persistence.Query;
import java.math.BigInteger;
import java.util.*;

public class MediaGeneralDAOImplHib implements MediaGeneralDAO {
    private MediaDirectoryDAO mediaDirectoryDAO = new MediaDirectoryDAOImplHib();
    private FolderDAO folderDAO = new FolderDAOImplHib();
    private MediaFileVersionDAO mediaFileVersionDAO = new MediaFileVersionDAOImplHib();

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

    public Collection<FolderSummaryDTO> loadDirectoryVersionStatus(Collection<Integer> mediaFileVersionIds) {
        HashMap<Integer, Folder> folderHashMap = new HashMap<>(); //Only local
        HashMap<Integer, String> mediaFilesIds = new HashMap<>();
        Query query = JPAConnection.getInstance().getEntityManager().createNativeQuery("SELECT mf.id mfid, mfv.id mfvid, f.drive_id, f.id fid, mf.original_filename FROM media_file_version mfv LEFT JOIN media_file mf ON mf.id = mfv.media_file_id LEFT JOIN media_file_instance mfi ON mfi.media_file_version_id = mfv.id LEFT JOIN folder f ON f.id = mfi.folder_id WHERE mf.id IN (:ids) ORDER BY 3,4");
        query.setParameter("ids", mediaFileVersionIds);
        List<Object[]> instances = query.getResultList();
        HashMap<Folder, FolderSummaryDTO> folderSummaries = new HashMap<>();
        FolderService folderService = new FolderService();
        for (Object[] a : instances) {
            Integer folderId = (Integer) a[3];
            folderHashMap.putIfAbsent(folderId, folderService.getFolder(folderId));
            Folder folder = folderHashMap.get(folderId);
            folderSummaries.putIfAbsent(folder, new FolderSummaryDTO(folder, mediaFilesIds));
            FolderSummaryDTO folderSummaryDTO = folderSummaries.get(folder);
            Integer mediaFileId = (Integer) a[0];
            mediaFilesIds.put(mediaFileId, a[4].toString());
            folderSummaryDTO.put(mediaFileId, (Integer) a[1]);
        }
        Folder[] folders = folderSummaries.keySet().toArray(new Folder[0]);
        Set<Set<Integer>> versions = new HashSet<>();
        for (int i = 0; i < folders.length-1; i++) {
            Folder folder = folders[i];
            FolderSummaryDTO folderSummaryDTO = folderSummaries.get(folder);
            Set<Integer> mfvIds = folderSummaryDTO.getVersions();
            if (versions.contains(mfvIds)) continue;
            for (int j = i+1; j < folders.length; j++) {
                FolderSummaryDTO folderSummaryDTO2 = folderSummaries.get(folders[j]);
                Set<Integer> mfvIds2 = folderSummaryDTO2.getVersions();
                if (versions.contains(mfvIds2)) continue;
                switch (folderSummaryDTO.compareWith(folderSummaryDTO2)) {
                    case FolderSummaryDTO.DISTINCT:
                        versions.add(mfvIds);
                        versions.add(mfvIds2);
                        break;
                }
            }
        }
        ArrayList<Set<Integer>> versionsList = new ArrayList<>(versions);
        for (FolderSummaryDTO folderSummaryDTO : folderSummaries.values()) {
            folderSummaryDTO.setVersion(versionsList);
        }
        ArrayList<FolderSummaryDTO> values = new ArrayList<>(folderSummaries.values());
        Collections.sort(values);
        return values;
    }

    @Override
    public Collection<VersionDTO> loadDirectoryVersions(int mediaDirectoryId) {
        Query query = JPAConnection.getInstance().getEntityManager().createNativeQuery("SELECT mf.id mfid, mfv.id mfvid, mfv.parent_id, f.drive_id, f.id fid, mf.original_filename FROM media_file_version mfv LEFT JOIN media_file mf ON mf.id = mfv.media_file_id LEFT JOIN media_file_instance mfi ON mfi.media_file_version_id = mfv.id LEFT JOIN folder f ON f.id = mfi.folder_id WHERE mf.media_directory_id = (:id) ORDER BY 1,2");
        query.setParameter("id", mediaDirectoryId);
        List<Object[]> instances = query.getResultList();
        HashMap<Integer, Integer> versionToMediaFile = new HashMap<>();
        HashMap<Integer, Integer> versionToParent = new HashMap<>();
        HashMap<Integer, Set<Folder>> versionToFolder = new HashMap<>();

        HashMap<Integer, String> mediaFileToName = new HashMap<>();
        HashMap<Integer, Integer> folderToDrive = new HashMap<>();
        Set<Integer> roots = new HashSet<>();

        for (Object[] record : instances) {
            Integer mfId = (Integer) record[0];
            Integer mfvId = (Integer) record[1];
            versionToMediaFile.putIfAbsent(mfvId, mfId);
            Integer parentId = (Integer) record[2];
            if (parentId == null) roots.add(mfvId);
            else versionToParent.putIfAbsent(mfvId, parentId);
            Integer driveId = (Integer) record[3];
            Integer folderId = (Integer) record[4];
            folderToDrive.putIfAbsent(folderId, driveId);
            versionToFolder.putIfAbsent(mfvId, new HashSet<>());
            versionToFolder.get(mfvId).add(folderDAO.getById(folderId));
            String originalFileName = record[5].toString();
            mediaFileToName.putIfAbsent(mfId, originalFileName);
        }

        Set<VersionDTO> rootNodes = new HashSet<>();
        for (Integer root : roots) {
            Set<Folder> folders = versionToFolder.get(root);
            boolean isNewNode = true;
            for (VersionDTO versionDTO : rootNodes) {
                Set<Folder> nodeFolders = versionDTO.getFolders();
                if (!getCommonElement(nodeFolders, folders).isEmpty()) {
                    nodeFolders.retainAll(folders);
                    isNewNode = false;
                }
            }
            if (isNewNode) {
                VersionDTO newNode = new VersionDTO(folders);
                rootNodes.add(newNode);
            }
        }

        for (Integer rootId : roots) {
            Set<Folder> folders = versionToFolder.get(rootId);
            for (VersionDTO versionDTO : rootNodes) {
                Set<Folder> nodeFolders = versionDTO.getFolders();
                Set<Folder> commonElements = getCommonElement(nodeFolders, folders);
                if (!commonElements.isEmpty()) {
                    versionDTO.getVersions().putIfAbsent(versionToMediaFile.get(rootId), new HashSet<>());
                    versionDTO.getVersions().get(versionToMediaFile.get(rootId)).add(rootId);
                    if (commonElements.size() == folders.size() && commonElements.size() == nodeFolders.size()) {
                        versionDTO.getSpecificVersions().add(mediaFileVersionDAO.getById(rootId));
                    }
                }
            }
        }

        Set<VersionDTO> nodes = new HashSet<>();
        for (Integer childId : versionToParent.keySet()) {
            Integer parentMFVId = versionToParent.get(childId);
            Integer parentMFId = versionToMediaFile.get(parentMFVId);
            Set<Folder> folders = versionToFolder.get(childId);
            boolean isNewNode = true;
            for (VersionDTO versionDTO : nodes) {
                Set<Folder> nodeFolders = versionDTO.getFolders();
                if (versionDTO.getParent().getVersions().get(parentMFId).contains(parentMFVId)) {
                    nodeFolders.retainAll(folders);
                    isNewNode = false;
                }
            }
            if (isNewNode) {
                VersionDTO newNode = new VersionDTO(folders);
                nodes.add(newNode);
                for (VersionDTO versionDTO : rootNodes) {
                    if (versionDTO.getVersions().get(parentMFId).contains(parentMFVId)) {
                        newNode.setParent(versionDTO);
                    }
                }
            }
        }

        for (Integer childId : versionToParent.keySet()) {
            Integer parentMFVId = versionToParent.get(childId);
            Integer parentMFId = versionToMediaFile.get(parentMFVId);
            for (VersionDTO versionDTO : nodes) {
                if (versionDTO.getParent().getVersions().get(parentMFId).contains(parentMFVId)) {
                    versionDTO.getVersions().putIfAbsent(versionToMediaFile.get(childId), new HashSet<>());
                    versionDTO.getVersions().get(versionToMediaFile.get(childId)).add(childId);
                }
            }
        }

        for (VersionDTO childNode : nodes) {
            if (childNode.getParent() != null) continue;
            nodeLoop:
            for (VersionDTO parentNode : nodes) {
                if (parentNode.equals(childNode)) continue;
                for (Integer mfId : childNode.getVersions().keySet()) {
                    Set<Integer> parentVersions = parentNode.getVersions().get(mfId);
                    if (parentVersions == null) continue nodeLoop;
                    for (Integer childMfvId : childNode.getVersions().get(mfId)) {
                        if (!parentVersions.contains(versionToParent.get(childMfvId))) continue nodeLoop;
                    }
                }
                childNode.setParent(parentNode);
            }
        }


        return rootNodes;
    }

    private Set<Folder> getCommonElement(Set<Folder> nodeFolders, Set<Folder> folders) {
        Set<Folder> common = new HashSet<>();
        common.addAll(nodeFolders);
        common.retainAll(folders);
        return common;
    }

    public static void main(String[] args) {
        JPAConnection.setMode(JPAConnection.DBMode.DEV);
        MediaGeneralDAO test = new MediaGeneralDAOImplHib();
        test.loadDirectoryVersions(12);
    }
}
