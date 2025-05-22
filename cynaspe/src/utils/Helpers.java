package utils;

import java.util.function.UnaryOperator;

import enums.WallDirection;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
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

    public static long fpsToNanos(int fps) {
        if (fps <= 0) {
            throw new IllegalArgumentException("FPS must be greater than zero");
        }
        return 1_000_000_000L / fps;
    }

    public static void restrictToNumericInput(TextField textField, long minValue, long maxValue) {
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*")) {
                try {
                    long value = newText.isEmpty() ? 0 : Long.parseLong(newText);
                    if (value >= minValue && value <= maxValue) {
                        return change;
                    }
                } catch (NumberFormatException e) {
                }
            }
            return null;
        };

        textField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal && textField.getText().isEmpty()) {
                textField.setText("0");
            }
        });

        textField.setTextFormatter(new TextFormatter<>(filter));
    }
}
