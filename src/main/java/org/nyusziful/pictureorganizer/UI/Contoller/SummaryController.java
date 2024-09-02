package org.nyusziful.pictureorganizer.UI.Contoller;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.MenuItem;
import org.nyusziful.pictureorganizer.DAL.Entity.Folder;
import org.nyusziful.pictureorganizer.DAL.Entity.MediaFileVersion;
import org.nyusziful.pictureorganizer.DTO.*;
import org.nyusziful.pictureorganizer.Service.MediaFileGeneralService;
import org.nyusziful.pictureorganizer.Service.MediaFileVersionService;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.net.URL;
import java.nio.file.Path;
import java.util.*;

public class SummaryController implements Initializable {
    @FXML
    private TreeView<SummaryDTO> summaryTree;
    @FXML
    private TreeView<VersionDTO> versionTree;

    private DirectorySummaryDTO selectedDirectory = null;
    private TreeItem<VersionDTO> vroot = new TreeItem<>();


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
        versionTree.setRoot(vroot);
        versionTree.setShowRoot(false);
        versionTree.setCellFactory(cb -> new VersionTreeCell());


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
                            for (FolderSummaryDTO folderSummaryDTO : MediaFileGeneralService.getInstance().loadDirectoryVersionStatus(directorySummaryDTO.getIds("Collision"))) {
                                TreeItem<SummaryDTO> folderSummaryDTOTreeItem = new TreeItem<>(folderSummaryDTO);
                                directorySummaryDTOTreeItem.getChildren().add(folderSummaryDTOTreeItem);
                                for (FileSummaryDTO fileSummaryDTO : folderSummaryDTO.getFilesSummaries()) {
                                    folderSummaryDTOTreeItem.getChildren().add(new TreeItem<>(fileSummaryDTO));
                                }
                            }
                        }
                    }
                });
            }
        }
        summaryTree.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> setSelectedDirectory(newValue));
    }

    private void setSelectedDirectory(TreeItem<SummaryDTO> selectedItem) {
        DirectorySummaryDTO newDirectoryDTO = null;
        if (selectedItem != null) {
            SummaryDTO newDirectory = null;
            if (selectedItem.getValue() instanceof DirectorySummaryDTO) {
                newDirectory = selectedItem.getValue();
            } else if (selectedItem.getValue() instanceof FolderSummaryDTO) {
                newDirectory = selectedItem.getParent().getValue();
            } else if (selectedItem.getValue() instanceof FileSummaryDTO) {
                newDirectory = selectedItem.getParent().getParent().getValue();
            }
            if (!Objects.equals(selectedDirectory, selectedItem.getValue())) {
                newDirectoryDTO = (DirectorySummaryDTO) newDirectory;
            }
        }
        if (!Objects.equals(selectedDirectory, newDirectoryDTO)) {
            updateVTree(newDirectoryDTO);
            selectedDirectory = newDirectoryDTO;
        }
    }

    private void updateVTree(DirectorySummaryDTO directoryDTO) {
        vroot.getChildren().clear();
        if (directoryDTO != null) {
            for (VersionDTO item : MediaFileGeneralService.getInstance().loadDirectoryVersions(directoryDTO.getMediaDirectory().getId())) {
                vroot.getChildren().add(new TreeItem<>(item));
            }
        }
    }

    public class SummaryTreeCell extends TreeCell<SummaryDTO> {
        private final CheckBox checkBox = new CheckBox();

        public SummaryTreeCell() {
            checkBox.setOnAction(e -> {
                getTreeView().getTreeItem(getIndex()).getValue().setSelectedValue(checkBox.isSelected());
                for (TreeItem<SummaryDTO> child : getTreeView().getTreeItem(getIndex()).getChildren()) {
                    child.getValue().setSelectedValue(checkBox.isSelected());
                    Event.fireEvent(child, new TreeItem.TreeModificationEvent<>(TreeItem.valueChangedEvent(), child));
                }
            });
        }

        @Override
        public void updateItem(SummaryDTO item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setContextMenu(null);
                setText("");
            } else {
                setText(item.toString());
                checkBox.setSelected(item.isSelected());
                if (item instanceof DirectorySummaryDTO) {
                    setGraphic(null);
                } else {
                    setGraphic(checkBox);
                    ContextMenu contextMenu = new ContextMenu();
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
                        MediaFileVersionService mediaFileVersionService = MediaFileVersionService.getInstance();

                        MenuItem setAsOriginal = new MenuItem("Set as original");
                        setAsOriginal.setOnAction(event -> {
                            for (Integer version : folderSummaryDTO.getVersions()) {
                                mediaFileVersionService.setAsOriginal(version, true);
                            }
                        });
                        contextMenu.getItems().add(setAsOriginal);

                        MenuItem setAsInvalid = new MenuItem("Set as invalid");
                        setAsInvalid.setOnAction(event -> {
                            for (Integer version : folderSummaryDTO.getVersions()) {
                                mediaFileVersionService.setAsInvalid(version, true);
                            }
                        });
                        contextMenu.getItems().add(setAsInvalid);
                        MenuItem setAsParent = new MenuItem("Set as Parent");
                        setAsParent.setOnAction(event -> {
                            HashMap<Integer, HashSet<Integer>> parents = new HashMap<>();
                            for (FileSummaryDTO version : folderSummaryDTO.getFilesSummaries()) {
                                Set<Integer> mediaFileVersionIds = version.getMediaFileVersionIds();
                                if (mediaFileVersionIds.size() != 1) return; //multiple versions of the same file in the "parent" directory
                                for (Integer mediaFileVersionId : mediaFileVersionIds) {
                                    for (TreeItem<SummaryDTO> sibling : getTreeItem().getParent().getChildren()) {
                                        if (getTreeItem().equals(sibling)) continue;
                                        if (sibling.getValue().isSelected()) {
                                            for (TreeItem<SummaryDTO> childItem : sibling.getChildren()) {
                                                FileSummaryDTO fileSummaryDTO = (FileSummaryDTO) childItem.getValue();
                                                if (version.getMediaFileId().equals(fileSummaryDTO.getMediaFileId())) {
                                                    if (fileSummaryDTO.getMediaFileVersionIds().size() != 1) continue; //multiple versions of the same file in the "slave" directory
                                                    if (fileSummaryDTO.getMediaFileVersionIds().contains(mediaFileVersionId)) continue;
                                                    System.out.println(mediaFileVersionId + ":" + fileSummaryDTO.getMediaFileVersionIds());
//                                                    parents.getOrDefault(mediaFileVersionId, new HashSet<>()).addAll(fileSummaryDTO.getMediaFileVersionIds());
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            for (Integer parent : parents.keySet()) {
                                for (Integer child : parents.get(parent)) {
                                    mediaFileVersionService.setParent(child, parent, true);
                                }
                            }
                        });
                        contextMenu.getItems().add(setAsParent);
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

    public class VersionTreeCell extends TreeCell<VersionDTO> {

        @Override
        public void updateItem(VersionDTO item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setTooltip(null);
                setText("");
            } else {
                setText(item.toString());
                StringBuilder sb = new StringBuilder("Directories:\n");
                for (Folder folder : item.getFolders()) {
                    sb.append(folder).append("\n");
                }
                sb.append("New Files:\n");
                for (MediaFileVersion newVersion : item.getNewVersions()) {
                    sb.append(newVersion.getMediaFile().getOriginalFilename()).append("(").append(newVersion).append(")\n");
                }
                setTooltip(new Tooltip(sb.toString()));
            }
        }
    }

}
