package utils;

import algorithm.AES;
import java.io.*;
import java.security.SecureRandom;

public class FileEncryption {
    private static final int IV_LENGTH = 16; // Độ dài IV cho AES-CBC

    /**
     * Mã hóa file sử dụng AES-CBC
     * @param inputFile File cần mã hóa
     * @param outputFile File đã mã hóa
     * @param key Khóa mã hóa (16, 24, hoặc 32 bytes cho AES-128, AES-192, hoặc AES-256)
     * @throws IOException Nếu có lỗi khi đọc/ghi file
     */
    public static void encryptFile(File inputFile, File outputFile, byte[] key) throws IOException {
        // Tạo IV ngẫu nhiên
        byte[] iv = generateIV();
        
        // Đọc nội dung file
        byte[] fileContent = readFile(inputFile);
        
        // Mã hóa nội dung
        byte[] encryptedContent = AES.encryptAES_CBC(fileContent, key, iv);
        
        // Ghi IV và nội dung đã mã hóa vào file
        try (FileOutputStream fos = new FileOutputStream(outputFile)) {
            // Ghi IV vào đầu file
            fos.write(iv);
            // Ghi nội dung đã mã hóa
            fos.write(encryptedContent);
        }
    }

    /**
     * Giải mã file đã được mã hóa bằng AES-CBC
     * @param inputFile File đã mã hóa
     * @param outputFile File sau khi giải mã
     * @param key Khóa giải mã
     * @throws IOException Nếu có lỗi khi đọc/ghi file
     */
    public static void decryptFile(File inputFile, File outputFile, byte[] key) throws IOException {
        // Đọc file đã mã hóa
        byte[] encryptedContent = readFile(inputFile);
        
        // Tách IV và nội dung đã mã hóa
        byte[] iv = new byte[IV_LENGTH];
        byte[] encryptedData = new byte[encryptedContent.length - IV_LENGTH];
        
        System.arraycopy(encryptedContent, 0, iv, 0, IV_LENGTH);
        System.arraycopy(encryptedContent, IV_LENGTH, encryptedData, 0, encryptedData.length);
        
        // Giải mã nội dung
        byte[] decryptedContent = AES.decryptAES_CBC(encryptedData, key, iv);
        
        // Ghi nội dung đã giải mã vào file
        try (FileOutputStream fos = new FileOutputStream(outputFile)) {
            fos.write(decryptedContent);
        }
    }

    /**
     * Đọc nội dung file vào mảng byte
     */
    private static byte[] readFile(File file) throws IOException {
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] content = new byte[(int) file.length()];
            fis.read(content);
            return content;
        }
    }

    /**
     * Tạo IV ngẫu nhiên
     */
    private static byte[] generateIV() {
        byte[] iv = new byte[IV_LENGTH];
        new SecureRandom().nextBytes(iv);
        return iv;
    }
} 