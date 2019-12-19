package tv.figbird.flyTimer.userInterface.scene;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import tv.figbird.flyTimer.userInterface.controllers.AbstractController;

import java.io.IOException;

public abstract class AbstractScene {

    protected Stage stage;
    protected Scene scene;
    protected FXMLLoader fxmlLoader;

    public AbstractScene(String fxmlUrl, String sceneTitle, double width, double height) throws IOException {
        stage = new Stage();
        buildScene(fxmlUrl, sceneTitle, width, height);
    }

    public AbstractScene(Stage stage, String fxmlUrl, String sceneTitle, double width, double height) throws IOException {
        this.stage = stage;
        buildScene(fxmlUrl, sceneTitle, width, height);
    }

    protected void buildScene(String fxmlUrl, String sceneTitle, double width, double height) throws IOException {
        this.fxmlLoader = new FXMLLoader(getClass().getResource(fxmlUrl));
        Parent root = getFxmlLoader().load();
        this.scene = new Scene(root, width, height);
        getStage().setTitle(sceneTitle);
        getStage().setScene(getScene());
    }

    public void show() {
        getStage().show();
        getStage().setOnCloseRequest((WindowEvent event) -> close());
    }

    public void close() {
        if (getStage().isShowing()) {
            getStage().close();
        }
    }

    protected FXMLLoader getFxmlLoader() {
        return this.fxmlLoader;
    }

    public Stage getStage() {
        return this.stage;
    }

    protected Scene getScene() {
        return this.scene;
    }

    public abstract AbstractController getController();
}
