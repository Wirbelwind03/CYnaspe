package controller;

import algorithms.BreadthFirstSolver;
import algorithms.DjikstraSolver;
import algorithms.ISolverAlgorithm;
import algorithms.RecursiveMazeSolver;
import enums.GenerationMode;
import enums.SolveAlgorithms;
import javafx.animation.AnimationTimer;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import utils.Helpers;
import utils.SpinnerText;

/**
 * Controller responsible for managing maze solving operations and UI updates.
 * <p>
 * This class extends the base {@code Controller} and manage the selection of
 * solving algorithms, the solving speed, and updates UI labels related
 * to solving progress and results.
 * </p>
 */
public class MazeSolverController extends Controller {
    /**
     * The maze controller holding the maze data.
     */
    private final MazeController mazeController;

    /**
     * The selected algorithm for solving the maze.
     */
    private SolveAlgorithms selectedAlgorithm;

    /**
     * The selected mode for solving the maze.
     */
    private GenerationMode selectedMode;

    /**
     * The instance of the algorithm used to solve the maze.
     */
    private ISolverAlgorithm solverAlgorithm;

    /**
     * Spinner that handles the speed of the solving.
     */
    private final Spinner<Integer> spinnerSpeed;

    /**
     * Label that shows the status of the solving.
     */
    private final Label labelStatus;

    /**
     * Label that shows the number of visited tiles.
     */
    private final Label labelVisitedTiles;

    /**
     * Label that shows the number of paths.
     */
    private final Label labelPath;

    /**
     * Label that shows the execution time of the solving algorithm.
     */
    private final Label labelTime;

    /**
     * Callback to call when the solving algorithm has started.
     */
    private Runnable onSolvingStarted;

    /**
     * Callback to call when the solving algorithm has finished.
     */
    private Runnable onSolvingFinished;

    /**
     * Timer used for showing the solving step by step.
     */
    private AnimationTimer solverTimer;


    /**
     * Constructs a new {@code MazeSolverController}.
     *
     * @param mazeController reference to the maze controller managing the maze
     * @param speedSpinner Spinner controlling the solver speed
     * @param labelStatus Label to display the status
     * @param labelVisitedTiles Label to display number of visited tiles
     * @param labelPath Label to display the number of path tiles
     * @param labelTime label to display the execution solving time
     */
    public MazeSolverController(
        MazeController mazeController,
        Spinner<Integer> speedSpinner,
        Label labelStatus,
        Label labelVisitedTiles,
        Label labelPath,
        Label labelTime
    ) {
        this.mazeController = mazeController;
        this.spinnerSpeed = speedSpinner;
        this.labelStatus = labelStatus;
        this.labelVisitedTiles = labelVisitedTiles;
        this.labelPath = labelPath;
        this.labelTime = labelTime;
    }

    /**
     * Set the callBack that will be called when the maze solving has started
     * @param callBack
     * The logic to run when "onSolvingStarted" get run
     */
    public void setOnSolvingStarted(Runnable callback){
        this.onSolvingStarted = callback;
    }

    /**
     * Set the callBack that will be called when the maze solving has finished
     * @param callBack
     * The logic to run when "onSolvingFinished" get run
     */
    public void setOnSolvingFinished(Runnable callback){
        this.onSolvingFinished = callback;
    }

    /**
     * Set the selected generation mode of the maze
     * @param generationMode
     * The generation mode used 
     */
    public void setSelectedMode(GenerationMode generationMode){
        selectedMode = generationMode;
    }

    /**
     * Set the selected solve algorithm of the maze
     * @param generationMode
     * The solve algoritm used 
     */
    public void setSelectedAlgorithm(SolveAlgorithms solveAlgorithm){
        selectedAlgorithm = solveAlgorithm;
    }

    /**
     * Solve the maze with an algorithm and mode
     */
    public void solve(){
        // If the timer still exist, stop it
        if (solverTimer != null){
            solverTimer.stop();
            solverTimer = null;
        }

        if (mazeController.isGenerating  // If the maze is still generating
        || selectedMode == null // If there's not a solving mode selected
        || selectedAlgorithm == null // If there's not a solving algorithm selected
        || !mazeController.hasMaze())  // If there's not maze on the mazeController
            return;

        mazeController.isGenerating = true;
        
        mazeController.resetTileStatus();

        if (onSolvingStarted != null) onSolvingStarted.run();;

        switch (selectedAlgorithm) {
            case SolveAlgorithms.DFS:
                solverAlgorithm = new RecursiveMazeSolver(mazeController.maze);
                break;

            case SolveAlgorithms.BFS:
                solverAlgorithm = new BreadthFirstSolver(mazeController.maze);
                break;

            case SolveAlgorithms.DJIKSTRA:
                solverAlgorithm = new DjikstraSolver(mazeController.maze);
                break;
        
            default:
                break;
        }

        if (solverAlgorithm != null){
            switch (selectedMode) {
                case GenerationMode.COMPLETE:
                    while (!solverAlgorithm.isComplete()){
                        solverAlgorithm.step();
                    }
                    mazeController.renderMaze(false);
                    finishedSolving();
                    break;
    
                case GenerationMode.STEP:
                    SpinnerText spinner = new SpinnerText(4);
                    labelStatus.setText(spinner.getCurrentFrame());
    
                    solverTimer = new AnimationTimer() {
                        private long lastUpdate = 0;
                        
                        @Override
                        public void handle(long now) {
                            if (now - lastUpdate >= Helpers.fpsToNanos(spinnerSpeed.getValue())) {
                                lastUpdate = now;
    
                                boolean done = solverAlgorithm.step();
                                mazeController.renderMaze(false);
                                updateSolverLabels();
                                spinner.nextFrame();
                                labelStatus.setText(spinner.getCurrentFrame());
                                if (done){
                                    stop();
                                    finishedSolving();
                                } 
                            }
                        }
                    };
                    solverTimer.start();
                    break;
            
                default:
                    break;
            }
        }
        
    }

    /**
     * When the algorithm has finished solving
     */
    private void finishedSolving(){
        mazeController.isGenerating = false;
        updateSolverLabels();
        labelStatus.setText("Traitement terminée");
        labelTime.setText(String.format("Temps de génération : %d ms", solverAlgorithm.getExecutionTime()));
        if (onSolvingFinished != null) onSolvingFinished.run();
    }

    /**
     * At each step of the algorithm
     */
    private void updateSolverLabels(){
        labelVisitedTiles.setText(String.format("Traitées : %d", solverAlgorithm.getVisitedCount()));
        labelPath.setText(String.format("Chemin final : %d", solverAlgorithm.getPathCount()));
        
    }
}
