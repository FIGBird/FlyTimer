package tv.figbird.flyTimer.splitTimer.core.entities;

import java.util.ArrayList;

public class Speedrun {

    private int attempts;
    private String game;
    private String category;
    private String runners;

    private ArrayList<Segment> segments;

    public int getAttempts() {
        return attempts;
    }

    public void setAttempts(int attempts) {
        this.attempts = attempts;
    }

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getRunners() {
        return runners;
    }

    public void setRunners(String runners) {
        this.runners = runners;
    }

    public ArrayList<Segment> getSegments() {
        if (segments == null) {
            segments = new ArrayList<>();
        }
        return segments;
    }

    public void setSegments(ArrayList<Segment> segments) {
        this.segments = segments;
    }
}
