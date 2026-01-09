package DAO;

import Config.DatabaseConnection;
import Model.Caffe;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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
    
    // Metode helper untuk validasi dan mendapatkan InputStream
    private InputStream getValidInputStream(Caffe cf) throws IOException {
        if (cf.getGambarStream() != null) {
            // Jika stream dari memory, buat stream baru dari data yang ada
            InputStream originalStream = cf.getGambarStream();
            
            // Baca semua byte dan buat ByteArrayInputStream baru
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = originalStream.read(buffer)) != -1) {
                baos.write(buffer, 0, bytesRead);
            }
            
            // Kembalikan stream baru yang bisa direset
            return new ByteArrayInputStream(baos.toByteArray());
            
        } else if (cf.getImagePath() != null && !cf.getImagePath().trim().isEmpty()) {
            // Validasi file dari path
            File file = new File(cf.getImagePath());
            
            if (!file.exists()) {
                throw new FileNotFoundException("File tidak ditemukan: " + cf.getImagePath());
            }
            
            if (!file.canRead()) {
                throw new IOException("Tidak dapat membaca file: " + cf.getImagePath());
            }
            
            if (file.isDirectory()) {
                throw new IOException("Path adalah direktori, bukan file: " + cf.getImagePath());
            }
            
            // Cek ukuran file (maksimal 16MB untuk aman)
            long maxSize = 16 * 1024 * 1024; // 16MB
            if (file.length() > maxSize) {
                throw new IOException("File terlalu besar. Ukuran: " + file.length() + 
                                      " bytes, Maksimal: " + maxSize + " bytes");
            }
            
            return new FileInputStream(file);
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
            
            try (InputStream is = getValidInputStream(cf)) {
                if (is != null) {
                    // Gunakan metode setBlob dengan length parameter
                    ps.setBlob(7, is, is.available());
                } else {
                    ps.setNull(7, java.sql.Types.BLOB);
                }
            }
            
            int rowsAffected = ps.executeUpdate();
            System.out.println("Insert berhasil! Baris terpengaruh: " + rowsAffected);
            return rowsAffected > 0;
            
        } catch (FileNotFoundException e) {
            System.err.println("[ERROR] File gambar tidak ditemukan: " + e.getMessage());
            return false;
        } catch (IOException e) {
            System.err.println("[ERROR] I/O Error: " + e.getMessage());
            return false;
        } catch (SQLException e) {
            System.err.println("[ERROR] Database Error: " + e.getMessage());
            
            // Cek error spesifik MySQL
            if (e.getMessage().contains("max_allowed_packet")) {
                System.err.println("[SOLUSI] File terlalu besar untuk MySQL. Perbesar max_allowed_packet di MySQL:");
                System.err.println("SET GLOBAL max_allowed_packet = 67108864;");
            }
            
            return false;
        } catch (Exception e) {
            System.err.println("[ERROR] Unexpected error: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
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
            
            try (InputStream is = getValidInputStream(cf)) {
                if (is != null) {
                    ps.setBlob(7, is, is.available());
                } else {
                    ps.setNull(7, java.sql.Types.BLOB);
                }
            }
            
            ps.setInt(8, cf.getId());
            
            int rowsAffected = ps.executeUpdate();
            System.out.println("Update berhasil! Baris terpengaruh: " + rowsAffected);
            return rowsAffected > 0;
            
        } catch (FileNotFoundException e) {
            System.err.println("[ERROR] File gambar tidak ditemukan: " + e.getMessage());
            return false;
        } catch (IOException e) {
            System.err.println("[ERROR] I/O Error: " + e.getMessage());
            return false;
        } catch (SQLException e) {
            System.err.println("[ERROR] Database Error: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.err.println("[ERROR] Unexpected error: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Metode lainnya tetap sama...
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
    public boolean delete(int idCaffe) {
        String sql = "DELETE FROM Caffe WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idCaffe);
            int rowsAffected = ps.executeUpdate();
            System.out.println("Data berhasil dihapus!");
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Caffe getById(int id) {
        Caffe cf = null;
        String sql = "SELECT * FROM Caffe WHERE id =?";
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
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