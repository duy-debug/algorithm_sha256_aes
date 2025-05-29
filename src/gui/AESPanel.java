package gui;

import algorithm.AES;
import utils.FileEncryption;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class AESPanel extends JPanel {
  private JTextArea inputTextArea;
  private JTextArea outputTextArea;
  private JTextField keyField;
  private JTextField ivField;
  private JButton encryptButton;
  private JButton decryptButton;
  private JButton selectFileButton;
  private JButton saveFileButton;
  private JButton generateIVButton;
  private JButton generateKeyButton;
  private JButton clearButton;
  private JButton exitButton;
  private JRadioButton textModeRadio;
  private JRadioButton fileModeRadio;
  private File selectedFile;
  private boolean isFileMode = false;
  private List<Double> encryptionTimes = new ArrayList<>();
  private List<Double> decryptionTimes = new ArrayList<>();

  // Màu sắc
  private static final Color PRIMARY_COLOR = new Color(41, 128, 185);
  private static final Color SECONDARY_COLOR = new Color(52, 152, 219);
  private static final Color BACKGROUND_COLOR = new Color(236, 240, 241);
  private static final Color TEXT_COLOR = new Color(44, 62, 80);
  private static final Color BORDER_COLOR = new Color(189, 195, 199);

  public AESPanel() {
    setLayout(new BorderLayout(15, 15));
    setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    setBackground(BACKGROUND_COLOR);

    // Tạo các thành phần
    createComponents();

    // Thêm các thành phần vào panel
    addComponentsToPanel();

    // Đăng ký các sự kiện
    registerEvents();
  }

  private void createComponents() {
    // Tạo font chữ
    Font labelFont = new Font("Segoe UI", Font.BOLD, 14);
    Font textFont = new Font("Segoe UI", Font.PLAIN, 14);
    Font buttonFont = new Font("Segoe UI", Font.BOLD, 13);

    // Tạo các text area
    inputTextArea = new JTextArea(10, 40);
    outputTextArea = new JTextArea(10, 40);
    inputTextArea.setFont(textFont);
    outputTextArea.setFont(textFont);
    inputTextArea.setLineWrap(true);
    outputTextArea.setLineWrap(true);
    outputTextArea.setEditable(false);

    // Tạo border cho text area
    Border textBorder = BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
    );
    inputTextArea.setBorder(textBorder);
    outputTextArea.setBorder(textBorder);

    // Tạo text fields
    keyField = new JTextField(32);
    ivField = new JTextField(32);
    keyField.setFont(textFont);
    ivField.setFont(textFont);
    keyField.setDocument(new javax.swing.text.PlainDocument() {
      @Override
      public void insertString(int offs, String str, javax.swing.text.AttributeSet a) throws javax.swing.text.BadLocationException {
        if (str == null) return;
        String newStr = (getLength() + str.length() <= 32) ? str : str.substring(0, 32 - getLength());
        if (newStr.matches("[0-9a-fA-F]+")) {
          super.insertString(offs, newStr, a);
        }
      }
    });
    ivField.setDocument(new javax.swing.text.PlainDocument() {
      @Override
      public void insertString(int offs, String str, javax.swing.text.AttributeSet a) throws javax.swing.text.BadLocationException {
        if (str == null) return;
        String newStr = (getLength() + str.length() <= 32) ? str : str.substring(0, 32 - getLength());
        if (newStr.matches("[0-9a-fA-F]+")) {
          super.insertString(offs, newStr, a);
        }
      }
    });

    // Tạo các nút
    encryptButton = createStyledButton("Mã hóa", buttonFont, "src/images/encoding.png");
    decryptButton = createStyledButton("Giải mã", buttonFont, "src/images/browser.png");
    selectFileButton = createStyledButton("Chọn File", buttonFont, "src/images/Open folder.png");
    saveFileButton = createStyledButton("Lưu File", buttonFont, "src/images/Save.png");
    generateIVButton = createStyledButton("Tạo IV", buttonFont, "src/images/creativity.png");
    generateKeyButton = createStyledButton("Tạo khóa", buttonFont, "src/images/key.png");
    clearButton = createStyledButton("Xóa", buttonFont, "src/images/Trash.png");
    exitButton = createStyledButton("Thoát", buttonFont, "src/images/switch.png");

    // Thêm radio buttons cho chế độ mã hóa
    textModeRadio = new JRadioButton("Mã hóa văn bản");
    fileModeRadio = new JRadioButton("Mã hóa file");
    textModeRadio.setSelected(true);
    textModeRadio.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    fileModeRadio.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    textModeRadio.setBackground(BACKGROUND_COLOR);
    fileModeRadio.setBackground(BACKGROUND_COLOR);

    ButtonGroup modeGroup = new ButtonGroup();
    modeGroup.add(textModeRadio);
    modeGroup.add(fileModeRadio);

    // Thêm sự kiện cho radio buttons
    textModeRadio.addActionListener(e -> {
      isFileMode = false;
      inputTextArea.setEditable(true);
      inputTextArea.setText("");
      selectedFile = null;
      updateButtonVisibility();
    });

    fileModeRadio.addActionListener(e -> {
      isFileMode = true;
      inputTextArea.setEditable(false);
      inputTextArea.setText("");
      selectedFile = null;
      updateButtonVisibility();
    });

    // Thiết lập tooltip
    keyField.setToolTipText("Nhập khóa (16 bytes cho AES)");
    ivField.setToolTipText("Nhập IV (16 bytes cho CBC mode)");
    clearButton.setToolTipText("Xóa nội dung đầu vào và kết quả");
  }

  private JButton createStyledButton(String text, Font font, String iconPath) {
    JButton button = new JButton(text);
    try {
      ImageIcon icon = new ImageIcon(iconPath);
      // Thay đổi kích thước icon nếu cần
      Image image = icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
      icon = new ImageIcon(image);
      button.setIcon(icon);
      // Thêm khoảng cách giữa icon và text
      button.setIconTextGap(5);
    } catch (Exception e) {
      System.out.println("Không thể tải icon: " + e.getMessage());
    }
    button.setFont(font);
    button.setForeground(Color.WHITE);
    button.setBackground(PRIMARY_COLOR);
    button.setFocusPainted(false);
    button.setBorderPainted(false);
    button.setCursor(new Cursor(Cursor.HAND_CURSOR));

    // Hiệu ứng hover
    button.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseEntered(java.awt.event.MouseEvent evt) {
        button.setBackground(SECONDARY_COLOR);
      }
      public void mouseExited(java.awt.event.MouseEvent evt) {
        button.setBackground(PRIMARY_COLOR);
      }
    });

    return button;
  }

  private void addComponentsToPanel() {
    // Panel cho input và output
    JPanel textPanel = new JPanel(new GridLayout(1, 2, 15, 0));
    textPanel.setBackground(BACKGROUND_COLOR);

    // Tạo panel cho input với tiêu đề
    JPanel inputPanel = new JPanel(new BorderLayout(5, 5));
    inputPanel.setBackground(BACKGROUND_COLOR);
    JLabel inputLabel = new JLabel("Dữ liệu đầu vào");
    inputLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
    inputLabel.setForeground(TEXT_COLOR);
    inputPanel.add(inputLabel, BorderLayout.NORTH);
    inputPanel.add(new JScrollPane(inputTextArea), BorderLayout.CENTER);

    // Tạo panel cho output với tiêu đề
    JPanel outputPanel = new JPanel(new BorderLayout(5, 5));
    outputPanel.setBackground(BACKGROUND_COLOR);
    JLabel outputLabel = new JLabel("Kết quả");
    outputLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
    outputLabel.setForeground(TEXT_COLOR);
    outputPanel.add(outputLabel, BorderLayout.NORTH);
    outputPanel.add(new JScrollPane(outputTextArea), BorderLayout.CENTER);

    textPanel.add(inputPanel);
    textPanel.add(outputPanel);

    // Panel cho khóa và IV
    JPanel keyPanel = new JPanel(new GridLayout(3, 1, 10, 10));
    keyPanel.setBackground(BACKGROUND_COLOR);

    // Panel cho khóa
    JPanel keyInputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    keyInputPanel.setBackground(BACKGROUND_COLOR);
    JLabel keyLabel = new JLabel("Khóa:");
    keyLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
    keyLabel.setForeground(TEXT_COLOR);
    keyInputPanel.add(keyLabel);
    keyInputPanel.add(keyField);
    keyInputPanel.add(generateKeyButton);

    // Panel cho IV
    JPanel ivPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    ivPanel.setBackground(BACKGROUND_COLOR);
    JLabel ivLabel = new JLabel("IV:");
    ivLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
    ivLabel.setForeground(TEXT_COLOR);
    ivPanel.add(ivLabel);
    ivPanel.add(ivField);
    ivPanel.add(generateIVButton);

    // Thêm radio buttons vào panel
    JPanel modePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    modePanel.setBackground(BACKGROUND_COLOR);
    modePanel.add(textModeRadio);
    modePanel.add(fileModeRadio);

    // Thêm modePanel vào keyPanel
    keyPanel.add(modePanel, 0);

    keyPanel.add(keyInputPanel);
    keyPanel.add(ivPanel);

    // Panel cho các nút
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
    buttonPanel.setBackground(BACKGROUND_COLOR);
    buttonPanel.add(encryptButton);
    buttonPanel.add(decryptButton);
    buttonPanel.add(selectFileButton);
    buttonPanel.add(saveFileButton);
    buttonPanel.add(clearButton);
    buttonPanel.add(exitButton);

    // Thêm các panel vào main panel
    add(keyPanel, BorderLayout.NORTH);
    add(buttonPanel, BorderLayout.SOUTH);
    add(textPanel, BorderLayout.CENTER);
  }

  private String getCurrentTime() {
    LocalDateTime now = LocalDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    return now.format(formatter);
  }

  private String formatExecutionTime(long startTime, long endTime) {
    double seconds = (endTime - startTime) / 1_000_000_000.0;
    return String.format("%.3f giây", seconds);
  }

  private String calculateAverageTime(List<Double> times) {
    if (times.isEmpty()) return "0.000 giây";
    double sum = times.stream().mapToDouble(Double::doubleValue).sum();
    return String.format("%.3f giây", sum / times.size());
  }

  private void registerEvents() {
    // Sự kiện cho nút Mã hóa
    encryptButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        try {
          String keyHex = keyField.getText();
          String ivHex = ivField.getText();
          if (keyHex.length() != 32 || !keyHex.matches("[0-9a-fA-F]{32}")) {
            JOptionPane.showMessageDialog(AESPanel.this,
                    "Khóa phải là 32 ký tự hex (16 byte)",
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return;
          }
          if (ivHex.length() != 32 || !ivHex.matches("[0-9a-fA-F]{32}")) {
            JOptionPane.showMessageDialog(AESPanel.this,
                    "IV phải là 32 ký tự hex (16 byte)",
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return;
          }

          byte[] keyBytes = hexToBytes(keyHex);
          byte[] ivBytes = hexToBytes(ivHex);

          if (isFileMode) {
            if (selectedFile == null) {
              JOptionPane.showMessageDialog(AESPanel.this,
                      "Vui lòng chọn file cần mã hóa",
                      "Cảnh báo",
                      JOptionPane.WARNING_MESSAGE);
              return;
            }

            // Mã hóa file
            File encryptedFile = new File(selectedFile.getAbsolutePath() + ".encrypted");
            long startTime = System.nanoTime();
            FileEncryption.encryptFile(selectedFile, encryptedFile, keyBytes);
            long endTime = System.nanoTime();
            
            String time = getCurrentTime();
            String executionTime = formatExecutionTime(startTime, endTime);
            JOptionPane.showMessageDialog(AESPanel.this,
                    "Đã mã hóa file thành công!\n" +
                    "File đã mã hóa: " + encryptedFile.getName() + "\n" +
                    "Thời gian: " + time + "\n" +
                    "Thời gian thực hiện: " + executionTime,
                    "Thông báo",
                    JOptionPane.INFORMATION_MESSAGE);
          } else {
            // Mã hóa văn bản
            String input = inputTextArea.getText();
            if (input.isEmpty()) {
              JOptionPane.showMessageDialog(AESPanel.this,
                      "Vui lòng nhập văn bản cần mã hóa",
                      "Cảnh báo",
                      JOptionPane.WARNING_MESSAGE);
              return;
            }
            byte[] inputBytes = input.getBytes(StandardCharsets.UTF_8);
            long startTime = System.nanoTime();
            byte[] encryptedBytes = AES.encryptAES_CBC(inputBytes, keyBytes, ivBytes);
            long endTime = System.nanoTime();
            
            String time = getCurrentTime();
            String executionTime = formatExecutionTime(startTime, endTime);
            outputTextArea.setText("Thời gian mã hóa: " + time + "\n" +
                                 "Thời gian thực hiện: " + executionTime + "\n\n" +
                                 bytesToHex(encryptedBytes));
          }
        } catch (Exception ex) {
          JOptionPane.showMessageDialog(AESPanel.this,
                  "Lỗi khi mã hóa: " + ex.getMessage(),
                  "Lỗi",
                  JOptionPane.ERROR_MESSAGE);
        }
      }
    });

    // Sự kiện cho nút Giải mã
    decryptButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        try {
          String keyHex = keyField.getText();
          String ivHex = ivField.getText();
          if (keyHex.length() != 32 || !keyHex.matches("[0-9a-fA-F]{32}")) {
            JOptionPane.showMessageDialog(AESPanel.this,
                    "Khóa phải là 32 ký tự hex (16 byte)",
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return;
          }
          if (ivHex.length() != 32 || !ivHex.matches("[0-9a-fA-F]{32}")) {
            JOptionPane.showMessageDialog(AESPanel.this,
                    "IV phải là 32 ký tự hex (16 byte)",
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return;
          }

          byte[] keyBytes = hexToBytes(keyHex);
          byte[] ivBytes = hexToBytes(ivHex);

          if (isFileMode) {
            if (selectedFile == null) {
              JOptionPane.showMessageDialog(AESPanel.this,
                      "Vui lòng chọn file cần giải mã",
                      "Cảnh báo",
                      JOptionPane.WARNING_MESSAGE);
              return;
            }

            // Giải mã file
            String decryptedPath = selectedFile.getAbsolutePath();
            if (decryptedPath.endsWith(".encrypted")) {
              decryptedPath = decryptedPath.substring(0, decryptedPath.length() - 10);
            }
            File decryptedFile = new File(decryptedPath);
            
            if (decryptedFile.exists()) {
              int response = JOptionPane.showConfirmDialog(AESPanel.this,
                      "File " + decryptedFile.getName() + " đã tồn tại. Bạn có muốn ghi đè không?",
                      "Xác nhận ghi đè",
                      JOptionPane.YES_NO_OPTION,
                      JOptionPane.QUESTION_MESSAGE);
              if (response != JOptionPane.YES_OPTION) {
                return;
              }
            }

            long startTime = System.nanoTime();
            FileEncryption.decryptFile(selectedFile, decryptedFile, keyBytes);
            long endTime = System.nanoTime();
            
            String time = getCurrentTime();
            String executionTime = formatExecutionTime(startTime, endTime);
            JOptionPane.showMessageDialog(AESPanel.this,
                    "Đã giải mã file thành công!\n" +
                    "File đã giải mã: " + decryptedFile.getName() + "\n" +
                    "Thời gian: " + time + "\n" +
                    "Thời gian thực hiện: " + executionTime,
                    "Thông báo",
                    JOptionPane.INFORMATION_MESSAGE);
          } else {
            // Giải mã văn bản
            String input = inputTextArea.getText();
            if (input.isEmpty()) {
              JOptionPane.showMessageDialog(AESPanel.this,
                      "Vui lòng nhập văn bản cần giải mã",
                      "Cảnh báo",
                      JOptionPane.WARNING_MESSAGE);
              return;
            }
            byte[] inputBytes = hexToBytes(input);
            long startTime = System.nanoTime();
            byte[] decryptedBytes = AES.decryptAES_CBC(inputBytes, keyBytes, ivBytes);
            long endTime = System.nanoTime();
            
            String time = getCurrentTime();
            String executionTime = formatExecutionTime(startTime, endTime);
            outputTextArea.setText("Thời gian giải mã: " + time + "\n" +
                                 "Thời gian thực hiện: " + executionTime + "\n\n" +
                                 new String(decryptedBytes, StandardCharsets.UTF_8));
          }
        } catch (Exception ex) {
          JOptionPane.showMessageDialog(AESPanel.this,
                  "Lỗi khi giải mã: " + ex.getMessage(),
                  "Lỗi",
                  JOptionPane.ERROR_MESSAGE);
        }
      }
    });

    // Sự kiện cho nút Chọn File
    selectFileButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(AESPanel.this);
        if (result == JFileChooser.APPROVE_OPTION) {
          selectedFile = fileChooser.getSelectedFile();
          inputTextArea.setText("Đã chọn file: " + selectedFile.getName());
        }
      }
    });

    // Sự kiện cho nút Lưu File
    saveFileButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (outputTextArea.getText().isEmpty()) {
          JOptionPane.showMessageDialog(AESPanel.this,
                  "Không có dữ liệu để lưu",
                  "Cảnh báo",
                  JOptionPane.WARNING_MESSAGE);
          return;
        }
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showSaveDialog(AESPanel.this);
        if (result == JFileChooser.APPROVE_OPTION) {
          File outputFile = fileChooser.getSelectedFile();
          try {
            if (isFileMode) {
              // Lưu file đã mã hóa/giải mã
              byte[] outputBytes = hexToBytes(outputTextArea.getText());
              writeFile(outputFile, outputBytes);
            } else {
              // Lưu văn bản đã mã hóa/giải mã dưới dạng text
              String content = outputTextArea.getText();
              try (FileWriter writer = new FileWriter(outputFile)) {
                writer.write(content);
              }
            }
            JOptionPane.showMessageDialog(AESPanel.this,
                    "Đã lưu file thành công!",
                    "Thông báo",
                    JOptionPane.INFORMATION_MESSAGE);
          } catch (IOException ex) {
            JOptionPane.showMessageDialog(AESPanel.this,
                    "Lỗi khi lưu file: " + ex.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
          }
        }
      }
    });

    // Sự kiện cho nút Tạo IV
    generateIVButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        ivField.setText(bytesToHex(iv));
      }
    });

    // Sự kiện cho nút Tạo khóa
    generateKeyButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        byte[] key = new byte[16];
        new SecureRandom().nextBytes(key);
        keyField.setText(bytesToHex(key));
      }
    });

    // Sự kiện cho nút Xóa
    clearButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        inputTextArea.setText("");
        outputTextArea.setText("");
        keyField.setText("");
        ivField.setText("");
        selectedFile = null;
        encryptionTimes.clear();
        decryptionTimes.clear();
      }
    });

    // Sự kiện cho nút Thoát
    exitButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        System.exit(0);
      }
    });
  }

  private byte[] readFile(File file) throws IOException {
    try (FileInputStream fis = new FileInputStream(file)) {
      byte[] content = new byte[(int) file.length()];
      fis.read(content);
      return content;
    }
  }

  private void writeFile(File file, byte[] data) throws IOException {
    try (FileOutputStream fos = new FileOutputStream(file)) {
      fos.write(data);
    }
  }

  private String bytesToHex(byte[] bytes) {
    StringBuilder hex = new StringBuilder();
    for (byte b : bytes) {
      hex.append(String.format("%02x", b));
    }
    return hex.toString();
  }

  private byte[] hexToBytes(String hex) {
    int len = hex.length();
    byte[] bytes = new byte[len / 2];
    for (int i = 0; i < len; i += 2) {
      bytes[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
              + Character.digit(hex.charAt(i + 1), 16));
    }
    return bytes;
  }

  private void updateButtonVisibility() {
    selectFileButton.setVisible(isFileMode);
    saveFileButton.setVisible(!isFileMode);
  }
}