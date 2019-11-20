package tv.figbird.splitTimer.core.speedRun;

public class Split {
    private long timeInMillis;

    public Split(long timeInMillis) {
        this.timeInMillis = timeInMillis;
    }

    public long getSplitTime() {
        return timeInMillis;
    }

}
