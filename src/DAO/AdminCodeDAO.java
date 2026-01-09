/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Config.DatabaseConnection;
import Model.Caffe;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author dwife
 */


public class AdminCodeDAO extends CoffeShopDAO  {
    
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
    
    //insert
    @Override
    public boolean insert(Caffe cf) {
        String sql = "INSERT INTO Caffe (kategori, daerah, namaCaffe, deskripsi, alamat, linkMaps, idPicture) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cf.getKategori());
            ps.setString(2, cf.getDaerah());
            ps.setString(3, cf.getNamaCaffe());
            ps.setString(4, cf.getDeskripsi());
            ps.setString(5, cf.getAlamat());
            ps.setString(6, cf.getLinkMaps());
            
            // Set parameter gambar (BLOB)
            if (cf.getGambarStream() != null) {
                // Set InputStream ke BLOB
                ps.setBlob(7, cf.getGambarStream());
            } else if (cf.getImagePath() != null && !cf.getImagePath().isEmpty()) {
                // Atau jika pakai path, konversi dulu ke InputStream
                InputStream is = new FileInputStream(new File(cf.getImagePath()));
                ps.setBlob(7, is);
                is.close();
            } else {
                // Jika tidak ada gambar, set null
                ps.setNull(7, java.sql.Types.BLOB);
            }
            
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
            
            }catch (FileNotFoundException e) {
                System.err.println("File gambar tidak ditemukan: " + e.getMessage());
                return false;
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
                e.printStackTrace();
                return false;
            }
    }
    
    
    //GETALL
    @Override
    public List<Caffe> getAll() {
        List<Caffe> list = new ArrayList<>();
        String sql = "SELECT * FROM Caffe";
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Caffe cf = new Caffe(
                rs.getInt("id"),
                rs.getString("kategori"),
                rs.getString("Daerah"),
                rs.getString("namaCaffe"),
                rs.getString("deskripsi"),
                rs.getString("alamat"),
                rs.getString("linkMaps"),
                rs.getString("idPicture")  
                );
                
                // Untuk BLOB gambar (jika ingin disimpan di objek)
                java.sql.Blob blob = rs.getBlob("idPicture");
                if (blob != null) {
                    cf.setGambarStream(blob.getBinaryStream());
                }
                
                list.add(cf);
            }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return list;
    }
    
    @Override
    //Update
    public boolean update(Caffe cf){
        String sql = "UPDATE Caffe SET kategori= ?, daerah=?, namaCaffe=?, deskripsi=?, alamat=?, linkMaps=?,imagePath=?, WHARE idCaffe=?";
        
        try (Connection conn =DatabaseConnection.getConnection(); 
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cf.getKategori());
            ps.setString(2, cf.getDaerah());
            ps.setString(3, cf.getNamaCaffe());
            ps.setString(4, cf.getDeskripsi());
            ps.setString(5, cf.getAlamat());
            ps.setString(6, cf.getLinkMaps());
            ps.setString(7, cf.getImagePath());
            // Set parameter gambar (BLOB)
            if (cf.getGambarStream() != null) {
                // Set InputStream ke BLOB
                ps.setBlob(7, cf.getGambarStream());
            } else if (cf.getImagePath() != null && !cf.getImagePath().isEmpty()) {
                // Atau jika pakai path, konversi dulu ke InputStream
                InputStream is = new FileInputStream(new File(cf.getImagePath()));
                ps.setBlob(7, is);
                is.close();
            } else {
                // Jika tidak ada gambar, set null
                ps.setNull(7, java.sql.Types.BLOB);
            }
            
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
            
        }catch (FileNotFoundException e) {
                System.err.println("File gambar tidak ditemukan: " + e.getMessage());
                return false;
        } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
                e.printStackTrace();
                return false;
        }
    }
    
    @Override
    //Delete
    public boolean delete(int idCaffe) {
        String sql = "DELETE FROM Caffe WHERE id=?";

        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idCaffe);
            ps.executeUpdate();
            System.out.println("Data berhasil dihapus!");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    @Override
    //getbyid
    public Caffe getById (int id){
        Caffe cf = null;
        String sql = "SELECT * FORM Caffe WHERE id =?";
        try (Connection conn = DatabaseConnection.getConnection();
            
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()){
                cf = new Caffe(
                rs.getInt("id"),
                rs.getString("kategori"),
                rs.getString("Daerah"),
                rs.getString("namaCaffe"),
                rs.getString("deskripsi"),
                rs.getString("alamat"),
                rs.getString("linkMaps"),
                rs.getString("idPicture") 
                );
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return cf;
    }
}