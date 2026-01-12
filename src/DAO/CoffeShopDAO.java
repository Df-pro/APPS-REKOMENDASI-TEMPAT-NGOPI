/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Config.DatabaseConnection;
import Model.Caffe;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author dwife
 */
public class CoffeShopDAO {


    public boolean insert(Caffe cf) {
        String sql = "INSERT INTO Caffe (kategori, daerah, namaCaffe, deskripsi, alamat, linkMaps, idPicture) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, cf.getKategori());
            ps.setString(2, cf.getDaerah());
            ps.setString(3, cf.getNamaCaffe());
            ps.setString(4, cf.getDeskripsi());
            ps.setString(5, cf.getAlamat());
            ps.setString(6, cf.getLinkMaps());

            //Hanya cek Stream (In-Memory)
            if (cf.getGambarStream() != null) {
                ps.setBlob(7, cf.getGambarStream());
            } else {
                ps.setNull(7, java.sql.Types.BLOB);
            }

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;

        } catch (Exception e) {
            System.err.println("Error Insert: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

   
    public List<Caffe> getAll() {
        List<Caffe> list = new ArrayList<>();

        String sql = "SELECT id, kategori, daerah, namaCaffe, deskripsi, alamat, linkMaps FROM Caffe";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Caffe cf = new Caffe(
                    rs.getInt("id"),
                    rs.getString("kategori"),
                    rs.getString("daerah"),
                    rs.getString("namaCaffe"),
                    rs.getString("deskripsi"),
                    rs.getString("alamat"),
                    rs.getString("linkMaps"),
                    (String) null
                );
                
                list.add(cf);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }


    
    public byte[] getImageBytesById(int id) {
        String sql = "SELECT idPicture FROM Caffe WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
             
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Blob blob = rs.getBlob("idPicture");
                    if (blob != null) {
                        return blob.getBytes(1, (int) blob.length());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public boolean update(Caffe cf) {
        boolean isNewImage = (cf.getGambarStream() != null);
        
        String sql;
        if (isNewImage) {
         
            sql = "UPDATE Caffe SET kategori=?, daerah=?, namaCaffe=?, deskripsi=?, alamat=?, linkMaps=?, idPicture=? WHERE id=?";
        } else {
          
            sql = "UPDATE Caffe SET kategori=?, daerah=?, namaCaffe=?, deskripsi=?, alamat=?, linkMaps=? WHERE id=?";
        }
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, cf.getKategori());
            ps.setString(2, cf.getDaerah());
            ps.setString(3, cf.getNamaCaffe());
            ps.setString(4, cf.getDeskripsi());
            ps.setString(5, cf.getAlamat());
            ps.setString(6, cf.getLinkMaps());

            if (isNewImage) {
                ps.setBlob(7, cf.getGambarStream());
                ps.setInt(8, cf.getId());
            } else {
                ps.setInt(7, cf.getId());
            }

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;

        } catch (Exception e) {
            System.err.println("Error Update: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }


    public boolean delete(int idCaffe) {
        String sql = "DELETE FROM Caffe WHERE id=?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, idCaffe);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
            
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public Caffe getById(int id) {
        Caffe cf = null;
        String sql = "SELECT * FROM Caffe WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                cf = new Caffe(
                    rs.getInt("id"),
                    rs.getString("kategori"),
                    rs.getString("daerah"),
                    rs.getString("namaCaffe"),
                    rs.getString("deskripsi"),
                    rs.getString("alamat"),
                    rs.getString("linkMaps"),
                    (String) null
                );


                Blob blob = rs.getBlob("idPicture");
                if (blob != null) {
                    // Simpan sebagai byte[] di memori
                    cf.setGambarData(blob.getBytes(1, (int) blob.length()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cf;
    }
    
    // Method untuk mendapatkan jumlah user terdaftar
    public int getTotalCaffe() {
        String sql = "SELECT COUNT(*) as total FROM Caffe";
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
    
    public int getTraditionalCaffeCount() {
        String sql = "SELECT COUNT(*) FROM Caffe WHERE kategori LIKE '%Traditional%' OR kategori LIKE '%Tradisional%'";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }
    
    public int getModernCaffeCount() {
        String sql = "SELECT COUNT(*) FROM Caffe WHERE kategori LIKE '%Modern%'";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }
}