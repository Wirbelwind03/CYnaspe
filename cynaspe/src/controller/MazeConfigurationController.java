package controller;

import java.util.Random;

import enums.DialogResult;
import enums.GenerationMode;
import enums.MazeType;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class MazeConfigurationController {

    public DialogResult dialogResult = DialogResult.CLOSED;

    @FXML private GridPane GridPaneRoot;

    @FXML private Spinner<Integer> SpinnerSeed;
    
    @FXML private Spinner<Integer> SpinnerNumCols;
    @FXML private Spinner<Integer> SpinnerNumRows;

    @FXML
    private ToggleGroup MazeTypeGroup;
    @FXML
    private ToggleGroup GenerationModeGroup;

    @FXML private RadioButton RadioButtonPerfectType;
    @FXML private RadioButton RadioButtonImperfectType;
    @FXML private RadioButton RadioButtonGenerationModeComplete;
    @FXML private RadioButton RadioButtonGenerationModeStep;

    @FXML
    public void initialize(){
        Random random = new Random();

        SpinnerValueFactory<Integer> seedValueFactory = 
            new SpinnerValueFactory.IntegerSpinnerValueFactory(0,Integer.MAX_VALUE);
        seedValueFactory.setValue(Math.abs(random.nextInt()));

        SpinnerSeed.setValueFactory(seedValueFactory);

        SpinnerValueFactory<Integer> mazeRowsValueFactory =
            new SpinnerValueFactory.IntegerSpinnerValueFactory(2,256);
        mazeRowsValueFactory.setValue(2);

        SpinnerValueFactory<Integer> mazeColsValueFactory =
            new SpinnerValueFactory.IntegerSpinnerValueFactory(2,256);
        mazeColsValueFactory.setValue(2);

        SpinnerNumCols.setValueFactory(mazeColsValueFactory);
        SpinnerNumRows.setValueFactory(mazeRowsValueFactory);

        RadioButtonPerfectType.setUserData(MazeType.PERFECT);
        RadioButtonImperfectType.setUserData(MazeType.IMPERFECT);
        RadioButtonGenerationModeComplete.setUserData(GenerationMode.COMPLETE);
        RadioButtonGenerationModeStep.setUserData(GenerationMode.STEP);
    }

    @FXML
    private void ButtonOK_Click(){
        Stage stage = (Stage) GridPaneRoot.getScene().getWindow();
        stage.close();
        dialogResult = DialogResult.OK;
    }

    public int getMazeSeed(){
        return SpinnerSeed.getValue();
    }

    public int getMazeNumRows(){
        return SpinnerNumRows.getValue();
    }

    public int getMazeNumColumns(){
        return SpinnerNumCols.getValue();
    }

    public GenerationMode getGenerationMode(){
        Toggle selected = GenerationModeGroup.getSelectedToggle();
        return selected != null ? (GenerationMode) selected.getUserData() : null;
    }

    public MazeType getMazeType(){
        Toggle selected = MazeTypeGroup.getSelectedToggle();
        return selected != null ? (MazeType) selected.getUserData() : null;
    }
}
