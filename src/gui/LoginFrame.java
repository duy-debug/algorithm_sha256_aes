package gui;

import algorithm.SHA_256;
import db.User;
import db.UserDatabase;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
public class LoginFrame extends JFrame {
  private JTextField usernameField;
  private JPasswordField passwordField;
  private JButton loginButton;
  private JButton registerButton;
  private SHA_256 sha256;

  private static final Color PRIMARY_COLOR = new Color(41, 128, 185);
  private static final Color SECONDARY_COLOR = new Color(52, 152, 219);
  private static final Color BACKGROUND_COLOR = new Color(236, 240, 241);
  private static final Color TEXT_COLOR = new Color(44, 62, 80);

  public LoginFrame() {
    sha256 = new SHA_256();
    initializeUI();
  }

  private void initializeUI() {
    setIconImage(new ImageIcon(getClass().getResource("/images/login.jpg")).getImage());
    setTitle("Đăng nhập");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(400, 300);
    setLocationRelativeTo(null);
    setResizable(false);

    // Tạo panel chính
    JPanel mainPanel = new JPanel();
    mainPanel.setLayout(new BorderLayout());
    mainPanel.setBackground(BACKGROUND_COLOR);

    // Tạo panel cho form
    JPanel formPanel = new JPanel();
    formPanel.setLayout(new GridBagLayout());
    formPanel.setBackground(BACKGROUND_COLOR);
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(5, 5, 5, 5);
    gbc.fill = GridBagConstraints.HORIZONTAL; // Đảm bảo điều khiển lấp đầy không gian theo chiều ngang

    // Username
    JLabel usernameLabel = new JLabel("Tên đăng nhập:");
    usernameLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
    usernameLabel.setForeground(TEXT_COLOR);
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.anchor = GridBagConstraints.WEST;
    formPanel.add(usernameLabel, gbc);


// Icon username
    ImageIcon userIcon = new ImageIcon(new ImageIcon(getClass().getResource("/images/User.png"))
            .getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH));
    JLabel userIconLabel = new JLabel(userIcon);

// Tạo panel chứa icon và trường nhập username
    JPanel usernamePanel = new JPanel(new BorderLayout(5, 0)); // khoảng cách 0 px ngang
    usernamePanel.setBackground(BACKGROUND_COLOR);
    usernamePanel.add(userIconLabel, BorderLayout.WEST);

    usernameField = new JTextField(20);
    usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    usernameField.setPreferredSize(new Dimension(260, 35));  // giảm chiều rộng để có chỗ cho icon
    usernamePanel.add(usernameField, BorderLayout.CENTER);

// Thêm vào formPanel
    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.gridwidth = 2;
    formPanel.add(usernamePanel, gbc);

    // Password
    JLabel passwordLabel = new JLabel("Mật khẩu:");
    passwordLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
    passwordLabel.setForeground(TEXT_COLOR);
    gbc.gridx = 0;
    gbc.gridy = 2;
    gbc.gridwidth = 1;
    formPanel.add(passwordLabel, gbc);

// Icon password
    ImageIcon passIcon = new ImageIcon(new ImageIcon(getClass().getResource("/images/padlock.png"))
            .getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH));
    JLabel passIconLabel = new JLabel(passIcon);

// Panel chứa icon và trường mật khẩu
    JPanel passwordPanel = new JPanel(new BorderLayout(5, 0));
    passwordPanel.setBackground(BACKGROUND_COLOR);
    passwordPanel.add(passIconLabel, BorderLayout.WEST);

    passwordField = new JPasswordField(20);
    passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    passwordField.setPreferredSize(new Dimension(260, 35));
    passwordPanel.add(passwordField, BorderLayout.CENTER);

// Thêm vào formPanel
    gbc.gridx = 0;
    gbc.gridy = 3;
    gbc.gridwidth = 2;
    formPanel.add(passwordPanel, gbc);

    // Buttons
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    buttonPanel.setBackground(BACKGROUND_COLOR);

