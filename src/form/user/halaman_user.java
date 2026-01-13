  /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

package form.user;

import DAO.CoffeShopDAO;
import Model.Caffe;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

/**
 *
 * @author dwife
 */

public class halaman_user extends javax.swing.JFrame {


    private final CoffeShopDAO caffeDAO = new CoffeShopDAO();
    private final ExecutorService imageExecutor = Executors.newFixedThreadPool(4);
    
    public halaman_user() {
        initComponents(); 
        this.setTitle("Info Ngopi Lurrrr");
        this.setExtendedState(MAXIMIZED_BOTH); 
        this.setLayout(new BorderLayout());
        this.getContentPane().remove(jPanel2);
        this.add(jPanel2, BorderLayout.NORTH);
        this.getContentPane().remove(panel_Dasar);
        
        JScrollPane globalScroll = new JScrollPane(panel_Dasar);
        globalScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        globalScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        globalScroll.setBorder(null); 
        globalScroll.getVerticalScrollBar().setUnitIncrement(25);
        this.add(globalScroll, BorderLayout.CENTER);
        
        
        
        setupHorizontalPanel(kontener1, scrollTradisional1);
        setupHorizontalPanel(kontener2, scrollModeren1);
        this.revalidate();
        this.repaint();
        loadDataAsync("", "Semua", "");
    }
    
    
    
 
    private void setupHorizontalPanel(JPanel panelKontener, JScrollPane scrollPane) {
            panelKontener.setLayout(new BoxLayout(panelKontener, BoxLayout.X_AXIS));
            panelKontener.setBackground(new Color(9, 64, 49));
            scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
            scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
            scrollPane.setBorder(null);
            scrollPane.addMouseWheelListener(e -> {

                if (e.getWheelRotation() != 0) {
                    javax.swing.JScrollBar sb = scrollPane.getHorizontalScrollBar();
                    int scrollAmount = e.getWheelRotation() * 40;
                    sb.setValue(sb.getValue() + scrollAmount);
                    e.consume();
                }
            });
        }


    private void loadDataAsync(String keyword, String kategori, String daerah) {
        showLoading(true);
        new SwingWorker<List<Caffe>, Void>() {
            @Override
            protected List<Caffe> doInBackground() throws Exception {
                return caffeDAO.getAll();
            }

            @Override
            protected void done() {
                try {
                    displayData(get(), keyword, kategori, daerah);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Error Load Data: " + e.getMessage());
                } finally {
                    showLoading(false);
                }
            }
        }.execute();
    }

    private void showLoading(boolean show) {
        if (show) {
            kontener1.removeAll();
            JLabel load = new JLabel("Memuat Data...", SwingConstants.CENTER);
            load.setForeground(Color.WHITE);
            load.setFont(new Font("Segoe UI", Font.ITALIC, 14));
            kontener1.add(load);
            kontener1.revalidate(); kontener1.repaint();
        }
    }

    private void displayData(List<Caffe> list, String keyword, String kategori, String daerah) {
        kontener1.removeAll();
        kontener2.removeAll();
        
        boolean foundTrad = false;
        boolean foundMod = false;

        for (Caffe c : list) {
            
            if (!keyword.isEmpty() && !c.getNamaCaffe().toLowerCase().contains(keyword.toLowerCase())) {
                continue;
            }
            

            JPanel card = createCard(c);
            String k = c.getKategori() != null ? c.getKategori().toLowerCase() : "";
            
            if (k.contains("tradisional")) {
                kontener1.add(card);
                kontener1.add(Box.createRigidArea(new Dimension(20, 0)));
                foundTrad = true;
                loadImageAsync(c.getId(), card);
            } 
            else if (k.contains("modern")) {
                kontener2.add(card);
                kontener2.add(Box.createRigidArea(new Dimension(20, 0))); 
                foundMod = true;
                loadImageAsync(c.getId(), card);
            }
        }

        if (!foundTrad) addEmptyMessage(kontener1, "Tidak ada data Tradisional");
        if (!foundMod) addEmptyMessage(kontener2, "Tidak ada data Modern");
        

        kontener1.revalidate(); kontener1.repaint();
        kontener2.revalidate(); kontener2.repaint();
    }
    
