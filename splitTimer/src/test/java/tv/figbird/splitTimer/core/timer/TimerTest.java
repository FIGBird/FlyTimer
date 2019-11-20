package tv.figbird.splitTimer.core.timer;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


class TimerTest {

    @Test
    void timerTests() throws Exception {
        Timer.start();
        Thread.sleep(100);
        assertTrue(Timer.getCurrentTime() >= 100 && Timer.getCurrentTime() <= 110);

        Thread.sleep(100);
        Timer.stop();
        Thread.sleep(100);
        assertTrue(Timer.getCurrentTime() >= 200 && Timer.getCurrentTime() <= 210);

        Timer.start();
        Thread.sleep(100);
        assertTrue(Timer.getCurrentTime() >= 300 && Timer.getCurrentTime() <= 310);

        Timer.reset();
        assertEquals(0, Timer.getCurrentTime());

    }

}