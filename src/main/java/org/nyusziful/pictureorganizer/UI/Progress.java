package org.nyusziful.pictureorganizer.UI;
import javafx.application.Platform;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.chart.XYChart;

public class Progress {
    private static Progress instance;
    private long progress;
    private long goal;
    private long speed;
    private final SimpleDoubleProperty progressPercent;
    private XYChart.Series speeds;

    private Progress() {
        progressPercent = new SimpleDoubleProperty(0);
        speeds = new XYChart.Series();
        reset();
    }

    public static Progress getInstance() {
        if (Progress.instance == null) {
            Progress.instance = new Progress();
        }
        return Progress.instance;
    }

    public long timeToReady() {
        return goal-progress;
    }

    public void setGoal(long goal) {
        this.goal = goal;
        setProgress(0);
    }

    public long getGoal() {
        return goal;
    }

    public void increaseProgress() {
        this.setProgress(progress + 1);
    }

    public void setProgress(long progress) {
        this.progress = progress;
        assert(goal != 0 || progress == 0);
        assert(goal >= progress);
        Platform.runLater(() -> {
            progressPercent.setValue(goal == 0 ? 0 : (progress / goal < 1 ? progress / goal : 1));
        });
    }

    public void setProgress(double progress, long speed) {
        this.progress = 0;
        this.goal = 0;
        progressPercent.setValue(progress);
        Platform.runLater(() -> {
//            progressPercent.setValue(progress);
            getSpeeds().getData().add(new XYChart.Data(progressPercent.getValue(), speed));
        });
    }



    public SimpleDoubleProperty getProgressProperty() {
        return progressPercent;
    }

    public double getProgress() {
        return progressPercent.getValue();
    }

    public void reset() {
        setGoal(0);
        Platform.runLater(() -> {
            getSpeeds().getData().clear();
            getSpeeds().getData().add(new XYChart.Data(0, 0));
        });
    }

    public XYChart.Series getSpeeds() {
        return speeds;
    }
}

