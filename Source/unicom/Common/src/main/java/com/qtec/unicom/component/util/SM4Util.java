package com.qtec.unicom.component.util;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * 修改SM4.java 中的填充数据
 * 该类中只包含 SM4CBC加解密，明文、密文都不是以16进制字符串处理的
 * */
public class SM4Util {

    public static final String KEY_ERROR = "key error!";
    public static final String IV_ERROR = "iv error!";



    /*FK=(FK0, FK1, FK2, FK3)为系统参数，用于密钥扩展算法sm4_setkey() */
    private static final long[] FK = {0xa3b1bac6, 0x56aa3350, 0x677d9197, 0xb27022dc};


    /* S 盒 */
    private static final byte[] SboxTable = {
            (byte) 0xd6, (byte) 0x90, (byte) 0xe9, (byte) 0xfe, (byte) 0xcc, (byte) 0xe1, 0x3d, (byte) 0xb7, 0x16, (byte) 0xb6, 0x14, (byte) 0xc2, 0x28, (byte) 0xfb, 0x2c, 0x05,
            0x2b, 0x67, (byte) 0x9a, 0x76, 0x2a, (byte) 0xbe, 0x04, (byte) 0xc3, (byte) 0xaa, 0x44, 0x13, 0x26, 0x49, (byte) 0x86, 0x06, (byte) 0x99,
            (byte) 0x9c, 0x42, 0x50, (byte) 0xf4, (byte) 0x91, (byte) 0xef, (byte) 0x98, 0x7a, 0x33, 0x54, 0x0b, 0x43, (byte) 0xed, (byte) 0xcf, (byte) 0xac, 0x62,
            (byte) 0xe4, (byte) 0xb3, 0x1c, (byte) 0xa9, (byte) 0xc9, 0x08, (byte) 0xe8, (byte) 0x95, (byte) 0x80, (byte) 0xdf, (byte) 0x94, (byte) 0xfa, 0x75, (byte) 0x8f, 0x3f, (byte) 0xa6,
            0x47, 0x07, (byte) 0xa7, (byte) 0xfc, (byte) 0xf3, 0x73, 0x17, (byte) 0xba, (byte) 0x83, 0x59, 0x3c, 0x19, (byte) 0xe6, (byte) 0x85, 0x4f, (byte) 0xa8,
            0x68, 0x6b, (byte) 0x81, (byte) 0xb2, 0x71, 0x64, (byte) 0xda, (byte) 0x8b, (byte) 0xf8, (byte) 0xeb, 0x0f, 0x4b, 0x70, 0x56, (byte) 0x9d, 0x35,
            0x1e, 0x24, 0x0e, 0x5e, 0x63, 0x58, (byte) 0xd1, (byte) 0xa2, 0x25, 0x22, 0x7c, 0x3b, 0x01, 0x21, 0x78, (byte) 0x87,
            (byte) 0xd4, 0x00, 0x46, 0x57, (byte) 0x9f, (byte) 0xd3, 0x27, 0x52, 0x4c, 0x36, 0x02, (byte) 0xe7, (byte) 0xa0, (byte) 0xc4, (byte) 0xc8, (byte) 0x9e,
            (byte) 0xea, (byte) 0xbf, (byte) 0x8a, (byte) 0xd2, 0x40, (byte) 0xc7, 0x38, (byte) 0xb5, (byte) 0xa3, (byte) 0xf7, (byte) 0xf2, (byte) 0xce, (byte) 0xf9, 0x61, 0x15, (byte) 0xa1,
            (byte) 0xe0, (byte) 0xae, 0x5d, (byte) 0xa4, (byte) 0x9b, 0x34, 0x1a, 0x55, (byte) 0xad, (byte) 0x93, 0x32, 0x30, (byte) 0xf5, (byte) 0x8c, (byte) 0xb1, (byte) 0xe3,
            0x1d, (byte) 0xf6, (byte) 0xe2, 0x2e, (byte) 0x82, 0x66, (byte) 0xca, 0x60, (byte) 0xc0, 0x29, 0x23, (byte) 0xab, 0x0d, 0x53, 0x4e, 0x6f,
            (byte) 0xd5, (byte) 0xdb, 0x37, 0x45, (byte) 0xde, (byte) 0xfd, (byte) 0x8e, 0x2f, 0x03, (byte) 0xff, 0x6a, 0x72, 0x6d, 0x6c, 0x5b, 0x51,
            (byte) 0x8d, 0x1b, (byte) 0xaf, (byte) 0x92, (byte) 0xbb, (byte) 0xdd, (byte) 0xbc, 0x7f, 0x11, (byte) 0xd9, 0x5c, 0x41, 0x1f, 0x10, 0x5a, (byte) 0xd8,
            0x0a, (byte) 0xc1, 0x31, (byte) 0x88, (byte) 0xa5, (byte) 0xcd, 0x7b, (byte) 0xbd, 0x2d, 0x74, (byte) 0xd0, 0x12, (byte) 0xb8, (byte) 0xe5, (byte) 0xb4, (byte) 0xb0,
            (byte) 0x89, 0x69, (byte) 0x97, 0x4a, 0x0c, (byte) 0x96, 0x77, 0x7e, 0x65, (byte) 0xb9, (byte) 0xf1, 0x09, (byte) 0xc5, 0x6e, (byte) 0xc6, (byte) 0x84,
            0x18, (byte) 0xf0, 0x7d, (byte) 0xec, 0x3a, (byte) 0xdc, 0x4d, 0x20, 0x79, (byte) 0xee, 0x5f, 0x3e, (byte) 0xd7, (byte) 0xcb, 0x39, 0x48};


