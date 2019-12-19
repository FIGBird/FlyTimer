package tv.figbird.flyTimer.splitTimer.core.speedRun;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RunAttemptTest {

    @Test
    void hasValidSplitsReturnsFalseWhenLastIsNull() {
        RunAttempt run = new RunAttempt(3);
        run.setSplit(1, null, 1);
        run.setSplit(2, null, 2);

        assertFalse(run.isValidAttempt());
    }

    @Test
    void getTotalAttemptTimeReturnsNegativeOneOnInvalidAttempt() {
        RunAttempt run = new RunAttempt(3);
        run.setSplit(1, null, 1);
        run.setSplit(2, null, 2);

        assertEquals(-1, run.getTotalAttemptTime());
    }

    @Test
    void getTotalAttemptTimeReturnsLastSplitEndTime() {
        RunAttempt run = new RunAttempt(5);
        run.setSplit(1, null, 1);
        run.setSplit(2, null, 2);
        run.setSplit(5, null, 5);

        assertEquals(5, run.getTotalAttemptTime());
    }

    @Test
    void hasValidSplitsReturnsTrueWithNullsInMiddle() {
        RunAttempt run = new RunAttempt(5);
        run.setSplit(1, null, 1);
        run.setSplit(2, null, 2);
        run.setSplit(5, null, 5);

        assertTrue(run.isValidAttempt());
    }

    @Test
    void hasValidSplitsReturnsFalseWhenFirstElementIsLargerThanLast() {
        RunAttempt run = new RunAttempt(2);
        run.setSplit(1, null, 5);
        run.setSplit(2, null, 2);

        assertFalse(run.isValidAttempt());
    }

    @Test
    void hasValidSplitsReturnsTrueWhenSplitsAreInOrder() {
        RunAttempt run = new RunAttempt(2);
        run.setSplit(1, null, 1);
        run.setSplit(2, null, 2);

        assertTrue(run.isValidAttempt());
    }

    @Test
    void hasValidSplitsReturnsTrueWhenDuplicateInOrderSplits() {
        RunAttempt run = new RunAttempt(3);
        run.setSplit(1, null, 1);
        run.setSplit(2, null, 1);
        run.setSplit(3, null, 3);

        assertTrue(run.isValidAttempt());
    }

    @Test
    void hasValidSplitsReturnsFalseWhenDuplicateOutOfOrderSplits() {
        RunAttempt run = new RunAttempt(3);
        run.setSplit(1, null, 1);
        run.setSplit(2, null, 3);
        run.setSplit(3, null, 1);

        assertFalse(run.isValidAttempt());
    }

    @Test
    void hasValidSplitsReturnsFalseWhenNoSplitsArePresent() {
        RunAttempt run = new RunAttempt(1);

        assertFalse(run.isValidAttempt());
    }

    @Test
    void compareReturnsNegativeIfGivenTimeIsSmaller() {
        RunAttempt run = new RunAttempt(1);
        run.setSplit(1, null, 500);

        assertEquals(-100, run.compareSplit(1, 400));
    }

    @Test
    void compareReturnsPositiveIfGivenTimeIsLarget() {
        RunAttempt run = new RunAttempt(1);
        run.setSplit(1, null, 500);

        assertEquals(100, run.compareSplit(1, 600));
    }

    @Test
    void compareReturnsZeroIfGivenTimeIsEqual() {
        RunAttempt run = new RunAttempt(1);
        run.setSplit(1, null, 500);

        assertEquals(0, run.compareSplit(1, 500));
    }

    @Test
    void compareReturnsNullIfSplitDataIsNull() {
        RunAttempt run = new RunAttempt(1);

        assertNull(run.compareSplit(1, 234));
    }

    @Test
    void setSplitThrowsExceptionWithBadNumber() {
        RunAttempt run = new RunAttempt(2);
        String message = "4 is not a valid split number between 1 and 2";

        Exception thrown = assertThrows(IndexOutOfBoundsException.class, ()-> {
           run.setSplit(4, null, 123);

        });

        assertEquals(message, thrown.getMessage());
    }

    @Test
    void getSplitReturnsNullWhenOutOfScope() {
        RunAttempt run = new RunAttempt(2);

        assertNull(run.getSplit(3));
    }

}