import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {
    public static void main(String[] args) throws Exception {
        launch(args);
    }
    
    @Override
    public void start(Stage s) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("view/MazeView.fxml"));
        s.setTitle("CYnaspe");
        s.setScene(new Scene(root));
        s.show();
    }
}
