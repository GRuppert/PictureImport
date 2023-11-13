package org.nyusziful.pictureorganizer.DTO;

import org.jetbrains.annotations.NotNull;
import org.nyusziful.pictureorganizer.DAL.Entity.MediaDirectory;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class DirectorySummaryDTO implements Comparable<DirectorySummaryDTO>, SummaryDTO {
    private MediaDirectory mediaDirectory;
    private HashMap<String, Set<Integer>> idsMap = new HashMap<>();
    public DirectorySummaryDTO(MediaDirectory mediaDirectory) {
        this.mediaDirectory = mediaDirectory;
    }

    public MediaDirectory getMediaDirectory() {
        return mediaDirectory;
    }

    @Override
    public int compareTo(@NotNull DirectorySummaryDTO o) {
        if (o.getMediaDirectory() == null) return 1;
        if (getMediaDirectory() == null) return -1;
        return getMediaDirectory().getId() - o.getMediaDirectory().getId();
    }

    public void put(String setName, Integer id) {
        Set<Integer> ids = idsMap.get(setName);
        if (ids == null) {
            ids = new HashSet<>();
            idsMap.put(setName, ids);
        }
        ids.add(id);
    }

    public String getSummaryText() {
        StringBuilder sb = new StringBuilder();
        for (String name : idsMap.keySet()) {
            sb.append("(").append(name).append(":").append(idsMap.get(name).size()).append(")");
        }
        sb.append(mediaDirectory);
        return sb.toString();
    }

    public Set<Integer> getIds(String name) {
        return idsMap.get(name);
    }
}
