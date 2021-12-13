package com.cucc.src.sm4;

/**
 * Created by wuzh
 * SM4  加密的数据应该是16字节或者16的整数倍个字节的数据，若不够16倍数字节应该补0x00数据，最后加密出来的数据和输入数据的长度应该一致
 * 密钥长度一致，是16字节
 */

import com.cucc.src.SMCommonUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class SM4 {
    private static final String FirstPadding = "80";
    private static final String ZeroPadding = "00";

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

    /* FK=(FK0, FK1, FK2, FK3)为系统参数，用于密钥扩展算法sm4_setkey() */
    private static final long[] FK = {0xa3b1bac6, 0x56aa3350, 0x677d9197, 0xb27022dc};

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
    public static final String KEY_ERROR = "key error!";
    public static final String UTF_8 = "UTF-8";
    public static final String IV_ERROR = "iv error!";

    /**
     * SM4算法ECB模式加密，电子密码本模式
     *
     * @param plainText 明文16进制字符串
     * @param key       秘钥16进制字符串，需要固定长度为32
     * @return 加密后的密文字符串
     */
    private String encryptDataECB(String plainText, String key) throws Exception {
        if (plainText == null || plainText.length() % 2 != 0) {
            throw new Exception("plainText error!");
        }
        if (key == null || key.length() != 32) {
            throw new Exception(KEY_ERROR);
        }
        long[] rk = new long[32];
        sm4_setkey(rk, SMCommonUtils.hexString2Bytes(key));
        byte[] cipherText = sm4_crypt_ecb(rk, SMCommonUtils.hexString2Bytes(plainText), true);
        return SMCommonUtils.printHexString(cipherText);
    }
    /* 需要处理的明文不是16进制字符串时，增加第三个参数isHexString为false */
    public String encryptDataECB(String plainText, String key, boolean isHexString) throws Exception {
        if (isHexString)
            return this.encryptDataECB(plainText, key);

        if (key == null || key.length() != 32) {
            throw new Exception(KEY_ERROR);
        }
        long[] rk = new long[32];
        sm4_setkey(rk, SMCommonUtils.hexString2Bytes(key));
        byte[] cipherText = sm4_crypt_ecb(rk, plainText.getBytes(UTF_8), true);
        return SMCommonUtils.printHexString(cipherText);
    }
    //hyf
    public byte[] encryptDataECB(byte[] plainText, String key) throws Exception {
        if (key == null || key.length() != 32) {
            throw new Exception(KEY_ERROR);
        }
        long[] rk = new long[32];
        sm4_setkey(rk, SMCommonUtils.hexString2Bytes(key));
        byte[] cipherText = sm4_crypt_ecb(rk, plainText, true);
        return cipherText;
    }

    /**
     * SM4算法ECB模式解密，电子密码本模式
     *
     * @param cipherText 密文16进制字符串
     * @param key        秘钥16进制字符串，需要固定长度为32
     * @return 解密后的明文字符串
     */
    private String decryptDataECB(String cipherText, String key) throws Exception {
        if (cipherText == null || (cipherText.length() % 32 != 0)) {
            throw new Exception("cipherText error!");
        }
        if (key == null || key.length() != 32) {
            throw new Exception(KEY_ERROR);
        }
        long[] rk = new long[32];
        sm4_setkey(rk, SMCommonUtils.hexString2Bytes(key));
        /*for (int i = 0; i < 16; i++) {
            SWAP(rk, i);
        }*/
        byte[] plainText = sm4_crypt_ecb(rk, SMCommonUtils.hexString2Bytes(cipherText), false);
        return SMCommonUtils.printHexString(plainText);
    }

    /* 需要返回的密文不是16进制字符串时，增加第三个参数isHexString为false */
    public String decryptDataECB(String cipherText, String key, boolean isHexString) throws Exception {
        if (isHexString)
            return this.decryptDataECB(cipherText, key);

        if (key == null || key.length() != 32) {
            throw new Exception(KEY_ERROR);
        }
        long[] rk = new long[32];
        sm4_setkey(rk, SMCommonUtils.hexString2Bytes(key));
        for (int i = 0; i < 16; i++) {
            SWAP(rk, i);
        }
        byte[] plainText = sm4_crypt_ecb(rk, SMCommonUtils.hexString2Bytes(cipherText), false);
        return new String(plainText, UTF_8);
    }
    //hyf
    public byte[] decryptDataECB(byte[] cipherText, String key) throws Exception {
        if (key == null || key.length() != 32) {
            throw new Exception(KEY_ERROR);
        }
        long[] rk = new long[32];
        sm4_setkey(rk, SMCommonUtils.hexString2Bytes(key));
        for (int i = 0; i < 16; i++) {
            SWAP(rk, i);
        }
        byte[] plainText = sm4_crypt_ecb(rk, cipherText, false);
        return plainText;
    }

    /*
     * ECB模式
     * @param input     明文输入为(X0 , X1 , X2 , X3 )
     * @param rk        轮密钥
     * @param isEncrypt 是否为加密，true为加密，false为解密
     * @return          密文输出为(Y0 , Y1 , Y2 , Y3 )
     */
    private byte[] sm4_crypt_ecb(long[] rk, byte[] input, boolean isEncrypt) throws Exception {
        if (isEncrypt) {    // 加密填充在加密操作前
            input = padding(input, isEncrypt);
        }
        int length = input.length;
        ByteArrayInputStream bins = new ByteArrayInputStream(input);
        ByteArrayOutputStream bous = new ByteArrayOutputStream();
        for (; length > 0; length -= 16) {
            byte[] in = new byte[16];
            byte[] out = new byte[16];
            bins.read(in);
            sm4_one_round(rk, in, out);
            bous.write(out);
        }
        byte[] output = bous.toByteArray();
        if (!isEncrypt) {   // 解密去除填充在解密操作后
            output = padding(output, isEncrypt);
        }
        bins.close();
        bous.close();
        return output;
    }

    /**
     * SM4算法CBC模式加密，密码分组链接模式
     *
     * @param plainText 明文16进制字符串
     * @param key       秘钥16进制字符串，需要固定长度为32
     * @param iv        初始化向量
     * @return 加密后的密文字符串
     */
    private String encryptDataCBC(String plainText, String key, String iv) throws Exception {
        if (plainText == null || plainText.length() % 2 != 0) {
            throw new Exception("plainText error!");
        }
        if (key == null || key.length() != 32) {
            throw new Exception(KEY_ERROR);
        }
        if (iv == null || iv.length() != 32) {
            throw new Exception(IV_ERROR);
        }
        long[] rk = new long[32];
        sm4_setkey(rk, SMCommonUtils.hexString2Bytes(key));
        byte[] cipherText = sm4_crypt_cbc(rk, SMCommonUtils.hexString2Bytes(iv), SMCommonUtils.hexString2Bytes(plainText), true);
        return SMCommonUtils.printHexString(cipherText);
    }

    /* 需要处理的明文不是16进制字符串时，增加第四个参数isHexString为false */
    public String encryptDataCBC(String plainText, String key, String iv, boolean isHexString) throws Exception {
        if (isHexString)
            return this.encryptDataCBC(plainText, key, iv);

        if (key == null || key.length() != 32) {
            throw new Exception(KEY_ERROR);
        }
        if (iv == null || iv.length() != 32) {
            throw new Exception(IV_ERROR);
        }
        long[] rk = new long[32];
        sm4_setkey(rk, SMCommonUtils.hexString2Bytes(key));
        byte[] cipherText = sm4_crypt_cbc(rk, SMCommonUtils.hexString2Bytes(iv), plainText.getBytes(UTF_8), true);
        return SMCommonUtils.printHexString(cipherText);
    }

    /**
     * SM4算法CBC模式解密，密码分组链接模式
     *
     * @param cipherText 密文16进制字符串
     * @param key        秘钥16进制字符串，需要固定长度为32
     * @param iv         初始化向量
     * @return 解密后的明文字符串
     */
    private String decryptDataCBC(String cipherText, String key, String iv) throws Exception {
        if (cipherText == null || (cipherText.length() % 32 != 0)) {
            throw new Exception("cipherText error!");
        }
        if (key == null || key.length() != 32) {
            throw new Exception(KEY_ERROR);
        }
        if (iv == null || iv.length() != 32) {
            throw new Exception(IV_ERROR);
        }
        long[] rk = new long[32];
        sm4_setkey(rk, SMCommonUtils.hexString2Bytes(key));
        for (int i = 0; i < 16; i++) {
            SWAP(rk, i);
        }
        byte[] plainText = sm4_crypt_cbc(rk, SMCommonUtils.hexString2Bytes(iv), SMCommonUtils.hexString2Bytes(cipherText), false);
        return SMCommonUtils.printHexString(plainText);
    }

    /* 需要返回的密文不是16进制字符串时，增加第三个参数isHexString为false */
    public String decryptDataCBC(String cipherText, String key, String iv, boolean isHexString) throws Exception {
        if (isHexString)
            return this.decryptDataCBC(cipherText, key, iv);

        if (key == null || key.length() != 32) {
            throw new Exception(KEY_ERROR);
        }
        if (iv == null || iv.length() != 32) {
            throw new Exception(IV_ERROR);
        }
        long[] rk = new long[32];
        sm4_setkey(rk, SMCommonUtils.hexString2Bytes(key));
        for (int i = 0; i < 16; i++) {
            SWAP(rk, i);
        }
        byte[] plainText = sm4_crypt_cbc(rk, SMCommonUtils.hexString2Bytes(iv), SMCommonUtils.hexString2Bytes(cipherText), false);
        return new String(plainText, UTF_8);
    }

    /*
     * CBC模式
     * @param rk        轮密钥
     * @param iv        初始化向量
     * @param input     待加密明文或带解密密文的byte数组
     * @param isEncrypt 是否是加密，true为加密，false为解密
     */
    private byte[] sm4_crypt_cbc(long[] rk, byte[] iv, byte[] input, boolean isEncrypt) throws IOException {
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

    /* 将长度为4的byte数组转换为long类型 */
    private long GET_ULONG_BE(byte[] b, int i) {
        return (long) (b[i] & 0xff) << 24 | (long) ((b[i + 1] & 0xff) << 16) | (long) ((b[i + 2] & 0xff) << 8) | (long) (b[i + 3] & 0xff) & 0xffffffffL;
    }

    private void PUT_ULONG_BE(long n, byte[] b, int i) {
        b[i] = (byte) (int) (0xFF & n >> 24);
        b[i + 1] = (byte) (int) (0xFF & n >> 16);
        b[i + 2] = (byte) (int) (0xFF & n >> 8);
        b[i + 3] = (byte) (int) (0xFF & n);
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

    private byte sm4Sbox(byte inch) {
        int i = inch & 0xFF;
        return SboxTable[i];
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

    //isEncrypt为true，加密填充；为false，解密去除填充
    private byte[] padding(byte[] input, boolean isEncrypt) {
        if (input == null) {
            return null;
        }
        byte[] ret;
        if (isEncrypt) {
            if (input.length % 16 == 0) return input;   // 明文长度正确的，不需要填充
            //填充:hex必须是32的整数倍填充 ,填充的是80  00 00 00
            int p = 16 - input.length % 16;
            String inputHex = SMCommonUtils.printHexString(input) + FirstPadding;
            StringBuilder stringBuffer = new StringBuilder(inputHex);
            for (int i = 0; i < p - 1; i++) {
                stringBuffer.append(ZeroPadding);
            }
            ret = SMCommonUtils.hexString2Bytes(stringBuffer.toString());
        } else {
            String inputHex = SMCommonUtils.printHexString(input);
            int i = inputHex.lastIndexOf(FirstPadding);
            if (i < 0) return input;    // 没有找到填充标识的，不需要去除填充
            String substring = inputHex.substring(0, i);
            ret = SMCommonUtils.hexString2Bytes(substring);
        }
        return ret;
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

    public static void main(String[] args) throws Exception {
        String plainText = "23bb3fc4e8392e64d278ec365e87572a51c96bee852ace91c46e344e26e16b79bd8a61811a5b8f28b6f547b4ff8e88c8aad579694721a91204493edcbc690229e70b201bbf56a4d9242b4554e2e53dafdeed406fa57ccbb0dfb8b7cd28fc34788be4e7ea3aea849c075e8145486faf4cd05b403803354d88f0865901f808b0dc5ea6c4db93f0bc65a58ebe277e685183f4a5be315eef2b288faefb7460516c32851999131cebc6c214fa8f928a11ebf1b27bb2a71fd4385b77e7bdb2857164dcf80f8059b2f52935a0e021c48f41f29cb3ec576443ebb3d192258a911091b62a287a774706e20936538934d888c762f986c70f976ca44ee1447663b66ec52d7fcf7f71cccc518c367ce8888debdbf9a297aa829d0b3902df5df39b08f1198a7599ef3d625bf72187d15c79241f3ed9b178eed0ec8158fa2ee39dc6d36edd135a5282b5dd598a614eeeb864a5bdd3dc0dce0aa7c58527395740203e77f65e39afb08a39d9bb37e28d444b941a5b3ff76399c1f2fc829614d8c07a93599ff974534da08fc33a5bebef30f6d11916cd1637ed43e16c8ec8ed7448a7733d05b2909b08bd856a4eec08976bc6912e65515df90c0480a530964947ecfdcd8fe02270e1055f0ddaaeee766e79e7b804ec77984be1ae2269010ae7845f1ea96319f31f038efd9311e5b9c8f610c3bfd73cc84bfec5c63d6d92aa5b3f0dad7ea56249ca8de030f3988fd3e8bf31d34ebcaaa7f6f754369998c394558218add31866232ef5208349b2406e52b568a0d2b6a6a2d5a5890fc20275c7448d9334b91a44e0dfc766e0857ab54d91cb575b1cf04b69d9f5fc5a338f9f424c7373bf6e3dfa644a08cb26da831dcdceaf8a556c9cf2f341a0ba911ada198f8faa3259546c0ab190186330ad862f430b69e85523f3598164a9898ef24b2a479a0e29b0be7f3c6fcfd8725139490e3975e6b404b8314918f66dd8dd7d336d9c66f8188c1ff960488de1a950d0ee1934c8c506acd385a0ee06a07acdc4f6a9f4173b3273978b3f30e2b9a4639f1e9baee3e92b879c63bda0aa449a333e5d797c69c674e77780315c5bb26d4e8e6d61ab4eb5663df44515c9eb17109e9dae1d08dc09191d4d6309d9eaf5c601c7abeb98660cb879aef66670d68a6d4496cb5b8f98241222938a48eb9ab10814ba0d5ca29fe90a63b7faa1d978b3601e50c51083b60be431952d9dbc7101b41970499c6045c8655340881d6c833652d583f3dac17ff9783696c09afe57311fb65f68fe13be63ea3441ab268397150cfe0952e1be7dcb24516311196c23b1e095156508e1460084811ab1d8507d8f3d6672ce37e0ebf414f459c7396d3580af24fee6d8b064a7fd7ee9cd1ddf09355af8a92b7b9cb526163a7a5cdaa4ccf9f6c51f1f4876965fc62b44dabdeaf1449c55bb05779cdb8bec69869dd7aa4165ed4de435c439e89f9747dbcc5c10f865c3980358518745b0cccae09950a1cfe554426ddf8b938b23b08c13b6d32fed9ae3398c2ac231f9c1e25d43ffa5cc18b8778ff166f4a086879ac28b2a425ed28c2e47da023c9ddc1cae378b2f848540ef6d233894aba9e9b31a4307e53afbe926776adb46d9a22772f4b3479ef9da5aef45690b490ad4fc7225ac6089391197526177207a317c78bb526e03b05555fc593b376985a48c5a5bf5e02b46c4d5e5e7876282cc4bbaff8f5783996a7dddf948b36f37aa9fb17b3a4837bbb5412e581d40e771100d8bafc9f735e251dbd6c1c820a32b770c4e6f80c4747efcbbf4b1d1dcf195f6d1c6238a47c81742924a055d7f7c9962aede1f01a4ec9d9f44fe6da1547c10a41444fd088940f6b17da0cfcdfcde1c29b51c1d9ece21a17fac426a85c150b9c2ee1810a880ed3b8f597b03f8";
        String pass = "a123456";
        String hexPass = SMCommonUtils.printHexString(pass.getBytes());
        System.out.println("hexPass:"+hexPass);
        String key = buZero(hexPass,32);
        SM4 sm4 = new SM4();

        System.out.println("ECB模式加解密");
        String cipherText = plainText;
//                sm4.encryptDataECB(plainText, key, false);
        System.out.println("密文: " + cipherText);
        String plainText2 = sm4.decryptDataECB(cipherText, key, false);
        System.out.println("明文: " + plainText2);

        String iv = "30303030303030303030303030303030";

        System.out.println("CBC模式加解密");
        String cipherText2 = sm4.encryptDataCBC(plainText, key, iv, false);
        System.out.println("密文: " + cipherText2);
        String plainText3 = sm4.decryptDataCBC(cipherText2, key, iv, false);
        System.out.println("明文: " + plainText3);
    }
    /**
     * 补0
     * @param str
     * @param num
     * @return
     */
    public static String buZero(String str,int num){
        int strLen = str.length();
        if(strLen<32){
            for (int i = 0; i < num-strLen; i++) {
                str += "0";
            }
        }
        return str;
    }
}
