package main;

import utils.FileEncryption;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class FileEncryptionDemo {
    public static void main(String[] args) {
        try {
            // Tạo một file test
            File inputFile = new File("test.txt");
            File encryptedFile = new File("test.encrypted");
            File decryptedFile = new File("test.decrypted");

            // Tạo nội dung test
            String content = "Đây là nội dung test cho việc mã hóa và giải mã file!";
            try (java.io.FileWriter writer = new java.io.FileWriter(inputFile)) {
                writer.write(content);
            }

            // Tạo khóa mã hóa (16 bytes cho AES-128)
            byte[] key = "MySecretKey12345".getBytes(StandardCharsets.UTF_8);

            // Mã hóa file
            System.out.println("Đang mã hóa file...");
            FileEncryption.encryptFile(inputFile, encryptedFile, key);
            System.out.println("Mã hóa thành công!");

            // Giải mã file
            System.out.println("Đang giải mã file...");
            FileEncryption.decryptFile(encryptedFile, decryptedFile, key);
            System.out.println("Giải mã thành công!");

            // Kiểm tra nội dung sau khi giải mã
            String decryptedContent = new String(java.nio.file.Files.readAllBytes(decryptedFile.toPath()), StandardCharsets.UTF_8);
            System.out.println("Nội dung sau khi giải mã: " + decryptedContent);

            // Xóa các file test
            inputFile.delete();
            encryptedFile.delete();
            decryptedFile.delete();

        } catch (IOException e) {
            System.err.println("Có lỗi xảy ra: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 