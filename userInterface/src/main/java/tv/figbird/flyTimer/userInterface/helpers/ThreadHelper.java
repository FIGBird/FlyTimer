package tv.figbird.flyTimer.userInterface.helpers;

import javafx.application.Platform;

public class ThreadHelper {

    public static Thread getTimedDeamon(Runnable action, long millis) {
        Thread thread = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(millis);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Platform.runLater(action);
            }
        });
        thread.setDaemon(true);
        return thread;
    }
}
