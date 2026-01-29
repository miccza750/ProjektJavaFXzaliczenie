package main.java.com.ProjektJavaFX;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLOutput;

public class UserChartsController {
    @FXML
    private Button backButton;
    @FXML
    private Pane Panel;
    @FXML
    private Text TextMoney;
    @FXML
    private BarChart<String, Number> energyBarChart;
    private int consumerName;
    SceneChange sceneChange = new SceneChange();
    @FXML
    public void initialize() {
        loadBarChart(consumerName);
        backButton.setOnAction(e -> {
                Stage stage = (Stage) backButton.getScene().getWindow();
                sceneChange.changeScene(stage, "UserPanel.fxml", "Panel użytkownika");
        });
    }
    public void loadBarChart(int consumerName) {
        energyBarChart.getData().clear();
        XYChart.Series<String, Number> data = new XYChart.Series<>();
        double production = getValue(true);
        double cost = getValue(false);
        data.getData().add(new XYChart.Data<>("Produkcja", production));
        data.getData().add(new XYChart.Data<>("Zużycie", cost));
        TextMoney.setText(new String(String.valueOf(Math.round( cost*0.92*100)/100))+"zł");
        energyBarChart.getData().add(data);
    }
    private double getValue(boolean production) {
        String sql = production ?
                "SELECT SUM(ep.power * 5.0 / 60.0 / 1000.0) FROM panel_energy ep " +
                        "JOIN panels p ON ep.panel_id=p.panel_id " +
                        "JOIN consumers c ON p.farm_id=c.farm_id " +
                        "WHERE c.username=? " :
                "SELECT SUM(energy_kwh) FROM consumer_energy " +
                        "JOIN consumers c ON c.consumer_id = consumer_energy.consumer_id WHERE c.username=?";

        try {
            DBConnect connection = new DBConnect();
            PreparedStatement stmt = connection.getConnection().prepareStatement(sql);
            stmt.setString(1, Session.getInstance().getName());
            System.out.println(stmt.toString());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                double value = rs.getDouble(1);
                if (rs.wasNull()) {
                    return 0;
                } else {
                    return value;
                }
            } else {
                return 0;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}



