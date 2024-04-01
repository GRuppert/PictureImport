package org.nyusziful.pictureorganizer.UI.Contoller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import org.nyusziful.pictureorganizer.DTO.DirectorySummaryDTO;
import org.nyusziful.pictureorganizer.DTO.FileSummaryDTO;
import org.nyusziful.pictureorganizer.DTO.FolderSummaryDTO;
import org.nyusziful.pictureorganizer.DTO.SummaryDTO;
import org.nyusziful.pictureorganizer.Service.MediaFileGeneralService;
import org.nyusziful.pictureorganizer.Service.MediaFileVersionService;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.net.URL;
import java.nio.file.Path;
import java.util.*;

import static java.awt.SystemColor.menu;

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
        summaryTree.setCellFactory(cb -> new SummaryTreeCell());
        for (DirectorySummaryDTO directorySummaryDTO : MediaFileGeneralService.getInstance().loadDirectoryVersionStatus()) {
            TreeItem<SummaryDTO> directorySummaryDTOTreeItem = new TreeItem<>(directorySummaryDTO);
            root.getChildren().add(directorySummaryDTOTreeItem);
            if (directorySummaryDTO.getIds("Collision") != null && !directorySummaryDTO.getIds("Collision").isEmpty()) {
                directorySummaryDTOTreeItem.getChildren().add(null);
                directorySummaryDTOTreeItem.setExpanded(false);
                directorySummaryDTOTreeItem.expandedProperty().addListener((observable, oldValue, newValue) -> {
                    if (newValue) {
                        if (directorySummaryDTOTreeItem.getChildren().contains(null)) {
                            directorySummaryDTOTreeItem.getChildren().clear();
                            for (FolderSummaryDTO loadDirectoryVersionStatus : MediaFileGeneralService.getInstance().loadDirectoryVersionStatus(directorySummaryDTO.getIds("Collision"))) {
                                TreeItem<SummaryDTO> folderSummaryDTOTreeItem = new TreeItem<>(loadDirectoryVersionStatus);
                                directorySummaryDTOTreeItem.getChildren().add(folderSummaryDTOTreeItem);
                                for (FileSummaryDTO fileSummaryDTO : loadDirectoryVersionStatus.getFilesSummaries()) {
                                    folderSummaryDTOTreeItem.getChildren().add(new TreeItem<>(fileSummaryDTO));
                                }
                            }
                        }
                    }
                });
            }
        }
    }

    public class SummaryTreeCell extends TreeCell<SummaryDTO> {
        @Override
        public void updateItem(SummaryDTO item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setContextMenu(null);
            } else {
                if (item instanceof DirectorySummaryDTO) {
                    setText(item.getSummaryText());
                } else {
                    ContextMenu contextMenu = new ContextMenu();
                    setText(item.getSummaryText());
                    setContextMenu(contextMenu);
                    if (item instanceof FolderSummaryDTO) {
                        FolderSummaryDTO folderSummaryDTO = (FolderSummaryDTO) item;
                        MenuItem copyPathToClipboard = new MenuItem("Copy path to clipboard");
                        copyPathToClipboard.setOnAction(event -> {
                            Path javaPath = folderSummaryDTO.getFolder().getJavaPath();
                            if (javaPath == null) JOptionPane.showMessageDialog(null, "No local path found for: " + folderSummaryDTO.getFolder());
                            else {
                                StringSelection stringSelection = new StringSelection(javaPath.toString());
                                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, stringSelection);
                            }
                        });
                        contextMenu.getItems().add(copyPathToClipboard);
                        contextMenu.getItems().add(new SeparatorMenuItem());
                        MenuItem setAsOriginal = new MenuItem("Set as original");
                        setAsOriginal.setOnAction(event -> {
                            MediaFileVersionService mediaFileVersionService = MediaFileVersionService.getInstance();
                            for (Integer version : folderSummaryDTO.getVersions()) {
                                mediaFileVersionService.setAsOriginal(version, true);
                            }
                        });
                        contextMenu.getItems().add(setAsOriginal);
                        MenuItem setAsInvalid = new MenuItem("Set as invalid");
                        setAsInvalid.setOnAction(event -> {
                            MediaFileVersionService mediaFileVersionService = MediaFileVersionService.getInstance();
                            for (Integer version : folderSummaryDTO.getVersions()) {
                                mediaFileVersionService.setAsInvalid(version, true);
                            }
                        });
                        contextMenu.getItems().add(setAsInvalid);
                        contextMenu.getItems().add(new MenuItem("Set parent"));
                    } else if (item instanceof FileSummaryDTO) {
                        contextMenu.getItems().add(new MenuItem("Set as original"));
                        contextMenu.getItems().add(new MenuItem("Set as invalid"));
                        contextMenu.getItems().add(new MenuItem("Set parent"));
                        contextMenu.setOnAction(event -> {
                            JOptionPane.showMessageDialog(null, "Works on " + ((FileSummaryDTO) item).getMediaFileVersionIds());
                        });
                    }
                }
            }
        }
    }
}
