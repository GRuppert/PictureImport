package org.nyusziful.Main;

import javafx.concurrent.Task;

public class CurrentTask {
    private Task currentTask;
    private static CurrentTask instance;

    private CurrentTask() {
    }

    public static CurrentTask getInstance() {
        if (CurrentTask.instance == null) {
            CurrentTask.instance = new CurrentTask();
        }
        return CurrentTask.instance;
    }

    public void registerTask(Task newTask) {
        currentTask = newTask;
    }

    public boolean cancel() {
        return currentTask.cancel();
    }
}
