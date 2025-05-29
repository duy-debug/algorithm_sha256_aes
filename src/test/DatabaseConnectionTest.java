package test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnectionTest {
  public static void main(String[] args) {
    // Thông tin kết nối SQL Server
    String url = "jdbc:sqlserver://LAPTOP-76GA8G9L\\SQLEXPRESS:1433;databaseName=FormLogin;encrypt=true;trustServerCertificate=true";
    String user = "sa";
    String password = "30102005Duy";

    try {
      // Kết nối tới SQL Server
      Connection conn = DriverManager.getConnection(url, user, password);
      System.out.println("✅ Kết nối thành công đến cơ sở dữ liệu!");
      conn.close();
    } catch (SQLException e) {
      System.out.println("❌ Kết nối thất bại: " + e.getMessage());
      e.printStackTrace();
    }
  }
}
