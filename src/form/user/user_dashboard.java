

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package form.user;

import DAO.CoffeShopDAO;
import Model.Caffe;
import java.awt.*;
import java.io.ByteArrayInputStream;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author dwife
 */

     //======= INI HASIL GPT, INI UNTUK TESTING AMBIL GAMBAR DARI DATABAE APAKAH TETEP LAG ATAU TIDAK======
public class user_dashboard extends javax.swing.JFrame {

    // 1. Siapkan DAO
    private final CoffeShopDAO dao = new CoffeShopDAO();
    
    // 2. Siapkan Wadah untuk Kartu-Kartu
    private JPanel mainContainer; 

    public user_dashboard() {
        // Init komponen bawaan Netbeans
        initComponents();
        
        // Setup Manual Tampilan (Biar gak usah drag-drop dulu buat tes)
        setupSimpleUI();
        
        // Mulai Load Data
        loadData();
    }
    
    // --- SETUP TAMPILAN SEDERHANA ---
    private void setupSimpleUI() {
        setTitle("Test Tampilan User - Lazy Loading");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Full Screen
        setLayout(new BorderLayout()); // Layout Utama
        
        // Header
        JLabel header = new JLabel("DAFTAR REKOMENDASI KOPI", SwingConstants.CENTER);
        header.setFont(new Font("Arial", Font.BOLD, 24));
        header.setBorder(new EmptyBorder(20, 0, 20, 0));
        add(header, BorderLayout.NORTH);
        
        // Container Grid (Tempat Kartu)
        mainContainer = new JPanel();
        // Grid: Baris Otomatis (0), 4 Kolom, Jarak 20px
        mainContainer.setLayout(new GridLayout(0, 4, 20, 20)); 
        mainContainer.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Scroll Pane (Agar bisa discroll)
        JScrollPane scroll = new JScrollPane(mainContainer);
        scroll.getVerticalScrollBar().setUnitIncrement(16); // Scroll biar smooth
        add(scroll, BorderLayout.CENTER);
    }
    
    // --- LOGIKA LOAD DATA ---
    private void loadData() {
        mainContainer.removeAll();
        
        // Tampilkan loading text sementara
        JLabel loading = new JLabel("Sedang mengambil data dari server...", SwingConstants.CENTER);
        mainContainer.add(loading);
        mainContainer.revalidate();
        
        // Worker Background (Ambil Data Teks Dulu)
        new SwingWorker<List<Caffe>, Void>() {
            @Override
            protected List<Caffe> doInBackground() throws Exception {
                // Panggil DAO getAll()
                return dao.getAll(); 
            }

            @Override
            protected void done() {
                try {
                    mainContainer.remove(loading); // Hapus loading
                    List<Caffe> data = get();
                    
                    if (data.isEmpty()) {
                        mainContainer.add(new JLabel("Data Kosong"));
                    } else {
                        // Loop data dan buat kartu
                        for (Caffe c : data) {
                            addCard(c);
                        }
                    }
                    mainContainer.revalidate();
                    mainContainer.repaint();
                } catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error koneksi database");
                }
            }
        }.execute();
    }
    
    // --- MEMBUAT 1 KARTU ---
    private void addCard(Caffe c) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        card.setBackground(Color.WHITE);
        
        // Area Gambar (Placeholder)
        JLabel imgLabel = new JLabel("Loading Img...", SwingConstants.CENTER);
        imgLabel.setPreferredSize(new Dimension(250, 150)); // Ukuran Gambar
        imgLabel.setMaximumSize(new Dimension(250, 150));
        imgLabel.setOpaque(true);
        imgLabel.setBackground(Color.LIGHT_GRAY);
        imgLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Nama Kafe
        JLabel nameLabel = new JLabel(c.getNamaCaffe());
        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Daerah
        JLabel regionLabel = new JLabel(c.getDaerah());
        regionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Masukkan ke Kartu
        card.add(Box.createVerticalStrut(10));
        card.add(imgLabel);
        card.add(Box.createVerticalStrut(10));
        card.add(nameLabel);
        card.add(regionLabel);
        card.add(Box.createVerticalStrut(10));
        
        // Masukkan Kartu ke Container Utama
        mainContainer.add(card);
        
        // DOWNLOAD GAMBAR DI BACKGROUND (Lazy Load)
        loadSingleImage(c.getId(), imgLabel);
    }
    
    // --- DOWNLOAD GAMBAR PER ITEM ---
    private void loadSingleImage(int id, JLabel targetLabel) {
        new SwingWorker<ImageIcon, Void>() {
            @Override
            protected ImageIcon doInBackground() throws Exception {
                // Ambil BLOB Gambar dari DAO
                byte[] imgData = dao.getImageBytesById(id);
                
                if (imgData != null) {
                    // Resize Gambar agar ringan
                    Image img = ImageIO.read(new ByteArrayInputStream(imgData));
                    Image scaled = img.getScaledInstance(250, 150, Image.SCALE_SMOOTH);
                    return new ImageIcon(scaled);
                }
                return null;
            }

            @Override
            protected void done() {
                try {
                    ImageIcon icon = get();
                    if (icon != null) {
                        targetLabel.setText(""); // Hapus teks loading
                        targetLabel.setIcon(icon);
                    } else {
                        targetLabel.setText("No Image");
                    }
                } catch (Exception e) {
                    targetLabel.setText("Error Img");
                }
            }
        }.execute();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(user_dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(user_dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(user_dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(user_dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new user_dashboard().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}




