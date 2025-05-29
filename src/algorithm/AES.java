package algorithm;

import utils.Constants;

public class AES {
  // Mã hóa AES chế độ CBC
  public static byte[] encryptAES_CBC(byte[] data, byte[] key, byte[] iv) {
    // Bước 1: Tạo các khóa con
    byte[][] expandedKey = expandKey(key);

    // Bước 2: Padding dữ liệu để đạt được độ dài là bội số của 16 bytes
    byte[] paddedData = padData(data);

    // Bước 3: Mã hóa từng khối 16 bytes
    byte[] result = new byte[paddedData.length];
    byte[] previousBlock = iv.clone();

    for (int i = 0; i < paddedData.length; i += 16) {
      byte[] block = new byte[16];
      System.arraycopy(paddedData, i, block, 0, 16);

      // XOR với khối trước đó (hoặc IV nếu là khối đầu tiên)
      for (int j = 0; j < 16; j++) {
        block[j] ^= previousBlock[j];
      }

      // Mã hóa khối hiện tại
      byte[] encryptedBlock = encryptBlock(block, expandedKey);

      // Sao chép khối đã mã hóa vào kết quả
      System.arraycopy(encryptedBlock, 0, result, i, 16);

      // Lưu khối hiện tại để sử dụng cho khối tiếp theo
      previousBlock = encryptedBlock.clone();
    }

    return result;
  }

  // Giải mã AES chế độ CBC
  public static byte[] decryptAES_CBC(byte[] encryptedData, byte[] key, byte[] iv) {
    // Bước 1: Tạo các khóa con
    byte[][] expandedKey = expandKey(key);

    // Bước 2: Giải mã từng khối 16 bytes
    byte[] paddedResult = new byte[encryptedData.length];
    byte[] previousBlock = iv.clone();

    for (int i = 0; i < encryptedData.length; i += 16) {
      byte[] block = new byte[16];
      System.arraycopy(encryptedData, i, block, 0, 16);

      // Lưu lại khối hiện tại để sử dụng sau khi giải mã
      byte[] currentBlock = block.clone();

      // Giải mã khối hiện tại
      byte[] decryptedBlock = decryptBlock(block, expandedKey);

      // XOR với khối trước đó (hoặc IV nếu là khối đầu tiên)
      for (int j = 0; j < 16; j++) {
        decryptedBlock[j] ^= previousBlock[j];
      }

      // Sao chép khối đã giải mã vào kết quả
      System.arraycopy(decryptedBlock, 0, paddedResult, i, 16);

      // Lưu khối hiện tại để sử dụng cho khối tiếp theo
      previousBlock = currentBlock;
    }

    // Bước 3: Loại bỏ padding
    return removePadding(paddedResult);
  }

  // Thêm padding PKCS#7
  private static byte[] padData(byte[] data) {
    int paddingLength = 16 - (data.length % 16);
    byte[] paddedData = new byte[data.length + paddingLength];
    System.arraycopy(data, 0, paddedData, 0, data.length);
    for (int i = data.length; i < paddedData.length; i++) {
      paddedData[i] = (byte) paddingLength;
    }
    return paddedData;
  }

  // Loại bỏ padding PKCS#7
  private static byte[] removePadding(byte[] paddedData) {
    int paddingLength = paddedData[paddedData.length - 1];
    byte[] data = new byte[paddedData.length - paddingLength];
    System.arraycopy(paddedData, 0, data, 0, data.length);
    return data;
  }