    /* CK=(CK0, CK1,…, CK31)为固定参数，用于密钥扩展算法sm4_setkey() */
    private static final long[] CK = {
            0x00070e15, 0x1c232a31, 0x383f464d, 0x545b6269,
            0x70777e85, 0x8c939aa1, 0xa8afb6bd, 0xc4cbd2d9,
            0xe0e7eef5, 0xfc030a11, 0x181f262d, 0x343b4249,
            0x50575e65, 0x6c737a81, 0x888f969d, 0xa4abb2b9,
            0xc0c7ced5, 0xdce3eaf1, 0xf8ff060d, 0x141b2229,
            0x30373e45, 0x4c535a61, 0x686f767d, 0x848b9299,
            0xa0a7aeb5, 0xbcc3cad1, 0xd8dfe6ed, 0xf4fb0209,
            0x10171e25, 0x2c333a41, 0x484f565d, 0x646b7279};


    /**
     * SM4算法CBC模式加密，密码分组链接模式
     *
     * @param plainText 明文16进制字符串
     * @param key       秘钥16进制字符串，需要固定长度为32
     * @param iv        初始化向量
     * @return 加密后的密文字符串
     */
    public byte[] encryptDataCBC(byte[] plainText, String key, String iv) throws Exception {
        if (key == null || key.length() != 32) {
            throw new Exception(KEY_ERROR);
        }
        if (iv == null || iv.length() != 32) {
            throw new Exception(IV_ERROR);
        }
        long[] rk = new long[32];
        sm4_setkey(rk, hexString2Bytes(key));
        byte[] cipherText = sm4_crypt_cbc(rk, hexString2Bytes(iv), plainText, true);
        return cipherText;
    }



    /**
     * SM4算法CBC模式解密，密码分组链接模式
     *
     * @param cipherText 密文16进制字符串
     * @param key        秘钥16进制字符串，需要固定长度为32
     * @param iv         初始化向量
     * @return 解密后的明文字符串
     */
    public byte[] decryptDataCBC(byte[] cipherText, String key, String iv) throws Exception {
        if (key == null || key.length() != 32) {
            throw new Exception(KEY_ERROR);
        }
        if (iv == null || iv.length() != 32) {
            throw new Exception(IV_ERROR);
        }
        long[] rk = new long[32];
        sm4_setkey(rk, hexString2Bytes(key));
        for (int i = 0; i < 16; i++) {
            SWAP(rk, i);
        }
        byte[] plainText = sm4_crypt_cbc(rk, hexString2Bytes(iv), cipherText, false);
        return plainText;
    }




    /**
     * 密钥扩展算法，由加密密钥key 生成轮密钥 rk
     * 加密密钥key 长度为128 比特，表示为MK=(MK0, MK1, MK2, MK3)，其中MKi(i=0,1,2,3)为字
     * 轮密钥rk 表示为(rk0, rk1, …, rk31)，其中rki(i=0,…,31)为字
     */
    private void sm4_setkey(long[] rk, byte[] key) {
        long[] MK = new long[4];
        long[] k = new long[36];
        MK[0] = GET_ULONG_BE(key, 0);
        MK[1] = GET_ULONG_BE(key, 4);
        MK[2] = GET_ULONG_BE(key, 8);
        MK[3] = GET_ULONG_BE(key, 12);
        k[0] = xor(MK[0], FK[0]);
        k[1] = xor(MK[1], FK[1]);
        k[2] = xor(MK[2], FK[2]);
        k[3] = xor(MK[3], FK[3]);
        for (int i = 0; i < 32; i++) {
            k[(i + 4)] = xor(k[i], sm4CalciRK(xor(k[(i + 1)], k[(i + 2)], k[(i + 3)], CK[i])));
            rk[i] = k[(i + 4)];
        }
    }

