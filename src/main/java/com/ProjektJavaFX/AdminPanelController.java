package main.java.com.ProjektJavaFX;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class AdminPanelController {
        @FXML
        private Button buttonStats;
        @FXML
        private Button buttonPanels;
        @FXML
        private Button buttonSumaction;
        @FXML
        private Pane Panel;
    @FXML
    private ScrollPane ErrorDisplay;
    @FXML
    private TextArea errorArea;
    @FXML
    private Button backButton;
        SceneChange sceneChange = new SceneChange();
    public void showError(Exception e) {
        errorArea.appendText(
                "\n-------- BŁĄD ---------\nBŁĄD POŁĄCZENIA Z BAZĄ DANYCH"
        );
    }

    @FXML
        public void initialize() {
        buttonStats.setOnAction(e -> {
                try {
                    DBConnect dbConnect = new DBConnect();
                    dbConnect.getConnection();
                    Stage stage = (Stage) buttonStats.getScene().getWindow();
                    sceneChange.changeScene(stage, "Charts.fxml", "Statystyki");
                } catch (Exception ex) {
                    showError(ex);
                }
            });
        buttonPanels.setOnAction(e -> {
            try {
                DBConnect dbConnect = new DBConnect();
                dbConnect.getConnection();
                Stage stage = (Stage) buttonPanels.getScene().getWindow();
                sceneChange.changeScene(stage, "Tables.fxml", "Panele");
            }catch (Exception ex) {
                showError(ex);
            }
            });
        buttonSumaction.setOnAction(e -> {
            Stage stage = (Stage) buttonSumaction.getScene().getWindow();
            sceneChange.changeScene(stage,"SymulatorPanels.fxml","Dodaj symulacyjne dane");
        });
        backButton.setOnAction(e -> {
            Stage stage = (Stage) backButton.getScene().getWindow();
            sceneChange.changeScene(stage,"LoginForm.fxml","Login");
        });
        }


}

