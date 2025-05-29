package main;

import db.DatabaseConnection;
import db.UserDatabase;
import gui.LoginFrame;

import javax.swing.SwingUtilities;

public class Main {
  public static void main(String[] args) {
    // Test kết nối database
    if (!DatabaseConnection.testConnection()) {
      System.out.println("Không thể kết nối đến database. Vui lòng kiểm tra lại cấu hình!");
      return;
    }

    // Khởi tạo database
    UserDatabase.createTable();

    // Sử dụng SwingUtilities để đảm bảo UI được tạo trong EDT
    SwingUtilities.invokeLater(() -> {
      // Hiện form đăng nhập trước
      LoginFrame loginFrame = new LoginFrame();
      loginFrame.setVisible(true);
      // Logic cho ứng dụng chính nếu đăng nhập thành công
    });
  }
}