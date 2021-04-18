package tv.figbird.flyTimer.userInterface.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import tv.figbird.flyTimer.splitTimer.core.entities.Segment;
import tv.figbird.flyTimer.splitTimer.core.entities.Speedrun;
import tv.figbird.flyTimer.userInterface.context.SpeedrunInstance;
import tv.figbird.flyTimer.userInterface.entities.SegmentManagerEntry;
import tv.figbird.flyTimer.userInterface.processors.SegmentManagerEntryProcessor;

import java.io.IOException;

import static tv.figbird.flyTimer.userInterface.processors.SegmentManagerEntryProcessor.getSegmentManagerEntryFromSegment;
import static tv.figbird.flyTimer.userInterface.processors.SegmentManagerEntryProcessor.processStringInputForTimes;

public class SegmentManagerController {

    public Button mainButton;
    public TableView<SegmentManagerEntry> segmentTable;
    public TextField gameNameField;
    public TextField categoryField;
    public TableColumn<SegmentManagerEntry, String> segmentNameColumn;
    public TableColumn<SegmentManagerEntry, String> personalBestColumn;
    public TableColumn<SegmentManagerEntry, String> bestDurationColumn;
    public TableColumn<SegmentManagerEntry, Boolean> isSkippedColumn;

    public void initialize() {
        initTable();
        gameNameField.setText(getSpeedrun().getGame());
        categoryField.setText(getSpeedrun().getCategory());
        setColumnsToEdit();
    }

    private void setColumnsToEdit() {
        segmentNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        segmentNameColumn.setOnEditCommit(t ->
                t.getTableView().getItems().get(t.getTablePosition().getRow()).setSegmentName(t.getNewValue()));

        personalBestColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        personalBestColumn.setOnEditCommit(t ->
                t.getTableView().getItems().get(t.getTablePosition().getRow()).setPersonalBest(processStringInputForTimes(t.getNewValue())));

        bestDurationColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        bestDurationColumn.setOnEditCommit(t ->
                t.getTableView().getItems().get(t.getTablePosition().getRow()).setBestDuration(processStringInputForTimes(t.getNewValue())));

        isSkippedColumn.setCellFactory(tc -> new CheckBoxTableCell<>());
    }


    private void initTable() {
        ObservableList<SegmentManagerEntry> items =
                FXCollections.observableArrayList();
        segmentTable.getItems().clear();
        for (Segment segment : getSpeedrun().getSegments()) {
            items.add(getSegmentManagerEntryFromSegment(segment));
        }
        segmentTable.getItems().addAll(items);
    }

    public void addSegment() {
        segmentTable.getItems().add(getSegmentManagerEntryFromSegment(new Segment()));
    }

    public void removeSegment() {
        int indexToRemove = segmentTable.getSelectionModel().getSelectedIndex();
        if (indexToRemove >= 0 && indexToRemove < segmentTable.getItems().size()) {
            segmentTable.getItems().remove(indexToRemove);
            segmentTable.getSelectionModel().select(indexToRemove);
        }
    }

    public void moveSegmentUp() {
        int indexToMove = segmentTable.getSelectionModel().getSelectedIndex();
        if (indexToMove > 0 && indexToMove < segmentTable.getItems().size()) {
            moveSegment(indexToMove, indexToMove - 1);
        }
    }

    public void moveSegmentDown() {
        int indexToMove = segmentTable.getSelectionModel().getSelectedIndex();
        if (indexToMove >= 0 && indexToMove < segmentTable.getItems().size() - 1) {
            moveSegment(indexToMove, indexToMove + 1);
        }
    }

    private void moveSegment(int indexToMove, int indexToMoveTo) {
        SegmentManagerEntry entryToMove = segmentTable.getItems().get(indexToMove);
        segmentTable.getItems().remove(indexToMove);
        segmentTable.getItems().add(indexToMoveTo, entryToMove);
        segmentTable.getSelectionModel().select(indexToMoveTo);
    }

    public void newSpeedrun() {
        SpeedrunInstance.getInstance().createNewSpeedrun();
        initTable();
    }

    public void save() {
        getSpeedrun().setCategory(categoryField.getText());
        getSpeedrun().setGame(gameNameField.getText());
        getSpeedrun().setSegments(SegmentManagerEntryProcessor.getSegmentsFromEntries(segmentTable.getItems()));
    }

    private Speedrun getSpeedrun() {
        return SpeedrunInstance.getInstance().getSpeedrun();
    }

    public void showMain(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Main.fxml"));
        Parent main = loader.load();
        MainController ctrl = loader.getController();
        Scene mainScene = new Scene(main, 400, 600);
        Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        ctrl.populateScene();

        window.setScene(mainScene);
        window.show();
    }
}
