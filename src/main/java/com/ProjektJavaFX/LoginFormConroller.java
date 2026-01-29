package main.java.com.ProjektJavaFX;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.sql.*;

public class LoginFormConroller {

    @FXML
    private Pane Panel;
    @FXML
    private ScrollPane ErrorDisplay;
    @FXML
    private TextArea errorArea;
    @FXML
    private TextField login;
    @FXML
    private PasswordField password;
    @FXML
    private Button buttonLogin;

    SceneChange sceneChange = new SceneChange();

    public void showError(String e) {
        errorArea.appendText("\n-------- BŁĄD ---------\n" + e);
    }

    @FXML
    public void initialize() {
        buttonLogin.setOnAction(e -> {
            String name = login.getText();
            String pass = password.getText();
            if (name.isEmpty() || pass.isEmpty()) {
                showError("Wprowadź login i hasło!");
                return;
            }
            if(name.equals("admin") && pass.equals("admin")){
                Stage stage = (Stage) buttonLogin.getScene().getWindow();
                sceneChange.changeScene(stage, "AdminPanel.fxml", "Admin Panel");
            }
            try (Connection conn = DBConnect.getConnection()) {
                String sql = "SELECT password FROM consumers WHERE username=?";
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, name);
                    try (ResultSet rs = stmt.executeQuery()) {
                        if (rs.next()) {
                            String storedHash = rs.getString("password");
                            if (pass.equals(storedHash)) {
                                Stage stage = (Stage) buttonLogin.getScene().getWindow();
                                sceneChange.changeScene(stage, "UserPanel.fxml", "User Panel");
                                Session.getInstance().setName(name);

                            } else {
                                showError("Niepoprawne hasło!");
                            }
                        } else {
                            showError("Użytkownik nie istnieje!");
                        }
                    }
                }
            } catch (SQLException ex) {
                showError("Błąd połączenia z bazą: " + ex.getMessage());
            }
        });
    }
}