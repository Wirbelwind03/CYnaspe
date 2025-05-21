package controller;

import algorithms.DjikstraSolver;
import algorithms.ISolverAlgorithm;
import enums.DialogResult;
import enums.WallDirection;
import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainController {
    private MazeController mazeController;
    private ISolverAlgorithm solverAlgorithm;

    @FXML private Canvas mazeCanvas;

    @FXML private ToggleGroup MazeSolverGroup;

    @FXML
    public void initialize(){
        mazeController = new MazeController(mazeCanvas);

        mazeCanvas.setFocusTraversable(true);
        mazeCanvas.setOnKeyPressed(this::onMazeCanvasKeyPressed);
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
            mazeController.constructMaze(mazeConfigController);
        }
    }

    @FXML
    private void onMazeCanvasClicked(MouseEvent event) {
        if (mazeController.isGenerating) return;
        if (mazeController.maze == null && mazeController.hoveredTile != null) return;
        
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
        if (mazeController.maze == null && mazeController.hoveredTile == null) return;

        switch (event.getCode()) {
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

        Toggle selected = MazeSolverGroup.getSelectedToggle();
        if (mazeController.maze != null){
            solverAlgorithm = new DjikstraSolver(mazeController);
            AnimationTimer timer = new AnimationTimer() {
                private long lastUpdate = 0;
                
                @Override
                public void handle(long now) {
                    if (now - lastUpdate >= 100_000_000) {
                        lastUpdate = now;
                        boolean done = solverAlgorithm.step();
                        mazeController.renderMaze();
                        if (done) stop();
                    }
                }
            };
            timer.start();
        }
    }
}
