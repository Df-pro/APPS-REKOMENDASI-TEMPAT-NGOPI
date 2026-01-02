/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Config.DatabaseConnection;
import java.sql.*;
/**
 *
 * @author dwife
 */


public class AdminCodeDAO {
    
    public boolean isValidAdminCode(String code) {
        String query = "SELECT * FROM admin_codes WHERE admin_code = ? AND is_used = 0";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, code);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
            
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean markCodeAsUsed(String code) {
        String query = "UPDATE admin_codes SET is_used = 1 WHERE admin_code = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, code);
            return stmt.executeUpdate() > 0;
            
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}



