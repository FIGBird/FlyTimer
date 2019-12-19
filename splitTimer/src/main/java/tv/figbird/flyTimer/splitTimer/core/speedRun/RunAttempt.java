package tv.figbird.flyTimer.splitTimer.core.speedRun;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;

public class RunAttempt implements Comparable<RunAttempt> {

    private ArrayList<Split> splits;
    private Logger logger = LogManager.getLogger(RunAttempt.class);
    private int totalSplits;

    public RunAttempt(int totalSplits) {
        this.totalSplits = totalSplits;
    }

    public void setSplit(int splitNumber, String splitName, long endTimeInMillis) {
        Split split = new Split(endTimeInMillis);
        if (splitName != null) {
            split.setSplitName(splitName);
        }
        setSplit(splitNumber, split);
    }

    public void setSplit(int splitNumber, Split split) {
        if (splitNumber > getTotalSplits() || splitNumber < 1) {
            IndexOutOfBoundsException exception = new IndexOutOfBoundsException(splitNumber + " is not a valid split number between 1 and " + getTotalSplits());
            logger.error(exception);
            throw exception;
        } else {
            getSplits().set(splitNumber - 1, split);
        }
    }

    private ArrayList<Split> getSplits() {
        if (splits == null) {
            this.splits = new ArrayList<>(Collections.nCopies(getTotalSplits(), null));
        }
        return splits;
    }

    public Split getSplit(int splitNumber) {
        if (splitNumber <= getSplits().size()) {
            return getSplits().get(splitNumber - 1);
        } else {
            return null;
        }
    }

    //This method is meant to get the number of splits which have data.
    public int getSplitCount() {
        int splitCount = 0;
        for (Split split : getSplits()) {
            if (split != null) {
                splitCount = splitCount + 1;
            }
        }
        return splitCount;
    }

    public int getTotalSplits() {
        return this.totalSplits;
    }

    public boolean isValidAttempt() {
        if (getSplitCount() == 0) {
            logger.info("Invalid RunAttempt - No Split data was found");
            return false;
        }
        if (getSplits().size() != getTotalSplits()) {
            logger.info("Invalid RunAttempt - Expected " + getTotalSplits() + " splits, but found " + getSplitCount());
            return false;
        }
        if (getSplit(getSplits().size()) == null) {
            logger.info("Invalid RunAttempt - The final split in the list is null");
            return false;
        }
        if (getSplits().stream().sorted(Split.getComparator()).collect(Collectors.toList()).equals(getSplits())) {
            logger.info("Valid RunAttempt - All splits are in ascending order based off of End Time");
            return true;
        } else {
            logger.info("Invalid RunAttempt - The splits are not in ascending order based off of End Time");
            return false;
        }
    }

    public long getTotalAttemptTime() {
        if (isValidAttempt()) {
            return getSplit(getTotalSplits()).getEndTime();
        } else {
            return -1;
        }
    }

    /**
     * This compare method will give the time in millis difference between the split in this attempt to the provided time.
     * Usage is meant for passing a current run time to see the saved time if a split is declared from the given time.
     * @param splitNumber This is the split number from this attempt.
     * @param timeInMillisToCompare The end time of a split or the current time in millis for comparason
     * @return The difference between the two end times as the given time reaches or exceeds the time saved on this attempt.
     */
    public Long compareSplit(int splitNumber, long timeInMillisToCompare) {
        if (splitNumber < 1) {
            IndexOutOfBoundsException exception = new IndexOutOfBoundsException(splitNumber + " is less than 1");
            logger.error(exception);
        }
        if (splitNumber > getTotalSplits()) {
            logger.info("Unable to compare to split " + splitNumber + " - split is greater than " + getTotalSplits());
            return null;
        }
        if (getSplit(splitNumber) == null) {
            logger.debug("No split data found for split " + splitNumber);
            return null;
        }
        return timeInMillisToCompare - getSplit(splitNumber).getEndTime();
    }

    /**
     * Compares this RunAttempt with the specified attempt for the purpose of determining order based on end time.
     * Note: this class has a natural ordering which is inconsistent with equals.
     * @param attempt The RunAttempt to be compared.
     * @return a negative integer, zero, or a positive integer as this RunAttempt is less than, equal to, or greater than the specified attempt based on end time.
     */
    @Override
    public int compareTo(RunAttempt attempt) {
        if (attempt == null) {
            IllegalArgumentException exception = new IllegalArgumentException("You may not compare a null RunAttempt");
            logger.error(exception);
            throw exception;
        }
        if (attempt.isValidAttempt() && this.isValidAttempt()) {
            return (int) (this.getTotalAttemptTime() - attempt.getTotalAttemptTime());
        } else {
            IllegalArgumentException exception = new IllegalArgumentException("One or both of the RunAttempts are invalid and unable to be compared");
            logger.error(exception);
            throw exception;
        }
    }
}
