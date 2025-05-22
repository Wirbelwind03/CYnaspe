package controller;

import java.io.File;

import algorithms.BreadthFirstSolver;
import algorithms.DjikstraSolver;
import algorithms.ISolverAlgorithm;
import algorithms.RecursiveMazeSolver;
import enums.DialogResult;
import enums.GenerationMode;
import enums.SolveAlgorithms;
import enums.WallDirection;
import io.MazeReader;
import io.MazeWriter;
import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.MazeModel;
import utils.Helpers;
import utils.KruskalMazeGenerator;
import utils.SpinnerText;

public class MainController extends Controller {
    private MazeController mazeController;
    private ISolverAlgorithm solverAlgorithm = null;

    private GenerationMode selectedSolverMode = null;
    private SolveAlgorithms selectedSolverAlgorithms = null;

    @FXML private Spinner<Integer> SpinnerGenerationSpeed;
    @FXML private Label LabelGenerationStatus;

    @FXML private Canvas mazeCanvas; // Canvas for rendering the maze

    @FXML private ToggleGroup MazeSolverGroup; // Radio buttons group for the maze solving algorithm
    @FXML private RadioButton RadioButtonMazeSolverDFS;
    @FXML private RadioButton RadioButtonMazeSolverBFS;
    @FXML private RadioButton RadioButtonMazeSolverDjisktra;
    
    @FXML private ToggleGroup MazeSolverModeGroup; // Radio buttons group for the solving mode 
    @FXML private RadioButton RadioButtonMazeSolverModeComplete;
    @FXML private RadioButton RadioButtonMazeSolverModeStep;
    @FXML private Button MazeButtonSolve; // "Résoudre" button

    @FXML private Label LabelPath; // Label showing the number of path tiles 
    @FXML private Label LabelVisitedTiles; // Label showing the number of tiles visited
    @FXML private Label LabelGenerationTime; // Label showing the maze solving generation time

    @FXML private MenuItem MenuItemMazeSave;

