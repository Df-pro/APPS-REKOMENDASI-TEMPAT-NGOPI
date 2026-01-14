  /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package form.Login;
import DAO.UserDAO;
import Model.User;
import form.Admin.DashboardAdmin;
import form.user.User_InfoNgopi;
import form.user.halaman_user;
import javax.swing.*;
import java.awt.event.*;
import java.util.prefs.Preferences;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
/**
 *
 * @author dwife
 */


public class LoginForm extends javax.swing.JFrame {
    private UserDAO userDAO;
    private Preferences prefs;
    
    public LoginForm() {
        
        
        initComponents();
        // ini code buat ful layar otomatis 
       //this.setExtendedState(JFrame.MAXIMIZED_BOTH);

        
        setLocationRelativeTo(null);
        setTitle("Xsiri Coffee Shop - Login");
        
        userDAO = new UserDAO();
        prefs = Preferences.userNodeForPackage(LoginForm.class);

        chkRemember.setText("Remember Me");
        
        loadRememberedUser();
        setupKeyboardShortcuts();
    }
    
    private void loadRememberedUser() {
        if (prefs.getBoolean("remember", false)) {
            String savedUsername = prefs.get("username", "");
            if (!savedUsername.isEmpty()) {
                txtUsername.setText(savedUsername);
                chkRemember.setSelected(true);
                txtPassword.requestFocus();
            }
        }
    }
    
