/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.File;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.apache.commons.io.FilenameUtils;


/**
 *
 * @author gabor
 */
class Picture {
    private File file;
    private Image img;
    private long date;
    private long shift;
//    private Rectangle view = new Rectangle();
    private ImageView imageView;
    private String model = null;

    public Picture(File file, long date, String model) {
        this.file = file;
        this.date = date;
        this.model = model;
        imageView = getImage();
    }
    
    private ImageView getImage() {
        ImageView imgView;
        String thmbFile = file.getParent() + "\\thmb\\" + FilenameUtils.getBaseName(file.getName()) + "_thmb.jpg";
        imgView = new ImageView(new File(thmbFile).toURI().toString());
        imgView.setPreserveRatio(true);
        imgView.setSmooth(true);
        imgView.setCache(true);
        return imgView;
    }

    public long getDate() {return date+shift;}
    public void setShift(double step) {shift += step;}
    public String getModel() {return model;}
    
    public Node getView() {return imageView;}
    public void setVisible(boolean vis) {imageView.setVisible(vis);}
    public boolean isVisible() {return imageView.isVisible();}
    public void setX(double coord) {imageView.setX(coord);}
//    public void setY(double coord) {view.setY(coord);}
//    public void setSize(double coord) {view.setWidth(coord); view.setHeight(coord);}
    public void setSize(double size) {
        if (imageView.getImage().getHeight() < imageView.getImage().getWidth())
            imageView.setFitWidth(size);
        else 
            imageView.setFitHeight(size);
    }
    public double getX() {return imageView.getX();}
    public double getTranslateX() {return imageView.getTranslateX();}
    
}

