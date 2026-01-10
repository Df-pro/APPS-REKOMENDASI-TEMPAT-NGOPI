/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package form.Login;

import DAO.UserDAO;
import DAO.AdminCodeDAO;
import Model.User;
import javax.swing.*;
import java.awt.event.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author dwife
 * 
 */
public class RegisterForm extends javax.swing.JFrame {

    private UserDAO userDAO;
    private AdminCodeDAO adminCodeDAO;
    
    public RegisterForm() {
        initComponents();
        // ini code buat ful layar 
          //this.setExtendedState(JFrame.MAXIMIZED_BOTH);
          
        setLocationRelativeTo(null);
        setTitle("Xsiri Coffee Shop - Registration");
        
        userDAO = new UserDAO();
        adminCodeDAO = new AdminCodeDAO();
        

        selectedRole_User.setSelected(true);
        jLabel7.setVisible(false);
        txtAdminCode.setVisible(false);
        
        setupKeyboardShortcuts();
    }
    
    private void setupKeyboardShortcuts() {

        txtConfirm.getInputMap(JComponent.WHEN_FOCUSED).put(
            KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "register"
        );
        txtConfirm.getActionMap().put("register", new AbstractAction() {
            @Override 
            public void actionPerformed(ActionEvent e) {
                registerUser();
            }
        });

        txtUsername.getInputMap(JComponent.WHEN_FOCUSED).put(
            KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "next1"
        );
        txtUsername.getActionMap().put("next1", new AbstractAction() {
            @Override 
            public void actionPerformed(ActionEvent e) {
                txtEmail.requestFocus();
            }
        });
        

        txtEmail.getInputMap(JComponent.WHEN_FOCUSED).put(
            KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "next2"
        );
        txtEmail.getActionMap().put("next2", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                txtPassword.requestFocus();
            }
        });

        txtPassword.getInputMap(JComponent.WHEN_FOCUSED).put(
            KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "next3"
        );
        txtPassword.getActionMap().put("next3", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                txtConfirm.requestFocus();
            }
        });
        

        txtAdminCode.getInputMap(JComponent.WHEN_FOCUSED).put(
            KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "registerAdmin"
        );
        txtAdminCode.getActionMap().put("registerAdmin", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerUser();
            }
        });
    }
    
    private void toggleAdminCodeField(boolean show) {
        jLabel7.setVisible(show); 
        txtAdminCode.setVisible(show);
        
        if (show) {
            txtAdminCode.requestFocus();
        } else {
            txtAdminCode.setText("");
        }

        pack();
        setLocationRelativeTo(null);
    }
    
    private void registerUser() {
        String username = txtUsername.getText().trim();
        String email = txtEmail.getText().trim();
        String password = new String(txtPassword.getPassword());
        String confirm = new String(txtConfirm.getPassword());

        boolean isAdmin = selectedRole_Admin.isSelected();
        String adminCode = txtAdminCode.getText().trim();
        

        if (username.isEmpty()) {
            showError("Please enter username!");
            txtUsername.requestFocus();
            return;
        }
        
        if (!isValidUsername(username)) {
            showError("Username must be 3-20 characters (letters, numbers, underscore only)!");
            txtUsername.requestFocus();
            return;
        }
        
        if (email.isEmpty()) {
            showError("Please enter email!");
            txtEmail.requestFocus();
            return;
        }
        
        if (!isValidEmail(email)) {
            showError("Invalid email format!");
            txtEmail.requestFocus();
            return;
        }
        
        if (password.isEmpty()) {
            showError("Please enter password!");
            txtPassword.requestFocus();
            return;
        }
        
        if (password.length() < 6) {
            showError("Password must be at least 6 characters!");
            txtPassword.requestFocus();
            return;
        }
        
        if (confirm.isEmpty()) {
            showError("Please confirm password!");
            txtConfirm.requestFocus();
            return;
        }
        
        if (!password.equals(confirm)) {
            showError("Passwords do not match!");
            txtConfirm.setText("");
            txtPassword.setText("");
            txtPassword.requestFocus();
            return;
        }

        if (userDAO.isUsernameExists(username)) {
            showError("Username already exists! Please choose another.");
            txtUsername.requestFocus();
            return;
        }

        if (userDAO.isEmailExists(email)) {
            showError("Email already registered! Please use another email.");
            txtEmail.requestFocus();
            return;
        }

        if (isAdmin) {
            if (adminCode.isEmpty()) {
                showError("Admin Code is required for admin registration!");
                txtAdminCode.requestFocus();
                return;
            }

            if (!adminCodeDAO.isValidAdminCode(adminCode)) {
                showError("Invalid Admin Code! Please enter a valid admin code.");
                txtAdminCode.requestFocus();
                return;
            }
        }
        

        String hashedPassword = hashPassword(password);
        
        User newUser = new User(
            username,
            hashedPassword,
            email,
            "",  
            isAdmin
        );
        
        boolean success = userDAO.register(newUser);
        
        if (success) {

            if (isAdmin) {
                adminCodeDAO.markCodeAsUsed(adminCode);
            }
            
            JOptionPane.showMessageDialog(this,
                "Registration successful!\nYou can now login with your credentials.",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
            
            openLoginForm();
        } else {
            showError("Registration failed! Please try again.");
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
    
    private void openLoginForm() {
        LoginForm loginForm = new LoginForm();
        loginForm.setVisible(true);
        this.dispose();
    }
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, 
            message, 
            "Error", 
            JOptionPane.ERROR_MESSAGE);
    }
    
    private boolean isValidUsername(String username) {
        return username.matches("^[a-zA-Z0-9_]{3,20}$");
    }
    
    private boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }
    
    private void clearAllFields() {
        txtUsername.setText("");
        txtEmail.setText("");
        txtPassword.setText("");
        txtConfirm.setText("");
        txtAdminCode.setText("");
        selectedRole_User.setSelected(true); // Reset ke User
        toggleAdminCodeField(false);
        txtUsername.requestFocus();
    }
    
    private void validateUsernameOnType() {
        String username = txtUsername.getText().trim();
        if (!username.isEmpty() && !isValidUsername(username)) {
            txtUsername.setToolTipText("3-20 chars (letters, numbers, underscore)");
        } else {
            txtUsername.setToolTipText(null);
        }
    }
    
    private void validateEmailOnType() {
        String email = txtEmail.getText().trim();
        if (!email.isEmpty() && !isValidEmail(email)) {
            txtEmail.setToolTipText("Enter a valid email address");
        } else {
            txtEmail.setToolTipText(null);
        }
    }
    
    private void validatePasswordStrength() {
        String password = new String(txtPassword.getPassword());
        if (password.length() > 0 && password.length() < 6) {
            txtPassword.setToolTipText("Minimum 6 characters");
        } else {
            txtPassword.setToolTipText(null);
        }
    }
    
    private void validatePasswordConfirmation() {
        String password = new String(txtPassword.getPassword());
        String confirm = new String(txtConfirm.getPassword());
        
        if (!confirm.isEmpty() && !password.equals(confirm)) {
            txtConfirm.setToolTipText("Passwords do not match!");
        } else {
            txtConfirm.setToolTipText(null);
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

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtUsername = new javax.swing.JTextField();
        txtEmail = new javax.swing.JTextField();
        txtPassword = new javax.swing.JPasswordField();
        txtConfirm = new javax.swing.JPasswordField();
        selectedRole_Admin = new javax.swing.JRadioButton();
        selectedRole_User = new javax.swing.JRadioButton();
        txtAdminCode = new javax.swing.JTextField();
        btn_register = new javax.swing.JButton();
        btn_bck_login = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(9, 64, 49));

        jPanel1.setBackground(new java.awt.Color(9, 64, 49));
        jPanel1.setForeground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Segoe UI Black", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("REGISTRATION FORM");

        txtUsername.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtUsernameActionPerformed(evt);
            }
        });
        txtUsername.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtUsernameKeyReleased(evt);
            }
        });

        txtEmail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEmailActionPerformed(evt);
            }
        });
        txtEmail.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtEmailKeyReleased(evt);
            }
        });

        txtPassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPasswordActionPerformed(evt);
            }
        });
        txtPassword.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPasswordKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPasswordKeyReleased(evt);
            }
        });

        txtConfirm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtConfirmActionPerformed(evt);
            }
        });
        txtConfirm.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtConfirmKeyReleased(evt);
            }
        });

        selectedRole_Admin.setBackground(new java.awt.Color(9, 64, 49));
        buttonGroup1.add(selectedRole_Admin);
        selectedRole_Admin.setForeground(new java.awt.Color(255, 255, 255));
        selectedRole_Admin.setText("Admin");
        selectedRole_Admin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectedRole_AdminActionPerformed(evt);
            }
        });

        selectedRole_User.setBackground(new java.awt.Color(9, 64, 49));
        buttonGroup1.add(selectedRole_User);
        selectedRole_User.setForeground(new java.awt.Color(255, 255, 255));
        selectedRole_User.setText("User");
        selectedRole_User.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectedRole_UserActionPerformed(evt);
            }
        });

        txtAdminCode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAdminCodeActionPerformed(evt);
            }
        });

        btn_register.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn_register.setText("REGISTER");
        btn_register.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_registerActionPerformed(evt);
            }
        });

        btn_bck_login.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn_bck_login.setText("BACK TO LOGIN");
        btn_bck_login.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_bck_loginActionPerformed(evt);
            }
        });

        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Admin Code");

        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Role");

        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Confirm    :");

        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Password  :");

        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Email         :");

        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Username :");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jLabel2)
                    .addComponent(jLabel4)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jLabel7)
                            .addGap(32, 32, 32)
                            .addComponent(txtAdminCode))
                        .addComponent(jLabel5)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jLabel6)
                            .addGap(63, 63, 63)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(btn_register, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btn_bck_login))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addGap(43, 43, 43)
                                    .addComponent(selectedRole_User)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(selectedRole_Admin)
                                    .addGap(72, 72, 72))
                                .addComponent(txtEmail)
                                .addComponent(txtUsername)
                                .addComponent(txtConfirm)
                                .addComponent(txtPassword)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addGap(129, 129, 129)
                                    .addComponent(jLabel1)
                                    .addGap(0, 114, Short.MAX_VALUE))))))
                .addGap(88, 88, 88))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(28, 28, 28)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtUsername, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtConfirm, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(selectedRole_User)
                    .addComponent(selectedRole_Admin))
                .addGap(24, 24, 24)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txtAdminCode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 48, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_bck_login, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_register, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(35, 35, 35))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 6, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_registerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_registerActionPerformed
        // TODO add your handling code here:
        registerUser();
    }//GEN-LAST:event_btn_registerActionPerformed

    private void btn_bck_loginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_bck_loginActionPerformed
        // TODO add your handling code here:
        openLoginForm();
    }//GEN-LAST:event_btn_bck_loginActionPerformed

    private void selectedRole_UserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectedRole_UserActionPerformed
        // TODO add your handling code here:
        toggleAdminCodeField(false);
    }//GEN-LAST:event_selectedRole_UserActionPerformed

    private void selectedRole_AdminActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectedRole_AdminActionPerformed
        // TODO add your handling code here:
        toggleAdminCodeField(true);
    }//GEN-LAST:event_selectedRole_AdminActionPerformed

    private void txtConfirmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtConfirmActionPerformed
        // TODO add your handling code here:
        registerUser();
    }//GEN-LAST:event_txtConfirmActionPerformed

    private void txtPasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPasswordActionPerformed
        // TODO add your handling code here:
         txtConfirm.requestFocus();
    }//GEN-LAST:event_txtPasswordActionPerformed

    private void txtEmailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEmailActionPerformed
        // TODO add your handling code here:
        txtPassword.requestFocus();
    }//GEN-LAST:event_txtEmailActionPerformed

    private void txtUsernameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtUsernameActionPerformed
        // TODO add your handling code here:
         txtEmail.requestFocus();
    }//GEN-LAST:event_txtUsernameActionPerformed

    private void txtConfirmKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtConfirmKeyReleased
        // TODO add your handling code here:
        validatePasswordConfirmation();
    }//GEN-LAST:event_txtConfirmKeyReleased

    private void txtEmailKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEmailKeyReleased
        // TODO add your handling code here:
        validateEmailOnType();
    }//GEN-LAST:event_txtEmailKeyReleased

    private void txtUsernameKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtUsernameKeyReleased
        // TODO add your handling code here:
        validateUsernameOnType();
    }//GEN-LAST:event_txtUsernameKeyReleased

    private void txtAdminCodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAdminCodeActionPerformed
        // TODO add your handling code here:
        registerUser();
    }//GEN-LAST:event_txtAdminCodeActionPerformed

    private void txtPasswordKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPasswordKeyPressed
        // TODO add your handling code here:

    }//GEN-LAST:event_txtPasswordKeyPressed

    private void txtPasswordKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPasswordKeyReleased
        // TODO add your handling code here:
         validatePasswordStrength();
    }//GEN-LAST:event_txtPasswordKeyReleased

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
            java.util.logging.Logger.getLogger(RegisterForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(RegisterForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(RegisterForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(RegisterForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new RegisterForm().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_bck_login;
    private javax.swing.JButton btn_register;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JRadioButton selectedRole_Admin;
    private javax.swing.JRadioButton selectedRole_User;
    private javax.swing.JTextField txtAdminCode;
    private javax.swing.JPasswordField txtConfirm;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JPasswordField txtPassword;
    private javax.swing.JTextField txtUsername;
    // End of variables declaration//GEN-END:variables
}








