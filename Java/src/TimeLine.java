/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.File;
import java.time.Instant;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;


/**
 *
 * @author gabor
 */
public class TimeLine {
    private static final int      KEYBOARD_MOVEMENT_DELTA = 5;
    private static final Duration TRANSLATE_DURATION      = Duration.seconds(0.3);
    private long FirstDate = -1;
    private long LastDate = -1;
    private long FirstVisDate = -1;
    private long LastVisDate = -1;
    HashMap<String, Stripes> stripes = new HashMap<>();
    private int imgwidth = 100;
    private VBox stripeBox;
    private BorderPane controls = new BorderPane();
    private int zoomAmount = 25;
    private PicOrganizes view;

    public TimeLine(File dir, PicOrganizes view) {
        this.view = view;
        stripeBox = new VBox();
        stripes = StaticTools.readFiles(dir, this, view.getZone());
        Iterator<String> iterator = stripes.keySet().iterator();
        resetLimits();
        while(iterator.hasNext()) {
            Stripes stripe = stripes.get(iterator.next());
            stripeBox.getChildren().add(stripe.getView());
        }
        controls = createControls();
        stripeBox.getChildren().add(getControls());
/*        final TranslateTransition transition = createTranslateTransition(imgView);
        moveObjectOnKeyPress(scene, imgView);
        moveCircleOnMousePress(scene, imgView, transition);*/
    }

    private BorderPane createControls() {
        BorderPane result = new BorderPane();
        Button shiftLeftButton = new Button("<");
        Button shiftRightButton = new Button(">");
        Button zoomInButton = new Button("+");
        Button zoomOutButton = new Button("-");
        Button zoomTotalButton = new Button("O");
        shiftLeftButton.setOnAction(new EventHandler<ActionEvent>(){
             @Override
             public void handle(ActionEvent event) {
                 shift(1);                 
             }
         });
        shiftRightButton.setOnAction(new EventHandler<ActionEvent>(){
             @Override
             public void handle(ActionEvent event) {
                 shift(-1);                 
             }
         });
        zoomInButton.setOnAction(new EventHandler<ActionEvent>(){
             @Override
             public void handle(ActionEvent event) {
                 zoom(zoomAmount);                 
             }
         });
        zoomOutButton.setOnAction(new EventHandler<ActionEvent>(){
             @Override
             public void handle(ActionEvent event) {
                 zoom(-zoomAmount);                 
             }
         });
        zoomTotalButton.setOnAction(new EventHandler<ActionEvent>(){
             @Override
             public void handle(ActionEvent event) {
                 zoom(-10000);                 
             }
         });
        result.setLeft(shiftLeftButton);
        result.setRight(shiftRightButton);
        result.setTop(new HBox(zoomInButton, zoomOutButton, zoomTotalButton));
        result.setCenter(createRuler());
        return result;
    }
    
    public TilePane createRuler() {
        TilePane ruler = new TilePane();
        int size = 5;
        String[] values = rulerValues(size);
        for (int i=0; i < size; i++) {
            Button btn = new Button(values[i]);
            ruler.getChildren().add(btn);
            HBox.setHgrow(btn, Priority.ALWAYS);
        }
        return ruler;
    }    

    private String[] rulerValues(int size) {
        String[] values = new String[size];
        long step = (getLastVisDate() - getFirstVisDate()) / size;
        for (int i=0; i < size; i++) {
            values[i] = Instant.ofEpochSecond(getFirstVisDate() + i*step).atZone(ZoneId.systemDefault()).toLocalTime().toString();
        }
        return values;
    }    

    
    public void resetLimits() {
        boolean zoomFirst = false;
        boolean zoomLast = false;
        if (FirstDate == FirstVisDate) zoomFirst = true;
        if (LastDate == LastVisDate) zoomLast = true;
        Iterator<String> iterator = stripes.keySet().iterator();
        if (iterator.hasNext()) {
            Stripes get = stripes.get(iterator.next());
            setFirstDate(get.minDate);
            setLastDate(get.maxDate);
        }
        while(iterator.hasNext()) {
            Stripes get = stripes.get(iterator.next());
            if (get.minDate < FirstDate) setFirstDate(get.minDate);
            if (get.maxDate > LastDate) setLastDate(get.maxDate);
        }
        if (zoomFirst) setFirstVisDate(getFirstDate());
        if (zoomLast) setLastVisDate(getLastDate());
        if (zoomFirst || zoomLast) resetView();
    }
    
    /**
     * @recount the X coordinate for all pictures
     */
    public void resetView() {
        Iterator<String> iterator = stripes.keySet().iterator();
        while(iterator.hasNext()) {
            stripes.get(iterator.next()).setX();
        }
        getControls().setCenter(createRuler());
    }

