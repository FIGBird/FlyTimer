package tv.figbird.userInterface.helpers;

import javafx.stage.Stage;
import tv.figbird.userInterface.scene.MainScene;

import java.io.IOException;

public class DisplayManager {

    private final static DisplayManager instance = new DisplayManager();

    private Stage primaryStage;
    private MainScene mainScene;

    public static DisplayManager getInstance() {
        return instance;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public MainScene getMainScene() {
        if (mainScene == null) {
            try {
                mainScene = new MainScene(primaryStage, "FlyTimer", 400, 400);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return mainScene;
    }

}
