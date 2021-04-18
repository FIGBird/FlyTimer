package tv.figbird.flyTimer.userInterface.processors;

import tv.figbird.flyTimer.splitTimer.core.entities.Segment;
import tv.figbird.flyTimer.userInterface.entities.SegmentManagerEntry;

import java.util.ArrayList;
import java.util.List;

public class SegmentManagerEntryProcessor {

    public static String processStringInputForTimes(String string) {
        return string.replaceAll("[^\\d]", "");
    }

    public static Segment getSegmentFromEntry(SegmentManagerEntry entry) {
        Segment segment = new Segment();
        segment.setName(entry.getSegmentName());
        if (!entry.getBestDuration().equals("")) {
            segment.setBestDuration(Long.valueOf(entry.getBestDuration()));
        }
        if (entry.isIsSkipped()) {
            segment.setPersonalBest(-1, true);
        } else if (!entry.getPersonalBest().equals("")) {
            segment.setPersonalBest(Long.valueOf(entry.getPersonalBest()));
        }
        return segment;
    }

    public static ArrayList<Segment> getSegmentsFromEntries(List<SegmentManagerEntry> entries) {
        ArrayList<Segment> segments = new ArrayList<>();
        for (SegmentManagerEntry entry : entries) {
            segments.add(getSegmentFromEntry(entry));
        }
        return segments;
    }

    public static SegmentManagerEntry getSegmentManagerEntryFromSegment(Segment segment) {
        SegmentManagerEntry entry = new SegmentManagerEntry();

        if (segment.getName() == null) {
            entry.setSegmentName("New Segment");
        } else {
            entry.setSegmentName(segment.getName());
        }
        if (segment.getBestDuration() < 0) {
            entry.setBestDuration("");
        } else {
            entry.setBestDuration(String.valueOf(segment.getBestDuration()));
        }
        if (segment.getPersonalBest() == null) {
            entry.setPersonalBest("");
            entry.setIsSkipped(false);
        } else {
            if (segment.getPersonalBest().isSkipped()) {
                entry.setPersonalBest("");
                entry.setIsSkipped(true);
            } else {
                entry.setPersonalBest(String.valueOf(segment.getPersonalBest().getEndedAt()));
                entry.setIsSkipped(false);
            }
        }
        return entry;
    }
}
