
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class LoginFormConroller{
    @FXML
    private Pane Panel;
    @FXML
    private ScrollPane ErrorDisplay;
    @FXML
    private TextArea errorArea;
    SceneChange sceneChange = new SceneChange();
    public void showError(String e) {
        errorArea.appendText(
                "\n-------- BŁĄD ---------\n"+e
        );
    }


}

