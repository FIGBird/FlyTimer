package tv.figbird.flyTimer.splitTimer.core.speedRun;

import java.util.Comparator;

public class Split implements Comparable<Split> {

    private String splitName;
    private long endTimeInMillis;
    private long splitTime;

    public Split() {

    }

    public Split(long endTimeInMillis) {
        this.endTimeInMillis = endTimeInMillis;
    }

    public String getSplitName() {
        return splitName;
    }

    public void setSplitName(String splitName) {
        this.splitName = splitName;
    }

    public long getEndTime() {
        return endTimeInMillis;
    }

    public void setEndTime(long endTimeInMillis) {
        this.endTimeInMillis = endTimeInMillis;
    }

    public long getSplitTime() {
        return splitTime;
    }

    public void setSplitTime(long totalSplitTime) {
        this.splitTime = totalSplitTime;
    }

    /**
     * Compares this Split with the specified split for order by comparing the end times.
     * Note: this class has a natural ordering which is inconsistent with equals.
     * Note: Since lists of Splits may contain nulls which represent skipped splits, a null value is treated as equal for sorting.
     * @param split The split to be compared.
     * @return a negative integer, zero, or a positive integer as this split is less than, equal to, or greater than the specified split based on end time.
     */
    @Override
    public int compareTo(Split split) {
        if (split == null) {
            return 0;
        }
        return (int) (this.getEndTime() - split.getEndTime());
    }

    public static Comparator<Split> getComparator() {
        return (o1, o2) -> {
            if (o1 == null || o2 == null) {
                return 0;
            } else {
                return o1.compareTo(o2);
            }
        };
    }
}
