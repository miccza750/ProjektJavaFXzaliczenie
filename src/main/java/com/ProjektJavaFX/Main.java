package main.java.com.ProjektJavaFX;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {
    static void main() {
        launch(Main.class);
    }
    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/main/resources/LoginForm.fxml"));
        Scene scene = new Scene(fxmlLoader.load(),311,423);
        stage.setScene(scene);
        stage.setTitle("login");
        stage.show();
    }
}
