package controller;

import enums.GenerationMode;
import javafx.animation.AnimationTimer;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import model.MazeModel;
import utils.Helpers;
import utils.KruskalMazeGenerator;
import utils.SpinnerText;

/**
 * Controller responsible for managing maze generation settings.
 * <p>
 * This class extends the base {@code Controller} and handles user inputs and logic
 * related to generating new mazes.
 * </p>
 */
public class MazeGenerationController extends Controller {
    /**
     * The controller managing the maze data.
     */
    private final MazeController mazeController;

    /**
     * The Spinner used to set the speed of the generation.
     */
    private final Spinner<Integer> spinnerSpeed;

    /**
     * The Label that shows the generation status.
     */
    private final Label labelStatus;

    /**
     * The callback to call when the generation has started.
     */
    private Runnable onGenerationStarted;

    /**
     * The callback to call when the generation has finished.
     */
    private Runnable onGenerationFinished;

    /**
     * The timer used for the step-by-step generation.
     */
    private AnimationTimer generationTimer;


    /**
     * Constructs a new {@code MazeGenerationController} that handle the maze generation
     * @param mazeController
     * The controller handling the maze backend
     * @param speedSpinner
     * The Spinner used to change the speed of the generation
     * @param labelStatus
     * The Label used to tell if the generation has finished or not
     */
    public MazeGenerationController(
        MazeController mazeController,
        Spinner<Integer> speedSpinner,
        Label labelStatus
    ) {
        this.mazeController = mazeController;
        this.spinnerSpeed = speedSpinner;
        this.labelStatus = labelStatus;
    }

    /**
     * Set the callBack that will be called when the maze generation has started
     * @param callback
     * The logic to run when "onGenerationStarted" get run
     */
    public void setOnGenerationStarted(Runnable callback){
        this.onGenerationStarted = callback;
    }

    /**
     * Set the callBack that will be called when the maze generation has finished
     * @param callback
     * The logic to run when "onGenerationFinished" get run
     */
    public void setOnGenerationFinished(Runnable callback) {
        this.onGenerationFinished = callback;
    }

    /**
     * Construct the maze on the mazeCanvas
     * @param mazeConfigurationController
     * The controller used to create the maze
     */
    public void constructMaze(MazeConfigurationController mazeConfigurationController){
        // If the timer still exist, stop its
        if (generationTimer != null){
            generationTimer.stop();
            generationTimer = null;
        }

        mazeController.maze = new MazeModel(mazeConfigurationController.getMazeNumRows(), mazeConfigurationController.getMazeNumColumns());
        // Use Kruskal algorithm to generate the maze
        KruskalMazeGenerator generator = new KruskalMazeGenerator(mazeController.maze, mazeConfigurationController.getMazeSeed(), mazeConfigurationController.getMazeType());

        if (onGenerationStarted != null) onGenerationStarted.run();

        // Show the maze generation depending on the mode
        switch (mazeConfigurationController.getGenerationMode()) {
            case GenerationMode.COMPLETE:
                // instant
                while (!generator.isComplete()){
                    generator.step();
                }
                // Maze generation has finished
                mazeController.renderMaze(false);
                if (onGenerationFinished != null) onGenerationFinished.run();
                break;

            case GenerationMode.STEP:
                // Show the spinner text
                SpinnerText spinner = new SpinnerText(4);
                labelStatus.setText(spinner.getCurrentFrame());
                
                // Start the animation
                generationTimer = new AnimationTimer() {
                    private long lastUpdate = 0;

                    @Override
                    public void handle(long now) {
                        
                        if (now - lastUpdate >= Helpers.fpsToNanos(spinnerSpeed.getValue())) {
                            lastUpdate = now;

                            if (!generator.isComplete()) {
                                // Show each step of the algorithm
                                generator.step();
                                mazeController.renderMaze(true);
                                // Show the next frame (aka character) of the spinner text
                                // Update the label that show the spinner text
                                labelStatus.setText(spinner.nextFrame());
                            } else {
                                // When the algorithm has finished
                                stop();
                                if (onGenerationFinished != null) onGenerationFinished.run();
                            }
                        }
                    }
                };

                generationTimer.start();
                break;
        
            
            default:
                break;
        }
    }
}
