package tv.figbird.flyTimer.splitTimer.core;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class SplitTimerTest {

    //TODO: Write full tests

    @Test
    @Disabled
    void testing() throws Exception {
        SplitTimer timer = new SplitTimer(4);

        timer.start();

        Thread.sleep(1000);
        timer.split();
        timer.skipSplit();

        Thread.sleep(1000);
        timer.split();

        Thread.sleep(500);
        timer.skipSplit();

        Thread.sleep(1000);
        timer.split();

        Thread.sleep(1000);
        timer.split();

        Thread.sleep(1000);
        timer.split();
    }
}