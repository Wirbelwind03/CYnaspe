package controller;

import java.util.Random;

import javafx.fxml.FXML;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MazeConfigurationController {

    @FXML
    private VBox VBRoot;

    @FXML
    private Spinner<Integer> SpinnerSeed;
    
    @FXML
    private Spinner<Integer> SpinnerMazeCols;
    @FXML
    private Spinner<Integer> SpinnerMazeRows;

    @FXML
    public void initialize(){
        Random random = new Random();

        SpinnerValueFactory<Integer> seedValueFactory = 
            new SpinnerValueFactory.IntegerSpinnerValueFactory(0,Integer.MAX_VALUE);
        seedValueFactory.setValue(Math.abs(random.nextInt()));

        SpinnerSeed.setValueFactory(seedValueFactory);

        SpinnerValueFactory<Integer> mazeRowsValueFactory =
            new SpinnerValueFactory.IntegerSpinnerValueFactory(2,6);
        mazeRowsValueFactory.setValue(2);

        SpinnerValueFactory<Integer> mazeColsValueFactory =
            new SpinnerValueFactory.IntegerSpinnerValueFactory(2,6);
        mazeColsValueFactory.setValue(2);

        SpinnerMazeCols.setValueFactory(mazeColsValueFactory);
        SpinnerMazeRows.setValueFactory(mazeRowsValueFactory);
    }

    @FXML
    private void BOK_Click(){
        Stage stage = (Stage) VBRoot.getScene().getWindow();
        stage.close();
    }

    public int getMazeSeed(){
        return SpinnerSeed.getValue();
    }

    public int getMazeNumRows(){
        return SpinnerMazeRows.getValue();
    }

    public int getMazeNumColumns(){
        return SpinnerMazeCols.getValue();
    }
}
