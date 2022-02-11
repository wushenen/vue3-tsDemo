package com.unicom.quantum.component.util;

/**
 * Created by duhc on 2017/12/12.
 */
public class HexUtils {
    /**
     * 字节数组转成十六进制字符串
     *
     * @param byteArray
     * @return
     */
    public static String bytesToHexString(byte[] byteArray) {
        int length = byteArray.length;
        StringBuilder sb = new StringBuilder(length);
        for (byte b : byteArray) {
            int v = b & 0xFF;
            if (v < 16) {
                sb.append('0');
            }
            sb.append(Integer.toHexString(v));

        }
        return sb.toString().toUpperCase();
    }

    /**
     * 十六进制字符串转成字节数组
     *
     * @param hex
     * @return
     */
    public static byte[] hexStringToBytes(String hex) {
        int length = hex.length();
        byte[] data = new byte[length / 2];
        for (int i = 0; i < length - 1; i += 2) {
            data[i / 2] = (byte) (((Character.digit(hex.charAt(i), 16) << 4) + (Character.digit(hex.charAt(i + 1), 16))));
        }
        return data;
    }
}
