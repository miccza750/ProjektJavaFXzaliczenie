import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;

import java.sql.*;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
    ObservableList<String> panelFarmNames = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        try (Connection conn = DBConnect.getConnection()) {
            try (Statement stmt = conn.createStatement()) {
                try (ResultSet rs = stmt.executeQuery("SELECT name FROM farms")) {
                    //ObservableList<String> farms = FXCollections.observableArrayList();
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
                "SELECT ep.panel_id,DATE_FORMAT(ep.timestamp, '%H:%i') AS time, ep.power " +
                        "FROM panel_energy ep " +
                        "JOIN panels p ON ep.panel_id = p.panel_id " +
                        "JOIN farms f ON p.farm_id = f.farm_id " +
                        "WHERE ep.timestamp >= '" + from +
                        "' AND ep.timestamp < '" + to +
                        "' AND f.name = ? " +
                        "ORDER BY ep.panel_id, ep.timestamp;";
        System.out.println(sql);
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            if (from == null || to == null) {
                return;
            }

            stmt.setString(1,name);

            try (ResultSet rs = stmt.executeQuery()) {

                List<XYChart.Series<String, Number>> seriesList = new ArrayList<>();
                int currentPanelId = -1;
                XYChart.Series<String, Number> currentSeries = null;
                while (rs.next()) {

                    int panelId = rs.getInt("panel_id");
                    String czasStr = rs.getString("time");
                    float moc = rs.getFloat("power");

                    if (panelId != currentPanelId) {
                        currentSeries = new XYChart.Series<>();
                        currentSeries.setName("Panel " + panelId);
                        seriesList.add(currentSeries);
                        currentPanelId = panelId;
                    }
                    currentSeries.getData().add(new XYChart.Data<>(czasStr, moc));
                }

                lineChart.getData().addAll(seriesList);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}