package main.java.com.ProjektJavaFX;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.sql.SQLException;

public class UserPanelController {
        @FXML
        private Button buttonStats;
        @FXML
        private Pane Panel;
        SceneChange sceneChange = new SceneChange();
    @FXML
    private Button backButton;
        @FXML
        public void initialize() {
            backButton.setOnAction(e -> {
                Stage stage = (Stage) backButton.getScene().getWindow();
                sceneChange.changeScene(stage, "LoginForm.fxml", "Login");
            });
            buttonStats.setOnAction(e -> {
                try {
                    DBConnect.getConnection();
                    Stage stage = (Stage) buttonStats.getScene().getWindow();
                    sceneChange.changeScene(stage, "UserCharts.fxml", "Statystyki");
                }catch (SQLException ex){
                    System.out.println("bład");
                }
            });
        }
    }



