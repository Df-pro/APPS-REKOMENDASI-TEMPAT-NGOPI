/*
 * INPONGOPILUR.java
 * Main application class (Titik Awal Aplikasi)
 */
package inpo.ngopilurr;

import form.Login.LoginForm;
import javax.swing.*;

public class INPONGOPILUR {
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // 1. Setup Tampilan (Agar tombol-tombol terlihat modern sesuai OS)
        try {
            // Opsi 1: Pakai Nimbus (Tampilan Modern Java)
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
            // Opsi 2: Pakai System (Mirip Windows asli) -> Hapus komen bawah jika ingin pakai ini
            // UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            
        } catch (ClassNotFoundException | InstantiationException | 
                 IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        
        // 2. Jalankan Form Login
        SwingUtilities.invokeLater(() -> {
            // Membuka jendela Login
            LoginForm login = new LoginForm();
            
            // Posisikan di tengah layar
            login.setLocationRelativeTo(null);
            
            // Tampilkan
            login.setVisible(true);
        });
    }
}