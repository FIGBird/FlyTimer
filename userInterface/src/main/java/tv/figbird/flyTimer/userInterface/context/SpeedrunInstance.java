package tv.figbird.flyTimer.userInterface.context;

import tv.figbird.flyTimer.splitTimer.core.entities.Speedrun;

public class SpeedrunInstance {

    private static SpeedrunInstance instance = null;

    private Speedrun speedrun;

    public static SpeedrunInstance getInstance() {
        if (instance == null) {
            instance = new SpeedrunInstance();
        }
        return instance;
    }

    public Speedrun getSpeedrun() {
        return speedrun;
    }

    public void setSpeedrun(Speedrun speedrun) {
        this.speedrun = speedrun;
    }
}
