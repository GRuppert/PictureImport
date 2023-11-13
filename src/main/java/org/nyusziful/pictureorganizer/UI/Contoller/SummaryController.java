package org.nyusziful.pictureorganizer.UI.Contoller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Callback;
import org.nyusziful.pictureorganizer.DTO.DirectorySummaryDTO;
import org.nyusziful.pictureorganizer.DTO.FileSummaryDTO;
import org.nyusziful.pictureorganizer.DTO.FolderSummaryDTO;
import org.nyusziful.pictureorganizer.DTO.SummaryDTO;
import org.nyusziful.pictureorganizer.Service.MediaFileGeneralService;

import java.net.URL;
import java.util.*;

public class SummaryController implements Initializable {
    @FXML
    private TreeView<SummaryDTO> summaryTree;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
/*        HashMap<MediaDirectory, HashMap<Status, List<MediaFileVersion>>> summary = new HashMap<>();
        for (MediaFile mediafile : MediaFileService.getInstance().getMediafiles()) {
            MediaDirectory mediaDirectory = mediafile.getMediaDirectory();
            HashMap<Status, List<MediaFileVersion>> directorySummary = summary.get(mediaDirectory);
            if (directorySummary == null) {
                directorySummary = new HashMap<>();
                summary.put(mediaDirectory, directorySummary);
            }
            mfvLoop:
            for (MediaFileVersion mediaFileVersion : MediaFileVersionService.getInstance().getMediafileVersionsByMediaFile(mediafile)) {
                if (Boolean.TRUE.equals(mediaFileVersion.isInvalid())) continue mfvLoop;
                for (Media media : mediaFileVersion.getMedia()) {
                    if (MediaFileHash.EMPTYHASH.equals(media.getImage().getHash())) continue mfvLoop;
                }
                if (MediaFileVersionService.getInstance().getMediafileVersionsByParent(mediaFileVersion) == null) continue;
                HashSet<Drive> nobackup = new HashSet<>();
                for (MediaFileInstance mediaFileInstance : MediaFileInstanceService.getInstance().getMediaFilesInstancesByVersion(mediaFileVersion)) {
                    nobackup.add(mediaFileInstance.getDrive());
                }
                if (nobackup.size() < 3) {
                    List<MediaFileVersion> mediaFileVersions = directorySummary.get(Status.NOBackup);
                    if (mediaFileVersions == null) {
                        mediaFileVersions = new ArrayList<>();
                        directorySummary.put(Status.NOBackup, mediaFileVersions);
                    }
                    mediaFileVersions.add(mediaFileVersion);
                }
            }
            size++;
        }*/
        TreeItem<SummaryDTO> root = new TreeItem<>();
        summaryTree.setRoot(root);
        summaryTree.setEditable(true);
        summaryTree.setShowRoot(false);
        summaryTree.setCellFactory(summaryDTOTreeNode -> {
            return new SummaryTreeCell();
        });
        for (DirectorySummaryDTO directorySummaryDTO : MediaFileGeneralService.getInstance().loadDirectoryVersionStatus()) {
            TreeItem<SummaryDTO> directorySummaryDTOTreeItem = new TreeItem<>(directorySummaryDTO);
            root.getChildren().add(directorySummaryDTOTreeItem);
            if (directorySummaryDTO.getMediaDirectory() != null && directorySummaryDTO.getMediaDirectory().getId() == 27) {
                for (FolderSummaryDTO loadDirectoryVersionStatus : MediaFileGeneralService.getInstance().loadDirectoryVersionStatus(directorySummaryDTO.getIds("Collision"))) {
                    TreeItem<SummaryDTO> folderSummaryDTOTreeItem = new TreeItem<>(loadDirectoryVersionStatus);
                    directorySummaryDTOTreeItem.getChildren().add(folderSummaryDTOTreeItem);
                    for (FileSummaryDTO fileSummaryDTO : loadDirectoryVersionStatus.getFilesSummaries()) {
                        folderSummaryDTOTreeItem.getChildren().add(new TreeItem<>(fileSummaryDTO));
                    }

                }
                directorySummaryDTOTreeItem.setExpanded(true);
            } else {
                directorySummaryDTOTreeItem.setExpanded(false);
            }
        }
    }

    public class SummaryTreeCell extends TreeCell<SummaryDTO> {
        @Override
        public void updateItem(SummaryDTO item, boolean empty) {
            super.updateItem(item, empty);
            if (!empty) {
                setText(item.getSummaryText());
            }
        }

        @Override
        public void startEdit() {
            super.startEdit();
        }
    }
}
