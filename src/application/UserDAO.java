package application;

import java.sql.*;

public class UserDAO {

    private static final String URL = "jdbc:mysql://localhost:3306/cms";
    private static final String USER = "root";
    private static final String PASSWORD = "12345";

    public boolean authenticateUser(String username, String password, String userType) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ? AND userType = ?";
        try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, userType);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public void registerUser(String username, String email, String password, String userType) throws SQLException {
        String sql = "INSERT INTO users (username, email, password, userType) VALUES (?, ?, ?, ?)";
        try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, email);
            pstmt.setString(3, password);  
            pstmt.setString(4, userType);
            pstmt.executeUpdate();
        }
    }
    public int getUserId(String username, String userType) {
        String query = "SELECT id FROM users WHERE username = ? AND userType = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, username);
            pstmt.setString(2, userType);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Return a default value (e.g., -1) if the user is not found
    }
}
