package tv.figbird.flyTimer.splitTimer.core.entities;

import java.util.HashMap;

public class Split {

    private Split nextSplit;
    private Split previousSplit;
    private HashMap<String, SplitTime> savedTimes;
    private String splitName = "";

    public Split getNextSplit() {
        return nextSplit;
    }

    public void setNextSplit(Split nextSplit) {
        if (nextSplit != null && nextSplit.equals(this)) {
            throw new IllegalArgumentException("Error setting next split on " + this.getName() + " - Next split cannot be set to itself.");
        }
        this.nextSplit = nextSplit;
    }

    public Split getPreviousSplit() {
        return previousSplit;
    }

    public void setPreviousSplit(Split previousSplit) {
        if (previousSplit != null && previousSplit.equals(this)) {
            throw new IllegalArgumentException("Error setting previous split on " + this.getName() + " - Previous split cannot be set to itself.");
        }
        this.previousSplit = previousSplit;
    }

    public boolean hasNext() {
        return nextSplit != null;
    }

    public boolean hasPrev() {
        return previousSplit != null;
    }

    public HashMap<String, SplitTime> getSavedTimes() {
        if (savedTimes == null) {
            savedTimes = new HashMap<>();
        }
        return savedTimes;
    }

    public String getName()  {
        return this.splitName;
    }

    public void setName(String splitName) {
        this.splitName = splitName;
    }

    public void saveTime(String timeName, SplitTime timeToSave) {
        getSavedTimes().put(timeName, timeToSave);
    }

    public SplitTime getSavedTime(String timeName) {
        return getSavedTimes().get(timeName);
    }

}
