package com.qtec.src;

import com.qtec.src.util.HexUtils;
import org.bouncycastle.crypto.DataLengthException;
import com.qtec.gmhelper.SM4Util;
import com.qtec.src.api.Element;
import com.qtec.src.field.curve.CurveElement;

import java.math.BigInteger;
import java.util.Arrays;

/**
 * Created by mzy on 2017/4/26.
 * SM9 加密 + 解密
 */
public class SM9Engine {

    public static final String UTF_8 = "UTF-8";
    private String id;
    // K1_len为分组密码算法中密钥K1的比特长度，K2_len为函数MAC(K2, Z)中密钥K2的比特长度
    private int k1len, k2len, mlen;
    private SM9KeyGenerationCenter kgc;
    private SM9EncryptPrivateKey privatekey;
    private byte[] hid;
    // type为0，表示使用基于密钥派生函数的序列密码算法；为1，表示使用结合密钥派生函数的分组密码算法
    private int type;
    private static final int DIGEST_LENGTH = 32;    // SM3加密后长度，为256 / 8
    // 下面变量为加密过程中的kgc.pair(in1, in2)操作返回值。这里保证同一SM9Engine对象，该操作只执行一次
    private Element ENC_GT_G;

    public SM9Engine(SM9KeyGenerationCenter kgc, byte[] hid) {
        this.kgc = kgc;
        this.hid = hid;
    }

    public void initEncrypt(String id, int k1, int k2, int mlen, int type) {
        this.id = id;
        this.k1len = k1;
        this.k2len = k2;
        this.mlen = mlen;
        this.type = type;
    }

    public void initDecrypt(String id, SM9EncryptPrivateKey key, int k1, int k2, int type) {
        this.id = id;
        this.privatekey = key;
        this.k1len = k1;
        this.k2len = k2;
        this.type = type;
    }

