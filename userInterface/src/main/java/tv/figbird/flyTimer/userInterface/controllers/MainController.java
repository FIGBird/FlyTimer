package tv.figbird.flyTimer.userInterface.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import tv.figbird.flyTimer.splitTimer.core.entities.Segment;
import tv.figbird.flyTimer.splitTimer.core.entities.SegmentTypes;
import tv.figbird.flyTimer.splitTimer.core.entities.Speedrun;
import tv.figbird.flyTimer.splitTimer.core.timer.SplitTimer;
import tv.figbird.flyTimer.splitTimer.utilities.TimeDisplayUtil;
import tv.figbird.flyTimer.userInterface.context.SpeedrunInstance;
import tv.figbird.flyTimer.userInterface.entities.SegmentEntry;
import tv.figbird.flyTimer.userInterface.helpers.TableViewHelper;
import tv.figbird.flyTimer.userInterface.helpers.ThreadHelper;

import java.io.IOException;

public class MainController {

    //FX Variables
    public Button stopOrResumeButton;
    public Label timerDisplay;
    public Button resetButton;
    public Label gameDisplay;
    public Label categoryDisplay;
    public Label attemptCounter;
    public Button segmentManagerButton;
    public Button splitButton;
    public TableView<SegmentEntry> segmentTimesTable;


    //Other Variables
    private SplitTimer splitTimer;
    private boolean isTimerRunning = false;
    private boolean isReset = true;
    private int splitIndex;

    //Start Up Methods
    public void initialize() {
        loadSpeedrun();
        splitTimer = new SplitTimer();
        ThreadHelper.getTimedDeamon(this::updateDisplayTimerText, 10).start();
        initializeTimes();
    }

    private void initializeTimes() {
        ObservableList<SegmentEntry> items =
                FXCollections.observableArrayList();
        segmentTimesTable.getItems().clear();
        long sumOfBest = 0;

        for (Segment segment : getSpeedrun().getSegments()) {
            SegmentEntry entry = new SegmentEntry();

            entry.setName(segment.getName());
            if (segment.getHistories().get(SegmentTypes.PERSONAL_BEST) != null) {
                entry.setPb(TimeDisplayUtil.getDisplayTime(segment.getHistories().get(SegmentTypes.PERSONAL_BEST).getEndedAt()));
            }
            sumOfBest = sumOfBest + segment.getBestDuration();
            entry.setBest(TimeDisplayUtil.getDisplayTime(sumOfBest));
            items.add(entry);
        }
        segmentTimesTable.getItems().addAll(items);

    }

    private void loadSpeedrun() {
        //TODO: create automatic deserializer for last used Speedrun

        Speedrun speedrun = new Speedrun();
        speedrun.setAttempts(0);
        speedrun.setGame("Test Game");
        speedrun.setCategory("Any%");
        speedrun.setRunners("FIGBird");

        int totalSegments = 40;
        int i = 1;
        while (i <= totalSegments) {
            Segment segment = new Segment();
            segment.setName("Segment " + i);
            segment.setHistoricTime(SegmentTypes.PERSONAL_BEST, ((i * 1000) + 500));
            segment.setBestDuration(1000);
            speedrun.getSegments().add(segment);
            i = i + 1;
        }

        SpeedrunInstance.getInstance().setSpeedrun(speedrun);
        updateSpeedrunDisplay();
    }

    //Timer Action Methods
    //TODO: Create Keybindings
    public void stopOrResumeTimer() {
        if (splitIndex < getSpeedrun().getSegments().size()) {
            if (isTimerRunning) {
                splitTimer.stop();
                stopOrResumeButton.setText("Start");
                isTimerRunning = false;
            } else {
                splitTimer.start();
                stopOrResumeButton.setText("Stop");
                isTimerRunning = true;
            }
            if (isReset) {
                incrementAttempt();
                this.isReset = false;
                splitIndex = 0;
                segmentTimesTable.getSelectionModel().select(splitIndex);
                segmentTimesTable.scrollTo(splitIndex);
            }
        }
    }