    private void shift(int sign) {
        int step = (int) (sign * (FirstVisDate - LastVisDate) / 10);
        if (step < 0) {
            if (FirstVisDate + step >= FirstDate) {
                FirstVisDate += step;
                LastVisDate += step;
            } else {
                LastVisDate += (FirstDate-FirstVisDate);
                FirstVisDate = FirstDate;
            }
        } else {
            if (LastVisDate + step <= LastDate) {
                FirstVisDate += step;
                LastVisDate += step;
            } else {
                FirstVisDate += (LastDate-LastVisDate);
                LastVisDate = LastDate;
            }
        }
        resetView();
    }

    private void zoom(int step) {
        long zoomingPoint = (LastVisDate + FirstVisDate)/2;
        zoom(step, zoomingPoint);
    }
    
    private void zoom(int step, long zoomingPoint) {
        long range = LastVisDate - FirstVisDate;
        double newRatio = (step > 0) ? 100d / (100d + step) : (100d + step) / 100d;
        double picsWidth = stripes.entrySet().iterator().next().getValue().getPicsWidth();
        if (step > 0) {
            if ((range * newRatio * imgwidth) >= picsWidth) {
                FirstVisDate += (zoomingPoint - FirstVisDate) * (1 - newRatio);
                LastVisDate -= (LastVisDate - zoomingPoint) * (1 - newRatio);
            } else {
                
            }
        } else {
            FirstVisDate -= (zoomingPoint - FirstVisDate) * (1 - newRatio);
            LastVisDate += (LastVisDate - zoomingPoint) * (1 - newRatio);
            if (LastVisDate >= LastDate) {
                FirstVisDate += (LastDate - LastVisDate);
            }
            if (FirstVisDate <= FirstDate) {
                LastVisDate += (FirstDate-FirstVisDate);
            }
            if (LastVisDate >= LastDate) {
                LastVisDate = LastDate;
            }
            if (FirstVisDate <= FirstDate) {
                FirstVisDate = FirstDate;
            }
        }
        resetView();        
    }

    private void moveObject(ImageView object, double delta) {
    object.setX(object.getX() + delta);
}
    
    private TranslateTransition createTranslateTransition(final ImageView object) {
    final TranslateTransition transition = new TranslateTransition(TRANSLATE_DURATION, object);
    transition.setOnFinished(new EventHandler<ActionEvent>() {
      @Override public void handle(ActionEvent t) {
        object.setX(object.getTranslateX() + object.getX());
//        object.setY(object.getTranslateY() + object.getY());
        object.setTranslateX(0);
//        object.setTranslateY(0);
      }
    });
    return transition;
  }

    private void moveObjectOnKeyPress(Scene scene, final ImageView object) {
    scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
      @Override public void handle(KeyEvent event) {
        switch (event.getCode()) {
//          case UP:    object.setX(object.getX() - KEYBOARD_MOVEMENT_DELTA); break;
          case RIGHT: moveObject(object, KEYBOARD_MOVEMENT_DELTA); break;
//          case DOWN:  circle.setCenterY(circle.getCenterY() + KEYBOARD_MOVEMENT_DELTA); break;
          case LEFT:  moveObject(object, -KEYBOARD_MOVEMENT_DELTA); break;
        }
      }
    });
  }

    private void moveCircleOnMousePress(Scene scene, final ImageView object, final TranslateTransition transition) {
    scene.setOnMousePressed(new EventHandler<MouseEvent>() {
      @Override public void handle(MouseEvent event) {
        if (!event.isControlDown()) {
          object.setX(event.getSceneX());
//          object.setY(event.getSceneY());
        } else {
          transition.setToX(event.getSceneX() - object.getX());
//          transition.setToY(event.getSceneY() - object.getY());
          transition.playFromStart();
        }  
      }
    });
  } 

    // <editor-fold defaultstate="collapsed" desc="Getter-Setter section">
    /**
     * @return the controls
     */
    public BorderPane getControls() {
        return controls;
    }

    /**
     * @return the FirstDate
     */
    public long getFirstDate() {
        return FirstDate;
    }

    /**
     * @param FirstDate the FirstDate to set
     */
    public void setFirstDate(long FirstDate) {
        this.FirstDate = FirstDate;
    }

    /**
     * @return the LastDate
     */
    public long getLastDate() {
        return LastDate;
    }

    /**
     * @param LastDate the LastDate to set
     */
    public void setLastDate(long LastDate) {
        this.LastDate = LastDate;
    }

    /**
     * @return the FirstVisDate
     */
    public long getFirstVisDate() {
        return FirstVisDate;
    }

    /**
     * @param FirstVisDate the FirstVisDate to set
     */
    public void setFirstVisDate(long FirstVisDate) {
        this.FirstVisDate = FirstVisDate;
    }

    /**
     * @return the LastVisDate
     */
    public long getLastVisDate() {
        return LastVisDate;
    }

    /**
     * @param LastVisDate the LastVisDate to set
     */
    public void setLastVisDate(long LastVisDate) {
        this.LastVisDate = LastVisDate;
    }

    /**
     * @return the imgwidth
     */
    public int getImgwidth() {
        return imgwidth;
    }

    /**
     * @return the stripeBox
     */
    public VBox getStripeBox() {
        return stripeBox;
    }
    // </editor-fold>
}
