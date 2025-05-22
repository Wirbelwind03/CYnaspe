package utils;

public class SpinnerText {
    private final String[] frames = {"◐", "◓", "◑", "◒"};
    private int index = 0;
    private int callCount = 0;
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
     * A String of the current frame
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
     * A String of the current frame
     */
    public String getCurrentFrame(){
        return frames[index];
    }
}
