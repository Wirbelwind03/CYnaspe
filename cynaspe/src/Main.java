import java.util.Scanner;

import controller.MazeConsoleController;
import controller.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main entry point for the application.
 * <p>
 * Rwo modes:
 * <ul>
 *   <li>UI mode (default) - launches the JavaFX GUI</li>
 *   <li>Console mode - runs a text-based console interface</li>
 * </ul>
 * Command-line argument:
 * <pre>
 *   java Main ui      // launch GUI
 *   java Main console // launch console mode
 * </pre>
 * </p>
 */
public class Main extends Application {
    /**
     * The mode to launch the application in ("ui" or "console").
     */
    private static String launchMode;

    /**
     * Main method, entry point of the application.
     * Decides whether to run the UI or console mode based on arguments.
     * @param args
     * command-line arguments; the first argument can specify the launch mode ("ui" or "console")
     * @throws Exception
     * if an error occurs during launch
     */
    public static void main(String[] args) throws Exception {
        // Default to UI if no argument is provided
        launchMode = (args.length > 0) ? args[0].toLowerCase() : "ui";

        if (launchMode.equals("console")){
            runConsoleMode();
        } else {
            launch(args);
        }
    }
    
    /**
     * JavaFX start method. Initializes and displays the main application window.
     * Only runs if the launch mode is "ui".
     * @param s 
     * the primary stage for this application
     * @throws Exception 
     * if loading the FXML fails
     */
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
