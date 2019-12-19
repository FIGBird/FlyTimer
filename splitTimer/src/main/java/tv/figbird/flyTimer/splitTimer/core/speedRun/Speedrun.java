package tv.figbird.flyTimer.splitTimer.core.speedRun;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;

public class Speedrun {

    private HashMap<String, RunAttempt> savedAttempts;
    private String categoryName;
    private int totalSplits;
    private long attemptCount;

    private Logger logger = LogManager.getLogger(Speedrun.class);


    public Speedrun(String categoryName, int totalSplits) {
        this.categoryName = categoryName;
        this.totalSplits = totalSplits;
    }

    public String getCategoryName() {
        return this.categoryName;
    }

    public int getTotalSplits() {
        if (this.totalSplits == 0) {
            return 1;
        } else {
            return this.totalSplits;
        }
    }

    public void saveAttempt(String attemptName, RunAttempt attempt) {
        if (attempt.isValidAttempt()) {
            getSavedAttempts().put(attemptName, attempt);
        } else {
            logger.error("Unable to Save Attempt - invalid attempt detected.");
        }
    }

    private HashMap<String, RunAttempt> getSavedAttempts() {
        if (savedAttempts == null) {
            this.savedAttempts = new HashMap<>();
        }
        return this.savedAttempts;
    }

    public RunAttempt getNewAttempt() {
        addAttempt();
        return new RunAttempt(getTotalSplits());
    }

    private void addAttempt() {
        this.attemptCount = attemptCount + 1;
    }

    public void setAttemptCount(long attemptCount) {
        if (attemptCount < 0) {
            logger.error("Invalid Attempt Count - Unable to set attemptCount to " + attemptCount + ", must be 0 or a positive value.");
        } else {
            this.attemptCount = attemptCount;
        }
    }

    public enum ReservedNames {
        PERSONAL_BEST("Personal Best"),
        WORLD_RECORD("World Record"),
        PERSONAL_WORST("Worst Time")
        ;

        public final String displayName;

        ReservedNames(String displayName) {
            this.displayName = displayName;
        }
    }
}