    private void addEmptyMessage(JPanel panel, String text) {
        JLabel lbl = new JLabel(text);
        lbl.setForeground(Color.WHITE);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        panel.add(lbl);
    }

    // buat kartu 
    
    private JPanel createCard(Caffe c) {
        JPanel card = new JPanel(new BorderLayout());

        Dimension dim = new Dimension(250, 300);
        card.setPreferredSize(dim);
        card.setMaximumSize(dim);
        card.setMinimumSize(dim);
        
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(new Color(252, 208, 89), 2));


        JLabel imgLabel = new JLabel("Loading...", SwingConstants.CENTER);
        imgLabel.setPreferredSize(new Dimension(250, 180));
        imgLabel.setOpaque(true);
        imgLabel.setBackground(new Color(240, 240, 240));
        imgLabel.setForeground(Color.GRAY);
        imgLabel.putClientProperty("targetImg", true);
        
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));
        
        JLabel nameLabel = new JLabel(c.getNamaCaffe());
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        nameLabel.setForeground(new Color(9, 64, 49)); 
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel locLabel = new JLabel(c.getDaerah());
        locLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        locLabel.setForeground(Color.GRAY);
        locLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        infoPanel.add(nameLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(locLabel);
        
        card.add(imgLabel, BorderLayout.CENTER);
        card.add(infoPanel, BorderLayout.SOUTH);
        
        return card;
    }
    
    
    private void loadImageAsync(int id, JPanel card) {
        imageExecutor.submit(() -> {
            try {
                byte[] imgData = caffeDAO.getImageBytesById(id);
                if (imgData != null) {
                    ByteArrayInputStream bais = new ByteArrayInputStream(imgData);
                    Image raw = ImageIO.read(bais);
                    Image scaled = raw.getScaledInstance(250, 180, Image.SCALE_SMOOTH);
                    ImageIcon icon = new ImageIcon(scaled);
                    
                    SwingUtilities.invokeLater(() -> {
                        for (Component comp : card.getComponents()) {
                            if (comp instanceof JLabel && ((JLabel)comp).getClientProperty("targetImg") != null) {
                                ((JLabel)comp).setText("");
                                ((JLabel)comp).setIcon(icon);
                            }
                        }
                    });
                }
            } catch (Exception e) {
             
            }
        });
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        txtUserName = new javax.swing.JLabel();
        panel_Dasar = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        scrollTradisional = new javax.swing.JScrollPane();
        scrollTradisional1 = new javax.swing.JScrollPane();
        kontener1 = new javax.swing.JPanel();
        scrollModeren1 = new javax.swing.JScrollPane();
        kontener2 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        btnSearch = new javax.swing.JButton();
        txtCari = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtUserName1 = new javax.swing.JLabel();

        txtUserName.setBackground(new java.awt.Color(9, 64, 49));
        txtUserName.setFont(new java.awt.Font("Segoe UI Black", 1, 24)); // NOI18N
        txtUserName.setForeground(new java.awt.Color(255, 255, 255));
        txtUserName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtUserName.setText("ADMIN");
        txtUserName.setFocusable(false);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        panel_Dasar.setBackground(new java.awt.Color(9, 64, 49));

        jLabel6.setFont(new java.awt.Font("Segoe UI Black", 1, 56)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Info Ngopi Lurrrr");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(252, 208, 89));
        jLabel7.setText("Tradisional");

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(252, 208, 89));
        jLabel8.setText("Modern");

        scrollTradisional1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        kontener1.setBackground(new java.awt.Color(204, 255, 204));

        javax.swing.GroupLayout kontener1Layout = new javax.swing.GroupLayout(kontener1);
        kontener1.setLayout(kontener1Layout);
        kontener1Layout.setHorizontalGroup(
            kontener1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1252, Short.MAX_VALUE)
        );
        kontener1Layout.setVerticalGroup(
            kontener1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 319, Short.MAX_VALUE)
        );

        scrollTradisional1.setViewportView(kontener1);

        scrollTradisional.setViewportView(scrollTradisional1);

        scrollModeren1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        kontener2.setBackground(new java.awt.Color(204, 255, 204));

        javax.swing.GroupLayout kontener2Layout = new javax.swing.GroupLayout(kontener2);
        kontener2.setLayout(kontener2Layout);
        kontener2Layout.setHorizontalGroup(
            kontener2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1254, Short.MAX_VALUE)
        );
        kontener2Layout.setVerticalGroup(
            kontener2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 305, Short.MAX_VALUE)
        );

        scrollModeren1.setViewportView(kontener2);

        javax.swing.GroupLayout panel_DasarLayout = new javax.swing.GroupLayout(panel_Dasar);
        panel_Dasar.setLayout(panel_DasarLayout);
        panel_DasarLayout.setHorizontalGroup(
            panel_DasarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_DasarLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(panel_DasarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(scrollTradisional)
                    .addComponent(scrollModeren1)
                    .addGroup(panel_DasarLayout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addContainerGap())
                    .addGroup(panel_DasarLayout.createSequentialGroup()
                        .addGroup(panel_DasarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        panel_DasarLayout.setVerticalGroup(
            panel_DasarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_DasarLayout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addComponent(jLabel6)
                .addGap(106, 106, 106)
                .addComponent(jLabel7)
                .addGap(18, 18, 18)
                .addComponent(scrollTradisional, javax.swing.GroupLayout.PREFERRED_SIZE, 323, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35)
                .addComponent(jLabel8)
                .addGap(18, 18, 18)
                .addComponent(scrollModeren1, javax.swing.GroupLayout.PREFERRED_SIZE, 307, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(57, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(252, 208, 89));

        jLabel1.setText("Beranda");

        jLabel2.setText("Tradisional");

        jLabel3.setText("Modern");

        btnSearch.setText("Search");
        btnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchActionPerformed(evt);
            }
        });

        jLabel4.setText("InfoNgopi");

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/Profile.png"))); // NOI18N
        jLabel5.setText("i");
        jLabel5.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        txtUserName1.setBackground(new java.awt.Color(9, 64, 49));
        txtUserName1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txtUserName1.setForeground(new java.awt.Color(255, 255, 255));
        txtUserName1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtUserName1.setText("ADMIN");
        txtUserName1.setFocusable(false);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addComponent(jLabel1)
                        .addGap(62, 62, 62)
                        .addComponent(jLabel2)
                        .addGap(83, 83, 83)
                        .addComponent(jLabel3)
                        .addGap(69, 69, 69)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtCari)
                        .addGap(18, 18, 18)
                        .addComponent(btnSearch)
                        .addGap(76, 76, 76)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtUserName1)))
                .addGap(94, 94, 94))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(txtCari, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSearch)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5))
                .addContainerGap(61, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(txtUserName1)
                .addGap(14, 14, 14))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(panel_Dasar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(panel_Dasar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        // TODO add your handling code here:
            String keyword = txtCari.getText().trim();
            loadDataAsync(keyword, "Semua", "");

    }//GEN-LAST:event_btnSearchActionPerformed

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
            java.util.logging.Logger.getLogger(halaman_user.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(halaman_user.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(halaman_user.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(halaman_user.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new halaman_user().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnSearch;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel kontener1;
    private javax.swing.JPanel kontener2;
    private javax.swing.JPanel panel_Dasar;
    private javax.swing.JScrollPane scrollModeren1;
    private javax.swing.JScrollPane scrollTradisional;
    private javax.swing.JScrollPane scrollTradisional1;
    private javax.swing.JTextField txtCari;
    private javax.swing.JLabel txtUserName;
    private javax.swing.JLabel txtUserName1;
    // End of variables declaration//GEN-END:variables
}



