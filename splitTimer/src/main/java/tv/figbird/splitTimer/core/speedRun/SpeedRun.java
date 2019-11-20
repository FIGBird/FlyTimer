package tv.figbird.splitTimer.core.speedRun;


import java.util.ArrayList;

public class SpeedRun {

    private ArrayList<Split> splits;
    private String name;

    public SpeedRun() {

    }

    public SpeedRun(String name) {
        this.name = name;
    }

    public void addSplit(long timeInMillis) {
        getSplits().add(new Split(timeInMillis));
    }

    protected ArrayList<Split> getSplits() {
        if(splits == null) {
            this.splits = new ArrayList<>();
        }
        return splits;
    }

    public int getSplitCount() {
        return splits.size();
    }

    public Split getSplit(int index) {
        return splits.get(index);
    }

    public void setName(String name) {
        this.name = name;
    }
}
