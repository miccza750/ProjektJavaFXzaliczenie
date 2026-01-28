import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.*;

public class TablesController extends Application {

    @FXML
    private TableView<PanelRow> table;
    @FXML
    private TableColumn<PanelRow, Integer> IDColumn;
    @FXML
    private TableColumn<PanelRow, Integer> IDFarmColumn;
    @FXML
    private TableColumn<PanelRow, String> DateColumn;
    @FXML
    private TableColumn<PanelRow, String> TurnOnColumn;
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
        IDColumn.setCellValueFactory(cell -> cell.getValue().getId().asObject());
        IDFarmColumn.setCellValueFactory(cell -> cell.getValue().getFarm_id().asObject());
        DateColumn.setCellValueFactory(cell -> cell.getValue().getopenDate());
        TurnOnColumn.setCellValueFactory(cell -> cell.getValue().getturnOn());

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
            ResultSet rs = stmt.executeQuery("SELECT * FROM panels");
            while (rs.next()) {
                boolean wlaczony = rs.getBoolean("status");
                data.add(new PanelRow(
                        rs.getInt("panel_id"),
                        rs.getInt("farm_id"),
                        rs.getDate("installation_date").toString(),
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
            conn.DeleteRowFromTabel(selected.getINTId(),"panels");
    }catch(SQLException ex){
            System.out.println( ex.getMessage());
        }
        refreshTable();
    }
    private void addNewPanel(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AddForm.fxml"));
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
            String setted = selected.getturnOnString();
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