    /*将长度为4的byte数组转换为long类型*/
    private long GET_ULONG_BE(byte[] b, int i) {
        return (long) (b[i] & 0xff) << 24 | (long) ((b[i + 1] & 0xff) << 16) | (long) ((b[i + 2] & 0xff) << 8) | (long) (b[i + 3] & 0xff) & 0xffffffffL;
    }


    /**
     * 多个32位bit数的异或运算
     * 由于Java中长整型进行32位的异或（^）运算可能会溢出导致负值，故将结果与0x0FFFFFFFFL进行与运算，保证运算结果正确
     *
     * @return 异或结果 32位bit 长整型
     */
    private long xor(long... params) {
        long res = 0L;
        for (int i = 0; i < params.length; i++) {
            res = res ^ params[i];
        }
        return res & 0x0FFFFFFFFL;
    }


    /**
     * CBC模式
     * @param rk        轮密钥
     * @param iv        初始化向量
     * @param input     待加密明文或待解密密文的byte数组
     * @param isEncrypt 是否是加密，true为加密，false为解密
     */
    private byte[] sm4_crypt_cbc(long[] rk, byte[] iv, byte[] input, boolean isEncrypt) throws IOException {
        /*if (isEncrypt) {    // 加密填充在加密操作前
            input = padding(input, isEncrypt);
        }*/
        int length = input.length;
        ByteArrayInputStream bins = new ByteArrayInputStream(input);
        ByteArrayOutputStream bous = new ByteArrayOutputStream();
        if (isEncrypt) {
            for (; length > 0; length -= 16) {
                byte[] in = new byte[16];
                byte[] out = new byte[16];
                byte[] out1 = new byte[16];
                bins.read(in);
                for (int i = 0; i < 16; i++) {
                    out[i] = ((byte) (xor(in[i], iv[i])));
                }
                sm4_one_round(rk, out, out1);
                System.arraycopy(out1, 0, iv, 0, 16);
                bous.write(out1);
            }
        } else {
            byte[] temp = new byte[16];
            for (; length > 0; length -= 16) {
                byte[] in = new byte[16];
                byte[] out = new byte[16];
                byte[] out1 = new byte[16];
                bins.read(in);
                System.arraycopy(in, 0, temp, 0, 16);
                sm4_one_round(rk, in, out);
                for (int i = 0; i < 16; i++) {
                    out1[i] = ((byte) (xor(out[i], iv[i])));
                }
                System.arraycopy(temp, 0, iv, 0, 16);
                bous.write(out1);
            }
        }

        byte[] output = bous.toByteArray();
        if (!isEncrypt) {   // 解密去除填充在解密操作后
            output = padding(output, isEncrypt);
        }
        bins.close();
        bous.close();
        return output;
    }

    public byte[] encryptDataCBCWithPadding(byte[] plainText, String key, String iv) throws Exception {
        if (key == null || key.length() != 32) {
            throw new Exception(KEY_ERROR);
        }
        if (iv == null || iv.length() != 32) {
            throw new Exception(IV_ERROR);
        }
        long[] rk = new long[32];
        sm4_setkey(rk, hexString2Bytes(key));
        byte[] cipherText = sm4_crypt_cbc_with_padding(rk, hexString2Bytes(iv), plainText, true);
        return cipherText;
    }
    public byte[] decryptDataCBCWithPadding(byte[] cipherText, String key, String iv) throws Exception {
        if (key == null || key.length() != 32) {
            throw new Exception(KEY_ERROR);
        }
        if (iv == null || iv.length() != 32) {
            throw new Exception(IV_ERROR);
        }
        long[] rk = new long[32];
        sm4_setkey(rk, hexString2Bytes(key));
        for (int i = 0; i < 16; i++) {
            SWAP(rk, i);
        }
        byte[] plainText = sm4_crypt_cbc_with_padding(rk, hexString2Bytes(iv), cipherText, false);
        return plainText;
    }
    private byte[] sm4_crypt_cbc_with_padding(long[] rk, byte[] iv, byte[] input, boolean isEncrypt) throws IOException {
        if (isEncrypt) {    // 加密填充在加密操作前
            input = padding(input, isEncrypt);
        }
        int length = input.length;
        ByteArrayInputStream bins = new ByteArrayInputStream(input);
        ByteArrayOutputStream bous = new ByteArrayOutputStream();
        if (isEncrypt) {
            for (; length > 0; length -= 16) {
                byte[] in = new byte[16];
                byte[] out = new byte[16];
                byte[] out1 = new byte[16];
                bins.read(in);
                for (int i = 0; i < 16; i++) {
                    out[i] = ((byte) (xor(in[i], iv[i])));
                }
                sm4_one_round(rk, out, out1);
                System.arraycopy(out1, 0, iv, 0, 16);
                bous.write(out1);
            }
        } else {
            byte[] temp = new byte[16];
            for (; length > 0; length -= 16) {
                byte[] in = new byte[16];
                byte[] out = new byte[16];
                byte[] out1 = new byte[16];
                bins.read(in);
                System.arraycopy(in, 0, temp, 0, 16);
                sm4_one_round(rk, in, out);
                for (int i = 0; i < 16; i++) {
                    out1[i] = ((byte) (xor(out[i], iv[i])));
                }
                System.arraycopy(temp, 0, iv, 0, 16);
                bous.write(out1);
            }
        }

        byte[] output = bous.toByteArray();
        if (!isEncrypt) {   // 解密去除填充在解密操作后
            output = padding(output, isEncrypt);
        }
        bins.close();
        bous.close();
        return output;
    }

