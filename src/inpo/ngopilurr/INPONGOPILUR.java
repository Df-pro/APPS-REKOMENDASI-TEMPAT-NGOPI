/*
 * NIPONGOPLURR.java
 * Main application class
 */
package inpo.ngopilurr; // atau package nipongoplurr;

import form.LoginForm;
import javax.swing.*;
import java.awt.*;

public class INPONGOPILUR extends JFrame {
    
    public INPONGOPILUR() {
        initComponents();
    }
    
    private void initComponents() {
        setTitle("NIPONGOPLURR System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        
        // Main panel dengan card layout
        JPanel mainPanel = new JPanel(new CardLayout());
        
        // Tambahkan semua form ke card layout
        mainPanel.add(new LoginForm(), "Login");
        
        getContentPane().add(mainPanel);
        
        // Show login form pertama kali
        CardLayout cl = (CardLayout) mainPanel.getLayout();
        cl.show(mainPanel, "Login");
    }
    
    public static void main(String[] args) {
        try {
            // Set look and feel
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | 
                 IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            new INPONGOPILUR().setVisible(true);
        });
    }
}