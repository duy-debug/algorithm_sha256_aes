package db;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
  private static final String URL = "jdbc:sqlserver://LAPTOP-76GA8G9L\\SQLEXPRESS:1433;databaseName=FormLogin;encrypt=true;trustServerCertificate=true";
  private static final String USER = "sa";
  private static final String PASSWORD = "30102005Duy";

  public static Connection getConnection() throws SQLException {
    try {
      Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
      return DriverManager.getConnection(URL, USER, PASSWORD);
    } catch (ClassNotFoundException e) {
      throw new SQLException("SQL Server JDBC Driver not found.", e);
    }
  }

  // Phương thức test kết nối
  public static boolean testConnection() {
    try (Connection conn = getConnection()) {
      if (conn != null) {
        System.out.println("Kết nối database thành công!");
        return true;
      }
    } catch (SQLException e) {
      System.out.println("Lỗi kết nối database: " + e.getMessage());
      e.printStackTrace();
    }
    return false;
  }
}