package tv.figbird.flyTimer.splitTimer.core.timer;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TimerTest {

    @Test
    void timerStartTest() {
        Timer.start();
        long timeToCheck = Timer.getCurrentTime();

        assertTrue(timeToCheck > 0 && timeToCheck < 100);
    }

    @Test
    void timerStopTest() throws Exception {
        Timer.start();
        Timer.stop();
        Thread.sleep(10);

        long timeToCheck = Timer.getCurrentTime();

        assertTrue(timeToCheck > 0 && timeToCheck < 10);
    }

    @Test
    void timerResetTest() {
        Timer.start();
        Timer.stop();
        Timer.reset();

        assertEquals(0, Timer.getCurrentTime());
    }

    @Test
    void timerResumeTest() throws Exception {
        Timer.start();
        Timer.stop();
        Thread.sleep(10);
        Timer.start();
        Thread.sleep(10);
        Timer.stop();

        long timeToCheck = Timer.getCurrentTime();

        assertTrue(timeToCheck > 0 && timeToCheck > 10 && timeToCheck < 20);
    }

}