    /**
     * 加密过程
     *
     * @param block 待加密消息M的byte数组形式
     * @return c    密文C的byte数组
     * @throws Exception
     */
    public byte[] processEncrypt(byte[] block) throws Exception {
        if (id == null) {
            throw new Exception("not initial for encrypt");
        }
        BigInteger N = kgc.getN();
        CurveElement g1 = kgc.getG1();
        CurveElement g2 = kgc.getG2();
        CurveElement ppube = kgc.getPpube();

        byte[] merge = SMCommonUtils.byteMerger(this.id.getBytes(UTF_8), this.hid);
        BigInteger h1 = Sm9Utils.h1(merge, N);
        // 计算群G1 中的元素QB=[H1(IDB||hid, N)]P1+Ppub-e
        CurveElement qb = g1.duplicate().mul(h1).add(ppube);

        CurveElement c1;
        byte[] k, k1, k2, c2;
        int k2lenDiv8 = k2len >> 3;     // k2len / 8
        do {
            // 产生随机数r，在区间[1, N-1]；
            BigInteger r = SMCommonUtils.randomInN(N);
            // 计算群G1 中的元素C1=[r]QB
            c1 = qb.duplicate().mul(r);
            // 计算群GT 中的元素g=e(Ppub-e, P2)
            if (null == ENC_GT_G){
                ENC_GT_G = kgc.pair(ppube, g2);
            }
            // 计算群GT 中的元素w=g^r
            Element w = ENC_GT_G.duplicate().pow(r);
            byte[] join = SMCommonUtils.join(c1.toBytes(), SMCommonUtils.GTFiniteElementToByte(w), this.id.getBytes(UTF_8));
            // 按加密明文的方法分类进行计算
            if (type == 0) {    // 如果加密明文的方法是基于密钥派生函数KDF的序列密码算法
                // 计算整数klen=mlen+K2_len，*8运算是得到它们的二进制长度
                int klen = mlen << 3 + k2len;
                // 计算K=KDF(C1||w||IDB, klen)
                k = Sm9Utils.KDF(join, klen);
                k1 = new byte[mlen];
                k2 = new byte[k2lenDiv8];

                System.arraycopy(k, 0, k1, 0, mlen);
                System.arraycopy(k, mlen, k2, 0, k2lenDiv8);
            } else {            // 如果加密明文的方法是结合密钥派生函数的分组密码算法
                int k1lenDiv8 = k1len >> 3;     // k1len / 8
                // 计算整数klen=K1_len+K2_len
                int klen = k1len + k2len;
                // 计算K=KDF(C1||w||IDB, klen)
                k = Sm9Utils.KDF(join, klen);
                k1 = new byte[k1lenDiv8];
                k2 = new byte[k2lenDiv8];
                System.arraycopy(k, 0, k1, 0, k1lenDiv8);
                System.arraycopy(k, k1lenDiv8, k2, 0, k2lenDiv8);
            }
        } while (testZeros(k1));

        if (type == 0) {
            // 计算C2= M⊕K1
            c2 = xor(block, k1);
        } else {
            /*
             * 计算C2=Enc(K1, M)，用密钥K1对明文m进行加密，其输出为SM4密文比特串C2。
             * Enc()：分组加密算法，Dec()：分组解密算法，需要使用国密SM4分组密码算法
             */
//            c2 = Sm9Utils.Enc(k1, block);
            c2 =  SM4Util.encrypt_ECB_Padding(k1,block);

        }
        // 计算C3=MAC(K2, C2)
        byte[] c3 = Sm9Utils.MAC(k2, c2);
        // 输出密文C=C1||C3||C2，注意拼接的顺序
        byte[] c = SMCommonUtils.join(c1.toBytes(), c3, c2);

        // 下面封装为了解密过程中识别出C1、C2、C3
//        byte[] c1x = c1.getX().toBytes();
//        byte[] c1y = c1.getY().toBytes();
//        ASN1Sequence seq;
//        ASN1EncodableVector v = new ASN1EncodableVector();
//        v.add(new ASN1Integer(c1x));
//        v.add(new ASN1Integer(c1y));
//        v.add(new DEROctetString(c3));
//        v.add(new DEROctetString(c2));
//        seq = new DERSequence(v);
//        byte[] c;
//        try {
//            c = seq.getEncoded(ASN1Encoding.DER);
//        } catch (Exception e) {
//            throw new IllegalArgumentException("encoding error in getInstance: " + e.toString());
//        }
        return c;
    }

