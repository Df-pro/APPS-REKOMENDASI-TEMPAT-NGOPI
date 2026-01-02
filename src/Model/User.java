package Model;

public class User {
    private int userId;
    private String username;
    private String passwordHash;
    private boolean isAdmin;
    private String email;
    private String fullName;
    

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
    

    public int getUserId() {
        return userId;
    }
    
    public String getUsername() {
        return username; 
    }
    
    public String getPasswordHash() {
        return passwordHash; 
    }
    public boolean isAdmin() {
        return isAdmin;
    }
    public String getEmail() { 
        return email; 
    }
    
    public String getFullName() { 
        return fullName; 
    }
    
    @Override
    public String toString() {
        return username + (isAdmin ? " (Admin)" : " (User)");
    }
}

