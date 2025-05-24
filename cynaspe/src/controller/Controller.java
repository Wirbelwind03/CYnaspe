package controller;

import javafx.stage.Stage;

/**
 * Abstract base class for JavaFX controllers.
 * <p>
 * Provides common functionality for controllers, such as managing the primary stage
 * of the application window.
 * </p>
 */
public abstract class Controller {
    /**
     * Default constructor.
     */
    public Controller() {
        
    }


    /** The primary stage of the JavaFX application */
    public Stage primaryStage; 

    /**
     * Set the primary stage of the controller
     * Called by the application when initializing the controller
     * @param stage
     * A {@code Stage} that represent primary stage of the application
     */
    public void setPrimaryStage(Stage stage) {
        primaryStage = stage;
    }
}