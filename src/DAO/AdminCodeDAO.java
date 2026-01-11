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

public class AdminCodeDAO extends CoffeShopDAO {


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

    

    @Override
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
                    rs.getString("linkMaps")
                );
                list.add(cf);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
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

    
    
    @Override
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
                ps.setBlob(7, cf.getGambarStream());
            } else {
                ps.setNull(7, java.sql.Types.BLOB);
            }
            
            return ps.executeUpdate() > 0;
            
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    
    @Override
    public boolean update(Caffe cf) {
        
        boolean adaGambarBaru = (cf.getGambarStream() != null);
        
        String sql;
        if (adaGambarBaru) {
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
            
            if (adaGambarBaru) {
                ps.setBlob(7, cf.getGambarStream());
                ps.setInt(8, cf.getId());
            } else {
                ps.setInt(7, cf.getId());
            }
            
            return ps.executeUpdate() > 0;
            
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}