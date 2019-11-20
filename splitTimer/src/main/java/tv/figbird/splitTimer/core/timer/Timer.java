package tv.figbird.splitTimer.core.timer;

public class Timer {

    private static long startTime = 0;
    private static long storedTime = 0;


    public static void start() {
        startTime = System.currentTimeMillis();
    }

    public static void stop() {
        if (startTime != 0) {
            storedTime = storedTime + (System.currentTimeMillis() - startTime);
            startTime = 0;
        }
    }

    public static void reset() {
        startTime = 0;
        storedTime = 0;
    }


    public static long getCurrentTime() {
        if (startTime != 0) {
            return storedTime + (System.currentTimeMillis() - startTime);
        } else {
            return storedTime;
        }
    }
}
