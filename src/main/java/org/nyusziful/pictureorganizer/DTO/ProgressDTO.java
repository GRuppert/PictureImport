package org.nyusziful.pictureorganizer.DTO;

import javafx.application.Platform;
import javafx.scene.chart.XYChart;

public class ProgressDTO {
    private long progress;
    private long goal;
    private long speed;

    public void setGoal(long goal) {
        this.goal = goal;
        setProgress(0);
    }

    public void setProgress(long progress) {
        this.progress = progress;
        assert(goal != 0 || progress == 0);
        assert(goal >= progress);
    }

    public void setProgress(double progress, long speed) {
        this.progress = 0;
        this.goal = 0;
    }

    public void increaseProgress() {
        this.setProgress(progress + 1);
    }


    public long getGoal() {
        return goal;
    }

    public void reset() {
        setGoal(0);
    }
}
