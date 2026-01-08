package DAO;

import Model.User;
import Config.DatabaseConnection;
import java.sql.*;

public class UserDAO {
    
    public User login(String username, String passwordHash) {
        String query = "SELECT * FROM users WHERE username = ? AND password_hash = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, username);
            stmt.setString(2, passwordHash);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return new User(
                    rs.getInt("user_id"),
                    rs.getString("username"),
                    rs.getString("password_hash"),
                    rs.getBoolean("is_admin"),
                    rs.getString("email"),
                    rs.getString("full_name")
                );
            }
            
        } catch (SQLException e) {
            System.err.println("Login error: " + e.getMessage());
        }
        return null;
    }
    
    public boolean register(User user) {
        String query = "INSERT INTO users (username, password_hash, email, full_name, is_admin) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPasswordHash());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getFullName());
            stmt.setBoolean(5, user.isAdmin());
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Register error: " + e.getMessage());
            return false;
        }
    }
    
    public boolean isEmailExists(String email) {
        String query = "SELECT COUNT(*) FROM users WHERE email = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            
        } catch (SQLException e) {
            System.err.println("Email check error: " + e.getMessage());
        }
        return false;
    }
    
    public boolean isUsernameExists(String username) {
        String query = "SELECT COUNT(*) FROM users WHERE username = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            
        } catch (SQLException e) {
            System.err.println("Username check error: " + e.getMessage());
        }
        return false;
    }
}



