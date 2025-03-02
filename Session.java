package pack305;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Session {

    private static String username; // اسم المستخدم الحالي
    private static String Fname; // الاسم الأول
    private static String Lname; // الاسم الأخير
    private static String role; // الدور (User أو Consultative)
    private static String currentChatUser; // المستخدم الحالي في الدردشة

    // معلومات الاتصال بقاعدة البيانات
    private static final String DB_URL = "jdbc:mysql://localhost:3306/project2";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "layanasdf";

    // Getters and Setters
    public static String getRole() {
        return role;
    }

    public static void setRole(String role) {
        Session.role = role;
    }

    public static String getFname() {
        return Fname;
    }

    public static void setFname(String Fname) {
        Session.Fname = Fname;
    }

    public static String getLname() {
        return Lname;
    }

    public static void setLname(String Lname) {
        Session.Lname = Lname;
    }

    public static void setUsername(String user) {
        username = user;
        fetchFullName(); // استدعاء الدالة لجلب الاسم الكامل تلقائيًا
    }

    public static String getUsername() {
        return username;
        
    }

    public static String getCurrentChatUser() {
        return currentChatUser;
    }

    public static void setCurrentChatUser(String chatUser) {
        currentChatUser = chatUser;
    }

    private static void fetchFullName() {
        boolean found = false;
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {

          
            String userQuery = "SELECT FirstName, LastName FROM users WHERE Username = ?";
            try (PreparedStatement userStmt = connection.prepareStatement(userQuery)) {
                userStmt.setString(1, username);
                ResultSet userResult = userStmt.executeQuery();

                if (userResult.next()) {
                    Fname = userResult.getString("FirstName"); 
                    Lname = userResult.getString("LastName"); 
                    found = true; 
                }
            }

    
            if (!found) {
                String consultativeQuery = "SELECT FirstName, LastName FROM consultative WHERE Username = ?";
                try (PreparedStatement consultStmt = connection.prepareStatement(consultativeQuery)) {
                    consultStmt.setString(1, username);
                    ResultSet consultResult = consultStmt.executeQuery();

                    if (consultResult.next()) {
                        Fname = consultResult.getString("FirstName"); 
                        Lname = consultResult.getString("LastName"); 
                        found = true; 
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace(); 
        }
    }
}