  // Mã hóa một khối 16 bytes
  private static byte[] encryptBlock(byte[] block, byte[][] expandedKey) {
    // Chuyển đổi khối thành ma trận state
    byte[][] state = new byte[4][4];
    for (int i = 0; i < 4; i++) {
      for (int j = 0; j < 4; j++) {
        state[j][i] = block[i * 4 + j];
      }
    }

    // Thêm khóa vòng đầu tiên
    addRoundKey(state, expandedKey, 0);

    // Thực hiện 9 vòng chính
    for (int round = 1; round < 10; round++) {
      subBytes(state);
      shiftRows(state);
      mixColumns(state);
      addRoundKey(state, expandedKey, round);
    }

    // Vòng cuối cùng (không có mixColumns)
    subBytes(state);
    shiftRows(state);
    addRoundKey(state, expandedKey, 10);

    // Chuyển đổi ma trận state thành mảng byte
    byte[] result = new byte[16];
    for (int i = 0; i < 4; i++) {
      for (int j = 0; j < 4; j++) {
        result[i * 4 + j] = state[j][i];
      }
    }

    return result;
  }

  // Giải mã một khối 16 bytes
  private static byte[] decryptBlock(byte[] block, byte[][] expandedKey) {
    // Chuyển đổi khối thành ma trận state
    byte[][] state = new byte[4][4];
    for (int i = 0; i < 4; i++) {
      for (int j = 0; j < 4; j++) {
        state[j][i] = block[i * 4 + j];
      }
    }

    // Thêm khóa vòng cuối cùng
    addRoundKey(state, expandedKey, 10);

    // Thực hiện 9 vòng chính ngược lại
    for (int round = 9; round > 0; round--) {
      invShiftRows(state);
      invSubBytes(state);
      addRoundKey(state, expandedKey, round);
      invMixColumns(state);
    }

    // Vòng đầu tiên (không có invMixColumns)
    invShiftRows(state);
    invSubBytes(state);
    addRoundKey(state, expandedKey, 0);

    // Chuyển đổi ma trận state thành mảng byte
    byte[] result = new byte[16];
    for (int i = 0; i < 4; i++) {
      for (int j = 0; j < 4; j++) {
        result[i * 4 + j] = state[j][i];
      }
    }

    return result;
  }

  // Thay thế byte bằng S-box
  private static void subBytes(byte[][] state) {
    for (int i = 0; i < 4; i++) {
      for (int j = 0; j < 4; j++) {
        state[i][j] = Constants.SBOX[state[i][j] & 0xff];
      }
    }
  }

  // Thay thế byte bằng Inverse S-box
  private static void invSubBytes(byte[][] state) {
    for (int i = 0; i < 4; i++) {
      for (int j = 0; j < 4; j++) {
        state[i][j] = Constants.INV_SBOX[state[i][j] & 0xff];
      }
    }
  }

  // Dịch chuyển hàng
  private static void shiftRows(byte[][] state) {
    // Hàng 0 không dịch chuyển
    // Hàng 1 dịch trái 1 byte
    byte temp = state[1][0];
    state[1][0] = state[1][1];
    state[1][1] = state[1][2];
    state[1][2] = state[1][3];
    state[1][3] = temp;

    // Hàng 2 dịch trái 2 byte
    temp = state[2][0];
    state[2][0] = state[2][2];
    state[2][2] = temp;
    temp = state[2][1];
    state[2][1] = state[2][3];
    state[2][3] = temp;

    // Hàng 3 dịch trái 3 byte
    temp = state[3][3];
    state[3][3] = state[3][2];
    state[3][2] = state[3][1];
    state[3][1] = state[3][0];
    state[3][0] = temp;
  }

  // Dịch chuyển hàng ngược lại
  private static void invShiftRows(byte[][] state) {
    // Hàng 0 không dịch chuyển
    // Hàng 1 dịch phải 1 byte
    byte temp = state[1][3];
    state[1][3] = state[1][2];
    state[1][2] = state[1][1];
    state[1][1] = state[1][0];
    state[1][0] = temp;

    // Hàng 2 dịch phải 2 byte
    temp = state[2][0];
    state[2][0] = state[2][2];
    state[2][2] = temp;
    temp = state[2][1];
    state[2][1] = state[2][3];
    state[2][3] = temp;

    // Hàng 3 dịch phải 3 byte
    temp = state[3][0];
    state[3][0] = state[3][1];
    state[3][1] = state[3][2];
    state[3][2] = state[3][3];
    state[3][3] = temp;
  }

