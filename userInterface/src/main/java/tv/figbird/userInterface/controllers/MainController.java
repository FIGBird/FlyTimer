package tv.figbird.userInterface.controllers;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import tv.figbird.splitTimer.core.SplitTimer;
import tv.figbird.splitTimer.utilities.TimeDisplayUtil;
import tv.figbird.userInterface.helpers.ThreadHelper;

public class MainController extends AbstractController {
    public Label timerDisplay;
    public Label currentSplitDisplay;
    public Button stopButton;
    public Button startButton;
    public Button splitButton;
    public ListView<String> splits;

    private SplitTimer splitTimer;

    public void initialize() {
        this.splitTimer = new SplitTimer();
        ThreadHelper.getTimedDeamon(this::updateDisplayTimerText, 10).start();
        ThreadHelper.getTimedDeamon(this::updateButtonText, 10).start();
    }

    public void startTimer() {
        splitTimer.start();
        splits.getItems().clear();
    }

    public void pauseOrResumeTimer() {
        splitTimer.pauseOrResume();
    }

    public void split() {
        long splitTime = splitTimer.split();
        if (splitTime != 0) {
            splits.getItems().add(TimeDisplayUtil.getDisplayTime(splitTime));
        }
    }

    protected void updateDisplayTimerText() {
        timerDisplay.setText(TimeDisplayUtil.getDisplayTime(splitTimer.getCurrentTime()));
        currentSplitDisplay.setText(TimeDisplayUtil.getDisplayTime(splitTimer.getCurrentSplitTime()));
    }

    protected void updateButtonText() {
        if (splitTimer.isRunning()) {
            stopButton.setText("Pause");
        } else {
            stopButton.setText("Resume");
        }
    }

    public void setSpeedGame(String gameName, int totalSplits) {
        splitTimer.setCurrentGame(gameName, totalSplits);
    }

}
