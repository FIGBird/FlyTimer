package tv.figbird.splitTimer.core;


import tv.figbird.splitTimer.core.speedRun.SpeedGame;
import tv.figbird.splitTimer.core.speedRun.SpeedRun;
import tv.figbird.splitTimer.core.timer.Timer;

public class SplitTimer {

    private SpeedRun currentSpeedRun;
    private SpeedGame currentGame;

    private long splitStartTime;
    private int currentSplit;
    private boolean isRunning = false;

    //TODO: Change to take a SpeedGame
    public void setCurrentGame(String gameName, int totalSplits) {
        this.currentGame = new SpeedGame();
        currentGame.setGameName(gameName);
        currentGame.setTotalSplits(totalSplits);
    }

    public void start() {
        Timer.reset();
        Timer.start();
        isRunning = true;
        currentSpeedRun = new SpeedRun();
        this.splitStartTime = 0;
        this.currentSplit = 1;
    }

    public long split() {
        if (currentSplit <= getTotalSplits() && isRunning) {
            long splitEndTime = Timer.getCurrentTime();
            long totalSplitTime = splitEndTime - splitStartTime;
            currentSpeedRun.addSplit(totalSplitTime);
            splitStartTime = splitEndTime;

            if(currentSplit == getTotalSplits()) {
                Timer.stop();
            }
            currentSplit = currentSplit + 1;
            return totalSplitTime;
        }
        return 0;
    }

    public long getCurrentTime() {
        return Timer.getCurrentTime();
    }

    public long getCurrentSplitTime() {
        return Timer.getCurrentTime() - splitStartTime;
    }

    public void pause() {
        Timer.stop();
        isRunning = false;
    }

    public void resume() {
        if (currentSplit <= getTotalSplits()) {
            Timer.start();
            isRunning = true;
        }
    }

    public void pauseOrResume() {
        if (currentSpeedRun == null) {
            return;
        }
        if (isRunning) {
            pause();
        } else {
            resume();
        }
    }

    public boolean isRunning() {
        return isRunning;
    }

    public int getTotalSplits() {
        return this.currentGame.getTotalSplits();
    }
}
