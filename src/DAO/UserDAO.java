/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Model.User;
import Config.DatabaseConnection;
import Model.Caffe;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author dwife
 */


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
            stmt.setBoolean(5, user.getIsAdmin());
            
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
    
   
    // 0 -> "User", 1 -> "Admin"
    private String intToRole(int isAdminInt) {
        return isAdminInt == 1 ? "Admin" : "User";
    }
    
    // "User" -> 0, "Admin" -> 1
    private int roleToInt(String isAdmin) {
        return "Admin".equalsIgnoreCase(isAdmin) ? 1 : 0;
    }
    
    
    // Table User
    public List<User> getAllUser() {
        List<User> list = new ArrayList<>();

        String sql = "SELECT user_id, created_at ,username, email, is_admin  FROM users";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                User us = extractUserFromResultSet(rs);
                list.add(us);
                
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    
    
    // Method untuk mendapatkan jumlah user terdaftar
    public int getTotalUsers() {
        String sql = "SELECT COUNT(*) as total FROM users";
        int total = 0;
            
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            if (rs.next()) {
                total = rs.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return total;
    }

    private User extractUserFromResultSet(ResultSet rs) throws SQLException {
        User us = new User();
        us.setUserId(rs.getInt("user_id"));
        us.setEmail(rs.getString("email"));
        us.setUsername(rs.getString("username"));
        
        // Mengambil is_admin sebagai boolean (0/1)
        boolean isAdmin = rs.getBoolean("is_admin");
        us.setIsAdmin(isAdmin);
        
        // Konversi Timestamp ke LocalDateTime
        Timestamp timestamp = rs.getTimestamp("created_at");
        if (timestamp != null) {
            us.setCreatedAt(timestamp.toLocalDateTime());
        }
        
        return us;
        
    }
    
    
}


