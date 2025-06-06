# Algorithm SHA-256 & AES Project

## Giới thiệu
Đây là một ứng dụng Java được thiết kế để thực hiện các chức năng mã hóa và bảo mật dữ liệu sử dụng các thuật toán SHA-256 và AES. Ứng dụng bao gồm giao diện người dùng đồ họa (GUI) và tích hợp với cơ sở dữ liệu SQL Server.

## Tính năng chính
- Mã hóa dữ liệu sử dụng thuật toán SHA-256
- Mã hóa và giải mã dữ liệu sử dụng thuật toán AES
- Giao diện người dùng đồ họa (GUI) thân thiện
- Kết nối và tương tác với cơ sở dữ liệu SQL Server
- Hệ thống đăng nhập bảo mật

## Cấu trúc project
```
src/
├── algorithm/    # Chứa các thuật toán mã hóa
│   ├── AES.java       # Cài đặt thuật toán AES
│   └── SHA_256.java   # Cài đặt thuật toán SHA-256
├── db/          # Xử lý kết nối và tương tác với cơ sở dữ liệu
│   ├── DatabaseConnection.java  # Quản lý kết nối database
│   ├── User.java               # Model người dùng
│   └── UserDatabase.java       # Xử lý thao tác với bảng người dùng
├── gui/         # Giao diện người dùng
│   ├── AESPanel.java       # Panel cho chức năng AES
│   ├── LoginFrame.java     # Form đăng nhập
│   ├── MainFrame.java      # Form chính
│   ├── RegisterFrame.java  # Form đăng ký
│   └── SHA_256Panel.java   # Panel cho chức năng SHA-256
├── images/      # Tài nguyên hình ảnh
├── main/        # Mã nguồn chính của ứng dụng
│   ├── FileEncryptionDemo.java  # Demo mã hóa file
│   ├── Main.java                # Điểm khởi động ứng dụng
│   └── User.java                # Model người dùng (có thể trùng với db/User.java)
├── test/        # Các test case
└── utils/       # Các tiện ích và công cụ hỗ trợ
    ├── Constants.java        # Các hằng số
    └── FileEncryption.java   # Tiện ích mã hóa file
```

## Yêu cầu hệ thống
- Java Development Kit (JDK)
- SQL Server
- SQL Server JDBC Driver (sqljdbc_12.10)

## Cài đặt
1. Clone repository này về máy local của bạn
2. Đảm bảo đã cài đặt JDK và SQL Server
3. Cấu hình kết nối cơ sở dữ liệu trong file `src/db/DatabaseConnection.java` (sửa thông tin server, database, username, password)
4. Build và chạy project:
   - Sử dụng IDE (như IntelliJ IDEA, Eclipse): Mở project và chạy class `src/main/Main.java`
   - Hoặc sử dụng Maven/Gradle (nếu có): Chạy lệnh build và run tương ứng

## Sử dụng
1. Chạy ứng dụng
2. Đăng nhập vào hệ thống
3. Sử dụng các chức năng mã hóa/giải mã thông qua giao diện người dùng

## Bảo mật
- Ứng dụng sử dụng các thuật toán mã hóa mạnh (SHA-256 và AES)
- Thông tin đăng nhập được mã hóa và bảo vệ
- Kết nối cơ sở dữ liệu được bảo mật

## Đóng góp
Mọi đóng góp đều được hoan nghênh. Vui lòng tạo issue hoặc pull request để đóng góp.

## Giấy phép
Project này được phân phối dưới giấy phép MIT.