// Icon cho nút đăng nhập
    ImageIcon loginIcon = new ImageIcon(new ImageIcon(getClass().getResource("/images/enter.png"))
            .getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
    loginButton = new JButton("Đăng nhập", loginIcon);
    loginButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
    loginButton.setBackground(PRIMARY_COLOR);
    loginButton.setForeground(Color.WHITE);
    loginButton.setFocusPainted(false);
    loginButton.setPreferredSize(new Dimension(140, 35));
    loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
    loginButton.setHorizontalAlignment(SwingConstants.LEFT);
    loginButton.setIconTextGap(10); // khoảng cách giữa icon và text

// Icon cho nút đăng ký
    ImageIcon registerIcon = new ImageIcon(new ImageIcon(getClass().getResource("/images/registration.png"))
            .getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
    registerButton = new JButton("Đăng ký", registerIcon);
    registerButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
    registerButton.setBackground(SECONDARY_COLOR);
    registerButton.setForeground(Color.WHITE);
    registerButton.setFocusPainted(false);
    registerButton.setPreferredSize(new Dimension(140, 35));
    registerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
    registerButton.setHorizontalAlignment(SwingConstants.LEFT);
    registerButton.setIconTextGap(10);

    buttonPanel.add(loginButton);
    buttonPanel.add(registerButton);

    // Thêm padding cho form panel
    JPanel wrapperPanel = new JPanel(new BorderLayout());
    wrapperPanel.setBackground(BACKGROUND_COLOR);
    wrapperPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
    wrapperPanel.add(formPanel, BorderLayout.CENTER);

    // Thêm các panel vào main panel
    mainPanel.add(wrapperPanel, BorderLayout.CENTER);
    mainPanel.add(buttonPanel, BorderLayout.SOUTH);
    // Thêm main panel vào frame
    setContentPane(mainPanel);

    // Đăng ký sự kiện
    registerEvents();
  }

  private void registerEvents() {
    loginButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
          JOptionPane.showMessageDialog(LoginFrame.this,
                  "Vui lòng nhập đầy đủ thông tin!",
                  "Lỗi",
                  JOptionPane.ERROR_MESSAGE);
          return;
        }

        // Băm mật khẩu
        byte[] passwordBytes = password.getBytes();
        byte[] hashedPassword = sha256.calculateSHA256(passwordBytes);
        String hashedPasswordHex = bytesToHex(hashedPassword);

        // Kiểm tra đăng nhập
        User user = UserDatabase.findUser(username);
          if (user != null && user.getHashedPassword().equals(hashedPasswordHex)) {
          ImageIcon successIcon = new ImageIcon(getClass().getResource("/images/Accept.png"));

          // Tạo custom button cho thông báo
          UIManager.put("OptionPane.buttonBackground", SECONDARY_COLOR);
          UIManager.put("OptionPane.buttonForeground", Color.WHITE);
          UIManager.put("OptionPane.buttonFont", new Font("Segoe UI", Font.BOLD, 14));

          // Hiển thị thông báo đăng nhập thành công
          int response = JOptionPane.showOptionDialog(
                  LoginFrame.this,
                  "Đăng nhập thành công!",
                  "Thông báo",
                  JOptionPane.DEFAULT_OPTION,
                  JOptionPane.INFORMATION_MESSAGE,
                  successIcon,
                  null,
                  null
          );

          // Reset lại màu mặc định
          UIManager.put("OptionPane.buttonBackground", null);
          UIManager.put("OptionPane.buttonForeground", null);
          UIManager.put("OptionPane.buttonFont", null);

          if (response == JOptionPane.OK_OPTION || response == 0) {
            // Hiển thị MainFrame trong Event Dispatch Thread
            SwingUtilities.invokeLater(() -> {
              MainFrame mainFrame = new MainFrame();
              mainFrame.setVisible(true);

              // Đóng LoginFrame
              dispose();
            });
          }
        } else {
          // Tạo custom button cho thông báo lỗi
          UIManager.put("OptionPane.buttonBackground", SECONDARY_COLOR);
          UIManager.put("OptionPane.buttonForeground", Color.WHITE);
          UIManager.put("OptionPane.buttonFont", new Font("Segoe UI", Font.BOLD, 14));

          JOptionPane.showMessageDialog(LoginFrame.this,
                  "Tên đăng nhập hoặc mật khẩu không đúng!",
                  "Lỗi",
                  JOptionPane.ERROR_MESSAGE);

          // Reset lại màu mặc định
          UIManager.put("OptionPane.buttonBackground", null);
          UIManager.put("OptionPane.buttonForeground", null);
          UIManager.put("OptionPane.buttonFont", null);
        }
      }
    });


    registerButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        new RegisterFrame().setVisible(true);
      }
    });
  }

  private String bytesToHex(byte[] bytes) {
    StringBuilder result = new StringBuilder();
    for (byte b : bytes) {
      result.append(String.format("%02x", b));
    }
    return result.toString();
  }
}