    private void setupKeyboardShortcuts() {

        txtPassword.getInputMap(JComponent.WHEN_FOCUSED).put(
            KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "login"
        );
        txtPassword.getActionMap().put("login", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loginUser();
            }
        });


        txtUsername.getInputMap(JComponent.WHEN_FOCUSED).put(
            KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "next"
        );
        txtUsername.getActionMap().put("next", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                txtPassword.requestFocus();
            }
        });
    }
    
    private void loginUser() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());


        if (username.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Please enter username!", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            txtUsername.requestFocus();
            return;
        }
        
        if (password.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Please enter password!", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            txtPassword.requestFocus();
            return;
        }

        String hashedPassword = hashPassword(password);
        
        try {

            User user = userDAO.login(username, hashedPassword);
            
            if (user != null) {

                saveRememberMe(username, chkRemember.isSelected());
                
                JOptionPane.showMessageDialog(this, 
                    "Login successful! Welcome " + user.getFullName(), 
                    "Success", 
                    JOptionPane.INFORMATION_MESSAGE);

                redirectToDashboard(user);
                dispose();
                
            } else {

                JOptionPane.showMessageDialog(this, 
                    "Invalid username or password!", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
                txtPassword.setText("");
                txtPassword.requestFocus();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Database Error: " + e.getMessage(), 
                "Connection Error", 
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private void saveRememberMe(String username, boolean remember) {
        try {
            if (remember) {
                prefs.put("username", username);
                prefs.putBoolean("remember", true);
            } else {
                prefs.remove("username");
                prefs.putBoolean("remember", false);
            }
            prefs.flush();
        } catch (Exception e) {
            System.err.println("Error saving preferences: " + e.getMessage());
        }
    }
    
    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Hashing algorithm not available: " + e.getMessage());
            return password;
        }
    }
    
    private void redirectToDashboard(User user) {
        try {
            if (user.getIsAdmin()) {

                DashboardAdmin adminDash = new DashboardAdmin(user);
                adminDash.setVisible(true);
                this.dispose();
            } else {
                halaman_user userdash = new halaman_user();
                userdash.setVisible(true);
                
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error opening dashboard: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private void openRegisterForm() {
        try {
            RegisterForm registerForm = new RegisterForm();
            registerForm.setVisible(true);
            this.dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Cannot open registration form: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void forgotPassword() {
        String email = JOptionPane.showInputDialog(
            this,
            "Enter your registered email:",
            "Forgot Password",
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (email != null && !email.trim().isEmpty()) {
            if (!isValidEmail(email)) {
                JOptionPane.showMessageDialog(this,
                    "Invalid email format!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            try {
                if (userDAO.isEmailExists(email)) {
                    JOptionPane.showMessageDialog(this,
                        "Password reset link has been sent to:\n" + email,
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this,
                        "Email not found in our system!",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "Error: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }
    
    private void savePreferences() {
        try {
            prefs.putBoolean("remember", chkRemember.isSelected());
            if (chkRemember.isSelected()) {
                prefs.put("username", txtUsername.getText().trim());
            } else {
                prefs.remove("username");
            }
            prefs.flush();
        } catch (Exception e) {
            System.err.println("Error saving preferences: " + e.getMessage());
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtUsername = new javax.swing.JTextField();
        txtPassword = new javax.swing.JPasswordField();
        btnLogin = new javax.swing.JButton();
        btnRegister = new javax.swing.JButton();
        btnForgot = new javax.swing.JButton();
        chkRemember = new javax.swing.JCheckBox();
        jLabel4 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(9, 64, 49));
        jPanel1.setPreferredSize(new java.awt.Dimension(540, 540));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Segoe UI Black", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(9, 64, 49));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("LOGIN FORM ");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 20, 168, 51));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(9, 64, 49));
        jLabel2.setText("Username:");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 110, 141, -1));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(9, 64, 49));
        jLabel3.setText("Password :");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 190, 152, -1));

        txtUsername.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtUsernameActionPerformed(evt);
            }
        });
        jPanel1.add(txtUsername, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 110, 310, 39));

        txtPassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPasswordActionPerformed(evt);
            }
        });
        jPanel1.add(txtPassword, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 180, 310, 40));

        btnLogin.setBackground(new java.awt.Color(9, 64, 49));
        btnLogin.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnLogin.setForeground(new java.awt.Color(255, 255, 255));
        btnLogin.setText("LOGIN ");
        btnLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLoginActionPerformed(evt);
            }
        });
        jPanel1.add(btnLogin, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 330, -1, -1));

        btnRegister.setBackground(new java.awt.Color(9, 64, 49));
        btnRegister.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnRegister.setForeground(new java.awt.Color(255, 255, 255));
        btnRegister.setText("REGISTER ");
        btnRegister.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegisterActionPerformed(evt);
            }
        });
        jPanel1.add(btnRegister, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 330, -1, -1));

        btnForgot.setBackground(new java.awt.Color(9, 64, 49));
        btnForgot.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnForgot.setForeground(new java.awt.Color(255, 255, 255));
        btnForgot.setText("Forgot Password?");
        btnForgot.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnForgotActionPerformed(evt);
            }
        });
        jPanel1.add(btnForgot, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 410, -1, -1));

        chkRemember.setBackground(new java.awt.Color(255, 255, 255));
        chkRemember.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        chkRemember.setForeground(new java.awt.Color(9, 64, 49));
        chkRemember.setText("chkRemember");
        chkRemember.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkRememberActionPerformed(evt);
            }
        });
        jPanel1.add(chkRemember, new org.netbeans.lib.awtextra.AbsoluteConstraints(67, 257, -1, -1));

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/latar Login.png"))); // NOI18N
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 550, 540));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 6, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoginActionPerformed
        // TODO add your handling code here:
        loginUser();
    }//GEN-LAST:event_btnLoginActionPerformed

    private void txtUsernameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtUsernameActionPerformed
        // TODO add your handling code here:
      txtPassword.requestFocus();
    }//GEN-LAST:event_txtUsernameActionPerformed

    private void chkRememberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkRememberActionPerformed
        // TODO add your handling code here:
        savePreferences();
    }//GEN-LAST:event_chkRememberActionPerformed

    private void btnRegisterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegisterActionPerformed
        // TODO add your handling code here:
         openRegisterForm();
    }//GEN-LAST:event_btnRegisterActionPerformed

    private void btnForgotActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnForgotActionPerformed
        // TODO add your handling code here:
          forgotPassword();
    }//GEN-LAST:event_btnForgotActionPerformed

    private void txtPasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPasswordActionPerformed
        // TODO add your handling code here:
       loginUser();
    }//GEN-LAST:event_txtPasswordActionPerformed

     

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new LoginForm().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnForgot;
    private javax.swing.JButton btnLogin;
    private javax.swing.JButton btnRegister;
    private javax.swing.JCheckBox chkRemember;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPasswordField txtPassword;
    private javax.swing.JTextField txtUsername;
    // End of variables declaration//GEN-END:variables
}




