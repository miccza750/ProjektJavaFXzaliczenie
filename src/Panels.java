import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.*;

public class Panels extends Application {

    @FXML
    private TableView<PanelRow> table;
    @FXML
    private TableColumn<PanelRow, Integer> IDColumn;
    @FXML
    private TableColumn<PanelRow, String> NazwaColumn;
    @FXML
    private TableColumn<PanelRow, String> DzienColumn;
    @FXML
    private TableColumn<PanelRow, String> WlaczonyColumn;
    @FXML
    private Button DeleteButton;
    @FXML
    private Button AddButton;
    @FXML
    private Button OnOffButton;
    @FXML
    private Button RefreshButton;
    private ObservableList<PanelRow> data;
    public DBConnect conn;
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

    }

    @FXML
    public void initialize() throws SQLException, ClassNotFoundException {
        data = FXCollections.observableArrayList();

        // Podłączenie kolumn do właściwości PanelRow
        IDColumn.setCellValueFactory(cell -> cell.getValue().getId().asObject());
        NazwaColumn.setCellValueFactory(cell -> cell.getValue().getNazwa());
        DzienColumn.setCellValueFactory(cell -> cell.getValue().getDzienOtwarcia());
        WlaczonyColumn.setCellValueFactory(cell -> cell.getValue().getWlaczony());

        table.setItems(data);
            RefreshButton.setOnAction(e -> refreshTable());
            DeleteButton.setOnAction(e -> deleteSelected());
            OnOffButton.setOnAction(e -> setSelected());
            AddButton.setOnAction(e -> addNewPanel());
        refreshTable();
    }
    private void refreshTable() {
        data.clear();
        try (Connection conn = DBConnect.getConnection()) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM lista_paneli");
            while (rs.next()) {
                boolean wlaczony = rs.getBoolean("Właczony");
                data.add(new PanelRow(
                        rs.getInt("id"),
                        rs.getString("nazwa"),
                        rs.getDate("dzien_otwarcia").toString(),
                        wlaczony ? "tak" : "nie"
                ));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    private void deleteSelected(){
        try{
            conn = new DBConnect();
            PanelRow selected = table.getSelectionModel().getSelectedItem();
            conn.DeleteRowFromTabel(selected.getINTId());
    }catch(SQLException ex){
            System.out.println( ex.getMessage());
        }
        refreshTable();
    }
    private void addNewPanel(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("DodawaniePaneluFXML.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setTitle("Dodaj panel");
            stage.setScene(scene);
            stage.initOwner(AddButton.getScene().getWindow());
            stage.show();
            refreshTable();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
    private void setSelected() {
        try{
            conn = new DBConnect();
            PanelRow selected = table.getSelectionModel().getSelectedItem();
            String setted = selected.getWlaczonyString();
            int settedBolean;
            if(setted.equals("tak")){
                settedBolean = 1;
            }else{
                settedBolean = 0;
            }
            conn.SetOnOffFromTabel(selected.getINTId(),settedBolean);
        }catch(SQLException ex){
            System.out.println( ex.getMessage());
        }
        refreshTable();
    }
    }
