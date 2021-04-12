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
import javafx.scene.input.KeyEvent;
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

import static tv.figbird.flyTimer.userInterface.context.KeyBinds.getBind;

public class MainController {

    //FX Variables
    public Button toggleTimerButton;
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
    private int segmentIndex;
    private boolean isStartOrSplit = true; //TODO: Move this to Config Context

    //Start Up Methods
    //TODO: Add logging support (Will help debugging)
    public void initialize() {
        loadSpeedrun();
        splitTimer = new SplitTimer();
        ThreadHelper.getTimedDeamon(this::updateDisplayTimerText, 10).start();
        initializeTimes();
    }

    void initializeTimes() {
        ObservableList<SegmentEntry> items =
                FXCollections.observableArrayList();
        segmentTimesTable.getItems().clear();
        long sumOfBest = 0;

        for (Segment segment : getSpeedrun().getSegments()) {
            SegmentEntry entry = new SegmentEntry();

            entry.setName(segment.getName());
            setSegmentEntryPbToTime(segment, entry);
            sumOfBest = sumOfBest + segment.getBestDuration();
            entry.setBest(TimeDisplayUtil.getDisplayTime(sumOfBest));
            entry.setBestDuration(sumOfBest);
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
    public void toggleTimer() {
        if (segmentIndex < getSpeedrun().getSegments().size()) {
            if (isTimerRunning) {
                splitTimer.stop();
                toggleTimerButton.setText("Start");
                isTimerRunning = false;
            } else {
                splitTimer.start();
                toggleTimerButton.setText("Stop");
                isTimerRunning = true;
            }
            if (isReset) {
                incrementAttempt();
                this.isReset = false;
                segmentIndex = 0;
                segmentTimesTable.getSelectionModel().select(segmentIndex);
                segmentTimesTable.scrollTo(segmentIndex);
            }
        }
    }

    public void split() {
        if (isTimerRunning && segmentIndex < getSpeedrun().getSegments().size()) {
            long endTime = splitTimer.getCurrentTime();
            Segment segment = getSpeedrun().getSegments().get(segmentIndex);
            SegmentEntry entry = segmentTimesTable.getItems().get(segmentIndex);

            segment.setEndedAt(endTime);
            entry.setCurrentTime(TimeDisplayUtil.getDisplayTime(endTime));

            long duration = getSegmentDuration(segmentIndex, endTime);
            entry.setBest(getTimeDifferenceDisplay(duration, segment.getBestDuration()));
            if (duration < segment.getBestDuration()) {
                segment.setBestDuration(duration);
            }

            if (segment.getHistoricTime(SegmentTypes.PERSONAL_BEST) == null) {
                entry.setPb(TimeDisplayUtil.getDisplayTime(0));
            } else {
                entry.setPb(getTimeDifferenceDisplay(endTime, segment.getHistoricTime(SegmentTypes.PERSONAL_BEST)));
            }

            //TODO: Enhance this code and/or split into new method
            if (segmentIndex == getSpeedrun().getSegments().size() - 1) {
                toggleTimer();
                //Checks if final split is better than PB and calls PB update
                if (segment.getHistoricTime(SegmentTypes.PERSONAL_BEST) != null
                        && endTime < segment.getHistoricTime(SegmentTypes.PERSONAL_BEST)) {
                    setPersonalBests();
                }
            }
            segmentIndex = segmentIndex + 1;
            scrollToCurrentSegment();
        }
    }

    private void startOrSplit() {
        if (isTimerRunning) {
            split();
        } else {
            toggleTimer();
        }
    }

    public void resetTimer() {
        if (isTimerRunning) {
            toggleTimer();
        }
        splitTimer.reset();
        this.isReset = true;
        clearTimes();
        initializeTimes();
        segmentIndex = 0;
    }

    private void undoSplit() {
        //TODO: Create special handling for undo final split (Since timer auto pauses code needs to "resume without pause")
        if (segmentIndex > 0 && segmentIndex < getSpeedrun().getSegments().size()) {
            Segment segment = getSpeedrun().getSegments().get(segmentIndex - 1);
            SegmentEntry entry = segmentTimesTable.getItems().get(segmentIndex - 1);
            long bestDuration = entry.getBestDuration().getValue();

            segment.setEndedAt(0);
            segment.setBestDuration(bestDuration);
            segment.setSkipped(false);
            entry.setCurrentTime("");
            entry.setBest(TimeDisplayUtil.getDisplayTime(bestDuration));

            setSegmentEntryPbToTime(segment, entry);
            segmentIndex = segmentIndex - 1;
            scrollToCurrentSegment();
        }
    }

    private void skipSegment() {
        if (segmentIndex != getSpeedrun().getSegments().size() - 1) {
            getSpeedrun().getSegments().get(segmentIndex).setSkipped(true);
            SegmentEntry entry = segmentTimesTable.getItems().get(segmentIndex);
            entry.setCurrentTime("--");
            entry.setPb("--");
            entry.setBest("--");
            segmentIndex = segmentIndex + 1;
            scrollToCurrentSegment();
        }
    }

    //Display Methods
    private void setSegmentEntryPbToTime(Segment segment, SegmentEntry entry) {
        if (segment.getHistories().get(SegmentTypes.PERSONAL_BEST) != null) {
            entry.setPb(TimeDisplayUtil.getDisplayTime(segment.getHistories().get(SegmentTypes.PERSONAL_BEST).getEndedAt()));
        }
    }

    private void scrollToCurrentSegment() {
        segmentTimesTable.getSelectionModel().select(segmentIndex);
        int[] ints = TableViewHelper.getVisibleRows(segmentTimesTable);
        if (segmentIndex >= ints[1] || segmentIndex < ints[0]) {
            segmentTimesTable.scrollTo(segmentIndex - (ints[1] - ints[0]));
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
        Parent splitManager = FXMLLoader.load(getClass().getResource("/fxml/SegmentManager.fxml"));
        Scene splitManagerScene = new Scene(splitManager, 600, 600);
        Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

        window.setScene(splitManagerScene);
        window.show();
    }

    public void createKeybindListeners() {
        Scene scene = timerDisplay.getScene();
        scene.setOnKeyPressed(this::handleKeybindings);
    }

    private void handleKeybindings(KeyEvent event) {
        switch (getBind(event.getCode())) {
            case SPLIT:
                if (isStartOrSplit) {
                    startOrSplit();
                } else {
                    split();
                }
                break;
            case RESET:
                resetTimer();
                break;
            case TOGGLE_TIMER:
                toggleTimer();
                break;
            case UNDO:
                undoSplit();
                break;
            case SKIP_SEGMENT:
                skipSegment();
                break;
        }
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
