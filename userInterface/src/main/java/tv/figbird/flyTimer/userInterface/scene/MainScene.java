package tv.figbird.flyTimer.userInterface.scene;

import javafx.stage.Stage;
import tv.figbird.flyTimer.userInterface.controllers.MainController;

import java.io.IOException;

public class MainScene extends AbstractScene {

    public MainScene(Stage stage, String sceneTitle, double width, double height) throws IOException {
        super(stage, "/fxml/Main.fxml", sceneTitle, width, height);
    }

    @Override
    public MainController getController() {
        return getFxmlLoader().getController();
    }
}
