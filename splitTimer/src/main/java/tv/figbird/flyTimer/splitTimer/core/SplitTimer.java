package tv.figbird.flyTimer.splitTimer.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tv.figbird.flyTimer.splitTimer.core.speedRun.RunAttempt;
import tv.figbird.flyTimer.splitTimer.core.speedRun.Speedrun;
import tv.figbird.flyTimer.splitTimer.core.speedRun.Split;
import tv.figbird.flyTimer.splitTimer.core.timer.Timer;

public class SplitTimer {

    private long splitStartTime;
    private int currentSplit;
    private boolean isRunning = false;
    private int totalSplits;
    private RunAttempt currentAttempt;
    private Speedrun currentSpeedrun;

    private Logger logger = LogManager.getLogger(SplitTimer.class);

    public SplitTimer() {

    }

    public SplitTimer(Speedrun currentSpeedrun) {
        this.currentSpeedrun = currentSpeedrun;
        setTotalSplits(currentSpeedrun.getTotalSplits());
    }

    public SplitTimer(int totalSplits) {
        setTotalSplits(totalSplits);
    }

    public void start() {
        resetTimer();
        clearSplits();
        Timer.start();
        setRunning(true);
    }

    public long split() {
        if (getCurrentSplit() <= getTotalSplits() && isRunning()) {
            long splitEndTime = Timer.getCurrentTime();
            long totalSplitTime = splitEndTime - splitStartTime;
            Split split = new Split(splitEndTime);
            split.setSplitTime(totalSplitTime);
            currentAttempt.setSplit(getCurrentSplit(), split);
            splitStartTime = splitEndTime;
            logger.info("Saving split " + getCurrentSplit() + " End Time: " + splitEndTime + " | Split Time: " + totalSplitTime);
            if (getCurrentSplit() == getTotalSplits()) {
                pause();
            }
            incrementCurrentSplit();
            return totalSplitTime;
        }
        logger.debug("Unable to split, timer is not running or there are no more splits in the current attempt.");
        return 0;
    }

    public void skipSplit() {
        if (getCurrentSplit() < getTotalSplits()) {
            logger.info("Skipping split number " + getCurrentSplit());
            incrementCurrentSplit();
        } else {
            logger.info("You may not skip split number " + getCurrentSplit() + " since it is the final split");
        }
    }

    public void pause() {
        Timer.stop();
        setRunning(false);
    }

    public void resume() {
        if (getCurrentSplit() <= getTotalSplits()) {
            Timer.start();
            setRunning(true);
        }
    }

    public void pauseOrResume() {
        if (isRunning()) {
            pause();
        } else {
            resume();
        }
    }

    //TODO: add skip split logic. This logic needs to be written with the compare logic for the GameManager

    public long getCurrentTime() {
        return Timer.getCurrentTime();
    }

    public long getCurrentSplitTime() {
        if (isRunning()) {
            return Timer.getCurrentTime() - splitStartTime;
        } else {
//            logger.debug("Timer is stopped, returning 0");
            return 0;
        }
    }

    public boolean isRunning() {
        return isRunning;
    }

    public int getTotalSplits() {
        if (totalSplits == 0) {
            return 1;
        } else {
            return this.totalSplits;
        }
    }

    public void setTotalSplits(int totalSplits) {
        this.totalSplits = totalSplits;
    }

//    private ArrayList<Split> getCurrentSplits() {
//        if (currentAttempt == null) {
//            currentAttempt = new ArrayList<>();
//        }
//        return currentAttempt;
//    }

//    public Speedrun getSpeedrunFromSplits() {
//        if (isRunning() || getCurrentSplits().size() == 0) {
//            //TODO: Add logger that this requires the run to finish and at least one split saved
//            return null;
//        } else {
//            return new Speedrun(getCurrentSplits());
//        }
//    }

    public void setCurrentSpeedrun(Speedrun speedrun) {
        this.currentSpeedrun = speedrun;
    }

    public int getCurrentSplit() {
        return this.currentSplit;
    }

    private void incrementCurrentSplit() {
        this.currentSplit = currentSplit + 1;
    }

    private void setRunning(boolean value) {
        this.isRunning = value;
        String state;
        if (isRunning()) {
            state = "running";
        } else {
            state = "stopped";
        }
        logger.debug("Split Timer is " + state);
    }

    private void resetTimer() {
        logger.debug("Resetting Split Timer");
        Timer.reset();
        this.splitStartTime = 0;
        this.currentSplit = 1;
    }

    private void clearSplits() {
        logger.debug("Clearing Splits");
        this.currentAttempt = new RunAttempt(getTotalSplits());
    }

    public void saveAttempt(String attemptName) {
        if (currentSpeedrun == null) {
            logger.warn("Unable to save " + attemptName + " - You may not save an attempt without a Speedrun to associate it with. ");
        } else {
            currentSpeedrun.saveAttempt(attemptName, currentAttempt);
        }
    }

}
