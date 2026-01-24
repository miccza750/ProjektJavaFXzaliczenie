
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class Start {
        @FXML
        private Button button1;
        @FXML
        private Button buttonPanele;
        @FXML
        private Pane Panel;
        SceneChange sceneChange = new SceneChange();

    @FXML
        public void initialize() {
            button1.setOnAction(e -> {
                Stage stage = (Stage) button1.getScene().getWindow();
                sceneChange.changeScene(stage,"WykresyPanelFXML.fxml","Statystyki");
                System.out.println("Przycisk button1 kliknięty!");
            });
            buttonPanele.setOnAction(e -> {
                Stage stage = (Stage) buttonPanele.getScene().getWindow();
                sceneChange.changeScene(stage,"plik2.fxml","Panele");
                System.out.println("Przycisk button1 kliknięty!");
            });
        }


}

