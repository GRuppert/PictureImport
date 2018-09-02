package org.nyusziful.TimeShift;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import javafx.scene.Node;

/**
 *
 * @author gabor
 */
public interface Collector {

    void add(Picture picture);

    Node getView();

    void remove(Picture picture);

    void setX();
    
    void shift(int sign);
}
