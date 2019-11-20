package tv.figbird.splitTimer.core.speedRun;

import java.util.HashMap;

public class SpeedGame {

    private HashMap<String, SpeedRun> savedRuns;
    private int totalSplits;
    private String gameName;

    public void putSpeedRun(String runName, SpeedRun speedRun) {
        getSavedRuns().put(runName, speedRun);
    }

    public void getSpeedRun(String runName) {
        getSavedRuns().get(runName);
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    protected HashMap<String, SpeedRun> getSavedRuns() {
        if (savedRuns == null) {
            savedRuns = new HashMap<>();
        }
        return savedRuns;
    }

    public int getTotalSplits() {
        return this.totalSplits;
    }

    public void setTotalSplits(int totalSplits) {
        this.totalSplits = totalSplits;
    }
}
