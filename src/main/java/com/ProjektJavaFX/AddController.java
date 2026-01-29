package main.java.com.ProjektJavaFX;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.time.LocalDate;
import main.java.com.ProjektJavaFX.exceptions.*;
import java.util.Date;
public class AddController {
    @FXML private TextField IdFarmField;
    @FXML private DatePicker datePicker;
    @FXML private Button saveButton;
    @FXML
    public void initialize(){
        saveButton.setOnAction(e -> {
            try {
                DBConnect connection = new DBConnect();
                int idFarm;
                try {
                    idFarm = Integer.parseInt(IdFarmField.getText());
                    if (!connection.farmExists(idFarm)) {
                        throw new WrongIndexIdFarm("Farma o podanym ID nie istnieje!");
                    }
                } catch (NumberFormatException err) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Informacja");
                    alert.setHeaderText(null);
                    alert.setContentText("błąd: " + err.getMessage());
                    alert.showAndWait();
                    return;
                }
                LocalDate ldate = datePicker.getValue();
                java.sql.Date sqlDate = java.sql.Date.valueOf(ldate);
                connection.AddRowtoListOfPanels(sqlDate,idFarm,"panels");

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