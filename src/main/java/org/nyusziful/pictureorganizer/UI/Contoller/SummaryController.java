package org.nyusziful.pictureorganizer.UI.Contoller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import org.nyusziful.pictureorganizer.DAL.Entity.Image;
import org.nyusziful.pictureorganizer.Service.ImageService;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class SummaryController implements Initializable {
    @FXML
    private Label image_num;

    @FXML
    private Label backup_num;

    @FXML
    private Label nobackup_num;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ImageService imageService = new ImageService();
        List<Image> images = imageService.getImages();
        image_num.setText(Integer.toString(images.size()));
        int hasBackup = 0;
        for (Image image : images) {
            if (image.getMediaFiles().size() > 2) hasBackup++;
        }
        backup_num.setText(Integer.toString(hasBackup));
        nobackup_num.setText(Integer.toString(images.size()-hasBackup));
    }

}
