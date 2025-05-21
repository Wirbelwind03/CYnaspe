package utils;

import enums.WallDirection;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;

public class Helpers {
    /**
     * Get the opposite wall of a wall
     * @param direction
     * The direction of the wall we want to get the opposite wall direction
     * @return
     * The opposite wall direction
     */
    public static WallDirection getOppositeDirection(WallDirection direction) {
        switch (direction) {
            case TOP: return WallDirection.BOTTOM;
            case BOTTOM: return WallDirection.TOP;
            case LEFT: return WallDirection.RIGHT;
            case RIGHT: return WallDirection.LEFT;
            default: throw new IllegalArgumentException("Unknown direction: " + direction);
        }
    }

    /**
     * 
     * @param <T>
     * @param group
     * @return
     */
    public static <T> T getSelectedUserData(ToggleGroup group) {
        if (group == null) return null;
        Toggle selected = group.getSelectedToggle();
        return selected != null ? (T) selected.getUserData() : null;
    }
}
