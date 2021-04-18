package tv.figbird.flyTimer.userInterface.processors;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tv.figbird.flyTimer.splitTimer.core.entities.Segment;
import tv.figbird.flyTimer.splitTimer.utilities.TimeDisplayUtil;
import tv.figbird.flyTimer.userInterface.entities.SegmentEntry;

import java.util.List;


public class SegmentEntryProcessor {

    private static Logger logger = LogManager.getLogger(SegmentEntryProcessor.class);

    public static SegmentEntry getEntryFromSegment(Segment segment) {
        logger.log(Level.INFO, "Creating SegmentEntry from segment: {}", segment.getName());
        SegmentEntry entry = new SegmentEntry();
        entry.setSegment(segment);
        entry.setName(segment.getName());
        if (segment.getPersonalBest() == null) {
            entry.setPb("");
        } else if (segment.getPersonalBest().isSkipped()) {
            entry.setPb("--");
        } else {
            entry.setPb(TimeDisplayUtil.getDisplayTime(segment.getPersonalBest().getEndedAt()));
        }
        return entry;
    }

    public static void setSumOfBests(List<SegmentEntry> entries) {
        logger.log(Level.INFO, "Calculating Sum of Best");
        long sumOfBest = 0;
        for (SegmentEntry entry : entries) {
            if (entry.getSegment().getBestDuration() > 0) {
                sumOfBest = sumOfBest + entry.getSegment().getBestDuration();
                entry.setSumOfBest(sumOfBest);
                entry.setBest(TimeDisplayUtil.getDisplayTime(entry.getSumOfBest()));
            } else {
                entry.setBest("");
            }
        }
    }

    public static boolean processSplit(SegmentEntry entry, long endedAt, long duration) {
        boolean hasDataToSave = false;
        entry.setCurrentTime(TimeDisplayUtil.getDisplayTime(endedAt));
        entry.setDuration(duration);
        logger.log(Level.INFO, "Segment Split: End - {} Duration - {}",
                endedAt,
                duration);
        if (entry.getSegment().getPersonalBest() == null) {
            entry.setPb(TimeDisplayUtil.getDisplayTime(0));
        } else if (entry.getSegment().getPersonalBest().isSkipped()) {
            entry.setPb(TimeDisplayUtil.getDisplayTime(0));
        } else {
            entry.setPb(TimeDisplayUtil.getTimeDifferenceDisplay(endedAt, entry.getSegment().getPersonalBest().getEndedAt()));
        }
        if (entry.getSegment().getBestDuration() > 0) {
            entry.setBest(TimeDisplayUtil.getTimeDifferenceDisplay(duration, entry.getSegment().getBestDuration()));
            if (entry.getSegment().getBestDuration() > duration) {
                hasDataToSave = true;
            }
        } else {
            hasDataToSave = true;
            entry.setBest(TimeDisplayUtil.getDisplayTime(0));
        }
        entry.getSegment().setEndedAt(endedAt);
        return hasDataToSave;
    }

    public static boolean processFinalSplit(List<SegmentEntry> entries, long endedAt, int attempt) {
        logger.log(Level.INFO, "Processing Final Split");
        boolean hasDataToSave = false;
        boolean isPersonalBest = false;
        Segment finalSegment = entries.get(entries.size() - 1).getSegment();
        if (finalSegment.getPersonalBest() == null || finalSegment.getPersonalBest().getEndedAt() > endedAt) {
            logger.log(Level.INFO, "Detected Personal Best Time. New PB -> {}", endedAt);
            isPersonalBest = true;
            hasDataToSave = true;
        }
        for (SegmentEntry entry : entries) {
            if (isPersonalBest) {
                entry.getSegment().setPrevPersonalBest(entry.getSegment().getPersonalBest());
                if (entry.getSegment().isSkipped()) {
                    entry.getSegment().setPersonalBest(attempt, true);
                } else {
                    entry.getSegment().setPersonalBest(attempt, entry.getSegment().getEndedAt());
                }
            }
            if (entry.getDuration() < entry.getSegment().getBestDuration() && !entry.getSegment().isSkipped()) {
                hasDataToSave = true;
                entry.getSegment().setPrevBestDuration(entry.getSegment().getBestDuration());
                entry.getSegment().setBestDuration(entry.getDuration());
                logger.log(Level.INFO, "Updating Best Duration for {} from {} to {}",
                        entry.getName(),
                        entry.getSegment().getPrevBestDuration(),
                        entry.getDuration());
            }
        }
        return hasDataToSave;
    }

    public static void processSkip(SegmentEntry entry) {
        logger.log(Level.INFO, "Skipping segment - {}", entry.getName());
        entry.setCurrentTime("--");
        entry.setPb("--");
        entry.setBest("--");
        entry.getSegment().setSkipped(true);
    }

    public static void clearTime(SegmentEntry entry) {
        entry.getSegment().setEndedAt(-1);
        entry.setCurrentTime("");
        entry.setBest(TimeDisplayUtil.getDisplayTime(entry.getSumOfBest()));
        entry.setDuration(-1);

        if (entry.getSegment().getPersonalBest() == null) {
            entry.setPb("");
        } else {
            if (entry.getSegment().getPersonalBest().isSkipped()) {
                entry.setPb("--");
            } else {
                entry.setPb(TimeDisplayUtil.getDisplayTime(entry.getSegment().getPersonalBest().getEndedAt()));
            }
        }
        entry.getSegment().setSkipped(false);
    }

    public static void processUndoSplit(SegmentEntry entry) {
        logger.log(Level.INFO, "Undo Split triggered for {}", entry.getName());
        clearTime(entry);
    }
}
