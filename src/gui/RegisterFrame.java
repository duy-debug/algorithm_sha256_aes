package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import algorithm.SHA_256;
import db.UserDatabase;

class RegisterFrame extends JFrame {
  private JTextField usernameField;
  private JPasswordField passwordField;
  private JPasswordField confirmPasswordField;
  private JButton registerButton;
  private JButton cancelButton;
  private SHA_256 sha256;

  private static final Color PRIMARY_COLOR = new Color(41, 128, 185);
  private static final Color SECONDARY_COLOR = new Color(52, 152, 219);
  private static final Color BACKGROUND_COLOR = new Color(236, 240, 241);
  private static final Color TEXT_COLOR = new Color(44, 62, 80);

  public RegisterFrame() {
    sha256 = new SHA_256();
    setIconImage(new ImageIcon(getClass().getResource("/images/registration.png")).getImage());
    initializeUI();
  }

  private void initializeUI() {
    setTitle("Đăng ký");
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    setSize(400, 350);
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

// Panel chứa icon và usernameField
    JPanel usernamePanel = new JPanel(new BorderLayout(5, 0)); // khoảng cách ngang 5px
    usernamePanel.setBackground(BACKGROUND_COLOR);
    usernamePanel.add(userIconLabel, BorderLayout.WEST);

    usernameField = new JTextField(20);
    usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    usernameField.setPreferredSize(new Dimension(260, 35));  // chỉnh lại để còn chỗ icon
    usernamePanel.add(usernameField, BorderLayout.CENTER);

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

// Panel chứa icon và passwordField
    JPanel passwordPanel = new JPanel(new BorderLayout(5, 0));
    passwordPanel.setBackground(BACKGROUND_COLOR);
    passwordPanel.add(passIconLabel, BorderLayout.WEST);

    passwordField = new JPasswordField(20);
    passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    passwordField.setPreferredSize(new Dimension(260, 35));
    passwordPanel.add(passwordField, BorderLayout.CENTER);

    gbc.gridx = 0;
    gbc.gridy = 3;
    gbc.gridwidth = 2;
    formPanel.add(passwordPanel, gbc);

    // Confirm Password
    JLabel confirmPasswordLabel = new JLabel("Xác nhận mật khẩu:");
    confirmPasswordLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
    confirmPasswordLabel.setForeground(TEXT_COLOR);
    gbc.gridx = 0;
    gbc.gridy = 4;
    gbc.gridwidth = 1;
    formPanel.add(confirmPasswordLabel, gbc);

// Icon confirm password
    ImageIcon confirmPassIcon = new ImageIcon(new ImageIcon(getClass().getResource("/images/customer.png"))
            .getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH));
    JLabel confirmPassIconLabel = new JLabel(confirmPassIcon);

// Panel chứa icon và confirmPasswordField
    JPanel confirmPasswordPanel = new JPanel(new BorderLayout(5, 0));
    confirmPasswordPanel.setBackground(BACKGROUND_COLOR);
    confirmPasswordPanel.add(confirmPassIconLabel, BorderLayout.WEST);

    confirmPasswordField = new JPasswordField(20);
    confirmPasswordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    confirmPasswordField.setPreferredSize(new Dimension(260, 35));
    confirmPasswordPanel.add(confirmPasswordField, BorderLayout.CENTER);

    gbc.gridx = 0;
    gbc.gridy = 5;
    gbc.gridwidth = 2;
    formPanel.add(confirmPasswordPanel, gbc);

