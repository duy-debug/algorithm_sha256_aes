package gui;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
  private AESPanel aesPanel;
  private SHA_256Panel sha256Panel;
  private JTabbedPane tabbedPane;

  public MainFrame() {
    setTitle("Ứng dụng Mã hóa và Băm");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(850, 600);
    setLocationRelativeTo(null);
    // Thiết lập icon cho ứng dụng
    try {
      // Thay đổi đường dẫn đến file icon của bạn
      ImageIcon icon = new ImageIcon("src/images/logo.png");
      // Thay đổi kích thước icon nếu cần
      Image image = icon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
      setIconImage(image);
    } catch (Exception e) {
      System.out.println("Không thể tải icon: " + e.getMessage());
    }
    // Khởi tạo các panel
    aesPanel = new AESPanel();
    sha256Panel = new SHA_256Panel();

    // Tạo tabbed pane
    tabbedPane = new JTabbedPane();
    tabbedPane.addTab("AES", aesPanel);
    tabbedPane.addTab("SHA-256", sha256Panel);

    // Thêm tabbed pane vào frame
    add(tabbedPane);
  }
}