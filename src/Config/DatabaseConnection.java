package Config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
        
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/xsiri_coffee_shop?connectTimeout=5000&socketTimeout=30000";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";
    
    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Koneksi gagal: " + e.getMessage());
            return null;
        }
    }
    
    
    
    /*
        private static final String URL = "jdbc:mysql://31.97.189.68:3306/xsiri_coffee_shop";
        private static final String USERNAME = "root";
        private static final String PASSWORD = "Bosmuda@101.";
    
    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL Driver not found", e);
        }
    } */
}