    /**
     * 解密过程
     *
     * @param block 待解密的密文C的byte数组形式
     * @return m    解密出的明文
     * @throws Exception
     */
    public byte[] processDecrypt(byte[] block) throws Exception {
        System.out.println("block:"+ HexUtils.bytesToHexString(block));
        if ( id == null || privatekey == null) {
            throw new Exception("not initial for decrypt");
        }
        int mlen = block.length - 64 - DIGEST_LENGTH;
        byte[] c1x = new byte[32];
        byte[] c1y = new byte[32];
        byte[] c2 = new byte[mlen];
        byte[] c3 = new byte[DIGEST_LENGTH];
        System.arraycopy(block, 0, c1x, 0, 32);
        System.arraycopy(block, 32, c1y, 0, 32);
        System.arraycopy(block, 64, c3, 0, DIGEST_LENGTH);
        System.arraycopy(block, 64 + DIGEST_LENGTH, c2, 0, mlen);
        BigInteger x = new BigInteger(1, c1x);
        BigInteger y = new BigInteger(1, c1y);
        System.out.println("c2:"+ HexUtils.bytesToHexString(c2));

        // 取出C1、C2、C3的步骤，对应加密过程中对C1、C2、C3的封装
//        ASN1Sequence asn1Obj = ASN1Sequence.getInstance(block);
//        ASN1Integer c1x_encoded = ASN1Integer.getInstance(asn1Obj.getObjectAt(0));
//        ASN1Integer c1y_encoded = ASN1Integer.getInstance(asn1Obj.getObjectAt(1));
//        DEROctetString c3_encoded = (DEROctetString) asn1Obj.getObjectAt(2);
//        DEROctetString c2_encoded = (DEROctetString) asn1Obj.getObjectAt(3);
//
//        BigInteger x = c1x_encoded.getPositiveValue();
//        BigInteger y = c1y_encoded.getPositiveValue();
//        byte[] c2 = c2_encoded.getOctets();
//        byte[] c3 = c3_encoded.getOctets();

        // 从C中取出比特串C1，将C1的数据类型转换为椭圆曲线上的点，验证C1属于G1是否成立，不成立则报错并退出
        CurveElement c1;
        try {
            c1 = kgc.getCurve1().newElement();
            c1.getX().set(x);
            c1.getY().set(y);
            c1.setInfFlag(0);
        } catch (Exception e) {
            throw new Exception("c1 is invalid");
        }
        Element w = kgc.pair(c1, privatekey.getDe());
        byte[] wByte = SMCommonUtils.GTFiniteElementToByte(w);
        byte[] c1Byte = c1.toBytes();

        byte[] k, k1, k2, m;
        int k2lenDiv8 = k2len >> 3;     // k2len / 8 = 32
        // 按加密明文的方法分类进行解密
        if (type == 0) {    // 如果加密明文的方法为基于KDF的序列密码
            int c2Length = c2.length;
            // 计算整数klen=mlen+K2_len，此处mlen为C2的长度
            int klen = c2Length << 3 + k2len;
            byte[] merge = SMCommonUtils.join(c1Byte, wByte, id.getBytes(UTF_8));
            k = Sm9Utils.KDF(merge, klen);

            k1 = new byte[c2Length];
            k2 = new byte[k2lenDiv8];
            System.arraycopy(k, 0, k1, 0, c2Length);
            System.arraycopy(k, c2Length, k2, 0, k2lenDiv8);
            if (testZeros(k1)) {
                throw new Exception("k1 is zero");
            }
            m = xor(c2, k1);
        } else {    // 如果加密明文的方法为SM4分组密码算法
            int k1lenDiv8 = k1len >> 3;     // k1len / 8 = 16
            int klen = k1len + k2len;
            byte[] merge = SMCommonUtils.join(c1Byte, wByte, id.getBytes(UTF_8));
            k = Sm9Utils.KDF(merge, klen);
            System.out.println("k:"+ HexUtils.bytesToHexString(k));

            k1 = new byte[k1lenDiv8];
            k2 = new byte[k2lenDiv8];
//            k2 = new byte[k.length-k1lenDiv8];
            System.arraycopy(k, 0, k1, 0, k1lenDiv8);
            System.arraycopy(k, k1lenDiv8, k2, 0, k2lenDiv8);
            if (testZeros(k1)) {
                throw new Exception("k1 is zero");
            }
            /*
             * 计算M=Dec(K1, C2)，用密钥K1对密文C2进行解密，其输出为明文比特串m或“错误”
             * Enc()：分组加密算法，Dec()：分组解密算法，需要使用国密SM4分组密码算法
             */
//            m = Sm9Utils.Dec(k1, c2);
            m = SM4Util.decrypt_ECB_Padding(k1, c2);

        }
        // 计算u=MAC(K2, C2)
        byte[] u = Sm9Utils.MAC(k2, c2);
        // 从C中取出比特串C3，若u不等于C3，则报错并退出
        if (!Arrays.equals(u, c3)) {
            throw new Exception("mac not right");
        }
        return m;
    }

    /**
     * 判断字节数组是否全0
     *
     * @param in
     * @return
     */
    private boolean testZeros(byte[] in) {
        for (byte b : in) {
            if (b != 0)
                return false;
        }
        return true;
    }

    /**
     * ⊕：长度相等的两个比特串按比特的模2加运算
     **/
    private byte[] xor(byte[] op1, byte[] op2) {
        if (op1.length != op2.length) {
            throw new DataLengthException("op1's length is different with op2 in XOR operation");
        }
        byte[] out = new byte[op1.length];
        for (int i = 0; i < out.length; i++) {
            out[i] = (byte) ((op1[i] ^ op2[i]) & 0xff);
        }
        return out;
    }
}
