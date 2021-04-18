package tv.figbird.flyTimer.userInterface.context;

import tv.figbird.flyTimer.splitTimer.core.entities.Segment;
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
        if (speedrun == null) {
            speedrun = new Speedrun();
            //TODO: Decide for no speedrun
            Segment segment = new Segment();
            segment.setName("1");
            speedrun.getSegments().add(segment);
        }
        return speedrun;
    }

    public void setSpeedrun(Speedrun speedrun) {
        this.speedrun = speedrun;
    }

    public void createNewSpeedrun() {
        Speedrun run = new Speedrun();
        Segment segment = new Segment();
        segment.setName("New Segment");
        run.setGame("New Game");
        run.getSegments().add(segment);
        run.setCategory("Any%");
        setSpeedrun(run);
    }
}
