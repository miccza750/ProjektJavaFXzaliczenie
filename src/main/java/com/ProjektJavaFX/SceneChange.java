package main.java.com.ProjektJavaFX;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;

import java.io.IOException;

public class SceneChange {
    public void changeScene(Stage stage, String fxmlFile, String title) {
        try {
            String fxml = "/main/resources/"+fxmlFile;
            Parent root = FXMLLoader.load(getClass().getResource(fxml));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle(title);
            stage.show();

            System.out.println("Scena zmieniona na: " + fxmlFile);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Błąd przy zmianie sceny na: " + fxmlFile);
        }
    }
}
