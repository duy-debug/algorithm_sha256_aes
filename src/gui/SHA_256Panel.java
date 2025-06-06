package gui;

import algorithm.SHA_256;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SHA_256Panel extends JPanel {
    private JTextArea inputTextArea;
    private JTextArea outputTextArea;
    private JButton hashButton;
    private JButton selectFileButton;
    private JButton saveFileButton;
    private JButton clearButton;
    private JButton exitButton;
    private JButton compareButton;
    private JRadioButton textModeRadio;
    private JRadioButton fileModeRadio;
    private JRadioButton compareModeRadio;
    private JRadioButton verifyModeRadio;
    private File selectedFile;
    private File compareFile;
    private boolean isFileMode = false;
    private boolean isCompareMode = false;
    private boolean isVerifyMode = false;
    private Map<String, String> fileHashes = new HashMap<>(); // Lưu hash của các file

    // Màu sắc
    private static final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private static final Color SECONDARY_COLOR = new Color(52, 152, 219);
    private static final Color BACKGROUND_COLOR = new Color(236, 240, 241);
    private static final Color TEXT_COLOR = new Color(44, 62, 80);
    private static final Color BORDER_COLOR = new Color(189, 195, 199);

    public SHA_256Panel() {
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

        // Tạo các nút
        hashButton = createStyledButton("Băm", buttonFont, "src/images/encoding.png");
        selectFileButton = createStyledButton("Chọn File", buttonFont, "src/images/Open folder.png");
        saveFileButton = createStyledButton("Lưu File", buttonFont, "src/images/Save.png");
        compareButton = createStyledButton("So sánh", buttonFont, "src/images/comparison.png");
        clearButton = createStyledButton("Xóa", buttonFont, "src/images/Trash.png");
        exitButton = createStyledButton("Thoát", buttonFont, "src/images/switch.png");

        // Thêm radio buttons cho chế độ băm
        textModeRadio = new JRadioButton("Băm văn bản");
        fileModeRadio = new JRadioButton("Băm file");
        compareModeRadio = new JRadioButton("So sánh băm 2 file");
        verifyModeRadio = new JRadioButton("Kiểm tra file");
        textModeRadio.setSelected(true);
        textModeRadio.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        fileModeRadio.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        compareModeRadio.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        verifyModeRadio.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textModeRadio.setBackground(BACKGROUND_COLOR);
        fileModeRadio.setBackground(BACKGROUND_COLOR);
        compareModeRadio.setBackground(BACKGROUND_COLOR);
        verifyModeRadio.setBackground(BACKGROUND_COLOR);

        ButtonGroup modeGroup = new ButtonGroup();
        modeGroup.add(textModeRadio);
        modeGroup.add(fileModeRadio);
        modeGroup.add(compareModeRadio);
        modeGroup.add(verifyModeRadio);

        // Thêm sự kiện cho radio buttons
        textModeRadio.addActionListener(e -> {
            isFileMode = false;
            isCompareMode = false;
            isVerifyMode = false;
            inputTextArea.setEditable(true);
            inputTextArea.setText("");
            selectedFile = null;
            compareFile = null;
            updateButtonVisibility();
        });

        fileModeRadio.addActionListener(e -> {
            isFileMode = true;
            isCompareMode = false;
            isVerifyMode = false;
            inputTextArea.setEditable(false);
            inputTextArea.setText("");
            selectedFile = null;
            compareFile = null;
            updateButtonVisibility();
        });

        compareModeRadio.addActionListener(e -> {
            isFileMode = false;
            isCompareMode = true;
            isVerifyMode = false;
            inputTextArea.setEditable(false);
            inputTextArea.setText("");
            selectedFile = null;
            compareFile = null;
            updateButtonVisibility();
        });

        verifyModeRadio.addActionListener(e -> {
            isFileMode = false;
            isCompareMode = false;
            isVerifyMode = true;
            inputTextArea.setEditable(false);
            inputTextArea.setText("");
            selectedFile = null;
            compareFile = null;
            updateButtonVisibility();
        });
    }

    private JButton createStyledButton(String text, Font font, String iconPath) {
        JButton button = new JButton(text);
        try {
            ImageIcon icon = new ImageIcon(iconPath);
            Image image = icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            icon = new ImageIcon(image);
            button.setIcon(icon);
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

        // Panel cho mode selection
        JPanel modePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        modePanel.setBackground(BACKGROUND_COLOR);
        modePanel.add(textModeRadio);        modePanel.add(fileModeRadio);
        modePanel.add(compareModeRadio);
        modePanel.add(verifyModeRadio);

        // Panel cho các nút
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        buttonPanel.add(hashButton);
        buttonPanel.add(selectFileButton);
        buttonPanel.add(saveFileButton);
        buttonPanel.add(compareButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(exitButton);

        // Thêm các panel vào main panel
        add(modePanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.SOUTH);
        add(textPanel, BorderLayout.CENTER);

        // Cập nhật trạng thái ban đầu
        updateButtonVisibility();
    }

    private void registerEvents() {
        // Sự kiện cho nút Băm
        hashButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (isVerifyMode) {
                        if (selectedFile == null) {
                            JOptionPane.showMessageDialog(SHA_256Panel.this,
                                    "Vui lòng chọn file cần kiểm tra",
                                    "Cảnh báo",
                                    JOptionPane.WARNING_MESSAGE);
                            return;
                        }

                        // Tính hash hiện tại của file
                        byte[] fileBytes = readFile(selectedFile);
                        byte[] currentHashBytes = new SHA_256().calculateSHA256(fileBytes);
                        String currentHash = bytesToHex(currentHashBytes);

                        // Kiểm tra với hash đã lưu
                        String savedHash = fileHashes.get(selectedFile.getAbsolutePath());
                        if (savedHash == null) {
                            // Lưu hash mới
                            fileHashes.put(selectedFile.getAbsolutePath(), currentHash);
                            JOptionPane.showMessageDialog(SHA_256Panel.this,
                                    "Đã lưu hash cho file: " + selectedFile.getName() + "\n" +
                                    "Hash: " + currentHash,
                                    "Thông báo",
                                    JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            // So sánh với hash đã lưu
                            boolean isChanged = !savedHash.equals(currentHash);
                            String message = "Kết quả kiểm tra file: " + selectedFile.getName() + "\n\n" +
                                          "Hash đã lưu: " + savedHash + "\n" +
                                          "Hash hiện tại: " + currentHash + "\n\n" +
                                          (isChanged ? "⚠️ File đã bị thay đổi!" : "✓ File không bị thay đổi");
                            
                            JOptionPane.showMessageDialog(SHA_256Panel.this,
                                    message,
                                    isChanged ? "Cảnh báo" : "Thông báo",
                                    isChanged ? JOptionPane.WARNING_MESSAGE : JOptionPane.INFORMATION_MESSAGE);
                        }
                    } else if (isFileMode) {
                        if (selectedFile == null) {
                            JOptionPane.showMessageDialog(SHA_256Panel.this,
                                    "Vui lòng chọn file cần băm",
                                    "Cảnh báo",
                                    JOptionPane.WARNING_MESSAGE);
                            return;
                        }

                        // Băm file
                        long startTime = System.nanoTime();
                        byte[] fileBytes = readFile(selectedFile);
                        byte[] hashBytes = new SHA_256().calculateSHA256(fileBytes);
                        String hash = bytesToHex(hashBytes);
                        long endTime = System.nanoTime();
                        
                        String time = getCurrentTime();
                        String executionTime = formatExecutionTime(startTime, endTime);
                        JOptionPane.showMessageDialog(SHA_256Panel.this,
                                "Đã băm file thành công!\n" +
                                "File: " + selectedFile.getName() + "\n" +
                                "Thời gian: " + time + "\n" +
                                "Thời gian thực hiện: " + executionTime + "\n\n" +
                                "Hash: " + hash,
                                "Thông báo",
                                JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        // Băm văn bản
                        String input = inputTextArea.getText();
                        if (input.isEmpty()) {
                            JOptionPane.showMessageDialog(SHA_256Panel.this,
                                    "Vui lòng nhập văn bản cần băm",
                                    "Cảnh báo",
                                    JOptionPane.WARNING_MESSAGE);
                            return;
                        }
                        byte[] inputBytes = input.getBytes(StandardCharsets.UTF_8);
                        long startTime = System.nanoTime();
                        byte[] hashBytes = new SHA_256().calculateSHA256(inputBytes);
                        String hash = bytesToHex(hashBytes);
                        long endTime = System.nanoTime();
                        
                        String time = getCurrentTime();
                        String executionTime = formatExecutionTime(startTime, endTime);
                        outputTextArea.setText("Thời gian băm: " + time + "\n" +
                                             "Thời gian thực hiện: " + executionTime + "\n\n" +
                                             hash);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(SHA_256Panel.this,
                            "Lỗi: " + ex.getMessage(),
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
                int result = fileChooser.showOpenDialog(SHA_256Panel.this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    if (isCompareMode) {
                        if (selectedFile == null) {
                            selectedFile = fileChooser.getSelectedFile();
                            inputTextArea.setText("File 1: " + selectedFile.getName());
                        } else {
                            compareFile = fileChooser.getSelectedFile();
                            inputTextArea.setText("File 1: " + selectedFile.getName() + "\nFile 2: " + compareFile.getName());
                        }
                    } else {
                        selectedFile = fileChooser.getSelectedFile();
                        inputTextArea.setText("Đã chọn file: " + selectedFile.getName());
                    }
                }
            }
        });

        // Sự kiện cho nút Xóa
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inputTextArea.setText("");
                outputTextArea.setText("");
                selectedFile = null;
                compareFile = null;
            }
        });

        // Sự kiện cho nút Thoát
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        // Sự kiện cho nút Lưu File
        saveFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (outputTextArea.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(SHA_256Panel.this,
                            "Không có kết quả để lưu",
                            "Cảnh báo",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Text Files", "txt"));
                int result = fileChooser.showSaveDialog(SHA_256Panel.this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    if (!file.getName().toLowerCase().endsWith(".txt")) {
                        file = new File(file.getAbsolutePath() + ".txt");
                    }
                    try {
                        java.io.FileWriter writer = new java.io.FileWriter(file);
                        writer.write(outputTextArea.getText());
                        writer.close();
                        JOptionPane.showMessageDialog(SHA_256Panel.this,
                                "Đã lưu kết quả băm thành công",
                                "Thông báo",
                                JOptionPane.INFORMATION_MESSAGE);
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(SHA_256Panel.this,
                                "Lỗi khi lưu file: " + ex.getMessage(),
                                "Lỗi",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        // Sự kiện cho nút So sánh
        compareButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedFile == null || compareFile == null) {
                    JOptionPane.showMessageDialog(SHA_256Panel.this,
                            "Vui lòng chọn hai file cần so sánh",
                            "Cảnh báo",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                try {
                    // Tính hash cho file thứ nhất
                    byte[] file1Bytes = readFile(selectedFile);
                    long startTime1 = System.nanoTime();
                    byte[] hash1Bytes = new SHA_256().calculateSHA256(file1Bytes);
                    long endTime1 = System.nanoTime();
                    String hash1 = bytesToHex(hash1Bytes);
                    double time1 = (endTime1 - startTime1) / 1_000_000_000.0;

                    // Tính hash cho file thứ hai
                    byte[] file2Bytes = readFile(compareFile);
                    long startTime2 = System.nanoTime();
                    byte[] hash2Bytes = new SHA_256().calculateSHA256(file2Bytes);
                    long endTime2 = System.nanoTime();
                    String hash2 = bytesToHex(hash2Bytes);
                    double time2 = (endTime2 - startTime2) / 1_000_000_000.0;

                    // So sánh hash và tốc độ
                    boolean isEqual = hash1.equals(hash2);
                    String speedComparison;
                    double timeDiff = Math.abs(time1 - time2);
                    
                    if (timeDiff < 0.001) { // Nếu chênh lệch nhỏ hơn 1ms
                        speedComparison = "Hai file có tốc độ băm tương đương";
                    } else if (time1 < time2) {
                        speedComparison = String.format("File 1 băm nhanh hơn %.3f giây", timeDiff);
                    } else {
                        speedComparison = String.format("File 2 băm nhanh hơn %.3f giây", timeDiff);
                    }

                    String result = String.format(
                            "File 1: %s\nKích thước: %d bytes\nHash: %s\nThời gian: %.3f giây\n\n" +
                            "File 2: %s\nKích thước: %d bytes\nHash: %s\nThời gian: %.3f giây\n\n" +
                            "Kết quả so sánh hash: %s\n" +
                            "Kết quả so sánh tốc độ: %s",
                            selectedFile.getName(), file1Bytes.length, hash1, time1,
                            compareFile.getName(), file2Bytes.length, hash2, time2,
                            isEqual ? "Hai file giống nhau" : "Hai file khác nhau",
                            speedComparison
                    );

                    outputTextArea.setText(result);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(SHA_256Panel.this,
                            "Lỗi khi so sánh: " + ex.getMessage(),
                            "Lỗi",
                            JOptionPane.ERROR_MESSAGE);
                }
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

    private String getCurrentTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return now.format(formatter);
    }

    private String formatExecutionTime(long startTime, long endTime) {
        double seconds = (endTime - startTime) / 1_000_000_000.0;
        return String.format("%.3f giây", seconds);
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }

    private void updateButtonVisibility() {
        // Hiển thị/ẩn nút băm
        hashButton.setVisible(!isCompareMode);
        
        // Hiển thị/ẩn nút chọn file - hiển thị trong chế độ băm file, so sánh và kiểm tra
        selectFileButton.setVisible(isFileMode || isCompareMode || isVerifyMode);
        
        // Hiển thị/ẩn nút lưu file
        saveFileButton.setVisible(!isCompareMode && !isFileMode && !isVerifyMode);
        
        // Hiển thị/ẩn nút so sánh
        compareButton.setVisible(isCompareMode);
        
        // Hiển thị/ẩn text area
        outputTextArea.setVisible(!isFileMode && !isVerifyMode);
    }
}