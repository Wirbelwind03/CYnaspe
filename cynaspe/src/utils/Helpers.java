package utils;

import java.util.function.UnaryOperator;

import enums.WallDirection;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;

/**
 * Utility class providing helper methods.
 */
public class Helpers {
    /**
     * Default constructor  
     */
    public Helpers() {

    }

    /**
     * Get the opposite wall of a wall
     * @param direction
     * The direction of the wall we want to get the opposite wall direction
     * @return
     * The opposite {@code WallDirection}
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
     * Return the user data from the selected toggle in a ToggleGroup
     * @param <T>
     * The type to cast to
     * @param group
     * The ToggleGroup which we get the user data
     * @return
     * The user data cast to type T,
     * {@code null} if no toggle or the group is null
     */
    public static <T> T getSelectedUserData(ToggleGroup group) {
        if (group == null) return null;
        Toggle selected = group.getSelectedToggle();
        return selected != null ? (T) selected.getUserData() : null;
    }

    /**
     * Convert FPS to Nanos
     * @param fps
     * The FPS to convert from
     * @return
     * A {@code long} that represent Nanos
     */
    public static long fpsToNanos(int fps) {
        if (fps <= 0) {
            throw new IllegalArgumentException("FPS must be greater than zero");
        }
        return 1_000_000_000L / fps;
    }

    /**
     * Restrict a textField to numeric input
     * @param textField
     * The TextField to restrict
     * @param minValue
     * The mininum value that can be entered in the TextField
     * @param maxValue
     * The maximum value that can be entered in the TextField
     */
    public static void restrictToNumericInput(TextField textField, long minValue, long maxValue) {
        UnaryOperator<TextFormatter.Change> filter = change -> {
            // Get the entered text
            String newText = change.getControlNewText();
            // Restrict to numeric value
            if (newText.matches("\\d*")) {
                try {
                    // If the TextField is empty, put 0, otherwise, parse the string and convert to long
                    long value = newText.isEmpty() ? 0 : Long.parseLong(newText);
                    // If the value is smaller than minValue or bigger than maxValue
                    // Don't change the value
                    if (value >= minValue && value <= maxValue) {
                        return change;
                    }
                } catch (NumberFormatException e) {
                }
            }
            return null;
        };

        // Event whenever the text change in the TextField
        textField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            // If the TextField empty, put 0 automatically
            if (!newVal && textField.getText().isEmpty()) {
                textField.setText("0");
            }
        });

        textField.setTextFormatter(new TextFormatter<>(filter));
    }
}
