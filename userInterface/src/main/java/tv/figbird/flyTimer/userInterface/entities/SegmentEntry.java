package tv.figbird.flyTimer.userInterface.entities;

import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

public class SegmentEntry {

    private final SimpleStringProperty name;
    private final SimpleStringProperty currentTime;
    private final SimpleStringProperty pb;
    private final SimpleStringProperty best;

    private final SimpleLongProperty bestDuration;

    public SegmentEntry() {
        this.name = new SimpleStringProperty("");
        this.currentTime = new SimpleStringProperty("");
        this.pb = new SimpleStringProperty("");
        this.best = new SimpleStringProperty("");
        this.bestDuration = new SimpleLongProperty(0);
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getCurrentTime() {
        return currentTime.get();
    }

    public SimpleStringProperty currentTimeProperty() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime.set(currentTime);
    }

    public String getPb() {
        return pb.get();
    }

    public SimpleStringProperty pbProperty() {
        return pb;
    }

    public void setPb(String pb) {
        this.pb.set(pb);
    }

    public String getBest() {
        return best.get();
    }

    public SimpleStringProperty bestProperty() {
        return best;
    }

    public void setBest(String best) {
        this.best.set(best);
    }

    public SimpleLongProperty getBestDuration() {
        return bestDuration;
    }

    public void setBestDuration(long duration) {
        this.bestDuration.set(duration);
    }
}
