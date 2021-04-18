package tv.figbird.flyTimer.userInterface.entities;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

public class SegmentManagerEntry {

    private final SimpleStringProperty segmentName;
    private final SimpleStringProperty personalBest;
    private final SimpleStringProperty bestDuration;
    private final SimpleBooleanProperty isSkipped;


    public SegmentManagerEntry() {
        this.segmentName = new SimpleStringProperty("");
        this.bestDuration = new SimpleStringProperty();
        this.personalBest = new SimpleStringProperty();
        this.isSkipped = new SimpleBooleanProperty(false);
    }

    public String getSegmentName() {
        return segmentName.get();
    }

    public SimpleStringProperty segmentNameProperty() {
        return segmentName;
    }

    public void setSegmentName(String segmentName) {
        this.segmentName.set(segmentName);
    }

    public String getPersonalBest() {
        return personalBest.get();
    }

    public SimpleStringProperty personalBestProperty() {
        return personalBest;
    }

    public void setPersonalBest(String personalBest) {
        this.personalBest.set(personalBest);
    }

    public String getBestDuration() {
        return bestDuration.get();
    }

    public SimpleStringProperty bestDurationProperty() {
        return bestDuration;
    }

    public void setBestDuration(String bestDuration) {
        this.bestDuration.set(bestDuration);
    }

    public boolean isIsSkipped() {
        return isSkipped.get();
    }

    public SimpleBooleanProperty isSkippedProperty() {
        return isSkipped;
    }

    public void setIsSkipped(boolean isSkipped) {
        this.isSkipped.set(isSkipped);
    }
}
