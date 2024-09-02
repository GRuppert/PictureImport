package org.nyusziful.pictureorganizer.DTO;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import org.jetbrains.annotations.NotNull;
import org.nyusziful.pictureorganizer.DAL.Entity.Folder;

import java.util.*;
import java.util.stream.Collectors;

public class FolderSummaryDTO implements Comparable<FolderSummaryDTO>, SummaryDTO {
    private Folder folder;
    private HashMap<Integer, FileSummaryDTO> idsMap = new HashMap<>();
    private HashMap<Integer, String> mediaFilesIds;
    private int[] version = new int[0];
    private BooleanProperty selected = new SimpleBooleanProperty(false);
    public static final int MATCH = 0;
    public static final int DISTINCT = -1;
    public static final int MIX = 1;


    public FolderSummaryDTO(Folder folder, HashMap<Integer, String> mediaFilesIds) {
        this.folder = folder;
        this.mediaFilesIds = mediaFilesIds;
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
        idsMap.putIfAbsent(mediaFileId, new FileSummaryDTO(mediaFileId));
        idsMap.get(mediaFileId).add(mediaFileVersionId);
    }


    public Collection<FileSummaryDTO> getFilesSummaries() {
        return idsMap.values().stream().sorted().collect(Collectors.toList());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(idsMap.keySet().containsAll(mediaFilesIds.keySet()) ? "FULL" : (idsMap.keySet().size() + "/" + mediaFilesIds.size()));
        sb.append(" V");
        for (int i : version) {
            sb.append(i).append("-");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(" ").append(folder.getId()).append(" ").append(folder);
        return sb.toString();
    }

    public int compareWith(FolderSummaryDTO other) {
        return compareWith(getVersions(), other.getVersions());
    }

    public static int compareWith(Set<Integer> versions, Set<Integer> otherVersions) {
        if (versions.containsAll(otherVersions) && otherVersions.containsAll(versions)) return MATCH;
        versions.retainAll(otherVersions);
        return versions.size() == 0 ? DISTINCT : MIX;
    }

    public Set<Integer> getVersions() {
        HashSet<Integer> mfvIds = new HashSet<>();
        for (FileSummaryDTO value : idsMap.values()) {
            mfvIds.addAll(value.getMediaFileVersionIds());
        }
        return mfvIds;
    }

    public void setVersion(ArrayList<Set<Integer>> mfvIdsList) {
        ArrayList<Integer> versionList = new ArrayList<>();
        for (int i = 0; i < mfvIdsList.size(); i++) {
            Set<Integer> mfvIds = mfvIdsList.get(i);
            int compared = compareWith(getVersions(), mfvIds);
            if (compared == MATCH || compared == MIX) versionList.add(i);
        }
        version = new int[versionList.size()];
        for (int i = 0; i < versionList.size(); i++) {
            Integer version = versionList.get(i);
            this.version[i] = version;
        }
        for (Integer i : idsMap.keySet()) {
            idsMap.get(i).setName(mediaFilesIds.get(i));
        }
    }

    @Override
    public boolean isSelected() {
        return selected.get();
    }

    @Override
    public void setSelectedValue(boolean selected) {
        this.selected.set(selected);
    }

    @Override
    public BooleanProperty getSelected() {
        return selected;
    }


}
