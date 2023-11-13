package org.nyusziful.pictureorganizer.DTO;

import org.jetbrains.annotations.NotNull;

import java.util.*;

public class FileSummaryDTO implements Comparable<FileSummaryDTO>, SummaryDTO {
    private Integer mediaFileId;

    private String originalFileName;
    private Set<Integer> mediaFileVersionIds = new HashSet<>();
    public FileSummaryDTO(Integer mediaFileId) {
        this.mediaFileId = mediaFileId;
    }

    public void add(Integer mediaFileVersionId) {
        mediaFileVersionIds.add(mediaFileVersionId);
    }

    @Override
    public int compareTo(@NotNull FileSummaryDTO o) {
        if (o.mediaFileId == null) return 1;
        if (mediaFileId == null) return -1;
        return mediaFileId - o.mediaFileId;
    }

    public String getSummaryText() {
        StringBuilder sb = new StringBuilder();
        sb.append(originalFileName).append(":");
        ArrayList<Integer> list = new ArrayList<>(getMediaFileVersionIds());
        Collections.sort(list);
        for (Integer mediaFileVersionId : list) {
            sb.append(mediaFileVersionId).append(" ");
        }
        return sb.toString();
    }

    public Set<Integer> getMediaFileVersionIds() {
        return new HashSet<>(mediaFileVersionIds);
    }

    public void setName(String originalFileName) {
        this.originalFileName = originalFileName;
    }
}
