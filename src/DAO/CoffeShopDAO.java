
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Config.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import Model.Caffe;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

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

            if (cf.getGambarStream() != null) {
                ps.setBlob(7, cf.getGambarStream());
            } else if (cf.getImagePath() != null && !cf.getImagePath().isEmpty()) {
                InputStream is = new FileInputStream(new File(cf.getImagePath()));
                ps.setBlob(7, is);
                is.close();
            } else {
                ps.setNull(7, java.sql.Types.BLOB);
            }

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;

        } catch (FileNotFoundException e) {
            System.err.println("File gambar tidak ditemukan: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

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
                        rs.getString("daerah"),
                        rs.getString("namaCaffe"),
                        rs.getString("deskripsi"),
                        rs.getString("alamat"),
                        rs.getString("linkMaps"),
                        rs.getString("idPicture")
                );


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


    public boolean update(Caffe cf) {
        String sql = "UPDATE Caffe SET kategori=?, daerah=?, namaCaffe=?, deskripsi=?, alamat=?, linkMaps=?, idPicture=? WHERE id=?";
        
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
            } else if (cf.getImagePath() != null && !cf.getImagePath().isEmpty()) {
                InputStream is = new FileInputStream(new File(cf.getImagePath()));
                ps.setBlob(7, is);
                is.close();
            } else {
                ps.setNull(7, java.sql.Types.BLOB);
            }
            
            ps.setInt(8, cf.getId());

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;

        } catch (FileNotFoundException e) {
            System.err.println("File gambar tidak ditemukan: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
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
                        rs.getString("idPicture")
                );

                java.sql.Blob blob = rs.getBlob("idPicture");
                if (blob != null) {
                    cf.setGambarStream(blob.getBinaryStream());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cf;
    }
}


