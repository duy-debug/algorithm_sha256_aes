package algorithm;

public class SHA_256 {
  // Hằng số cho SHA-256
  private static final int[] K = {
          0x428a2f98, 0x71374491, 0xb5c0fbcf, 0xe9b5dba5, 0x3956c25b, 0x59f111f1, 0x923f82a4, 0xab1c5ed5,
          0xd807aa98, 0x12835b01, 0x243185be, 0x550c7dc3, 0x72be5d74, 0x80deb1fe, 0x9bdc06a7, 0xc19bf174,
          0xe49b69c1, 0xefbe4786, 0x0fc19dc6, 0x240ca1cc, 0x2de92c6f, 0x4a7484aa, 0x5cb0a9dc, 0x76f988da,
          0x983e5152, 0xa831c66d, 0xb00327c8, 0xbf597fc7, 0xc6e00bf3, 0xd5a79147, 0x06ca6351, 0x14292967,
          0x27b70a85, 0x2e1b2138, 0x4d2c6dfc, 0x53380d13, 0x650a7354, 0x766a0abb, 0x81c2c92e, 0x92722c85,
          0xa2bfe8a1, 0xa81a664b, 0xc24b8b70, 0xc76c51a3, 0xd192e819, 0xd6990624, 0xf40e3585, 0x106aa070,
          0x19a4c116, 0x1e376c08, 0x2748774c, 0x34b0bcb5, 0x391c0cb3, 0x4ed8aa4a, 0x5b9cca4f, 0x682e6ff3,
          0x748f82ee, 0x78a5636f, 0x84c87814, 0x8cc70208, 0x90befffa, 0xa4506ceb, 0xbef9a3f7, 0xc67178f2
  };

  // Hàm tính toán hash SHA-256
  public byte[] calculateSHA256(byte[] data) {
    // Bước 1: Padding dữ liệu
    byte[] paddedData = padSHA256(data);

    // Bước 2: Khởi tạo giá trị hash ban đầu
    int[] H = {
            0x6a09e667, 0xbb67ae85, 0x3c6ef372, 0xa54ff53a,
            0x510e527f, 0x9b05688c, 0x1f83d9ab, 0x5be0cd19
    };

    // Bước 3: Xử lý từng khối 512 bits (64 bytes)
    for (int i = 0; i < paddedData.length; i += 64) {
      processBlock(paddedData, i, H);
    }

    // Bước 4: Trả về kết quả hash
    byte[] hash = new byte[32]; // 256 bits = 32 bytes
    for (int i = 0; i < 8; i++) {
      hash[i * 4] = (byte) ((H[i] >>> 24) & 0xFF);
      hash[i * 4 + 1] = (byte) ((H[i] >>> 16) & 0xFF);
      hash[i * 4 + 2] = (byte) ((H[i] >>> 8) & 0xFF);
      hash[i * 4 + 3] = (byte) (H[i] & 0xFF);
    }

    return hash;
  }

  // Hàm padding dữ liệu cho SHA-256
  private byte[] padSHA256(byte[] data) {
    // Tính toán kích thước dữ liệu sau padding
    int length = data.length;
    int padLength = 64 - ((length + 8) % 64); // Đảm bảo đủ chỗ cho 8 bytes độ dài
    if (padLength < 1) {
      padLength += 64; // Nếu không đủ chỗ, thêm một khối nữa
    }

    // Tạo mảng mới với kích thước đã padding
    byte[] paddedData = new byte[length + padLength + 8];

    // Sao chép dữ liệu gốc
    System.arraycopy(data, 0, paddedData, 0, length);

    // Thêm bit 1 đầu tiên
    paddedData[length] = (byte) 0x80;

    // Thêm độ dài ban đầu (tính bằng bits) vào cuối
    long bitLength = (long) length * 8;
    for (int i = 0; i < 8; i++) {
      paddedData[paddedData.length - 8 + i] = (byte) ((bitLength >>> (56 - i * 8)) & 0xFF);
    }

    return paddedData;
  }

  // Hàm xử lý từng khối 512 bits
  private void processBlock(byte[] data, int offset, int[] H) {
    // Bước 1: Chuẩn bị message schedule
    int[] W = new int[64];

    // Sao chép 16 words đầu tiên từ khối dữ liệu hiện tại
    for (int i = 0; i < 16; i++) {
      W[i] = ((data[offset + i * 4] & 0xFF) << 24) |
              ((data[offset + i * 4 + 1] & 0xFF) << 16) |
              ((data[offset + i * 4 + 2] & 0xFF) << 8) |
              (data[offset + i * 4 + 3] & 0xFF);
    }

    // Mở rộng 16 words ban đầu thành 64 words
    for (int i = 16; i < 64; i++) {
      int s0 = rotateRight(W[i - 15], 7) ^ rotateRight(W[i - 15], 18) ^ (W[i - 15] >>> 3);
      int s1 = rotateRight(W[i - 2], 17) ^ rotateRight(W[i - 2], 19) ^ (W[i - 2] >>> 10);
      W[i] = W[i - 16] + s0 + W[i - 7] + s1;
    }

    // Bước 2: Khởi tạo các giá trị làm việc
    int a = H[0];
    int b = H[1];
    int c = H[2];
    int d = H[3];
    int e = H[4];
    int f = H[5];
    int g = H[6];
    int h = H[7];

    // Bước 3: Lặp chính
    for (int i = 0; i < 64; i++) {
      int S1 = rotateRight(e, 6) ^ rotateRight(e, 11) ^ rotateRight(e, 25);
      int ch = (e & f) ^ ((~e) & g);
      int temp1 = h + S1 + ch + K[i] + W[i];
      int S0 = rotateRight(a, 2) ^ rotateRight(a, 13) ^ rotateRight(a, 22);
      int maj = (a & b) ^ (a & c) ^ (b & c);
      int temp2 = S0 + maj;

      h = g;
      g = f;
      f = e;
      e = d + temp1;
      d = c;
      c = b;
      b = a;
      a = temp1 + temp2;
    }

    // Bước 4: Cập nhật giá trị hash
    H[0] += a;
    H[1] += b;
    H[2] += c;
    H[3] += d;
    H[4] += e;
    H[5] += f;
    H[6] += g;
    H[7] += h;
  }

  // Hàm xoay phải (rotate right)
  private int rotateRight(int value, int shift) {
    return (value >>> shift) | (value << (32 - shift));
  }
}