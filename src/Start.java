
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Start {
        @FXML
        private Button button1;
        @FXML
        private Button buttonPanele;
        @FXML
        private Button buttonSumylacja;
        @FXML
        private Pane Panel;
    @FXML
    private ScrollPane ErrorDisplay;
    @FXML
    private TextArea errorArea;
        SceneChange sceneChange = new SceneChange();
    public void showError(Exception e) {
        errorArea.appendText(
                "\n-------- BŁĄD ---------\nBŁĄD POŁĄCZENIA Z BAZĄ DANYCH"
        );
    }

    @FXML
        public void initialize() {
            button1.setOnAction(e -> {
                try {
                    DBConnect dbConnect = new DBConnect();
                    dbConnect.getConnection();
                    Stage stage = (Stage) button1.getScene().getWindow();
                    sceneChange.changeScene(stage, "Charts.fxml", "Statystyki");
                } catch (Exception ex) {
                    showError(ex);
                }
            });
            buttonPanele.setOnAction(e -> {
                Stage stage = (Stage) buttonPanele.getScene().getWindow();
                sceneChange.changeScene(stage, "Tables.fxml","Panele");
                System.out.println("Przycisk button1 kliknięty!");
            });
        buttonSumylacja.setOnAction(e -> {
            Stage stage = (Stage) buttonSumylacja.getScene().getWindow();
            sceneChange.changeScene(stage,"SymulatorPanels.fxml","Panele");
            System.out.println("Przycisk button1 kliknięty!");
        });
        }


}

