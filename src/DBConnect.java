import java.sql.*;
import java.util.Random;

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
    public void getData(String tabela) {
        try {
            Connection conn = getConnection();
            String sql = "SELECT * FROM "+tabela+";";

                try (Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery(sql)) {

                    if (rs.next()) {
                        String godzina = rs.getString("godzina");
                        double napiecie = rs.getDouble("napiecie");
                        double prad = rs.getDouble("prad");
                        double moc = rs.getDouble("moc");

                        System.out.println(
                                "\nTimestamp: " + godzina +
                                        "\nNapięcie: " + napiecie +
                                        "\nPrąd: " + prad +
                                        "\nMoc: " + moc
                        );
                    }
                } catch (SQLException e) {
                    System.out.println("bład połączenia z bazą danych");
                }
            } catch (Exception e) {
            System.out.println("bląd" + e.getMessage());
        }
    }
    public void AddRow(String tabela) throws SQLException {
        try (Connection conn = DBConnect.getConnection();){
        Random rand = new Random();
        int napiecie =  rand.nextInt(30) + 15;
        int prad = rand.nextInt(10);
        int moc = prad*napiecie;
        System.out.println(napiecie + " " + prad + " " + moc);
        String sql = "INSERT INTO "+tabela+" (godzina, napiecie, prad, moc) VALUES (CURRENT_TIMESTAMP(), ?, ?, ?)";
        if(conn != null) {
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, napiecie);
                ps.setInt(2, prad);
                ps.setInt(3, moc);
                ps.executeUpdate();
                System.out.println("Poprawne dane wstawione!");
            } catch (SQLException e) {
                System.out.println("bląd" + e.getMessage());
            }
        }
    }catch (SQLException e){
            System.out.println("bląd" + e.getMessage());
        }
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
