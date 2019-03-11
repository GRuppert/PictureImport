package org.nyusziful.pictureorganizer.TimeShift;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.util.ArrayList;
import java.util.Iterator;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;


/**
 *
 * @author gabor
 */
public class Stripes implements Collector {
    ArrayList<Picture> pictures = new ArrayList<>();
    long minDate = -1;
    long maxDate = -1;
    int index;
    TimeLine view;
    Button shiftLeftButton = new Button("<");
    Button shiftRightButton = new Button(">");
    Label label;
    BorderPane border = new BorderPane();
    int buttonWidth = 50;
    Pane picPane = new Pane();


    

    public Stripes(String model, int ind, TimeLine view) {
        this(model, ind, new ArrayList<Picture>(), view);
    }

    public Stripes(String model, int ind, ArrayList<Picture> pics, TimeLine view) {
        label = new Label(model);
        this.view = view;
        index = ind;
        pictures = pics;
        Iterator<Picture> iterator = pictures.iterator();
        while(iterator.hasNext()) {
            Picture picture = iterator.next();
            initPic(picture);
        }
        resetLimits();
        shiftLeftButton.setOnAction(new EventHandler<ActionEvent>(){
             @Override
             public void handle(ActionEvent event) {
                 shift(-1);                 
             }
         });
        shiftRightButton.setOnAction(new EventHandler<ActionEvent>(){
             @Override
             public void handle(ActionEvent event) {
                 shift(1);                 
             }
         });
        label.setPrefHeight(30);
        border.setTop(label);
        shiftLeftButton.setPrefWidth(buttonWidth);
        border.setLeft(shiftLeftButton);       
        shiftLeftButton.setPrefWidth(buttonWidth);
        border.setRight(shiftRightButton); 
        border.setBottom(new Separator());
        border.setCenter(picPane);
    }

    @Override
    public void add(Picture picture) {
        pictures.add(picture);
        initPic(picture);
        resetLimits();
    }   
    
    private void initPic(Picture picture) {
//        picture.setY(index*(view.imgwidth + 50));
        picture.setSize(view.getImgwidth());
        picPane.getChildren().add(picture.getView());
    }

    @Override
    public void remove(Picture picture) {
        pictures.remove(picture);
        resetLimits();
    }

    private void resetLimits() {
        Iterator<Picture> iterator = pictures.iterator();
        if (iterator.hasNext()) {
            Picture picture = iterator.next();
            minDate = picture.getDate();
            maxDate = picture.getDate();
        }
        while(iterator.hasNext()) {
            Picture picture = iterator.next();
            if (picture.getDate() < minDate) minDate = picture.getDate();
            if (picture.getDate() > maxDate) maxDate = picture.getDate();
        }
        view.resetLimits();
    }

    @Override
    public void setX() {
        Iterator<Picture> iterator = pictures.iterator();
        while (iterator.hasNext()) {
            Picture picture = iterator.next();
            if (picture.getDate() <= view.getLastVisDate() && picture.getDate() >= view.getFirstVisDate()) {
                picture.setVisible(true);
                picture.setX((picture.getDate()-view.getFirstVisDate()) * (picPane.getWidth()-view.getImgwidth()) / (view.getLastVisDate()-view.getFirstVisDate()));
            } else {
                picture.setVisible(false);
            }
        }        
    }

    @Override
    public Node getView() {
        return border;
    }

    public void shift(int sign) {
        long shift = sign * 10;
        Iterator<Picture> iterator = pictures.iterator();
        while(iterator.hasNext()) {
            Picture picture = iterator.next();
            picture.setShift(shift * (view.getLastVisDate()-view.getFirstVisDate()) / (picPane.getWidth()-view.getImgwidth()));
        }
        resetLimits();
    }
    
    public double getPicsWidth() {
        return picPane.getWidth();
    }
}
    

