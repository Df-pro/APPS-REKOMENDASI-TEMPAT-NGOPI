package Config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String URL = "jdbc:mysql://ferdi101.my.id:3306/xsiri_coffee_shop";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "Bosmuda";
    
    public static Connection getConnection() {
        try {

            Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            return conn;
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
            return null;
        }
    }
}
