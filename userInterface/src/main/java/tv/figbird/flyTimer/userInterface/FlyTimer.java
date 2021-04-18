package tv.figbird.flyTimer.userInterface;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import tv.figbird.flyTimer.userInterface.controllers.MainController;

public class FlyTimer extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Main.fxml"));
        Parent root = loader.load();
        Scene mainScene = new Scene(root, 400, 600);
        MainController mainController = loader.getController();
        mainController.loadSpeedrun();
        mainController.populateScene();
        primaryStage.setTitle("Timer");
        primaryStage.setScene(mainScene);
        primaryStage.show();


    }

    public static void main(String[] args) {
        launch(args);
    }
}