package controller;

import enums.DialogResult;
import enums.GenerationMode;
import enums.TileStatus;
import enums.WallDirection;
import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.MazeModel;
import model.TileModel;
import utils.KruskalMazeGenerator;

public class MainController {
    private MazeConfigurationController mazeConfigController;
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
        mazeConfigController = loader.getController();

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
}
