package tv.figbird.flyTimer.splitTimer.core.timer;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


class TimerTest {

    @Test
    void timerTests() throws Exception {
        Timer.start();
        Thread.sleep(10);
        assertTrue(Timer.getCurrentTime() >= 10 && Timer.getCurrentTime() <= 15);

        Thread.sleep(10);
        Timer.stop();
        Thread.sleep(10);
        assertTrue(Timer.getCurrentTime() >= 20 && Timer.getCurrentTime() <= 25);

        Timer.start();
        Thread.sleep(10);
        assertTrue(Timer.getCurrentTime() >= 30 && Timer.getCurrentTime() <= 35);

        Timer.reset();
        assertEquals(0, Timer.getCurrentTime());
    }

}