    // Buttons
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    buttonPanel.setBackground(BACKGROUND_COLOR);

// Tạo icon cho nút đăng ký
    ImageIcon registerIcon = new ImageIcon(new ImageIcon(getClass().getResource("/images/registration.png")).getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
    registerButton = new JButton("Đăng ký", registerIcon);
    registerButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
    registerButton.setBackground(PRIMARY_COLOR);
    registerButton.setForeground(Color.WHITE);
    registerButton.setFocusPainted(false);
    registerButton.setPreferredSize(new Dimension(140, 35));
    registerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
    registerButton.setHorizontalAlignment(SwingConstants.LEFT);
    registerButton.setIconTextGap(10);

// Tạo icon cho nút quay lại
    ImageIcon backIcon = new ImageIcon(new ImageIcon(getClass().getResource("/images/back-button.png")).getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
    cancelButton = new JButton("Quay lại", backIcon);
    cancelButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
    cancelButton.setBackground(SECONDARY_COLOR);
    cancelButton.setForeground(Color.WHITE);
    cancelButton.setFocusPainted(false);
    cancelButton.setPreferredSize(new Dimension(140, 35));
    cancelButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
    cancelButton.setHorizontalAlignment(SwingConstants.LEFT);
    cancelButton.setIconTextGap(10);

    buttonPanel.add(registerButton);
    buttonPanel.add(cancelButton);

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
    registerButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());

        // Kiểm tra thông tin nhập vào
        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
          // Tạo custom button cho thông báo lỗi
          UIManager.put("OptionPane.buttonBackground", SECONDARY_COLOR);
          UIManager.put("OptionPane.buttonForeground", Color.WHITE);
          UIManager.put("OptionPane.buttonFont", new Font("Segoe UI", Font.BOLD, 14));

          JOptionPane.showMessageDialog(RegisterFrame.this,
                  "Vui lòng nhập đầy đủ thông tin!",
                  "Lỗi",
                  JOptionPane.ERROR_MESSAGE);

          // Reset lại màu mặc định
          UIManager.put("OptionPane.buttonBackground", null);
          UIManager.put("OptionPane.buttonForeground", null);
          UIManager.put("OptionPane.buttonFont", null);
          return;
        }

        if (!password.equals(confirmPassword)) {
          // Tạo custom button cho thông báo lỗi
          UIManager.put("OptionPane.buttonBackground", SECONDARY_COLOR);
          UIManager.put("OptionPane.buttonForeground", Color.WHITE);
          UIManager.put("OptionPane.buttonFont", new Font("Segoe UI", Font.BOLD, 14));

          JOptionPane.showMessageDialog(RegisterFrame.this,
                  "Mật khẩu xác nhận không khớp!",
                  "Lỗi",
                  JOptionPane.ERROR_MESSAGE);

          // Reset lại màu mặc định
          UIManager.put("OptionPane.buttonBackground", null);
          UIManager.put("OptionPane.buttonForeground", null);
          UIManager.put("OptionPane.buttonFont", null);
          return;
        }

        if (UserDatabase.isUserExists(username)) {
          // Tạo custom button cho thông báo lỗi
          UIManager.put("OptionPane.buttonBackground", SECONDARY_COLOR);
          UIManager.put("OptionPane.buttonForeground", Color.WHITE);
          UIManager.put("OptionPane.buttonFont", new Font("Segoe UI", Font.BOLD, 14));

          JOptionPane.showMessageDialog(RegisterFrame.this,
                  "Tên đăng nhập đã tồn tại!",
                  "Lỗi",
                  JOptionPane.ERROR_MESSAGE);

          // Reset lại màu mặc định
          UIManager.put("OptionPane.buttonBackground", null);
          UIManager.put("OptionPane.buttonForeground", null);
          UIManager.put("OptionPane.buttonFont", null);
          return;
        }

        // Băm mật khẩu
        byte[] passwordBytes = password.getBytes();
        byte[] hashedPassword = sha256.calculateSHA256(passwordBytes);
        String hashedPasswordHex = bytesToHex(hashedPassword);

        // Tạo user mới và thêm vào database
        db.User newUser = new db.User(username, hashedPasswordHex);
        UserDatabase.addUser(newUser);
        // Tạo icon thành công
        ImageIcon successIcon = new ImageIcon(getClass().getResource("/images/Accept.png"));

        // Tạo custom button cho thông báo
        UIManager.put("OptionPane.buttonBackground", SECONDARY_COLOR);
        UIManager.put("OptionPane.buttonForeground", Color.WHITE);
        UIManager.put("OptionPane.buttonFont", new Font("Segoe UI", Font.BOLD, 14));

        // Hiển thị thông báo với icon tùy chỉnh
        JOptionPane.showMessageDialog(RegisterFrame.this,
                "Đăng ký thành công!",
                "Thông báo",
                JOptionPane.INFORMATION_MESSAGE,
                successIcon);

        // Reset lại màu mặc định
        UIManager.put("OptionPane.buttonBackground", null);
        UIManager.put("OptionPane.buttonForeground", null);
        UIManager.put("OptionPane.buttonFont", null);

        // Đóng form đăng ký
        dispose();
      }
    });

    cancelButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        dispose();
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