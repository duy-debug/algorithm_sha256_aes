package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDatabase {
  // Tạo bảng users nếu chưa tồn tại
  public static void createTable() {
    String sql = "IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'users') " +
            "CREATE TABLE users (" +
            "id INT IDENTITY(1,1) PRIMARY KEY," +
            "username NVARCHAR(50) UNIQUE NOT NULL," +
            "password_hash NVARCHAR(64) NOT NULL" +
            ")";

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.execute();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public static void addUser(User user) {
    String sql = "INSERT INTO users (username, password_hash) VALUES (?, ?)";

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

      stmt.setString(1, user.getUsername());
      stmt.setString(2, user.getHashedPassword());
      stmt.executeUpdate();

    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public static User findUser(String username) {
    String sql = "SELECT * FROM users WHERE username = ?";

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

      stmt.setString(1, username);
      ResultSet rs = stmt.executeQuery();

      if (rs.next()) {
        return new User(
                rs.getString("username"),
                rs.getString("password_hash")
        );
      }

    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  public static boolean isUserExists(String username) {
    return findUser(username) != null;
  }
}