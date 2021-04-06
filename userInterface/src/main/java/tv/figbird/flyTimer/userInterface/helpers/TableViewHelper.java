package tv.figbird.flyTimer.userInterface.helpers;


import com.sun.javafx.scene.control.skin.TableViewSkin;
import com.sun.javafx.scene.control.skin.VirtualFlow;
import javafx.scene.control.TableView;

public class TableViewHelper {

    public static int[] getVisibleRows(TableView table) {
        VirtualFlow<?> vf = (VirtualFlow<?>) ((TableViewSkin<?>) table.getSkin()).getChildren().get(1);
        int firstIndex = vf.getFirstVisibleCell().getIndex();
        int lastIndex = vf.getLastVisibleCell().getIndex();

        return new int[]{firstIndex, lastIndex};
    }

}