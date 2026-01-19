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
 * 
 * * @author dwife
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

   
            if (cf.getGambarStream() != null) {
                ps.setBinaryStream(7, cf.getGambarStream()); 
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
        String sql = "SELECT id, kategori, daerah, namaCaffe, deskripsi, alamat, linkMaps FROM Caffe ORDER BY id DESC"; // Urutkan terbaru
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Caffe cf = new Caffe();
                cf.setId(rs.getInt("id"));
                cf.setKategori(rs.getString("kategori"));
                cf.setDaerah(rs.getString("daerah"));
                cf.setNamaCaffe(rs.getString("namaCaffe"));
                cf.setDeskripsi(rs.getString("deskripsi"));
                cf.setAlamat(rs.getString("alamat"));
                cf.setLinkMaps(rs.getString("linkMaps"));
                list.add(cf);
            }
        } catch (Exception e) {
            System.err.println("Error GetAll: " + e.getMessage());
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
            System.err.println("Error GetImage: " + e.getMessage());
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
                ps.setBinaryStream(7, cf.getGambarStream());
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
            System.err.println("Error Delete: " + e.getMessage());
            return false;
        }
    }
    
    
    public Caffe getById(int id) {
        Caffe cf = null;
        String sql = "SELECT * FROM Caffe WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    cf = new Caffe();
                    cf.setId(rs.getInt("id"));
                    cf.setKategori(rs.getString("kategori"));
                    cf.setDaerah(rs.getString("daerah"));
                    cf.setNamaCaffe(rs.getString("namaCaffe"));
                    cf.setDeskripsi(rs.getString("deskripsi"));
                    cf.setAlamat(rs.getString("alamat"));
                    cf.setLinkMaps(rs.getString("linkMaps"));
                    
               
                    Blob blob = rs.getBlob("idPicture");
                    if (blob != null) {
                        cf.setGambarData(blob.getBytes(1, (int) blob.length()));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cf;
    }
    
    public List<Caffe> searchCaffe(String keyword) {
        List<Caffe> list = new ArrayList<>();
        String sql = "SELECT id, kategori, daerah, namaCaffe, deskripsi, alamat, linkMaps FROM Caffe WHERE namaCaffe LIKE ? OR daerah LIKE ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            String searchPattern = "%" + keyword + "%";
            ps.setString(1, searchPattern);
            ps.setString(2, searchPattern);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Caffe cf = new Caffe();
                    cf.setId(rs.getInt("id"));
                    cf.setKategori(rs.getString("kategori"));
                    cf.setDaerah(rs.getString("daerah"));
                    cf.setNamaCaffe(rs.getString("namaCaffe"));
                    cf.setDeskripsi(rs.getString("deskripsi"));
                    cf.setAlamat(rs.getString("alamat"));
                    cf.setLinkMaps(rs.getString("linkMaps"));
                    
                    list.add(cf);
                }
            }
        } catch (Exception e) {
            System.err.println("Error Search: " + e.getMessage());
        }
        return list;
    }


    
    public int getTotalCaffe() {
        return getCount("SELECT COUNT(*) FROM Caffe");
    }
    
    public int getTraditionalCaffeCount() {
        return getCount("SELECT COUNT(*) FROM Caffe WHERE kategori LIKE '%Traditional%' OR kategori LIKE '%Tradisional%'");
    }
    
    public int getModernCaffeCount() {
        return getCount("SELECT COUNT(*) FROM Caffe WHERE kategori LIKE '%Modern%'");
    }
    

    private int getCount(String sql) {
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