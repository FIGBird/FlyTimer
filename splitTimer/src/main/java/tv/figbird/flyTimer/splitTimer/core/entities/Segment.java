package tv.figbird.flyTimer.splitTimer.core.entities;

public class Segment {

    private String name;

    private long endedAt = -1;
    private long bestDuration = -1;

    private boolean isSkipped = false;
    private boolean isReset = false;

    private History personalBest;

    private History prevPersonalBest;
    private long prevBestDuration = -1;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getEndedAt() {
        return endedAt;
    }

    public void setEndedAt(long endedAt) {
        this.endedAt = endedAt;
    }

    public long getBestDuration() {
        return bestDuration;
    }

    public void setBestDuration(long bestDuration) {
        this.bestDuration = bestDuration;
    }

    public boolean isSkipped() {
        return isSkipped;
    }

    public void setSkipped(boolean skipped) {
        isSkipped = skipped;
    }

    public boolean isReset() {
        return isReset;
    }

    public void setReset(boolean reset) {
        isReset = reset;
    }

    public History getPersonalBest() {
        return personalBest;
    }

    public void setPersonalBest(History personalBest) {
        this.personalBest = personalBest;
    }

    public void setPersonalBest(int attempt, long endedAt) {
        History pb = new History();
        pb.setEndedAt(endedAt);
        pb.setAttemptNumber(attempt);
        setPersonalBest(pb);
    }

    public void setPersonalBest(int attempt, boolean isSkipped) {
        History pb = new History();
        pb.setAttemptNumber(attempt);
        pb.setSkipped(isSkipped);
        setPersonalBest(pb);
    }

    public void setPersonalBest(long endedAt) {
        setPersonalBest(-1, endedAt);
    }

    public void setPrevPersonalBest(History pb) {
        this.prevPersonalBest = pb;
    }

    public History getPrevPersonalBest() {
        return this.prevPersonalBest;
    }

    public long getPrevBestDuration() {
        return prevBestDuration;
    }

    public void setPrevBestDuration(long prevBestDuration) {
        this.prevBestDuration = prevBestDuration;
    }
}
