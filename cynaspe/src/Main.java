import java.util.Scanner;

import controller.MazeConsoleController;
import controller.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    private static String launchMode;

    public static void main(String[] args) throws Exception {
        // Default to UI if no argument is provided
        launchMode = (args.length > 0) ? args[0].toLowerCase() : "ui";

        if (launchMode.equals("console")){
            runConsoleMode();
        } else {
            launch(args);
        }
    }
    
    @Override
    public void start(Stage s) throws Exception {
        if (!launchMode.equals("ui")) {
            return;
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("view/MainView.fxml"));
        Parent root = loader.load();

        MainController mainController = loader.getController();
        mainController.setPrimaryStage(s);
        
        s.setTitle("CYnaspe");
        s.setScene(new Scene(root));
        s.show();
    }

    /**
     * Run the program in console mode
     */
    private static void runConsoleMode(){
        System.out.println("Console mode");
        Scanner scanner = new Scanner(System.in);
        MazeConsoleController consoleController = new MazeConsoleController(scanner);
        consoleController.run();
        System.out.println("Console mode fini");
        scanner.close(); 
        System.exit(0);
    }
}
