import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;

import java.sql.*;
import java.time.Duration;
import java.time.LocalDate;
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
    private ListView listOfPanels;
    List<Integer> panelFarmIds = new ArrayList<>();
    @FXML
    public void initialize() {
        try (Connection conn = DBConnect.getConnection()) {
            try (Statement stmt = conn.createStatement()) {
                try (ResultSet rs = stmt.executeQuery("SELECT id_farmy FROM farmy")) {
                    while (rs.next()) {
                        panelFarmIds.add(rs.getInt("id_farmy"));
                    }

                }
            }
            listOfPanels.setItems(FXCollections.observableArrayList(panelFarmIds));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        loadChartData(null, null);
        lineChart.setCreateSymbols(false);
        lineChart.setAnimated(false);
        fromDatePicker.valueProperty().addListener((obs, oldVal, newVal) -> filterChart());
        toDatePicker.valueProperty().addListener((obs, oldVal, newVal) -> filterChart());
    }
    private void filterChart() {
        LocalDate from = fromDatePicker.getValue();
        LocalDate to = toDatePicker.getValue();
        loadChartData(from, to);
    }
    private void loadChartData(LocalDate from, LocalDate to) {
        lineChart.getData().clear();
        String sql =
                "SELECT ID_Panelu, DATE_FORMAT(godzina,'%H:%i') AS czas, moc " +
                        " FROM energia_paneli " +
                        "WHERE Dzien BETWEEN '" + from + "' AND '" + to + "' " +
                        " ORDER BY ID_Panelu, godzina";


    try(Connection conn = DBConnect.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {
            List<XYChart.Series<String, Number>> series = new ArrayList<>();
            int currentPanelId = -1;
            LocalTime prevTime = null;
            XYChart.Series<String, Number> currentSeries = null;
            while (rs.next()) {
                int panelId = rs.getInt("ID_Panelu");
                if (panelId != currentPanelId) {
                    currentSeries = new XYChart.Series<>();
                    currentSeries.setName("Panel " + panelId);
                    series.add(currentSeries);
                    currentPanelId = panelId;
                    prevTime = null;
                }
                String czas = rs.getString("czas");
                float moc = rs.getFloat("moc");
                LocalTime time = LocalTime.parse(czas);
                if (prevTime!=null){
                if(Duration.between(prevTime, time).toMinutes() > 1) {
                    if (!series.contains(currentSeries)) {
                    currentSeries = new XYChart.Series<>();
                    currentSeries.setName("Panel " + panelId);
                    series.add(currentSeries);
                    }
                }}
                prevTime = time;
                currentSeries.getData().add(new XYChart.Data<>(czas, moc));
            }
        lineChart.getData().addAll(series);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