    /*
     * 加密算法
     * 本算法的解密变换与加密变换结构相同，不同的仅是轮密钥rk 的使用顺序相反，所以只要提供的轮密钥rk 为SWAP()处理过的值，sm4_one_round方法便为一次解密变换了
     */
    private void sm4_one_round(long[] rk, byte[] input, byte[] output) {
        int i = 0;
        long[] X = new long[36];
        X[0] = GET_ULONG_BE(input, 0);
        X[1] = GET_ULONG_BE(input, 4);
        X[2] = GET_ULONG_BE(input, 8);
        X[3] = GET_ULONG_BE(input, 12);
        // 32次迭代运算
        while (i < 32) {
            X[(i + 4)] = sm4F(X[i], X[(i + 1)], X[(i + 2)], X[(i + 3)], rk[i]);
            i++;
        }
        // 反序变换
        PUT_ULONG_BE(X[35], output, 0);
        PUT_ULONG_BE(X[34], output, 4);
        PUT_ULONG_BE(X[33], output, 8);
        PUT_ULONG_BE(X[32], output, 12);
    }


    /*
     * 轮函数 F，本算法采用非线性迭代结构，以字为单位进行加密运算，称一次迭代运算为一轮变换
     * ( x0 , x1 , x2 , x3 )为输入
     * rk 为轮秘钥
     */
    private long sm4F(long x0, long x1, long x2, long x3, long rk) {
        return xor(x0, sm4Lt(xor(x1, x2, x3, rk)));
    }


    /* 合成置换 T */
    private long sm4Lt(long ka) {
        byte[] a = new byte[4];
        byte[] b = new byte[4];
        PUT_ULONG_BE(ka, a, 0);
        // 非线性变换τ
        b[0] = sm4Sbox(a[0]);
        b[1] = sm4Sbox(a[1]);
        b[2] = sm4Sbox(a[2]);
        b[3] = sm4Sbox(a[3]);
        long bb = GET_ULONG_BE(b, 0);
        // 线性变换L
        return xor(bb, ROTL(bb, 2), ROTL(bb, 10), ROTL(bb, 18), ROTL(bb, 24));
    }


    private void PUT_ULONG_BE(long n, byte[] b, int i) {
        b[i] = (byte) (int) (0xFF & n >> 24);
        b[i + 1] = (byte) (int) (0xFF & n >> 16);
        b[i + 2] = (byte) (int) (0xFF & n >> 8);
        b[i + 3] = (byte) (int) (0xFF & n);
    }


    private byte sm4Sbox(byte inch) {
        int i = inch & 0xFF;
        return SboxTable[i];
    }


    /* 32比特x 循环左移n 位 */
    private long ROTL(long x, int n) {
        return SHL(x, n) | x >> (32 - n);
    }

    private long SHL(long x, int n) {
        return (x & 0xFFFFFFFF) << n;
    }

    /* 交换轮密钥的顺序，由加密轮密钥得到解密轮密钥 */
    private void SWAP(long[] rk, int i) {
        long t = rk[i];
        rk[i] = rk[(31 - i)];
        rk[(31 - i)] = t;
    }

