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
    public Button startPause;
    public Label timerDisplay;
    public Button resetButton;
    public Label gameDisplay;
    public Label categoryDisplay;
    public Label attemptCounter;
    public Button splitManagerButton;
    public Button splitButton;
    public TableView splitTimesTable;


    //Other Variables
    private SplitTimer splitTimer;
    private boolean isTimerRunning = false;
    private boolean isReset = true;
    private int splitIndex;

    public void initialize() {
        loadSpeedrun();
        splitTimer = new SplitTimer();
        ThreadHelper.getTimedDeamon(this::updateDisplayTimerText, 10).start();
        initializeTimes();
    }

    public void startOrResumeTimer() {
        //TODO: Handle when run is complete
        if (isTimerRunning) {
            splitTimer.stop();
            startPause.setText("Start");
            isTimerRunning = false;
        } else {
            splitTimer.start();
            startPause.setText("Stop");
            isTimerRunning = true;
        }
        if (isReset) {
            incrementAttempt();
            this.isReset = false;
            splitIndex = 0;
            splitTimesTable.getSelectionModel().select(splitIndex);
            splitTimesTable.scrollTo(splitIndex);
        }

    }

    public void split() {
        if (isTimerRunning) {
            long endTime = splitTimer.getCurrentTime();
            Segment segment = getSpeedrun().getSegments().get(splitIndex);
            SegmentEntry entry = (SegmentEntry) splitTimesTable.getItems().get(splitIndex);
            segment.setEndedAt(endTime);
            entry.setCurrentTime(TimeDisplayUtil.getDisplayTime(endTime));

            long prevEndAt = 0;
            if (splitIndex > 0) {
                prevEndAt = getSpeedrun().getSegments().get(splitIndex - 1).getEndedAt();
            }
            long duration = segment.getEndedAt() - prevEndAt;
            long bestDiff = duration - segment.getBestDuration();
            if (duration < segment.getBestDuration()) {
                entry.setBest("-" + TimeDisplayUtil.getDisplayTime(Math.abs(bestDiff)));
                segment.setBestDuration(duration);
            } else {
                entry.setBest("+" + TimeDisplayUtil.getDisplayTime(bestDiff));
            }

            //PB compare
            if (segment.getHistoricTime(SegmentTypes.PERSONAL_BEST) == null) {
                entry.setPb(TimeDisplayUtil.getDisplayTime(0));
            } else {
                long pb = segment.getHistoricTime(SegmentTypes.PERSONAL_BEST);
                long pbDiff = endTime - pb;
                if (pbDiff < 0) {
                    entry.setPb("-" + TimeDisplayUtil.getDisplayTime(Math.abs(pbDiff)));
                } else {
                    entry.setPb("+" + TimeDisplayUtil.getDisplayTime(pbDiff));
                }
            }

            if (splitIndex == getSpeedrun().getSegments().size() - 1) {
                startOrResumeTimer();
                //Checks if final split is better than PB and calls PB update
                if (segment.getHistoricTime(SegmentTypes.PERSONAL_BEST) != null
                        && endTime < segment.getHistoricTime(SegmentTypes.PERSONAL_BEST)) {
                    setPersonalBests();

                }
            }
            splitIndex = splitIndex + 1;
            splitTimesTable.getSelectionModel().select(splitIndex);
            int[] ints = TableViewHelper.getVisibleRows(splitTimesTable);
            if (splitIndex >= ints[1] || splitIndex < ints[0]) {
                splitTimesTable.scrollTo(splitIndex - (ints[1] - ints[0]) + 1);
            }
        }

    }


    public void initializeTimes() {
        ObservableList<SegmentEntry> items =
                FXCollections.observableArrayList();
        splitTimesTable.getItems().clear();
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
        splitTimesTable.getItems().addAll(items);

    }

    public void resetTimer() {
        if (isTimerRunning) {
            startOrResumeTimer();
        }
        splitTimer.reset();
        this.isReset = true;
        clearTimes();
        initializeTimes();
    }

    private void updateDisplayTimerText() {
        timerDisplay.setText(TimeDisplayUtil.getDisplayTime(splitTimer.getCurrentTime()));
    }

    private void clearTimes() {
        for (Segment segment : getSpeedrun().getSegments()) {
            segment.setEndedAt(0);
        }
    }

    public void showSplitManager(ActionEvent actionEvent) throws IOException {
        Parent splitManager = FXMLLoader.load(getClass().getResource("/fxml/SplitManager.fxml"));
        Scene splitManagerScene = new Scene(splitManager, 600, 600);
        Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

        window.setScene(splitManagerScene);
        window.show();
    }

    private void incrementAttempt() {
        getSpeedrun().setAttempts(getSpeedrun().getAttempts() + 1);
        attemptCounter.setText(String.valueOf(getSpeedrun().getAttempts()));
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

    private Speedrun getSpeedrun() {
        return SpeedrunInstance.getInstance().getSpeedrun();
    }

    private void updateSpeedrunDisplay() {
        gameDisplay.setText(getSpeedrun().getGame());
        categoryDisplay.setText(getSpeedrun().getCategory());
        attemptCounter.setText(String.valueOf(getSpeedrun().getAttempts()));
    }

    private void setPersonalBests() {
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
}
