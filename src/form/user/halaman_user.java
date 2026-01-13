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
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
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


    private int currentTradCount = 0;
    private int currentModCount = 0;

    public halaman_user() {
        initComponents();
        setupForm();
        loadDataAsync("", "Semua", "");
    }


    private void setupForm() {
        this.setTitle("Info Ngopi Lurrrr - Rekomendasi Terbaik");
        this.setExtendedState(MAXIMIZED_BOTH);
        this.setLayout(new BorderLayout());


        this.getContentPane().remove(panel_kuning);
        this.add(panel_kuning, BorderLayout.NORTH);


        kontenerTradisional.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 20));
        kontenerTradisional.setBackground(new Color(9, 64, 49));
        
        kontenerModeren1.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 20));
        kontenerModeren1.setBackground(new Color(9, 64, 49));


        this.getContentPane().remove(panel_Dasar);
        panel_Dasar.removeAll();
        panel_Dasar.setLayout(new BoxLayout(panel_Dasar, BoxLayout.Y_AXIS));


        panel_Dasar.add(Box.createVerticalStrut(30));
        jLabel6.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel_Dasar.add(jLabel6);
        panel_Dasar.add(Box.createVerticalStrut(40));


        JPanel wrapJudulTrad = new JPanel(new FlowLayout(FlowLayout.LEFT));
        wrapJudulTrad.setBackground(new Color(9, 64, 49));
        wrapJudulTrad.add(jLabel7);
        wrapJudulTrad.setMaximumSize(new Dimension(32767, 45));
        panel_Dasar.add(wrapJudulTrad);
        panel_Dasar.add(kontenerTradisional);

        panel_Dasar.add(Box.createVerticalStrut(30));


        JPanel wrapJudulMod = new JPanel(new FlowLayout(FlowLayout.LEFT));
        wrapJudulMod.setBackground(new Color(9, 64, 49));
        wrapJudulMod.add(jLabel8);
        wrapJudulMod.setMaximumSize(new Dimension(32767, 45));
        panel_Dasar.add(wrapJudulMod);
        panel_Dasar.add(kontenerModeren1);
        
        panel_Dasar.add(Box.createVerticalStrut(50));

        JScrollPane globalScroll = new JScrollPane(panel_Dasar);
        globalScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        globalScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        globalScroll.setBorder(null);
        globalScroll.getVerticalScrollBar().setUnitIncrement(20);

        this.add(globalScroll, BorderLayout.CENTER);


        this.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent evt) {
                updatePanelHeight(kontenerTradisional, currentTradCount);
                updatePanelHeight(kontenerModeren1, currentModCount);
                revalidate();
            }
        });

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                imageExecutor.shutdownNow();
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
                    System.err.println("Error: " + e.getMessage());
                } finally {
                    showLoading(false);
                }
            }
        }.execute();
    }

    private void showLoading(boolean show) {
        if (show) {
            kontenerTradisional.removeAll();
            kontenerModeren1.removeAll();
            JLabel load = new JLabel("Sedang memuat data...", SwingConstants.CENTER);
            load.setFont(new Font("Segoe UI", Font.ITALIC, 16));
            load.setForeground(Color.WHITE);
            kontenerTradisional.add(load);
            kontenerTradisional.revalidate(); kontenerTradisional.repaint();
        }
    }

    private void displayData(List<Caffe> list, String keyword, String kategori, String daerah) {
        kontenerTradisional.removeAll();
        kontenerModeren1.removeAll();

        int countTrad = 0;
        int countMod = 0;

        for (Caffe c : list) {
            if (!keyword.isEmpty() && !c.getNamaCaffe().toLowerCase().contains(keyword.toLowerCase())) {
                continue;
            }

            JPanel card = createCard(c);
            String kat = c.getKategori() != null ? c.getKategori().toLowerCase() : "";

            if (kat.contains("tradisional")) {
                kontenerTradisional.add(card);
                countTrad++;
                loadImageAsync(c.getId(), card);
            } else {
                kontenerModeren1.add(card);
                countMod++;
                loadImageAsync(c.getId(), card);
            }
        }

        if (countTrad == 0) addEmptyMessage(kontenerTradisional, "Tidak ada data Tradisional");
        if (countMod == 0) addEmptyMessage(kontenerModeren1, "Tidak ada data Modern");

        currentTradCount = countTrad;
        currentModCount = countMod;


        updatePanelHeight(kontenerTradisional, countTrad);
        updatePanelHeight(kontenerModeren1, countMod);

        kontenerTradisional.revalidate(); kontenerTradisional.repaint();
        kontenerModeren1.revalidate(); kontenerModeren1.repaint();
    }
    
    
    private void updatePanelHeight(JPanel panel, int itemCount) {
        if (itemCount == 0) {
            panel.setPreferredSize(new Dimension(panel.getWidth(), 50));
            return;
        }

        int panelWidth = panel.getWidth();
        if (panelWidth <= 0) panelWidth = this.getWidth() - 50; 
        if (panelWidth <= 0) panelWidth = 1200;

        int cardWidth = 220; 
        int gap = 20;
        int effectiveItemWidth = cardWidth + gap;
        int columns = panelWidth / effectiveItemWidth;
        if (columns < 1) columns = 1; 
        int rows = (int) Math.ceil((double) itemCount / (double) columns);
        int cardHeight = 280; 
        int totalHeight = (rows * (cardHeight + gap)) + gap + 20;
        Dimension size = new Dimension(panelWidth, totalHeight);
        panel.setPreferredSize(size);
        panel.setMaximumSize(new Dimension(32767, totalHeight)); 
    }

    private void addEmptyMessage(JPanel panel, String text) {
        JLabel lbl = new JLabel(text);
        lbl.setForeground(Color.LIGHT_GRAY);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        panel.add(lbl);
    }


    private JPanel createCard(Caffe c) {
        JPanel card = new JPanel(new BorderLayout());
        Dimension dim = new Dimension(220, 280);
        card.setPreferredSize(dim);
        card.setMaximumSize(dim);
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(new Color(252, 208, 89), 2));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel imgLabel = new JLabel("Loading...", SwingConstants.CENTER);
        imgLabel.setPreferredSize(new Dimension(220, 160));
        imgLabel.setOpaque(true);
        imgLabel.setBackground(new Color(220, 220, 220));
        imgLabel.putClientProperty("targetImg", true);

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel nameLabel = new JLabel(c.getNamaCaffe());
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        nameLabel.setForeground(new Color(9, 64, 49));
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        String daerah = c.getDaerah();
        if (daerah != null && daerah.length() > 25) daerah = daerah.substring(0, 22) + "...";
        JLabel locLabel = new JLabel(daerah);
        locLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        locLabel.setForeground(Color.GRAY);
        locLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        infoPanel.add(nameLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(locLabel);

        card.add(imgLabel, BorderLayout.CENTER);
        card.add(infoPanel, BorderLayout.SOUTH);

        MouseAdapter adapter = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                User_InfoNgopi infoPage = new User_InfoNgopi(c);
                infoPage.setVisible(true);
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBorder(BorderFactory.createLineBorder(new Color(9, 64, 49), 4));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                card.setBorder(BorderFactory.createLineBorder(new Color(252, 208, 89), 2));
            }
        };
        addClickListenerRecursively(card, adapter);
        return card;
    }
    
    private void addClickListenerRecursively(Container container, MouseAdapter listener) {
        container.addMouseListener(listener);
        for (Component component : container.getComponents()) {
            if (component instanceof Container) {
                addClickListenerRecursively((Container) component, listener);
            } else {
                component.addMouseListener(listener);
            }
        }
    }

    private void loadImageAsync(int id, JPanel card) {
        imageExecutor.submit(() -> {
            try {
                byte[] imgData = caffeDAO.getImageBytesById(id);
                if (imgData != null) {
                    BufferedImage raw = ImageIO.read(new ByteArrayInputStream(imgData));
                    if (raw == null) return;
                    
                    int w = 220; int h = 160;
                    BufferedImage resized = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
                    Graphics2D g2 = resized.createGraphics();
                    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                    g2.drawImage(raw, 0, 0, w, h, null);
                    g2.dispose();
                    
                    ImageIcon icon = new ImageIcon(resized);
                    SwingUtilities.invokeLater(() -> {
                        for (Component c : card.getComponents()) {
                            if (c instanceof JLabel) {
                                JLabel l = (JLabel) c;
                                if (Boolean.TRUE.equals(l.getClientProperty("targetImg"))) {
                                    l.setText(""); l.setIcon(icon);
                                }
                            }
                        }
                    });
                }
            } catch (Exception e) {}
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
        panel_kuning = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        btnSearch = new javax.swing.JButton();
        txtCari = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtUserName1 = new javax.swing.JLabel();
        panel_Dasar = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        kontenerTradisional = new javax.swing.JPanel();
        kontenerModeren1 = new javax.swing.JPanel();

        txtUserName.setBackground(new java.awt.Color(9, 64, 49));
        txtUserName.setFont(new java.awt.Font("Segoe UI Black", 1, 24)); // NOI18N
        txtUserName.setForeground(new java.awt.Color(255, 255, 255));
        txtUserName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtUserName.setText("ADMIN");
        txtUserName.setFocusable(false);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        panel_kuning.setBackground(new java.awt.Color(252, 208, 89));

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
        jLabel5.setIcon(new javax.swing.ImageIcon("D:\\UAS PBO\\APPS-REKOMENDASI-TEMPAT-NGOPI\\src\\image\\Profile.png")); // NOI18N
        jLabel5.setText("i");
        jLabel5.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        txtUserName1.setBackground(new java.awt.Color(9, 64, 49));
        txtUserName1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txtUserName1.setForeground(new java.awt.Color(255, 255, 255));
        txtUserName1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtUserName1.setText("ADMIN");
        txtUserName1.setFocusable(false);

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

        javax.swing.GroupLayout kontenerTradisionalLayout = new javax.swing.GroupLayout(kontenerTradisional);
        kontenerTradisional.setLayout(kontenerTradisionalLayout);
        kontenerTradisionalLayout.setHorizontalGroup(
            kontenerTradisionalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1125, Short.MAX_VALUE)
        );
        kontenerTradisionalLayout.setVerticalGroup(
            kontenerTradisionalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 444, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout kontenerModeren1Layout = new javax.swing.GroupLayout(kontenerModeren1);
        kontenerModeren1.setLayout(kontenerModeren1Layout);
        kontenerModeren1Layout.setHorizontalGroup(
            kontenerModeren1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1125, Short.MAX_VALUE)
        );
        kontenerModeren1Layout.setVerticalGroup(
            kontenerModeren1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 444, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout panel_DasarLayout = new javax.swing.GroupLayout(panel_Dasar);
        panel_Dasar.setLayout(panel_DasarLayout);
        panel_DasarLayout.setHorizontalGroup(
            panel_DasarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel_DasarLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panel_DasarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(kontenerModeren1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(kontenerTradisional, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(75, 75, 75))
            .addGroup(panel_DasarLayout.createSequentialGroup()
                .addGroup(panel_DasarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panel_DasarLayout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addComponent(jLabel6))
                    .addGroup(panel_DasarLayout.createSequentialGroup()
                        .addGap(43, 43, 43)
                        .addGroup(panel_DasarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panel_DasarLayout.setVerticalGroup(
            panel_DasarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_DasarLayout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addComponent(jLabel6)
                .addGap(83, 83, 83)
                .addComponent(jLabel7)
                .addGap(18, 18, 18)
                .addComponent(kontenerTradisional, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(86, 86, 86)
                .addComponent(jLabel8)
                .addGap(30, 30, 30)
                .addComponent(kontenerModeren1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(50, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout panel_kuningLayout = new javax.swing.GroupLayout(panel_kuning);
        panel_kuning.setLayout(panel_kuningLayout);
        panel_kuningLayout.setHorizontalGroup(
            panel_kuningLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel_kuningLayout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(jLabel1)
                .addGap(62, 62, 62)
                .addComponent(jLabel2)
                .addGap(83, 83, 83)
                .addComponent(jLabel3)
                .addGap(69, 69, 69)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtCari, javax.swing.GroupLayout.DEFAULT_SIZE, 549, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(btnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(50, 50, 50)
                .addGroup(panel_kuningLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panel_kuningLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(txtUserName1))
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30))
            .addGroup(panel_kuningLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panel_Dasar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        panel_kuningLayout.setVerticalGroup(
            panel_kuningLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_kuningLayout.createSequentialGroup()
                .addContainerGap(10, Short.MAX_VALUE)
                .addGroup(panel_kuningLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(txtCari, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtUserName1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panel_Dasar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panel_kuning, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panel_kuning, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        // TODO add your handling code here:
        loadDataAsync(txtCari.getText().trim(), "Semua", "");
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
    private javax.swing.JPanel kontenerModeren1;
    private javax.swing.JPanel kontenerTradisional;
    private javax.swing.JPanel panel_Dasar;
    private javax.swing.JPanel panel_kuning;
    private javax.swing.JTextField txtCari;
    private javax.swing.JLabel txtUserName;
    private javax.swing.JLabel txtUserName1;
    // End of variables declaration//GEN-END:variables
}