    //isEncrypt为true，加密填充；为false，解密去除填充
    private byte[] padding(byte[] input, boolean isEncrypt) {
        if (input == null) {
            return null;
        }
        byte[] ret;
        if (isEncrypt) {
            int p = 16 - input.length % 16;
            byte[] pkcsPad = pkcsPad(p);
            byte[] merger = byteMerger(input, pkcsPad);
            String inputHex = printHexString(merger);
            StringBuilder stringBuffer = new StringBuilder(inputHex);
            ret = hexString2Bytes(stringBuffer.toString());
        } else {
            String inputHex = printHexString(input);
            char c = inputHex.toLowerCase().charAt(inputHex.length() - 1);
            int padLen = Integer.valueOf(c).intValue();
            if (padLen > 47 && padLen < 58){
                padLen -= 48;
            }else if (padLen > 96 && padLen < 103){
                padLen -= 87;
            }
            if (padLen >= 0 && padLen < 16){
                if (padLen == 0)
                    padLen = 32;
                else
                    padLen +=padLen;

                if (inputHex.length() < padLen){
                    return input;
                }
                String lastString = inputHex.substring(inputHex.length() - padLen);
                for (int i = 1; i < padLen; i=i+2) {
                    if (lastString.charAt(i) != c){
                        return input;
                    }
                }
                String substring = inputHex.substring(0,inputHex.length()-padLen);
                ret = hexString2Bytes(substring);
            }else
                return input;    // 没有找到填充标识的，不需要去除填充
        }
        return ret;
    }

    //PKCS#7填充给
    private byte[] pkcsPad(int len){
        byte[] forPad = new byte[len];
        switch (len){
            case 1 :
                forPad[0] = 0x1;
                break;
            case 2 :
                forPad[0] = 0x2;
                break;
            case 3 :
                forPad[0] = 0x3;
                break;
                case 4 :
                forPad[0] = 0x4;
                break;
            case 5 :
                forPad[0] = 0x5;
                break;
            case 6 :
                forPad[0] = 0x6;
                break;
            case 7 :
                forPad[0] = 0x7;
                break;
            case 8 :
                forPad[0] = 0x8;
                break;
            case 9 :
                forPad[0] = 0x9;
                break;
            case 10 :
                forPad[0] = 0xa;
                break;
            case 11:
                forPad[0] = 0xb;
                break;
            case 12 :
                forPad[0] = 0xc;
                break;
            case 13 :
                forPad[0] = 0xd;
                break;
            case 14 :
                forPad[0] = 0xe;
                break;
            case 15 :
                forPad[0] = 0xf;
                break;
            default:
                forPad[0] = 0x10;
        }
        for (int i = 1; i < len; i++) {
            forPad[i] = forPad[0];
        }
        return forPad;
    }

    /* T '变换 */
    private long sm4CalciRK(long ka) {
        byte[] a = new byte[4];
        byte[] b = new byte[4];
        PUT_ULONG_BE(ka, a, 0);
        // 非线性变换τ
        b[0] = sm4Sbox(a[0]);
        b[1] = sm4Sbox(a[1]);
        b[2] = sm4Sbox(a[2]);
        b[3] = sm4Sbox(a[3]);
        long bb = GET_ULONG_BE(b, 0);
        // 线性变换L '
        return xor(bb, ROTL(bb, 13), ROTL(bb, 23));
    }


    /**
     * 16进制转2进制
     * @param str
     * @return
     */
    public static byte[] hexString2Bytes(String str) {
        if (str == null || str.trim().equals("")) {
            return new byte[0];
        }
        int srtLength = str.length() >> 1;
        byte[] bytes = new byte[srtLength];
        String subStr;
        for (int i = 0; i < srtLength; i++) {
            subStr = str.substring(i << 1, (i << 1) + 2);
            bytes[i] = (byte) Integer.parseInt(subStr, 16);
        }
        return bytes;
    }

    /**
     * 2进制转16进制
     * @param b
     * @return
     */
    public static String printHexString(byte[] b) {
        int bLength = b.length;
        StringBuilder buf = new StringBuilder(bLength << 1);
        String hex;
        for (int i = 0; i < bLength; i++) {
            hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            buf.append(hex);
        }
        return buf.toString();
    }


    /**
     * 拼接2个byte数组
     */
    public static byte[] byteMerger(byte[] byte_1, byte[] byte_2) {
        int byte1Length = byte_1.length;
        int byte2Length = byte_2.length;
        byte[] byte_3 = new byte[byte1Length + byte2Length];
        System.arraycopy(byte_1, 0, byte_3, 0, byte1Length);
        System.arraycopy(byte_2, 0, byte_3, byte1Length, byte2Length);
        return byte_3;
    }
}