  // Nhân trong trường Galois
  private static byte gmul(byte a, byte b) {
    byte p = 0;
    byte counter;
    byte hi_bit_set;
    for (counter = 0; counter < 8; counter++) {
      if ((b & 1) != 0) {
        p ^= a;
      }
      hi_bit_set = (byte) (a & 0x80);
      a <<= 1;
      if (hi_bit_set != 0) {
        a ^= 0x1b; // x^8 + x^4 + x^3 + x + 1
      }
      b >>= 1;
    }
    return p;
  }

  // Trộn cột
  private static void mixColumns(byte[][] state) {
    for (int i = 0; i < 4; i++) {
      byte s0 = state[0][i];
      byte s1 = state[1][i];
      byte s2 = state[2][i];
      byte s3 = state[3][i];

      state[0][i] = (byte) (gmul((byte) 0x02, s0) ^ gmul((byte) 0x03, s1) ^ s2 ^ s3);
      state[1][i] = (byte) (s0 ^ gmul((byte) 0x02, s1) ^ gmul((byte) 0x03, s2) ^ s3);
      state[2][i] = (byte) (s0 ^ s1 ^ gmul((byte) 0x02, s2) ^ gmul((byte) 0x03, s3));
      state[3][i] = (byte) (gmul((byte) 0x03, s0) ^ s1 ^ s2 ^ gmul((byte) 0x02, s3));
    }
  }

  // Trộn cột ngược lại
  private static void invMixColumns(byte[][] state) {
    for (int i = 0; i < 4; i++) {
      byte s0 = state[0][i];
      byte s1 = state[1][i];
      byte s2 = state[2][i];
      byte s3 = state[3][i];

      state[0][i] = (byte) (gmul((byte) 0x0e, s0) ^ gmul((byte) 0x0b, s1) ^ gmul((byte) 0x0d, s2) ^ gmul((byte) 0x09, s3));
      state[1][i] = (byte) (gmul((byte) 0x09, s0) ^ gmul((byte) 0x0e, s1) ^ gmul((byte) 0x0b, s2) ^ gmul((byte) 0x0d, s3));
      state[2][i] = (byte) (gmul((byte) 0x0d, s0) ^ gmul((byte) 0x09, s1) ^ gmul((byte) 0x0e, s2) ^ gmul((byte) 0x0b, s3));
      state[3][i] = (byte) (gmul((byte) 0x0b, s0) ^ gmul((byte) 0x0d, s1) ^ gmul((byte) 0x09, s2) ^ gmul((byte) 0x0e, s3));
    }
  }

  // Thêm khóa vòng
  private static void addRoundKey(byte[][] state, byte[][] expandedKey, int round) {
    for (int i = 0; i < 4; i++) {
      for (int j = 0; j < 4; j++) {
        state[i][j] ^= expandedKey[round * 4 + i][j];
      }
    }
  }

  // Mở rộng khóa
  private static byte[][] expandKey(byte[] key) {
    byte[][] expandedKey = new byte[44][4];
    int keySize = key.length / 4;

    // Sao chép khóa ban đầu
    for (int i = 0; i < keySize; i++) {
      for (int j = 0; j < 4; j++) {
        expandedKey[i][j] = key[i * 4 + j];
      }
    }

    // Mở rộng khóa
    for (int i = keySize; i < 44; i++) {
      byte[] temp = expandedKey[i - 1].clone();
      if (i % keySize == 0) {
        // Rotate word
        byte tempByte = temp[0];
        temp[0] = temp[1];
        temp[1] = temp[2];
        temp[2] = temp[3];
        temp[3] = tempByte;

        // SubBytes
        for (int j = 0; j < 4; j++) {
          temp[j] = Constants.SBOX[temp[j] & 0xff];
        }

        // XOR với Rcon
        temp[0] ^= Constants.RCON[i / keySize - 1];
      }
      for (int j = 0; j < 4; j++) {
        expandedKey[i][j] = (byte) (expandedKey[i - keySize][j] ^ temp[j]);
      }
    }

    return expandedKey;
  }
}