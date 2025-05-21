package controller;

import enums.DialogResult;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainController {
    private MazeController mazeController;

    @FXML
    private Canvas mazeCanvas;

    @FXML
    public void initialize(){
        mazeController = new MazeController(mazeCanvas);
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

    public void onMazeCanvasClicked(javafx.scene.input.MouseEvent event) {
        if (mazeController.maze == null) return;
    
        double x = event.getX();
        double y = event.getY();
    
        double tileSize = mazeController.getTileSize();
    
        int column = (int)(x / tileSize);
        int row = (int)(y / tileSize);
    
        if (mazeController.isInsideMaze(row, column)) {
            System.out.println("Clicked tile: row = " + row + ", column = " + column);
        }
    }
}
