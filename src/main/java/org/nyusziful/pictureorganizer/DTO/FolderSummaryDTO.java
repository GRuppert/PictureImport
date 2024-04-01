package org.nyusziful.pictureorganizer.DTO;

import org.jetbrains.annotations.NotNull;
import org.nyusziful.pictureorganizer.DAL.Entity.Folder;

import java.util.*;
import java.util.stream.Collectors;

public class FolderSummaryDTO implements Comparable<FolderSummaryDTO>, SummaryDTO {
    private Folder folder;
    private HashMap<Integer, FileSummaryDTO> idsMap = new HashMap<>();
    private HashMap<Integer, String> mediaFilesIds;
    private int[] version = new int[0];
    private boolean selected;

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
        FileSummaryDTO fileSummaryDTO = idsMap.get(mediaFileId);
        if (fileSummaryDTO == null) {
            fileSummaryDTO = new FileSummaryDTO(mediaFileId);
            idsMap.put(mediaFileId, fileSummaryDTO);
        }
        fileSummaryDTO.add(mediaFileVersionId);
    }


    public Collection<FileSummaryDTO> getFilesSummaries() {
        return idsMap.values().stream().sorted().collect(Collectors.toList());
    }

    public String getSummaryText() {
        StringBuilder sb = new StringBuilder();
        sb.append(idsMap.keySet().containsAll(mediaFilesIds.keySet()) ? "FULL" : (idsMap.keySet().size() + "/" + mediaFilesIds.size()));
        sb.append(" V");
        for (int i : version) {
            sb.append(i).append("-");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(" ").append(folder);
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
        for (int j = 0; j < mfvIdsList.size(); j++) {
            Set<Integer> mfvIds = mfvIdsList.get(j);
            int i = compareWith(getVersions(), mfvIds);
            if (i == MATCH || i == MIX) versionList.add(j);
        }
        version = new int[versionList.size()];
        for (int j = 0; j < versionList.size(); j++) {
            Integer i = versionList.get(j);
            version[j] = i;
        }
        for (Integer i : idsMap.keySet()) {
            idsMap.get(i).setName(mediaFilesIds.get(i));
        }
    }

    @Override
    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

}
