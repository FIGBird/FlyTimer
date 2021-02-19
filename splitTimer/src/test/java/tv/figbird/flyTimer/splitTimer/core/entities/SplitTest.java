package tv.figbird.flyTimer.splitTimer.core.entities;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class SplitTest {

    @Test
    void hasNext() {
        Split currentSplit = new Split();
        Split nextSplit = new Split();

        currentSplit.setNextSplit(nextSplit);

        assertTrue(currentSplit.hasNext());
    }

    @Test
    void hasPrev() {
        Split currentSplit = new Split();
        Split previousSplit = new Split();

        currentSplit.setPreviousSplit(previousSplit);

        assertTrue(currentSplit.hasPrev());
    }
}