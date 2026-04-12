package org.nyusziful.pictureorganizer.DTO;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class FileSummaryDTO implements Comparable<FileSummaryDTO>, SummaryDTO {
    private Integer mediaFileId;
    private String originalFileName;
    private Set<Integer> mediaFileVersionIds = new HashSet<>();
    private BooleanProperty selected = new SimpleBooleanProperty(false);

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

    @Override
    public String toString() {
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

    public Integer getMediaFileId() {
        return mediaFileId;
    }
}
