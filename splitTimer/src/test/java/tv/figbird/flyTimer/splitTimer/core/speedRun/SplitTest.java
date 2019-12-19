package tv.figbird.flyTimer.splitTimer.core.speedRun;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class SplitTest {

    @Test
    void compareToTestReturnsNegativeIfGivenSplitIsLater() {
        Split earlier = new Split(100);
        Split later = new Split(200);

        assertTrue(earlier.compareTo(later) < 0);
    }

    @Test
    void compareToTestReturnsPositiveIfGivenSplitIsEarlier() {
        Split earlier = new Split(100);
        Split later = new Split(200);

        assertTrue(later.compareTo(earlier) > 0);
    }

    @Test
    void compareToTestReturnsZeroIfSplitsAreTheSame() {
        Split earlier = new Split(100);
        Split later = new Split(100);

        assertEquals(0, earlier.compareTo(later));
    }

    @Test
    void testListSortingWithNulls() {
        Split first = new Split(1);
        Split second = new Split(2);
        Split last = new Split(4);

        ArrayList<Split> splits = new ArrayList<>();
        splits.add(first);
        splits.add(second);
        splits.add(null);
        splits.add(null);
        splits.add(last);

        splits.sort(Split.getComparator());

        assertEquals(1, splits.get(0).getEndTime());
        assertEquals(2, splits.get(1).getEndTime());
        assertNull(splits.get(2));
        assertNull(splits.get(3));
        assertEquals(4, splits.get(4).getEndTime());
    }
}