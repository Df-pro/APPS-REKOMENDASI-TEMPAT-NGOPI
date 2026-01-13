package Service;

import java.util.HashMap;
import java.util.Map;


public class AuthService {

    class User {
        private int id;
        private String username;
        private String password;
        private boolean isAdmin;
        
        public User(int id, String username, String password, boolean isAdmin) {
            this.id = id;
            this.username = username;
            this.password = password;
            this.isAdmin = isAdmin;
        }
        
        public String getUsername() { return username; }
        public String getPassword() { return password; }
        public boolean isAdmin() { return isAdmin; }
    }
    
    private User currentUser;
    private Map<String, User> userDatabase;
    
    public AuthService() {
        currentUser = null;
        userDatabase = new HashMap<>();
        
        userDatabase.put("admin", new User(1, "admin", "admin123", true));
        userDatabase.put("user1", new User(2, "user1", "user123", false));
        userDatabase.put("user2", new User(3, "user2", "password", false));
    }
    
    public boolean login(String username, String password) {
        User user = userDatabase.get(username);
        
        if (user != null && user.getPassword().equals(password)) {
            currentUser = user;
            System.out.println("Login berhasil: " + username);
            return true;
        }
        
        System.out.println("Login gagal: " + username);
        return false;
    }
    
    public String getCurrentUser() {
        return currentUser != null ? currentUser.getUsername() : "Tidak ada user";
    }
    
    public boolean isAdmin() {
        return currentUser != null && currentUser.isAdmin();
    }
    
    public void logout() {
        if (currentUser != null) {
            System.out.println("🚪 Logout: " + currentUser.getUsername());
            currentUser = null;
        }
    }

    public static void main(String[] args) {
        System.out.println("=== AUTH SERVICE TEST ===");
        
        AuthService auth = new AuthService();
        

        testLogin(auth, "admin", "admin123", true);  
        testLogin(auth, "user1", "user123", true);       
        testLogin(auth, "admin", "wrong", false); 
        testLogin(auth, "unknown", "pass", false);  
        

        auth.login("admin", "admin123");
        System.out.println("\nStatus admin: " + auth.isAdmin());
        auth.logout();
        
        auth.login("user1", "user123");
        System.out.println("Status admin: " + auth.isAdmin());
        auth.logout();
    }
    
    private static void testLogin(AuthService auth, String user, String pass, boolean shouldSucceed) {
        System.out.println("\nTest: " + user + " / " + pass);
        boolean result = auth.login(user, pass);
        
        if (result == shouldSucceed) {
            System.out.println(" Test passed");
            if (result) {
                System.out.println("   User: " + auth.getCurrentUser());
                auth.logout();
            }
        } else {
            System.out.println(" Test failed");
        }
    }
}



