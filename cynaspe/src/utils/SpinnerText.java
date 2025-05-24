package utils;

/**
 * Class representing a simple spinner animation using a set of frames in the form of string.
 * The spinner cycles through given characters to simulate the spinning,
 * updating frames after skipping a specified number of calls.
 */
public class SpinnerText {
    private final String[] frames = {"◐", "◓", "◑", "◒"}; // The frames of the spinnerText
    private int index = 0; // Index used for jumping to the next frame, reset to 0 when the next happens
    private int callCount = 0; // The number of times the class has been called
    private final int skipFrames; // How many frame to skip before it get updated

    /**
     * Construct a instance of SpinnerText
     * @param skipFrames
     * How many frame to skip before it get updated
     */
    public SpinnerText(int skipFrames){
        this.skipFrames = Math.max(1, skipFrames);
    }

    /**
     * Jump to next frame of the spinner
     * @return
     * A {@code String} of the current frame
     */
    public String nextFrame() {
        callCount++;
        // If the call count is higher or equal to the skip frames
        if (callCount >= skipFrames) {
            callCount = 0; // Reset the call count
            index = (index + 1) % frames.length; // Update the frame
        }
        return frames[index]; // Return the frame
    }

    /**
     * Get the current frame of the spinner
     * @return
     * A {@code String} of the current frame
     */
    public String getCurrentFrame(){
        return frames[index];
    }
}
