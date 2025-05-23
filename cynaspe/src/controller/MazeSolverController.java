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

public class MazeSolverController {
    private final MazeController mazeController;

    private SolveAlgorithms selectedAlgorithm;
    private GenerationMode selectedMode;
    private ISolverAlgorithm solverAlgorithm;

    private final Spinner<Integer> spinnerSpeed;
    private final Label labelStatus;
    private final Label labelVisitedTiles;
    private final Label labelPath;
    private final Label labelTime;

    private Runnable onSolvingStarted;
    private Runnable onSolvingFinished;

    public MazeSolverController(
        MazeController mazeController,
        Spinner<Integer> speedSpinner,
        Label labelStatus,
        Label labelVisitedTiles,
        Label labelPath,
        Label labelTime,
        Button buttonSolve
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

    public void setSelectedMode(GenerationMode generationMode){
        selectedMode = generationMode;
    }

    public void setSelectedAlgorithm(SolveAlgorithms solveAlgorithm){
        selectedAlgorithm = solveAlgorithm;
    }

    /**
     * Solve the maze with an algorithm and mode
     */
    public void solve(){
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
    
                    AnimationTimer timer = new AnimationTimer() {
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
                    timer.start();
                    break;
            
                default:
                    break;
            }
        }
        
    }

    private void finishedSolving(){
        mazeController.isGenerating = false;
        updateSolverLabels();
        labelStatus.setText("Traitement terminée");
        if (onSolvingFinished != null) onSolvingFinished.run();
    }

    private void updateSolverLabels(){
        labelVisitedTiles.setText(String.format("Traitées : %d", solverAlgorithm.getVisitedCount()));
        labelPath.setText(String.format("Chemin final : %d", solverAlgorithm.getPathCount()));
        labelTime.setText(String.format("Temps de génération : %d ms", solverAlgorithm.getExecutionTime()));
    }
}
