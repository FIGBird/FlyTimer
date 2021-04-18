package tv.figbird.flyTimer.userInterface.entities;

import javafx.beans.property.SimpleStringProperty;

public class SpeedrunEntry {

    private final SimpleStringProperty gameName;
    private final SimpleStringProperty category;

    SpeedrunEntry() {
        this.gameName = new SimpleStringProperty("");
        this.category = new SimpleStringProperty("Any&");
    }

    public String getGameName() {
        return gameName.get();
    }

    public SimpleStringProperty gameNameProperty() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName.set(gameName);
    }

    public String getCategory() {
        return category.get();
    }

    public SimpleStringProperty categoryProperty() {
        return category;
    }

    public void setCategory(String category) {
        this.category.set(category);
    }
}
