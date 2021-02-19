package tv.figbird.flyTimer.splitTimer.core.entities;

public class SplitTime {

    private long splitTime = -1;
    private boolean isSkipped = true;

    public SplitTime() {
    }

    public SplitTime(long splitTime) {
        this.splitTime = splitTime;
        this.isSkipped = false;
    }

    public long getSplitTime() {
        return splitTime;
    }

    public void setSplitTime(long splitTime) {
        this.splitTime = splitTime;
    }

    public boolean isSkipped() {
        return isSkipped;
    }

    public void setSkipped(boolean skipped) {
        isSkipped = skipped;
    }
}