    @FXML
    public void initialize(){
        // Set the value of the spinner for the generation speed
        SpinnerValueFactory<Integer> generationSpeedValueFactory = 
            new SpinnerValueFactory.IntegerSpinnerValueFactory(1,60);
        generationSpeedValueFactory.setValue(10);
        SpinnerGenerationSpeed.setValueFactory(generationSpeedValueFactory);

        // Create the maze controller for the mazeCanvas
        mazeController = new MazeController(mazeCanvas);
        mazeCanvas.setFocusTraversable(true);
        mazeCanvas.setOnKeyPressed(this::onMazeCanvasKeyPressed);

        // Initialize
        RadioButtonMazeSolverDFS.setUserData(SolveAlgorithms.DFS);
        RadioButtonMazeSolverBFS.setUserData(SolveAlgorithms.BFS);
        RadioButtonMazeSolverDjisktra.setUserData(SolveAlgorithms.DJIKSTRA);
        MazeSolverGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            selectedSolverAlgorithms = Helpers.getSelectedUserData(MazeSolverGroup);
            if (selectedSolverMode != null && mazeController.maze != null){
                MazeButtonSolve.setDisable(newToggle == null);
            }
        });

        RadioButtonMazeSolverModeComplete.setUserData(GenerationMode.COMPLETE);
        RadioButtonMazeSolverModeStep.setUserData(GenerationMode.STEP);
        MazeSolverModeGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            selectedSolverMode = Helpers.getSelectedUserData(MazeSolverModeGroup);
            if (selectedSolverAlgorithms != null && mazeController.maze != null) {
                MazeButtonSolve.setDisable(newToggle == null);
            }
        });
    }

    @FXML
    private void MBMazeNew_Click(ActionEvent event) throws Exception{
        // Open the MazeConfigurationView
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/MazeConfigurationView.fxml"));
        Parent root = loader.load();
        // Get the controller of the MazeConfigurationView
        MazeConfigurationController mazeConfigurationController = loader.getController();

        // Show the window for the configuration of the maze
        Stage configStage = new Stage();
        configStage.setTitle("Configuration du labyrinthe");
        configStage.initModality(Modality.APPLICATION_MODAL);
        configStage.setScene(new Scene(root));
        configStage.showAndWait(); // Pause the code and wait until the MazeConfigurationView is closed
        
        // If the dialog has the result OK, create the maze
        if (mazeConfigurationController.dialogResult == DialogResult.OK){
            constructMaze(mazeConfigurationController);
            MenuItemMazeSave.setDisable(false);
        }
    }

    @FXML
    private void onMazeCanvasEntered(MouseEvent event){
        mazeCanvas.requestFocus();
    }

    @FXML
    private void onMazeCanvasClicked(MouseEvent event) {
        if (mazeController.isGenerating || mazeController.maze == null || mazeController.hoveredTile == null) return;
        
        mazeController.addWall(mazeController.hoveredTile, mazeController.hoveredWall);

        mazeController.renderMaze(false);
    }

    /**
     * When the user mouse move on the canvas
     * @param event
     * The mouse that has entered the canvas
     */
    @FXML
    private void onMazeCanvasMouseMoved(MouseEvent event) {
        if (mazeController.isGenerating || mazeController.maze == null) return;
    
        // Get the position of the mouse
        double x = event.getX();
        double y = event.getY();
    
        // Calculate the tileSize
        double tileSize = mazeController.getTileSize();
    
        // Get the row and column the mouse is in
        int column = (int)(x / tileSize);
        int row = (int)(y / tileSize);
    
        // Assign the hovered tile by getting the tile where the mouse is
        if (mazeController.isInsideMaze(row, column)) {
            mazeController.hoveredTile = mazeController.maze.getTile(row, column); 
        }
    }

    /**
     * When the user mouse exit the canvas
     * @param mouseEvent
     * The mouse that has exited
     */
    @FXML
    private void onMazeCanvasMouseExited(MouseEvent mouseEvent){
        mazeController.hoveredTile = null;
    }

    /**
     * When the user press a key on the canvas
     * @param event
     * The key that has been pressed
     */
    @FXML
    private void onMazeCanvasKeyPressed(KeyEvent event){
        if (mazeController.isGenerating || mazeController.maze == null || mazeController.hoveredTile == null) return;

        switch (event.getCode()) {
            // Arrow keys
            case Z:
                if (mazeController.hoveredTile.row == 0)
                    return;
                mazeController.hoveredWall = WallDirection.TOP;
                break;
            case D:
                if (mazeController.hoveredTile.column == mazeController.maze.numCols - 1)
                    return;
                mazeController.hoveredWall = WallDirection.RIGHT;
                break;
            case S:
                if (mazeController.hoveredTile.row == mazeController.maze.numRows - 1)
                    return;
                mazeController.hoveredWall = WallDirection.BOTTOM;
                break;
            case Q:
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

        mazeController.renderMaze(false);
    }


@FXML
    private void MenuItemChargerOnAction(ActionEvent event) throws Exception{
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Charger un labyrinthe");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Fichier labyrinthe", "*.maze")
        );
        File file = fileChooser.showOpenDialog(primaryStage);
        if (file != null){
            MazeModel maze = MazeReader.read(file);
            mazeController.setMaze(maze);
            MenuItemMazeSave.setDisable(false);
        }
    }

    @FXML
    private void MenuItemSauvegarderOnAction(ActionEvent event) throws Exception{
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Sauvegarder un labyrinthe");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Fichier labyrinthe", "*.maze")
        );
        File file = fileChooser.showSaveDialog(primaryStage);
        if (file != null){
            MazeWriter.write(mazeController.maze, file);
        }
    }

    /**
     * When the user click on the "Résoudre" button
     */
    @FXML
    private void MazeButtonSolveOnAction(){
        if (mazeController.isGenerating 
        || selectedSolverMode == null 
        || selectedSolverAlgorithms == null
        || mazeController.maze == null) 
            return;

        solveMaze();
    }

    /**
     * Construct the maze on the mazeCanvas
     * @param mazeConfigurationController
     * The controller used to create the maze
     */
    private void constructMaze(MazeConfigurationController mazeConfigurationController){
        mazeController.maze = new MazeModel(mazeConfigurationController.getMazeNumRows(), mazeConfigurationController.getMazeNumColumns());
        // Use Kruskal algorithm to generate the maze
        KruskalMazeGenerator generator = new KruskalMazeGenerator(mazeController.maze, mazeConfigurationController, mazeConfigurationController.getMazeType());

        mazeController.isGenerating = true;
        // Show the maze generation depending on the mode
        switch (mazeConfigurationController.getGenerationMode()) {
            case GenerationMode.COMPLETE:
                // instant
                while (!generator.isComplete()){
                    generator.step();
                }
                mazeController.renderMaze(false);
                mazeController.isGenerating = false;
                break;

            case GenerationMode.STEP:
                SpinnerText spinner = new SpinnerText(4);
                LabelGenerationStatus.setText(spinner.getCurrentFrame());
                
                AnimationTimer timer = new AnimationTimer() {
                    private long lastUpdate = 0;

                    @Override
                    public void handle(long now) {
                        
                        if (now - lastUpdate >= Helpers.fpsToNanos(SpinnerGenerationSpeed.getValue())) {
                            lastUpdate = now;

                            if (!generator.isComplete()) {
                                generator.step();
                                mazeController.renderMaze(true);
                                spinner.nextFrame();
                                LabelGenerationStatus.setText(spinner.nextFrame());
                            } else {
                                stop();
                                mazeController.isGenerating = false;
                                LabelGenerationStatus.setText("Génération terminée");
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

    /**
     * Solve the maze with an algorithm and mode
     */
    private void solveMaze(){
        mazeController.isGenerating = true;
        
        mazeController.maze.resetTileStatus();
        

        switch (selectedSolverAlgorithms) {
            case SolveAlgorithms.DFS:
                solverAlgorithm = new RecursiveMazeSolver(mazeController);
                break;

            case SolveAlgorithms.BFS:
                solverAlgorithm = new BreadthFirstSolver(mazeController);
                break;

            case SolveAlgorithms.DJIKSTRA:
                solverAlgorithm = new DjikstraSolver(mazeController);
                break;
        
            default:
                break;
        }

        if (solverAlgorithm != null){
            switch (selectedSolverMode) {
                case GenerationMode.COMPLETE:
                    while (!solverAlgorithm.isComplete()){
                        solverAlgorithm.step();
                    }
                    mazeController.renderMaze(false);
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
                            if (now - lastUpdate >= Helpers.fpsToNanos(SpinnerGenerationSpeed.getValue())) {
                                lastUpdate = now;
    
                                LabelVisitedTiles.setText(String.format("Traitées : %d", solverAlgorithm.getVisitedCount()));
                                LabelPath.setText(String.format("Chemin final : %d", solverAlgorithm.getPathCount()));
                                boolean done = solverAlgorithm.step();
                                mazeController.renderMaze(false);
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
