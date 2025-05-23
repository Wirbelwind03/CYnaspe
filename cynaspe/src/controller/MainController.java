package controller;

import java.io.File;

import enums.DialogResult;
import enums.GenerationMode;
import enums.SolveAlgorithms;
import io.MazeReader;
import io.MazeWriter;
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
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.MazeModel;
import utils.Helpers;

public class MainController extends Controller {
    private MazeController mazeController;
    private MazeCanvasController mazeCanvasController;
    private MazeSolverController mazeSolverController;
    private MazeGenerationController mazeGenerationController;

    private GenerationMode selectedSolverMode = null;
    private SolveAlgorithms selectedSolverAlgorithms = null;

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
            new SpinnerValueFactory.IntegerSpinnerValueFactory(1,60);
        generationSpeedValueFactory.setValue(10);
        SpinnerGenerationSpeed.setValueFactory(generationSpeedValueFactory);

        // Create the maze controller for the mazeCanvas
        mazeController = new MazeController(mazeCanvas);
        mazeCanvasController = new MazeCanvasController(mazeCanvas, mazeController);
        //
        mazeSolverController = new MazeSolverController(
            mazeController, 
            SpinnerGenerationSpeed, 
            LabelGenerationStatus, 
            LabelVisitedTiles, 
            LabelPath, 
            LabelGenerationTime, 
            MazeButtonSolve
        );
        mazeSolverController.setOnSolvingStarted(() -> {
            MazeButtonSolve.setDisable(true);
            // Desactivate these menu items
            MenuItemMazeNew.setDisable(true);
            // The user shouldn't be able to load a maze during the generation
            MenuItemMazeLoad.setDisable(true);
            // The user shouldn't be able to save a unfinished maze
            MenuItemMazeSave.setDisable(true);
        });
        mazeSolverController.setOnSolvingFinished(() -> {
            MazeButtonSolve.setDisable(false);
            // Activate these menu items
            MenuItemMazeNew.setDisable(false);
            MenuItemMazeLoad.setDisable(false);
            MenuItemMazeSave.setDisable(false);
        });
        //
        mazeGenerationController = new  MazeGenerationController(
            mazeController, 
            SpinnerGenerationSpeed, 
            LabelGenerationStatus
        );
        mazeGenerationController.setOnGenerationStarted(() -> {
            // Desactivate these menu items
            // The user shouldn't be able to load a maze during the generation
            MenuItemMazeLoad.setDisable(true);
            // The user shouldn't be able to save a unfinished maze
            MenuItemMazeSave.setDisable(true);
            // The mazeController is currently generating
            mazeController.isGenerating = true;
        });
        mazeGenerationController.setOnGenerationFinished(() -> {
            // The mazeController isn't generating anymore
            mazeController.isGenerating = false;
            // Update the label to tell the generation has finished
            LabelGenerationStatus.setText("Génération terminée");
            // If there has been a solver mode and solver algorithm selected during the generation
            // Activate this button
            if (selectedSolverMode != null && selectedSolverAlgorithms != null){
                MazeButtonSolve.setDisable(false);
            }
            
            MenuItemMazeLoad.setDisable(false);
            // Activate this menu item so the maze can saved
            MenuItemMazeSave.setDisable(false);
        });


        // Initialize
        RadioButtonMazeSolverDFS.setUserData(SolveAlgorithms.DFS);
        RadioButtonMazeSolverBFS.setUserData(SolveAlgorithms.BFS);
        RadioButtonMazeSolverDjisktra.setUserData(SolveAlgorithms.DJIKSTRA);
        MazeSolverGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            selectedSolverAlgorithms = Helpers.getSelectedUserData(MazeSolverGroup);
            mazeSolverController.setSelectedAlgorithm(selectedSolverAlgorithms);
            if (selectedSolverMode != null && mazeController.hasMaze() && !mazeController.isGenerating){
                MazeButtonSolve.setDisable(newToggle == null);
            }
        });

        RadioButtonMazeSolverModeComplete.setUserData(GenerationMode.COMPLETE);
        RadioButtonMazeSolverModeStep.setUserData(GenerationMode.STEP);
        MazeSolverModeGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            selectedSolverMode = Helpers.getSelectedUserData(MazeSolverModeGroup);
            mazeSolverController.setSelectedMode(selectedSolverMode);
            if (selectedSolverAlgorithms != null && mazeController.hasMaze() && !mazeController.isGenerating) {
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
            mazeGenerationController.constructMaze(mazeConfigurationController);
        }
    }

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
        mazeSolverController.solve();
    }
}