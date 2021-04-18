package tv.figbird.flyTimer.userInterface.controllers;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
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
import javafx.util.Duration;
import tv.figbird.flyTimer.splitTimer.core.entities.Segment;
import tv.figbird.flyTimer.splitTimer.core.entities.Speedrun;
import tv.figbird.flyTimer.splitTimer.core.timer.SplitTimer;
import tv.figbird.flyTimer.splitTimer.utilities.TimeDisplayUtil;
import tv.figbird.flyTimer.userInterface.context.SpeedrunInstance;
import tv.figbird.flyTimer.userInterface.entities.SegmentEntry;
import tv.figbird.flyTimer.userInterface.helpers.TableViewHelper;
import tv.figbird.flyTimer.userInterface.processors.SegmentEntryProcessor;

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
    private boolean hasDataToSave = false; //TODO: Hook this into save request on close/scene change

    //TODO: Update compares to handle -1 as new segments for the purpose of time. (Mostly needed for bestDuration)
    //TODO: Migrate all methods to call external methods for non UI processes
    //Start Up Methods
    //TODO: Add logging support (Will help debugging)
    public void initialize() {
        splitTimer = new SplitTimer();

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.millis(10),
                        e -> updateDisplayTimerText()));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    public void initializeTimes() {
        ObservableList<SegmentEntry> items =
                FXCollections.observableArrayList();
        segmentTimesTable.getItems().clear();
        for (Segment segment : getSpeedrun().getSegments()) {
            items.add(SegmentEntryProcessor.getEntryFromSegment(segment));
        }
        SegmentEntryProcessor.setSumOfBests(items);
        segmentTimesTable.getItems().addAll(items);
    }

    public void loadSpeedrun() {
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
            if (i == 1) {
                segment.setPersonalBest(-1, true);
            } else {
                segment.setPersonalBest(((i * 1000) + 500));
            }
            segment.setBestDuration(1000);
            speedrun.getSegments().add(segment);
            i = i + 1;
        }

        SpeedrunInstance.getInstance().setSpeedrun(speedrun);
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
        if (isTimerRunning && segmentIndex < segmentTimesTable.getItems().size()) {
            long segmentEndTime = splitTimer.getCurrentTime();
            SegmentEntry entry = segmentTimesTable.getItems().get(segmentIndex);
            long duration = getSegmentDuration(segmentIndex, segmentEndTime);
            if (SegmentEntryProcessor.processSplit(entry, segmentEndTime, duration)) {
                this.hasDataToSave = true;
            }
            if (segmentIndex == segmentTimesTable.getItems().size() - 1) {
                toggleTimer();
                if (SegmentEntryProcessor.processFinalSplit(segmentTimesTable.getItems(), segmentEndTime, getSpeedrun().getAttempts())) {
                    this.hasDataToSave = true;
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
        updateTimes();
        clearTimes();
        segmentIndex = 0;
    }

    private void updateTimes() {
        SegmentEntryProcessor.setSumOfBests(segmentTimesTable.getItems());
    }

    private void undoSplit() {
        //TODO: Create special handling for undo final split (Since timer auto pauses code needs to "resume without pause")
        if (segmentIndex > 0 && segmentIndex < segmentTimesTable.getItems().size()) {
            segmentIndex = segmentIndex - 1;
            SegmentEntryProcessor.processUndoSplit(segmentTimesTable.getItems().get(segmentIndex));
            scrollToCurrentSegment();
        }
    }

    private void skipSegment() {
        if (segmentIndex != segmentTimesTable.getItems().size() - 1) {
            SegmentEntryProcessor.processSkip(segmentTimesTable.getItems().get(segmentIndex));
            segmentIndex = segmentIndex + 1;
            scrollToCurrentSegment();
        }
    }

    private void scrollToCurrentSegment() {
        segmentTimesTable.getSelectionModel().select(segmentIndex);
        int[] ints = TableViewHelper.getVisibleRows(segmentTimesTable);
        if (segmentIndex >= ints[1] || segmentIndex < ints[0]) {
            segmentTimesTable.scrollTo(segmentIndex - (ints[1] - ints[0]));
        }
    }

    private void updateDisplayTimerText() {
        timerDisplay.setText(TimeDisplayUtil.getDisplayTime(splitTimer.getCurrentTime()));
    }

    private void clearTimes() {
        for (SegmentEntry entry : segmentTimesTable.getItems()) {
            SegmentEntryProcessor.clearTime(entry);
        }

    }

    private void incrementAttempt() {
        getSpeedrun().setAttempts(getSpeedrun().getAttempts() + 1);
        attemptCounter.setText(String.valueOf(getSpeedrun().getAttempts()));
    }

    public void updateSpeedrunDisplay() {
        gameDisplay.setText(getSpeedrun().getGame());
        categoryDisplay.setText(getSpeedrun().getCategory());
        attemptCounter.setText(String.valueOf(getSpeedrun().getAttempts()));
    }

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

    public void populateScene() {
        createKeybindListeners();
        initializeTimes();
        updateSpeedrunDisplay();
    }

    //Getters and Setters
    private Speedrun getSpeedrun() {
        return SpeedrunInstance.getInstance().getSpeedrun();
    }

    private long getSegmentDuration(int currentSegmentIndex, long segmentEndTime) {
        if (currentSegmentIndex == 0) {
            return segmentEndTime;
        } else {
            long prevSegmentEndTime = segmentTimesTable.getItems().get(currentSegmentIndex - 1).getSegment().getEndedAt();
            return segmentEndTime - prevSegmentEndTime;
        }
    }
}
