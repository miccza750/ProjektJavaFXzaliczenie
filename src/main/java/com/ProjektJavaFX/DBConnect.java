package main.java.com.ProjektJavaFX;
import java.sql.*;


public class DBConnect {
    public static Connection getConnection() throws SQLException {
        Connection conn = null;
        String url = "jdbc:mysql://localhost:3307/solar_farm";
        String user = "root";
        String password = "";
        conn = DriverManager.getConnection(url, user, password);
        System.out.println("Połączono z bazą danych");
        return conn;
    }
    public void AddRowtoListOfPanels(Date date,int farm_id, String tab) throws SQLException {
        try {
            Connection conn = DBConnect.getConnection();
            String sql = "INSERT INTO "+tab+" (`farm_id`,`status`,`installation_date`) " +
                    "VALUES ('" + farm_id + "',0,'"+date+"')";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.executeUpdate();

        }catch (SQLException e){
            System.out.println("bląd" + e.getMessage());        }
    }
    public void DeleteRowFromTabel(int row, String tab) throws SQLException {
        Connection conn = DBConnect.getConnection();
        String sql = "DELETE FROM `"+tab+"` WHERE `panel_id` = "+(row)+" ";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.executeUpdate();
            System.out.println("Usunieto poprawnie wiersz o ID: "+row);
        }
        catch (SQLException e){
            System.out.println("bład: " + e.getMessage());
        }
    }
    public void SetOnOffFromTabel(int row, int Setted) throws SQLException {
        Connection conn = DBConnect.getConnection();
        String sql = "UPDATE lista_paneli SET Właczony = "+Setted+" WHERE id = "+row+";";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.executeUpdate();
            System.out.println("Zmieniono poprawnie wiersz o ID: "+row);
        }
        catch (SQLException e){
            System.out.println("bład: " + e.getMessage());
        }
    }
    public boolean farmExists(int farmId) throws SQLException {
        String sql = "SELECT 1 FROM farms WHERE farm_id = "+farmId;
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        }
    }
    }
