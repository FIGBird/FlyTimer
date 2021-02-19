package tv.figbird.flyTimer.splitTimer.core.managers;


import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tv.figbird.flyTimer.splitTimer.core.entities.Split;

import java.util.ArrayList;

public class SplitManager {

    private ArrayList<Split> splits;
    private Split firstSplit;

    private static Logger logger = LogManager.getLogger(SplitManager.class);


    public Split getSplit(int index) {
        isValidIndex(index);
        return getSplits().get(index);
    }


    public void insertSplit(Split splitToInsert, int indexToInsert) {
        if (getSplits().isEmpty()) {
            logger.log(Level.INFO, "Split list is empty, setting index to 0.");
            indexToInsert = 0;
        } else {
            isValidIndex(indexToInsert);
        }
        hasSplit(splitToInsert);

        if (indexToInsert == 0) {
            setFirstSplit(splitToInsert);
        }
        if (indexToInsert < getSplits().size()) {
            Split splitAtIndex = getSplit(indexToInsert);
            splitToInsert.setNextSplit(splitAtIndex);
            splitToInsert.setPreviousSplit(splitAtIndex.getPreviousSplit());
            splitAtIndex.setPreviousSplit(splitToInsert);

            if (splitToInsert.getPreviousSplit() != null) {
                splitToInsert.getPreviousSplit().setNextSplit(splitToInsert);
            }
        }

        getSplits().add(splitToInsert);
        sortSplits();
    }

    public void addSplit(Split splitToAdd) {
        hasSplit(splitToAdd);
        logger.log(Level.INFO, "Adding split - " + splitToAdd.getName());
        if (getSplits().isEmpty()) {
            getSplits().add(splitToAdd);
            setFirstSplit(splitToAdd);
        } else {
            Split last = getSplits().get(getSplits().size() - 1);
            splitToAdd.setPreviousSplit(last);
            last.setNextSplit(splitToAdd);
            getSplits().add(splitToAdd);
        }
    }

    public void deleteSplit(Split splitToDelete) {
        logger.log(Level.INFO, "Deleting split - " + splitToDelete.getName());
        Split nextSplit = splitToDelete.getNextSplit();
        Split prevSplit = splitToDelete.getPreviousSplit();

        if (nextSplit == null) {
            logger.log(Level.INFO, splitToDelete.getName() + " does not have a next split.");
        } else {
            nextSplit.setPreviousSplit(prevSplit);
        }
        if (prevSplit == null) {
            logger.log(Level.INFO, splitToDelete.getName() + " does not have a previous split.");
        } else {
            prevSplit.setNextSplit(nextSplit);
        }
    }

    protected void setSplits(ArrayList<Split> splits) {
        this.splits = splits;
    }

    protected void sortSplits() {
        logger.log(Level.INFO, "Sorting Split List");
        if (getSplits().isEmpty()) {
            logger.log(Level.INFO, "Split List is empty");
        } else {
            Split currentSplit = this.firstSplit;
            getSplits().clear();

            while(currentSplit.hasNext()) {
                getSplits().add(currentSplit);
                currentSplit = currentSplit.getNextSplit();
            }
            getSplits().add(currentSplit);
        }
    }

    protected void setFirstSplit(Split firstSplit) {
        logger.log(Level.INFO, "Setting first split to " + firstSplit.getName());
        this.firstSplit = firstSplit;
    }

    protected Split getFirstSplit() {
        return this.firstSplit;
    }

    private ArrayList<Split> getSplits() {
        if (this.splits == null) {
            this.splits = new ArrayList<>();
        }
        return this.splits;
    }

    private void hasSplit(Split split) {
        if (getSplits().contains(split)) {
            String message = "Error adding split - " + split.getName() + " is already contained in the split list.";
            logger.log(Level.ERROR, message);
            throw new IllegalArgumentException(message);
        }
    }

    private void isValidIndex(int index) {
        if (index >= getSplits().size()) {
            String message = "Requested Index out of bounds - " + index + " is greater than or equal to list size of " + getSplits().size();
            logger.log(Level.ERROR, message);
            throw new IndexOutOfBoundsException(message);
        }
    }

}
