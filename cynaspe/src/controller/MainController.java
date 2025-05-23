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

    private AnimationTimer solverTimer = null;

    @FXML private Spinner<Integer> SpinnerGenerationSpeed; // Spinner that change the speed of the generation or the solve
    @FXML private Label LabelGenerationStatus; // Label that show the generation or solve status

    @FXML private Canvas mazeCanvas; // Canvas for rendering the maze

    // Radio buttons group for the maze solving algorithm
    @FXML private ToggleGroup MazeSolverGroup; 
    @FXML private RadioButton RadioButtonMazeSolverDFS;
    @FXML private RadioButton RadioButtonMazeSolverBFS;
    @FXML private RadioButton RadioButtonMazeSolverDjisktra;
    // Radio buttons group for the solving mode 
    @FXML private ToggleGroup MazeSolverModeGroup; 
    @FXML private RadioButton RadioButtonMazeSolverModeComplete;
    @FXML private RadioButton RadioButtonMazeSolverModeStep;

    @FXML private Button MazeButtonSolve; // "Résoudre" button

    @FXML private Label LabelPath; // Label showing the number of path tiles 
    @FXML private Label LabelVisitedTiles; // Label showing the number of tiles visited
    @FXML private Label LabelGenerationTime; // Label showing the maze solving generation time

    @FXML private MenuItem MenuItemMazeNew;
    @FXML private MenuItem MenuItemMazeLoad;
    @FXML private MenuItem MenuItemMazeSave;

    @FXML
    public void initialize(){
        // Set the value of the spinner for the generation speed
        SpinnerValueFactory<Integer> generationSpeedValueFactory = 
            new SpinnerValueFactory.IntegerSpinnerValueFactory(1,120);
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
            if (selectedSolverMode != null && mazeController.hasMaze() && !mazeController.isGenerating){
                MazeButtonSolve.setDisable(newToggle == null);
            }
        });

        RadioButtonMazeSolverModeComplete.setUserData(GenerationMode.COMPLETE);
        RadioButtonMazeSolverModeStep.setUserData(GenerationMode.STEP);
        MazeSolverModeGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            selectedSolverMode = Helpers.getSelectedUserData(MazeSolverModeGroup);
            if (selectedSolverAlgorithms != null && mazeController.hasMaze() && !mazeController.isGenerating) {
                MazeButtonSolve.setDisable(newToggle == null);
            }
        });
    }

    /**
     * 
     * @param event
     * @throws Exception
     */
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
        }
    }

    //#region MazeCanvas events

    /**
     * When the user mouse hover over the MazeCanvas
     * @param event
     * The mouse hovering over
     */
    @FXML
    private void onMazeCanvasEntered(MouseEvent event){
        mazeCanvas.requestFocus();
    }

    /**
     * When the user mouse click on the MazeCanvas
     * @param event
     * The mouse that has clicked
     */
    @FXML
    private void onMazeCanvasClicked(MouseEvent event) {
        if (mazeController.isGenerating || !mazeController.hasMaze() || mazeController.hoveredTile == null) return;
        
        // Add a wall with the direction the user want
        mazeController.addWall(mazeController.hoveredTile, mazeController.hoveredWall);

        // Render the maze with the new added wall
        mazeController.renderMaze(false);
    }

    /**
     * When the user mouse move on the canvas
     * @param event
     * The mouse that has entered the canvas
     */
    @FXML
    private void onMazeCanvasMouseMoved(MouseEvent event) {
        if (mazeController.isGenerating || !mazeController.hasMaze()) return;
    
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
            mazeController.hoveredTile = mazeController.getTile(row, column); 
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
        if (mazeController.isGenerating || !mazeController.hasMaze() || mazeController.hoveredTile == null) return;

        switch (event.getCode()) {
            // ZQSD keys
            case Z:
                // If the user is at the top border
                if (mazeController.hoveredTile.row == 0)
                    return;
                mazeController.hoveredWall = WallDirection.TOP;
                break;
            case D:
                // If the user is at the right border
                if (mazeController.hoveredTile.column == mazeController.maze.numCols - 1)
                    return;
                mazeController.hoveredWall = WallDirection.RIGHT;
                break;
            case S:
                // If the user is at the bottom border
                if (mazeController.hoveredTile.row == mazeController.maze.numRows - 1)
                    return;
                mazeController.hoveredWall = WallDirection.BOTTOM;
                break;
            case Q:
                // If the user is at the left border
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

        // Render the wall that is going to be added
        mazeController.renderMaze(false);
    }

    //#endregion

    //#region MenuItem

    /**
     * When the user click on the MenuItem "Charger"
     * @param event
     * The action done on the menuItem
     */
    @FXML
    private void MenuItemChargerOnAction(ActionEvent event) {
        try {
            File file = fileDialogForMazeFile("Charger un labyrinthe", false);
            // If a file has been opened
            if (file != null){
                // Read the maze file and set it on the mazeController
                MazeModel maze = MazeReader.read(file);
                mazeController.setMaze(maze);
                // Activate the menu item to save a maze
                MenuItemMazeSave.setDisable(false);
            }
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * When the user click on the MenuItem "Sauvegarder"
     * @param event
     * The action done on the menuItem 
     */
    @FXML
    private void MenuItemSauvegarderOnAction(ActionEvent event){
        try {
            File file = fileDialogForMazeFile("Sauvegarder un labyrinthe", true);
            if (file != null) {
                // Write the maze to the file
                MazeWriter.write(mazeController.maze, file);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Open a file dialog, used for actions tied to the maze file
     * @param title
     * The title of the file dialog
     * @param saveMode
     * True if the user is saving a maze file, False if the user is opening a maze file
     * @return
     * File that has been opened
     */
    private File fileDialogForMazeFile(String title, boolean saveMode) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichier labyrinthe", "*.maze"));
        return saveMode ? fileChooser.showSaveDialog(primaryStage) : fileChooser.showOpenDialog(primaryStage);
    }

    //#endregion

    /**
     * When the user click on the "Résoudre" button
     */
    @FXML
    private void MazeButtonSolveOnAction(){
        if (mazeController.isGenerating  // If the maze is still generating
        || selectedSolverMode == null // If there's not a solving mode selected
        || selectedSolverAlgorithms == null // If there's not a solving algorithm selected
        || !mazeController.hasMaze())  // If there's not maze on the mazeController
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

        // Desactivate these menu items
        // The user shouldn't be able to load a maze during the generation
        MenuItemMazeLoad.setDisable(true);
        // The user shouldn't be able to save a unfinished maze
        MenuItemMazeSave.setDisable(true);

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
                MenuItemMazeLoad.setDisable(false);
                // Activate this menu item so the maze can saved
                MenuItemMazeSave.setDisable(false);
                break;

            case GenerationMode.STEP:
                // Show the spinner text
                SpinnerText spinner = new SpinnerText(4);
                LabelGenerationStatus.setText(spinner.getCurrentFrame());
                
                // Start the animation
                solverTimer = new AnimationTimer() {
                    private long lastUpdate = 0;

                    @Override
                    public void handle(long now) {
                        
                        if (now - lastUpdate >= Helpers.fpsToNanos(SpinnerGenerationSpeed.getValue())) {
                            lastUpdate = now;

                            if (!generator.isComplete()) {
                                // Show each step of the algorithm
                                generator.step();
                                mazeController.renderMaze(true);
                                // Show the next frame (aka character) of the spinner text
                                // Update the label that show the spinner text
                                LabelGenerationStatus.setText(spinner.nextFrame());
                            } else {
                                // When the algorithm has finished
                                stop();
                                mazeController.isGenerating = false;
                                LabelGenerationStatus.setText("Génération terminée");
                                // If there has been a solver mode and solver algorithm selected during the generation
                                // Activate this button
                                if (selectedSolverMode != null && selectedSolverAlgorithms != null){
                                    MazeButtonSolve.setDisable(false);
                                }
                                
                                MenuItemMazeLoad.setDisable(false);
                                // Activate this menu item so the maze can saved
                                MenuItemMazeSave.setDisable(false);
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

    /**
     * Solve the maze with an algorithm and mode
     */
    private void solveMaze(){
        mazeController.isGenerating = true;
        
        mazeController.resetTileStatus();
        // Disable the button until the solver has finished
        MazeButtonSolve.setDisable(true);

        // Desactivate these menu items
        MenuItemMazeNew.setDisable(true);
        // The user shouldn't be able to load a maze during the generation
        MenuItemMazeLoad.setDisable(true);
        // The user shouldn't be able to save a unfinished maze
        MenuItemMazeSave.setDisable(true);

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
                    finishedSolving();
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
                                // Jump to the next step of the algorithm
                                boolean done = solverAlgorithm.step();
                                mazeController.renderMaze(false);
                                updateSolverLabels();
                                // Show the next frame of the spinner
                                LabelGenerationStatus.setText(spinner.nextFrame());
                                // If the algorithm has finished
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

    /**
     * Update when the solver has finished
     */
    private void finishedSolving(){
        mazeController.isGenerating = false;
        updateSolverLabels();
        LabelGenerationStatus.setText("Traitement terminée");
        MenuItemMazeNew.setDisable(false);
        MazeButtonSolve.setDisable(false);
    }

    /**
     * Update label that show information of the solve
     */
    private void updateSolverLabels(){
        LabelVisitedTiles.setText(String.format("Traitées : %d", solverAlgorithm.getVisitedCount()));
        LabelPath.setText(String.format("Chemin final : %d", solverAlgorithm.getPathCount()));
        LabelGenerationTime.setText(String.format("Temps de génération : %d ms", solverAlgorithm.getExecutionTime()));
    }
}
