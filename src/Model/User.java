package Model;

import java.time.LocalDateTime;

public class User {
    private int userId;
    private String username;
    private String passwordHash;
    private boolean isAdmin;
    private String email;
    private String fullName;
    private LocalDateTime createdAt;
    
    public User(){};
    
    public User(int userId, String username, String passwordHash, boolean isAdmin, 
                String email, String fullName) {
        this.userId = userId;
        this.username = username;
        this.passwordHash = passwordHash;
        this.isAdmin = isAdmin;
        this.email = email;
        this.fullName = fullName;
    }

    public User(String username, String passwordHash, String email, String fullName, boolean isAdmin) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.email = email;
        this.fullName = fullName;
        this.isAdmin = isAdmin;
    }
    
    public User(String username, String email, boolean isAdmin) {
        this.username = username;
        this.email = email;
        this.createdAt = LocalDateTime.now();
        this.isAdmin = isAdmin;
    }
    

    public int getUserId() {
        return userId;
    }
    
    public void setUserId(int userId){
        this.userId = userId;
    }
    
    public String getUsername() {
        return username; 
    }
    
    public void setUsername(String username){
        this.username = username;
    }
    
    public String getPasswordHash() {
        return passwordHash; 
    }
    
    public void setPasswordHash(String passwordHash){
        this.passwordHash = passwordHash;
    }
    
    public boolean getIsAdmin() {
        return isAdmin;
    }
    
    public void setIsAdmin(boolean isAdmin){
        this.isAdmin = isAdmin;
    }
    
    
    
    
    
    

    public String getRoleAsString() {
        return isAdmin ? "Admin" : "User";
    }
    
   
    public void setRoleFromString(String role) {
        this.isAdmin = "Admin".equalsIgnoreCase(role) || "1".equals(role);
    }
    

    public void setRoleFromInt(int role) {
        this.isAdmin = (role == 1);
    }
    
  
    public int getRoleAsInt() {
        return isAdmin ? 1 : 0;
    }
    
    public String getEmail() { 
        return email; 
    }
    
    public void setEmail(String email){
        this.email = email;
    }
    
    public String getFullName() { 
        return fullName; 
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    
    
    @Override
    public String toString() {
        return username + (isAdmin ? " (Admin)" : " (User)");
    }
}



