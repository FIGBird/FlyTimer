package tv.figbird.flyTimer.splitTimer.core.entities;

import java.util.HashMap;

public class Segment {

    private String name;

    private long endedAt;
    private long bestDuration;

    private boolean isSkipped = false;
    private boolean isReset = false;

    private HashMap<String, History> histories;

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

    public HashMap<String, History> getHistories() {
        if (histories == null) {
            histories = new HashMap<>();
        }
        return histories;
    }

    public Long getHistoricTime(String historyKey) {
        if (getHistories().get(historyKey) == null) {
            return null;
        } else {
            return getHistories().get(historyKey).getEndedAt();
        }
    }

    public void setHistoricTime(String historyKey, long endedAt, int attemptNumber) {
        setHistoricTime(historyKey, endedAt, attemptNumber, false);
    }

    public void setHistoricTime(String historyKey, long endedAt) {
        setHistoricTime(historyKey, endedAt, -1, false);
    }

    public void setHistoricTime(String historyKey, long endedAt, int attemptNumber, boolean isSkipped) {
        if (getHistoricTime(historyKey) == null) {
            History history = new History();
            history.setEndedAt(endedAt);
            history.setAttemptNumber(attemptNumber);
            history.setSkipped(isSkipped);
            getHistories().put(historyKey, history);
        } else {
            getHistories().get(historyKey).setEndedAt(endedAt);
            getHistories().get(historyKey).setAttemptNumber(attemptNumber);
            getHistories().get(historyKey).setSkipped(isSkipped);
        }
    }

    //TODO: build history management
}
