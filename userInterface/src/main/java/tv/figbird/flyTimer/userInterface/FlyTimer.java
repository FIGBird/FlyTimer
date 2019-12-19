package tv.figbird.flyTimer.userInterface;

import javafx.application.Application;
import javafx.stage.Stage;
import tv.figbird.flyTimer.userInterface.helpers.DisplayManager;

public class FlyTimer extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception {
        DisplayManager.getInstance().setPrimaryStage(primaryStage);
        DisplayManager.getInstance().getMainScene().getController().setTotalSplits(6);
        DisplayManager.getInstance().getMainScene().show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
