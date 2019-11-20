package tv.figbird.splitTimer.core;

import org.junit.jupiter.api.Test;

class SplitTimerTest {

    @Test
    void testing() throws Exception {
        SplitTimer timer = new SplitTimer();
        timer.setCurrentGame("TestGame", 3);

        timer.start();

        Thread.sleep(1000);
        System.out.println(timer.split());

        Thread.sleep(1000);
        System.out.println(timer.split());

        Thread.sleep(500);
        System.out.println(timer.getCurrentSplitTime());

        Thread.sleep(1000);
        System.out.println(timer.split());

        Thread.sleep(1000);
        System.out.println(timer.split());

        Thread.sleep(1000);
        System.out.println(timer.split());
    }

}