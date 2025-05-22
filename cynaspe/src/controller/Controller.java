package controller;

import javafx.stage.Stage;

public abstract class Controller {
    public Stage primaryStage;

    /**
     * Set the primary stage of the controller
     * Called by the application when initializing the controller
     * @param stage
     * The primary stage of the application
     */
    public void setPrimaryStage(Stage stage) {
        primaryStage = stage;
    }
}