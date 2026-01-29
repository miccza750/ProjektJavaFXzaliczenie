package main.java.com.ProjektJavaFX;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SymulatorController {
    @FXML private DatePicker datePicker;
    @FXML private Button saveButton;
    @FXML private Button backButton;
    SceneChange sceneChange = new SceneChange();
    @FXML
    public List<Integer> getAllRow(EnergyType type) {
        List<Integer> rows = new ArrayList<>();
        String sql = (type== EnergyType.PANEL) ? "SELECT panel_id FROM panels WHERE status = 1":
                "SELECT consumer_id FROM consumers";
        try {
            DBConnect connection = new DBConnect();
            PreparedStatement stmt = connection.getConnection().prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            String column = (type== EnergyType.PANEL) ? "panel_id" : "consumer_id";
            while (rs.next()) {
                rows.add(rs.getInt(column));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rows;
    }
    public void insertDayForPanel(int id, LocalDate day, EnergyType type) {
        String sql = (type == EnergyType.PANEL) ?
                "INSERT INTO panel_energy (panel_id, timestamp, power) VALUES (?, ?, ?)" :
                "INSERT INTO consumer_energy (consumer_id, timestamp, energy_kwh) VALUES (?, ?, ?)";
        try {
            DBConnect connection = new DBConnect();
            PreparedStatement stmt = connection.getConnection().prepareStatement(sql);
            LocalDateTime time = day.atStartOfDay();
            LocalDateTime end = day.plusDays(1).atStartOfDay();

            while (time.isBefore(end)) {
                stmt.setInt(1, id);
                stmt.setTimestamp(2, Timestamp.valueOf(time));
                stmt.setDouble(3, generatePower(time,id,type));
                stmt.addBatch();
                time = time.plusMinutes(5);
            }
            stmt.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private double generatePower(LocalDateTime time, int id, EnergyType type) {
        int hour = time.getHour();
        if (type == EnergyType.PANEL) {
            if (hour >= 6 && hour <= 18)
                return (Math.round(Math.sin((hour - 6) / 12.0 * Math.PI) * 600 + 200) + (Math.random() - 0.5) * 40 / 100) * 100 * (id * 0.5);
            return 0;
        }
        if (type == EnergyType.CONSUMER) {
            if (hour >= 6 && hour <= 18)
                return Math.round((0.1 + (id % 5) * 0.05 + Math.random()*0.6));
            return 0;
        }
        return 0;
        }

    public void initialize(){
        backButton.setOnAction(e -> {
            Stage stage = (Stage) backButton.getScene().getWindow();
            sceneChange.changeScene(stage, "AdminPanel.fxml", "Panel Admina");
        });
        saveButton.setOnAction(e -> {
            LocalDate date =datePicker.getValue();
            String sql = "SELECT 1 " +
                    "FROM panel_energy " +
                    "WHERE DATE(timestamp) = '" +date+
                    "' LIMIT 1;";
            try {
                DBConnect connection = new DBConnect();
                PreparedStatement stmt = connection.getConnection().prepareStatement(sql);
                ResultSet rs = stmt.executeQuery();
                System.out.println(sql);
                if (!rs.next()) {
                    List<Integer> panels = this.getAllRow(EnergyType.PANEL);
                    List<Integer> consumers = this.getAllRow(EnergyType.CONSUMER);
                    System.out.println(consumers.toString());
                    if (panels.isEmpty() || consumers.isEmpty()) {
                        System.out.println("brak rekordów");
                        return;
                    }
                        for (int panelId : panels) {
                            this.insertDayForPanel(panelId, date,EnergyType.PANEL);
                        }
                    for (int ConsumerId : consumers) {
                        this.insertDayForPanel(ConsumerId, date,EnergyType.CONSUMER);
                    }
                    }
                }catch (Exception ex){
                System.out.println(ex.getMessage());
            }
});
    }
}

