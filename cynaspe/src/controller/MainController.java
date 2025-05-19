package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainController {
    private MazeConfigurationController mazeConfigController;

    @FXML
    private VBox VBContainer;

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

        createMaze();        
    }

    private void createMaze() throws Exception {
        // Show the MazeView
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/MazeView.fxml"));
        Parent root = loader.load();

        MazeController mazeController = loader.getController();
        mazeController.constructMaze(mazeConfigController);

        VBContainer.getChildren().add(root);
    }
}
