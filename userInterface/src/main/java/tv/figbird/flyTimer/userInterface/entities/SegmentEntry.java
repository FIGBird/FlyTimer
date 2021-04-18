package tv.figbird.flyTimer.userInterface.entities;

import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import tv.figbird.flyTimer.splitTimer.core.entities.Segment;

public class SegmentEntry {

    private final SimpleStringProperty name;
    private final SimpleStringProperty currentTime;
    private final SimpleStringProperty pb;
    private final SimpleStringProperty best;

    private final SimpleLongProperty sumOfBest;
    private final SimpleLongProperty duration;

    private Segment segment;

    public SegmentEntry() {
        this.name = new SimpleStringProperty("");
        this.currentTime = new SimpleStringProperty("");
        this.pb = new SimpleStringProperty("");
        this.best = new SimpleStringProperty("");
        this.sumOfBest = new SimpleLongProperty(-1);
        this.duration = new SimpleLongProperty(-1);
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

    public Segment getSegment() {
        return segment;
    }

    public void setSegment(Segment segment) {
        this.segment = segment;
    }

    public long getSumOfBest() {
        return sumOfBest.get();
    }

    public SimpleLongProperty sumOfBestProperty() {
        return sumOfBest;
    }

    public void setSumOfBest(long sumOfBest) {
        this.sumOfBest.set(sumOfBest);
    }

    public long getDuration() {
        return duration.get();
    }

    public SimpleLongProperty durationProperty() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration.set(duration);
    }
}
