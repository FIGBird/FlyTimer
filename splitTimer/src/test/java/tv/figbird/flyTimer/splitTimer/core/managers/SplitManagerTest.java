package tv.figbird.flyTimer.splitTimer.core.managers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tv.figbird.flyTimer.splitTimer.core.entities.Split;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class SplitManagerTest {

    private SplitManager manager;
    private Split firstSplit;
    private Split secondSplit;
    private Split thirdSplit;

    @BeforeEach
    void setUp() {
        manager = new SplitManager();

        firstSplit = new Split();
        firstSplit.setName("FirstSplit");
        secondSplit = new Split();
        secondSplit.setName("SecondSplit");
        thirdSplit = new Split();
        thirdSplit.setName("ThirdSplit");

    }

    @Test
    void sortSplitTest() {
        firstSplit.setNextSplit(secondSplit);
        secondSplit.setNextSplit(thirdSplit);
        secondSplit.setPreviousSplit(firstSplit);
        thirdSplit.setPreviousSplit(secondSplit);
        ArrayList<Split> splits = new ArrayList<>();
        splits.add(thirdSplit);
        splits.add(firstSplit);
        splits.add(secondSplit);
        manager.setSplits(splits);
        manager.setFirstSplit(firstSplit);

        manager.sortSplits();

        assertEquals(manager.getSplit(0), firstSplit);
        assertEquals(manager.getSplit(1), secondSplit);
        assertEquals(manager.getSplit(2), thirdSplit);
    }

    @Test
    void addSplitTests() {
        manager.addSplit(firstSplit);
        manager.addSplit(secondSplit);
        manager.addSplit(thirdSplit);

        assertNull(firstSplit.getPreviousSplit());
        assertEquals(firstSplit, secondSplit.getPreviousSplit());
        assertEquals(secondSplit, thirdSplit.getPreviousSplit());

        assertEquals(secondSplit, firstSplit.getNextSplit());
        assertEquals(thirdSplit, secondSplit.getNextSplit());
        assertNull(thirdSplit.getNextSplit());
    }

    @Test
    void insertSplitBetweenTest() {
        manager.addSplit(firstSplit);
        manager.addSplit(thirdSplit);
        manager.insertSplit(secondSplit, 1);

        assertNull(firstSplit.getPreviousSplit());
        assertEquals(firstSplit, secondSplit.getPreviousSplit());
        assertEquals(secondSplit, thirdSplit.getPreviousSplit());

        assertEquals(secondSplit, firstSplit.getNextSplit());
        assertEquals(thirdSplit, secondSplit.getNextSplit());
        assertNull(thirdSplit.getNextSplit());

        assertEquals(secondSplit, manager.getSplit(1));
    }

    @Test
    void insertSplitFirst() {
        manager.addSplit(secondSplit);
        manager.addSplit(thirdSplit);
        manager.insertSplit(firstSplit, 0);

        assertNull(firstSplit.getPreviousSplit());
        assertEquals(firstSplit, secondSplit.getPreviousSplit());
        assertEquals(secondSplit, thirdSplit.getPreviousSplit());

        assertEquals(secondSplit, firstSplit.getNextSplit());
        assertEquals(thirdSplit, secondSplit.getNextSplit());
        assertNull(thirdSplit.getNextSplit());

        assertEquals(firstSplit, manager.getSplit(0));
    }

    @Test
    void insertSplitLastIndexOverSize() {
        int index = 5;
        String expected = "Requested Index out of bounds - " + index + " is greater than or equal to list size of 1";
        manager.addSplit(firstSplit);

        Throwable thrown = assertThrows(IndexOutOfBoundsException.class, () -> manager.insertSplit(secondSplit, index));

        assertEquals(expected, thrown.getMessage());
    }

    @Test
    void addSplitOutOfBounds() {
        int index = 5;
        String expected = "Requested Index out of bounds - " + index + " is greater than or equal to list size of 1";
        manager.addSplit(firstSplit);

        Throwable thrown = assertThrows(IndexOutOfBoundsException.class, () -> manager.getSplit(index));

        assertEquals(expected, thrown.getMessage());
    }

    @Test
    void insertIntoEmpty() {
        manager.insertSplit(firstSplit, 0);

        assertEquals(firstSplit, manager.getFirstSplit());
        assertEquals(firstSplit, manager.getSplit(0));
    }

    @Test
    void deleteSplitTests() {
        manager.addSplit(firstSplit);
        manager.addSplit(secondSplit);
        manager.addSplit(thirdSplit);

        manager.deleteSplit(secondSplit);

        assertEquals(firstSplit, thirdSplit.getPreviousSplit());
        assertEquals(thirdSplit, firstSplit.getNextSplit());
    }

    @Test
    void uniqueSplitError() {
        String expected = "Error adding split - " + firstSplit.getName() + " is already contained in the split list.";

        Throwable thrown = assertThrows(IllegalArgumentException.class, () ->{
            manager.addSplit(firstSplit);
            manager.addSplit(firstSplit);

        });

        assertEquals(expected, thrown.getMessage());
    }

}