package controller;

import enums.DialogResult;
import enums.WallDirection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainController {
    private MazeController mazeController;

    @FXML
    private Canvas mazeCanvas;

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
    public void onMazeCanvasClicked(MouseEvent event) {
        if (mazeController.isGenerating) return;
        if (mazeController.maze == null && mazeController.hoveredTile != null) return;
        
        mazeController.addWall(mazeController.hoveredTile, mazeController.hoveredWall);

        mazeController.renderMaze();
    }

    @FXML
    public void onMazeCanvasMouseMoved(MouseEvent event) {
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
    public void onMazeCanvasKeyPressed(KeyEvent event){
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
}