    public void split() {
        if (isTimerRunning && splitIndex < getSpeedrun().getSegments().size()) {
            long endTime = splitTimer.getCurrentTime();
            Segment segment = getSpeedrun().getSegments().get(splitIndex);
            SegmentEntry entry = segmentTimesTable.getItems().get(splitIndex);

            segment.setEndedAt(endTime);
            entry.setCurrentTime(TimeDisplayUtil.getDisplayTime(endTime));

            long duration = getSegmentDuration(splitIndex, endTime);
            if (duration < segment.getBestDuration()) {
                segment.setBestDuration(duration);
            }
            entry.setBest(getTimeDifferenceDisplay(duration, segment.getBestDuration()));

            if (segment.getHistoricTime(SegmentTypes.PERSONAL_BEST) == null) {
                entry.setPb(TimeDisplayUtil.getDisplayTime(0));
            } else {
                entry.setPb(getTimeDifferenceDisplay(endTime, segment.getHistoricTime(SegmentTypes.PERSONAL_BEST)));
            }

            //TODO: Enhance this code and/or split into new method
            if (splitIndex == getSpeedrun().getSegments().size() - 1) {
                stopOrResumeTimer();
                //Checks if final split is better than PB and calls PB update
                if (segment.getHistoricTime(SegmentTypes.PERSONAL_BEST) != null
                        && endTime < segment.getHistoricTime(SegmentTypes.PERSONAL_BEST)) {
                    setPersonalBests();
                }
            }
            splitIndex = splitIndex + 1;
            scrollToCurrentSegment();
        }
    }

    public void resetTimer() {
        if (isTimerRunning) {
            stopOrResumeTimer();
        }
        splitTimer.reset();
        this.isReset = true;
        clearTimes();
        initializeTimes();
        splitIndex = 0;
    }
    //TODO: Create Undo Action
    //TODO: Create Skip Action

    //Display Methods
    private void scrollToCurrentSegment() {
        segmentTimesTable.getSelectionModel().select(splitIndex);
        int[] ints = TableViewHelper.getVisibleRows(segmentTimesTable);
        if (splitIndex >= ints[1] || splitIndex < ints[0]) {
            segmentTimesTable.scrollTo(splitIndex - (ints[1] - ints[0]) + 1);
        }
    }

    private String getTimeDifferenceDisplay(long time, long timeToDiff) {
        long difference = time - timeToDiff;
        if (difference == 0) {
            return "+" + TimeDisplayUtil.getDisplayTime(0);
        } else if (difference < 0) {
            return "-" + TimeDisplayUtil.getDisplayTime(Math.abs(difference));
        } else {
            return "+" + TimeDisplayUtil.getDisplayTime(difference);
        }
    }

    private void updateDisplayTimerText() {
        timerDisplay.setText(TimeDisplayUtil.getDisplayTime(splitTimer.getCurrentTime()));
    }

    private void clearTimes() {
        for (Segment segment : getSpeedrun().getSegments()) {
            segment.setEndedAt(0);
        }
    }

    private void incrementAttempt() {
        getSpeedrun().setAttempts(getSpeedrun().getAttempts() + 1);
        attemptCounter.setText(String.valueOf(getSpeedrun().getAttempts()));
    }

    private void updateSpeedrunDisplay() {
        gameDisplay.setText(getSpeedrun().getGame());
        categoryDisplay.setText(getSpeedrun().getCategory());
        attemptCounter.setText(String.valueOf(getSpeedrun().getAttempts()));
    }

    //Process History Tracking Methods
    private void setPersonalBests() {
        //TODO: Update to use History object - Move this code to the Segment entity?
        for (Segment segment : getSpeedrun().getSegments()) {
            segment.setHistoricTime(SegmentTypes.PREV_PB, segment.getHistoricTime(SegmentTypes.PERSONAL_BEST));
            segment.setHistoricTime(SegmentTypes.PERSONAL_BEST, segment.getEndedAt());
        }
    }

    private void undoPersonalBestUpdate() {
        for (Segment segment : getSpeedrun().getSegments()) {
            segment.setHistoricTime(SegmentTypes.PERSONAL_BEST, segment.getHistoricTime(SegmentTypes.PREV_PB));
        }
    }

    //Misc Controls
    public void showSplitManager(ActionEvent actionEvent) throws IOException {
        Parent splitManager = FXMLLoader.load(getClass().getResource("/fxml/SplitManager.fxml"));
        Scene splitManagerScene = new Scene(splitManager, 600, 600);
        Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

        window.setScene(splitManagerScene);
        window.show();
    }

    //Getters and Setters
    private Speedrun getSpeedrun() {
        return SpeedrunInstance.getInstance().getSpeedrun();
    }

    private long getSegmentDuration(int currentSegmentIndex, long segmentEndTime) {
        if (currentSegmentIndex == 0) {
            return segmentEndTime;
        } else {
            long prevSegmentEndTime = getSpeedrun().getSegments().get(currentSegmentIndex - 1).getEndedAt();
            return segmentEndTime - prevSegmentEndTime;
        }
    }
}
