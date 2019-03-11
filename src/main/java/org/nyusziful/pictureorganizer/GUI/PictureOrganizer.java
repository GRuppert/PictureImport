package org.nyusziful.pictureorganizer.GUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.prefs.Preferences;

import static javafx.application.Application.launch;


public class PictureOrganizer extends Application {
    public static PictureOrganizer view;
    private Stage primaryStage;
    private int maxWidth, maxHeight;

    @Override
    public void start(Stage stage) throws Exception {
        view = this;
        this.primaryStage = stage;

        Parent fmxlLook = FXMLLoader.load(getClass().getResource("/fxml/main.fxml"));
        Scene mainScene = new Scene(fmxlLook);
        mainScene.getStylesheets().add("Chart.css");
        primaryStage.setScene(mainScene);

        //set Stage boundaries to visible bounds of the main screen
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        maxWidth = (int) (primaryScreenBounds.getWidth() - 150);
        maxHeight = (int) (primaryScreenBounds.getHeight() - 150);
        primaryStage.setX(primaryScreenBounds.getMinX());
        primaryStage.setY(primaryScreenBounds.getMinY());
        primaryStage.setWidth(maxWidth);
        primaryStage.setHeight(maxHeight);
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        CommonProperties.getInstance().save();
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
