import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.Date;

public class AddController {
    @FXML private TextField nameField;
    @FXML private DatePicker datePicker;
    @FXML private Button saveButton;
    @FXML
    public void initialize() {
        saveButton.setOnAction(e -> {
            try {
                String name = nameField.getText();
                LocalDate ldate = datePicker.getValue();
                java.sql.Date sqlDate = java.sql.Date.valueOf(ldate);
                DBConnect connection = new DBConnect();
                connection.AddRowtoListOfPanels(name,sqlDate);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Informacja");
                alert.setHeaderText(null);
                alert.setContentText("Panel został dodany poprawnie!");
                alert.showAndWait();

                Stage stage = (Stage) saveButton.getScene().getWindow();
                stage.close();
            }catch (Exception ex){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Informacja");
                alert.setHeaderText(null);
                alert.setContentText("błąd: " + ex.getMessage());
                alert.showAndWait();
            }
        });
    }
}