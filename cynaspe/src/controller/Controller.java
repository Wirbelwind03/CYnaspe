package controller;

import javafx.stage.Stage;

public abstract class Controller {
    public Stage primaryStage;

    public void setPrimaryStage(Stage stage) {
        primaryStage = stage;
    }
}