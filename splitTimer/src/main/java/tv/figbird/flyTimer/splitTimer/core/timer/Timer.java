package tv.figbird.flyTimer.splitTimer.core.timer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class Timer {

    private static long startTime = 0;
    private static long storedTime = 0;

    private static Logger logger = LogManager.getLogger(Timer.class);

    public static void start() {
        startTime = System.currentTimeMillis();
        logger.info("Timer Started");
        logger.debug("Setting Start Time to " + startTime);
    }

    public static void stop() {
        if (startTime != 0) {
            storedTime = storedTime + (System.currentTimeMillis() - startTime);
            startTime = 0;
            logger.info("Timer Stopped");
            logger.debug("Saving Time as " + storedTime);
        }
    }

    public static void reset() {
        startTime = 0;
        storedTime = 0;
        logger.info("Timer Reset");
    }

    public static long getCurrentTime() {
        if (startTime == 0) {
//            logger.debug("Timer is Stopped - Returning Stored Time: " + storedTime);
            return storedTime;
        } else {
            long currentTime = storedTime + (System.currentTimeMillis() - startTime);
//            logger.debug("Timer is Running - Returning Current Time: " + currentTime);
            return currentTime;
        }
    }
}
