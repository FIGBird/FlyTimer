package tv.figbird.flyTimer.splitTimer.core.timer;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class SplitTimer {

    private long startTime = 0;
    private long storedTime = 0;

    private static Logger logger = LogManager.getLogger(SplitTimer.class);

    public void start() {
        startTime = System.currentTimeMillis();
        logger.log(Level.INFO, "SplitTimer Started");
        logger.log(Level.INFO, "Setting Start Time to " + startTime);
    }

    public void stop() {
        if (startTime != 0) {
            storedTime = storedTime + (System.currentTimeMillis() - startTime);
            startTime = 0;
            logger.log(Level.INFO, "SplitTimer Stopped");
            logger.log(Level.INFO, "Saving Time as " + storedTime);
        }
    }

    public void reset() {
        startTime = 0;
        storedTime = 0;
        logger.log(Level.INFO, "SplitTimer Reset");
    }

    public long getCurrentTime() {
        if (startTime == 0) {
            return storedTime;
        } else {
            return storedTime + (System.currentTimeMillis() - startTime);
        }
    }
}
