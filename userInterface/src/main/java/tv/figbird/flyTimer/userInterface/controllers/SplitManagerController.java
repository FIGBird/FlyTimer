package tv.figbird.flyTimer.userInterface.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.io.IOException;

public class SplitManagerController {

    public Button mainButton;
    public TableView splitTable;

    //Variables
    boolean hasSaved = false;

    public void initialize() {

    }

    public void showMain(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Main.fxml"));
        Parent main = loader.load();
        MainController ctrl = loader.getController();
        ctrl.initializeTimes();
        Scene mainScene = new Scene(main, 400, 600);
        Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

        window.setScene(mainScene);
        window.show();
    }
}
