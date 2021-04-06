package tv.figbird.flyTimer.splitTimer.core.timer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SplitTimerTest {

    SplitTimer splitTimer;

    @BeforeEach
    void setup() {
        splitTimer = new SplitTimer();
    }

    @Test
    void timerStartTest() throws Exception {
        splitTimer.start();
        Thread.sleep(1);
        long timeToCheck = splitTimer.getCurrentTime();

        assertTrue(timeToCheck > 0 && timeToCheck < 100);
    }

    @Test
    void timerStopTest() throws Exception {
        splitTimer.start();
        Thread.sleep(1);
        splitTimer.stop();
        Thread.sleep(10);

        long timeToCheck = splitTimer.getCurrentTime();

        assertTrue(timeToCheck > 0 && timeToCheck < 10);
    }

    @Test
    void timerResetTest() {
        splitTimer.start();
        splitTimer.stop();
        splitTimer.reset();

        assertEquals(0, splitTimer.getCurrentTime());
    }

    @Test
    void timerResumeTest() throws Exception {
        splitTimer.start();
        splitTimer.stop();
        Thread.sleep(10);
        splitTimer.start();
        Thread.sleep(10);
        splitTimer.stop();

        long timeToCheck = splitTimer.getCurrentTime();

        assertTrue(timeToCheck > 10 && timeToCheck < 20);
    }

}