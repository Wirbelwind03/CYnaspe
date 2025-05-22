package controller;

import algorithms.DjikstraSolver;
import algorithms.ISolverAlgorithm;
import enums.DialogResult;
import enums.GenerationMode;
import enums.SolveAlgorithms;
import enums.WallDirection;
import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import utils.Helpers;
import utils.SpinnerText;

public class MainController {
    private MazeController mazeController;
    private ISolverAlgorithm solverAlgorithm;

    @FXML private Spinner<Integer> SpinnerGenerationSpeed;
    @FXML private Label LabelGenerationStatus;

    @FXML private Canvas mazeCanvas; // Canvas for rendering the maze

    @FXML private ToggleGroup MazeSolverGroup; // Radio buttons group for the maze solving algorithm
    
    @FXML private ToggleGroup MazeSolverModeGroup; // Radio buttons group for the solving mode 
    @FXML private RadioButton RadioButtonMazeSolverModeComplete;
    @FXML private RadioButton RadioButtonMazeSolverModeStep;
    @FXML private Button MazeButtonSolve;

    @FXML private Label LabelPath; // Label showing the number of path tiles 
    @FXML private Label LabelVisitedTiles; // Label showing the number of tiles visited
    @FXML private Label LabelGenerationTime; // Label showing the maze solving generation time

    @FXML
    public void initialize(){
        SpinnerValueFactory<Integer> generationSpeedValueFactory = 
            new SpinnerValueFactory.IntegerSpinnerValueFactory(1,60);
        generationSpeedValueFactory.setValue(10);
        SpinnerGenerationSpeed.setValueFactory(generationSpeedValueFactory);

        mazeController = new MazeController(mazeCanvas);

        mazeCanvas.setFocusTraversable(true);
        mazeCanvas.setOnKeyPressed(this::onMazeCanvasKeyPressed);

        RadioButtonMazeSolverModeComplete.setUserData(GenerationMode.COMPLETE);
        RadioButtonMazeSolverModeStep.setUserData(GenerationMode.STEP);
        MazeSolverModeGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            MazeButtonSolve.setDisable(newToggle == null);
        });
    }

    @FXML
    private void MBMazeNew_Click(ActionEvent event) throws Exception{
        // Open the MazeConfigurationView
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/MazeConfigurationView.fxml"));
        Parent root = loader.load();
        // Get the controller of the MazeConfigurationView
        MazeConfigurationController mazeConfigController = loader.getController();

        // Show the window for the configuration of the maze
        Stage configStage = new Stage();
        configStage.setTitle("Configuration du labyrinthe");
        configStage.initModality(Modality.APPLICATION_MODAL);
        configStage.setScene(new Scene(root));
        configStage.showAndWait(); // Pause the code and wait until the MazeConfigurationView is closed
        
        // If the dialog has the result OK, create the maze
        if (mazeConfigController.dialogResult == DialogResult.OK){
            mazeController.constructMaze(mazeConfigController, LabelGenerationStatus, SpinnerGenerationSpeed);
        }
    }

    @FXML
    private void onMazeCanvasEntered(MouseEvent event){
        mazeCanvas.requestFocus();
    }

    @FXML
    private void onMazeCanvasClicked(MouseEvent event) {
        if (mazeController.isGenerating) return;
        if (mazeController.maze == null) return;
        if (mazeController.hoveredTile == null) return;
        
        mazeController.addWall(mazeController.hoveredTile, mazeController.hoveredWall);

        mazeController.renderMaze();
    }

    @FXML
    private void onMazeCanvasMouseMoved(MouseEvent event) {
        if (mazeController.isGenerating) return;
        if (mazeController.maze == null) return;
    
        double x = event.getX();
        double y = event.getY();
    
        double tileSize = mazeController.getTileSize();
    
        int column = (int)(x / tileSize);
        int row = (int)(y / tileSize);
    
        if (mazeController.isInsideMaze(row, column)) {
            mazeController.hoveredTile = mazeController.maze.getTile(row, column); 
        }
    }

    @FXML
    private void onMazeCanvasMouseExited(MouseEvent mouseEvent){
        mazeController.hoveredTile = null;
    }

    @FXML
    private void onMazeCanvasKeyPressed(KeyEvent event){
        if (mazeController.isGenerating) return;
        if (mazeController.maze == null) return;
        if (mazeController.hoveredTile == null) return;

        switch (event.getCode()) {
            // Arrow keys
            case UP:
                if (mazeController.hoveredTile.row == 0)
                    return;
                mazeController.hoveredWall = WallDirection.TOP;
                break;
            case RIGHT:
                if (mazeController.hoveredTile.column == mazeController.maze.numCols - 1)
                    return;
                mazeController.hoveredWall = WallDirection.RIGHT;
                break;
            case DOWN:
                if (mazeController.hoveredTile.row == mazeController.maze.numRows - 1)
                    return;
                mazeController.hoveredWall = WallDirection.BOTTOM;
                break;
            case LEFT:
                if (mazeController.hoveredTile.column == 0)
                    return;
                mazeController.hoveredWall = WallDirection.LEFT;
                break;
            // Del key
            case DELETE:
                mazeController.removeWall(mazeController.hoveredTile, mazeController.hoveredWall);
                break;
            default:
                return;
        }

        mazeController.renderMaze();
    }

    @FXML
    private void MazeButtonSolveOnAction(){
        if (mazeController.isGenerating) return;

        // Get the selected solve mode from the radio buttons group
        GenerationMode solveMode = Helpers.getSelectedUserData(MazeSolverModeGroup);
        // If there isn't any solve mode selected, stop the function
        if (solveMode == null) return;

        // Get the selected solver algorithm from the radio buttons group
        SolveAlgorithms selectedSolverAlgorithm = Helpers.getSelectedUserData(MazeSolverGroup);
        // If there isn't selected solver algorithm, stop function
        // if (selectedSolverAlgorithm == null) return;

        // If there's a maze
        if (mazeController.maze != null){
            mazeController.isGenerating = true;
            
            mazeController.maze.resetTileStatus();
            solverAlgorithm = new DjikstraSolver(mazeController);

            switch (solveMode) {
                case GenerationMode.COMPLETE:
                    while (!solverAlgorithm.isComplete()){
                        solverAlgorithm.step();
                    }
                    mazeController.renderMaze();
                    mazeController.isGenerating = false;
                    LabelVisitedTiles.setText(String.format("Traitées : %d", solverAlgorithm.getVisitedCount()));
                    LabelPath.setText(String.format("Chemin final : %d", solverAlgorithm.getPathCount()));
                    break;

                case GenerationMode.STEP:
                    SpinnerText spinner = new SpinnerText(4);
                    LabelGenerationStatus.setText(spinner.getCurrentFrame());

                    AnimationTimer timer = new AnimationTimer() {
                        private long lastUpdate = 0;
                        
                        @Override
                        public void handle(long now) {
                            if (now - lastUpdate >= mazeController.getFPS(SpinnerGenerationSpeed.getValue())) {
                                lastUpdate = now;

                                LabelVisitedTiles.setText(String.format("Traitées : %d", solverAlgorithm.getVisitedCount()));
                                LabelPath.setText(String.format("Chemin final : %d", solverAlgorithm.getPathCount()));
                                boolean done = solverAlgorithm.step();
                                mazeController.renderMaze();
                                spinner.nextFrame();
                                LabelGenerationStatus.setText(spinner.getCurrentFrame());
                                if (done){
                                    stop();
                                    mazeController.isGenerating = false;
                                    LabelGenerationStatus.setText("Traitement terminée");
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
}
