package org.nyusziful.pictureorganizer.DTO;

import org.jetbrains.annotations.NotNull;
import org.nyusziful.pictureorganizer.DAL.Entity.Folder;
import org.nyusziful.pictureorganizer.DAL.Entity.MediaDirectory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class FolderSummaryDTO implements Comparable<FolderSummaryDTO>, SummaryDTO {
    private Folder folder;
    private HashMap<Integer, Set<Integer>> idsMap = new HashMap<>();
    private Set<Integer> mediaFiles;
    public FolderSummaryDTO(Folder folder, Set<Integer> mediaFiles) {
        this.folder = folder;
        this.mediaFiles = mediaFiles;
    }

    public Folder getFolder() {
        return folder;
    }

    @Override
    public int compareTo(@NotNull FolderSummaryDTO o) {
        if (o.getFolder() == null) return 1;
        if (getFolder() == null) return -1;
        return getFolder().getId() - o.getFolder().getId();
    }

    public void put(Integer mediaFileId, Integer mediaFileVersionId) {
        Set<Integer> ids = idsMap.get(mediaFileId);
        if (ids == null) {
            ids = new HashSet<>();
            idsMap.put(mediaFileId, ids);
        }
        ids.add(mediaFileVersionId);
    }

    public String getSummaryText() {
        StringBuilder sb = new StringBuilder();
        sb.append(folder).append(" ");
        sb.append(idsMap.keySet().containsAll(mediaFiles) ? "FULL" : (idsMap.keySet().size() + "/" + mediaFiles.size()));
        for (Integer name : idsMap.keySet()) {
            sb.append("(").append(name).append(":").append(idsMap.get(name).size()).append(")");
        }
        return sb.toString();
    }
}
