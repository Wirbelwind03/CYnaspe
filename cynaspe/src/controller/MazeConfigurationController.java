package controller;

import java.util.Random;

import enums.DialogResult;
import enums.GenerationMode;
import enums.MazeType;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import utils.Helpers;

public class MazeConfigurationController extends Controller {

    public DialogResult dialogResult = DialogResult.CLOSED;

    @FXML private GridPane GridPaneRoot;

    // @FXML private Spinner<Integer> SpinnerSeed;
    @FXML private TextField TextFieldSeed;
    
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

        // SpinnerValueFactory<Integer> seedValueFactory = 
        //     new SpinnerValueFactory.IntegerSpinnerValueFactory(0,Integer.MAX_VALUE);
        // seedValueFactory.setValue(Math.abs(random.nextInt()));
        // SpinnerSeed.setValueFactory(seedValueFactory);
        Helpers.restrictToNumericInput(TextFieldSeed, 0, Integer.MAX_VALUE);
        TextFieldSeed.setText(String.valueOf(Math.abs(random.nextInt())));

        SpinnerValueFactory<Integer> mazeRowsValueFactory =
            new SpinnerValueFactory.IntegerSpinnerValueFactory(2,256);
        mazeRowsValueFactory.setValue(16);

        SpinnerValueFactory<Integer> mazeColsValueFactory =
            new SpinnerValueFactory.IntegerSpinnerValueFactory(2,256);
        mazeColsValueFactory.setValue(16);

        SpinnerNumCols.setValueFactory(mazeColsValueFactory);
        SpinnerNumRows.setValueFactory(mazeRowsValueFactory);

        RadioButtonPerfectType.setUserData(MazeType.PERFECT);
        RadioButtonImperfectType.setUserData(MazeType.IMPERFECT);
        RadioButtonGenerationModeComplete.setUserData(GenerationMode.COMPLETE);
        RadioButtonGenerationModeStep.setUserData(GenerationMode.STEP);
    }

    /**
     * When the "OK" button has been clicked
     */
    @FXML
    private void ButtonOK_Click(){
        // Close the configuration window
        Stage stage = (Stage) GridPaneRoot.getScene().getWindow();
        stage.close();
        // Return a OK dialog result
        dialogResult = DialogResult.OK;
    }

    /**
     * Get the seed of the maze from the TextField
     * @return
     * The seed of the maze
     */
    public int getMazeSeed(){
        return Integer.parseInt(TextFieldSeed.getText());
    }

    /**
     * Get the number of rows of the maze from the Spinner
     * @return
     * The number of rows of the maze
     */
    public int getMazeNumRows(){
        return SpinnerNumRows.getValue();
    }

    /**
     * Get the number of columns of the maze from the Spinner
     * @return
     * The number of columns of the maze
     */
    public int getMazeNumColumns(){
        return SpinnerNumCols.getValue();
    }

    /**
     * Get the generation mode of the maze
     * @return
     * The generation mode used for the maze
     */
    public GenerationMode getGenerationMode(){
        Toggle selected = GenerationModeGroup.getSelectedToggle();
        return selected != null ? (GenerationMode) selected.getUserData() : null;
    }

    /**
     * Get the type of maze
     * @return
     * The type of maze (perfect or imperfect) used for the generation
     */
    public MazeType getMazeType(){
        Toggle selected = MazeTypeGroup.getSelectedToggle();
        return selected != null ? (MazeType) selected.getUserData() : null;
    }
}
