package tv.figbird.flyTimer.splitTimer.core.speedRun;

import java.util.ArrayList;
import java.util.HashMap;

public class Game {

    private HashMap<String, Speedrun> runCategories; // Category Name + Speedrun
    private String gameName;

    public void putSpeedRun(Speedrun speedRun) {
        getRunCategories().put(speedRun.getCategoryName(), speedRun);
    }

    public Speedrun getSpeedrunByCategory(String categoryName) {
        return getRunCategories().get(categoryName);
    }

    public ArrayList<String> getCategoryNames() {
        return new ArrayList<>(getRunCategories().keySet());
    }

    protected HashMap<String, Speedrun> getRunCategories() {
        if (runCategories == null) {
            runCategories = new HashMap<>();
        }
        return runCategories;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getGameName() {
        return gameName;
    }

}
