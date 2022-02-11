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

    public static byte[] short2byte(short s){
        byte[] b = new byte[2];
        for(int i = 0; i < 2; i++){
            int offset = 16 - (i+1)*8; //因为byte占4个字节，所以要计算偏移量
            b[i] = (byte)((s >> offset)&0xff); //把16位分为2个8位进行分别存储
        }
        return b;
    }

    public static short byte2short(byte[] b){
        short l = 0;
        for (int i = 0; i < 2; i++) {
            l<<=8; //<<=和我们的 +=是一样的，意思就是 l = l << 8
            l |= (b[i] & 0xff); //和上面也是一样的  l = l | (b[i]&0xff)
        }
        return l;
    }
}
