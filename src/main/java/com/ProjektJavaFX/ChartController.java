package main.java.com.ProjektJavaFX;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.sql.*;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ChartController {

    @FXML
    private LineChart<String, Number> lineChart;
    @FXML
    private CategoryAxis xAxis;
    @FXML
    private NumberAxis yAxis;
    @FXML
    private DatePicker fromDatePicker;
    @FXML
    private DatePicker toDatePicker;
    @FXML
    private ComboBox<String> ListOfFarms;
    SceneChange sceneChange = new SceneChange();
    @FXML
    private Button backButton;
    ObservableList<String> panelFarmNames = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        backButton.setOnAction(e -> {
            Stage stage = (Stage) backButton.getScene().getWindow();
            sceneChange.changeScene(stage, "AdminPanel.fxml", "Panel admina");
        });
        try (Connection conn = DBConnect.getConnection()) {
            try (Statement stmt = conn.createStatement()) {
                try (ResultSet rs = stmt.executeQuery("SELECT name FROM farms")) {
                    while (rs.next()) {
                        panelFarmNames.add(rs.getString("name"));
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            ListOfFarms.setItems(panelFarmNames);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        lineChart.setCreateSymbols(false);
        lineChart.setAnimated(false);

        ListOfFarms.valueProperty().addListener((obs, oldVal, newVal) -> filterChart());
        fromDatePicker.valueProperty().addListener((obs, oldVal, newVal) -> filterChart());
        toDatePicker.valueProperty().addListener((obs, oldVal, newVal) -> filterChart());
    }
    private void filterChart() {
        LocalDate from = fromDatePicker.getValue();
        LocalDate to = toDatePicker.getValue();
        String name = ListOfFarms.getSelectionModel().getSelectedItem();
        if(from != null && to != null && name != null) {
            System.out.println(from + " " + to + " " + name);
        loadChartData(from, to,name);
        }
    }

    private void loadChartData(LocalDate from, LocalDate to, String name) {
        lineChart.getData().clear();
        String sql =
                "SELECT ep.panel_id, ep.timestamp, ep.power " +
                        "FROM panel_energy ep JOIN panels p ON ep.panel_id = p.panel_id " +
                        "JOIN farms f ON p.farm_id = f.farm_id " +
                        "WHERE ep.timestamp >= ? AND ep.timestamp < DATE_ADD(?, INTERVAL 1 DAY) " +
                        "AND f.name = ? ORDER BY ep.panel_id, ep.timestamp";
        System.out.println(sql);
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            if (from == null || to == null) {
                return;
            }
            stmt.setDate(1, java.sql.Date.valueOf(from));
            stmt.setDate(2, java.sql.Date.valueOf(to));
            stmt.setString(3,name);

            try (ResultSet rs = stmt.executeQuery()) {

                List<XYChart.Series<String, Number>> seriesList = new ArrayList<>();
                int currentPanelId = -1;
                XYChart.Series<String, Number> currentSeries = null;
                while (rs.next()) {

                    int panelId = rs.getInt("panel_id");
                    Timestamp ts = rs.getTimestamp("timestamp");
                    float power = rs.getFloat("power");
                    String label = ts.toLocalDateTime().format(DateTimeFormatter.ofPattern("dd-MM HH:mm"));

                    if (panelId != currentPanelId) {
                        currentSeries = new XYChart.Series<>();
                        currentSeries.setName("Panel " + panelId);
                        seriesList.add(currentSeries);
                        currentPanelId = panelId;
                    }
                    currentSeries.getData().add(new XYChart.Data<>(label, power));
                }

                lineChart.getData().addAll(seriesList);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}