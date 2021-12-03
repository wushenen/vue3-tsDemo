package com.qtec.src.sm2;

import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECPoint;
import com.qtec.src.SMCommonUtils;

import java.io.*;
import java.math.BigInteger;
import java.util.Arrays;

/**
 * SM2公钥加密算法实现 包括 -签名,验签 -密钥交换 -公钥加密,私钥解密
 *
 * @author Potato
 */
public class SM2 {
    public static final String UTF_8 = "UTF-8";
    private static BigInteger n;
    private static BigInteger p;
    private static BigInteger a;
    private static BigInteger b;
    private static BigInteger gx;
    private static BigInteger gy;
    private static ECDomainParameters ecc_bc_spec;
    private static int w;
    private static BigInteger _2w;                  // 2 ^ w
    private static final int DIGEST_LENGTH = 32;    // SM3加密后长度，为256 / 8

    private static ECCurve.Fp curve;
    private static ECPoint G;

    private static SM2 THIS;

    public static ECCurve.Fp getCurve() {
        return curve;
    }

    private SM2() {
        this.n = Sm2Params.n;
        this.p = Sm2Params.p;
        this.a = Sm2Params.a;
        this.b = Sm2Params.b;
        this.gx = Sm2Params.gx;
        this.gy = Sm2Params.gy;
        this.w = (int) Math.ceil(n.bitLength() * 1.0 / 2) - 1;
        this._2w = new BigInteger("2").pow(w);

        this.curve = new ECCurve.Fp(p, // q
                a, // a
                b); // b
        this.G = curve.createPoint(gx, gy);
        this.ecc_bc_spec = new ECDomainParameters(curve, G, n);
    }

    public static SM2 getInstance() {
        if (THIS == null) {
            THIS = new SM2();
        }
        return THIS;
    }

    /**
     * 生成密钥对
     *
     * @return
     */
    public SM2KeyPair generateKeyPair() {
        // 该d为生产的私钥，为一个随机数d 在区间[1,N-2]
        BigInteger d = SMCommonUtils.randomInN(n.subtract(BigInteger.ONE));
        // SM2KeyPair对象的第一个参数为公钥，第二个参数为私钥
        SM2KeyPair keyPair = new SM2KeyPair(G.multiply(d).normalize(), d);
        if (checkPublicKey(keyPair.getPublicKey())) {
            return keyPair;
        } else {
            return null;
        }
    }

    /* 给定私钥，生成秘钥对 */
    public SM2KeyPair generateKeyPair(BigInteger privateKey) {
        // SM2KeyPair对象的第一个参数为公钥，第二个参数为私钥
        SM2KeyPair keyPair = new SM2KeyPair(G.multiply(privateKey).normalize(), privateKey);
        if (checkPublicKey(keyPair.getPublicKey())) {
            return keyPair;
        } else {
            return null;
        }
    }

    /**
     * 公钥加密
     *
     * @param input     加密原文
     * @param publicKey 公钥
     * @return
     */
    public byte[] encrypt(String input, ECPoint publicKey)throws Exception {

        byte[] inputBuffer = input.getBytes(UTF_8);
        byte[] C1Buffer, t;
        ECPoint kpb;
        do {
            /* 1 产生随机数k，k属于[1, n-1] */
            BigInteger k = SMCommonUtils.randomInN(n);
//            BigInteger k = new BigInteger("4C62EEFD6ECFC2B95B92FD6C3D9575148AFA17425546D49018E5388D49DD7B4F",16);

            /* 2 计算椭圆曲线点C1 = [k]G = (x1, y1) */
            ECPoint C1 = G.multiply(k).normalize();
            C1Buffer = C1.getEncoded(false);

            /*
             * 3 计算椭圆曲线点 S = [h]Pb
             */
            BigInteger h = ecc_bc_spec.getH();
            if (h != null) {
                ECPoint S = publicKey.multiply(h);
                if (S.isInfinity())
                    throw new IllegalStateException();
            }

            /* 4 计算 [k]PB = (x2, y2) */
            kpb = publicKey.multiply(k).normalize();

            /* 5 计算 t = KDF(x2||y2, klen) */
            byte[] merger = SMCommonUtils.byteMerger(kpb.getXCoord().toBigInteger().toByteArray(), kpb.getYCoord().toBigInteger().toByteArray());
            t = Sm2Utils.KDF(merger, inputBuffer.length);
        } while (allZero(t));

        /* 6 计算C2=M⊕t */
        byte[] C2 = new byte[inputBuffer.length];
        for (int i = 0; i < inputBuffer.length; i++) {
            C2[i] = (byte) (inputBuffer[i] ^ t[i]);
        }

        /* 7 计算C3 = Hash(x2 || M || y2) */
        byte[] C3 = Sm2Utils.sm3hash(kpb.getXCoord().toBigInteger().toByteArray(), inputBuffer,
                kpb.getYCoord().toBigInteger().toByteArray());

        /* 8 输出密文 C=C1 || C3 || C2 */
        /* C2长度与明文M长度相等，C3长度为SM3加密后的固定长度 */
        byte[] encryptResult = SMCommonUtils.join(C1Buffer, C3, C2);

        return encryptResult;
    }
    /**
     * 公钥加密  16进制公钥
     *
     * @param input     加密原文
     * @param publicKeyHex 公钥
     * @return
     */
    public byte[] encryptHex(String input, String publicKeyHex) throws Exception {
        ECPoint publicKey = curve.decodePoint(SMCommonUtils.hexString2Bytes(publicKeyHex));
        return encrypt( input,  publicKey);
    }
    /**
     * 获取公钥对象
     * @param publicKeyHex 16进制公钥
     * @return
     */
    public ECPoint getPublicKey(String publicKeyHex) {
        ECPoint publicKey = curve.decodePoint(SMCommonUtils.hexString2Bytes(publicKeyHex));
        return publicKey;
    }

    /**
     * 私钥解密
     *
     * @param encryptData 密文数据字节数组
     * @param privateKey  解密私钥
     * @return
     */
    public String decrypt(byte[] encryptData, BigInteger privateKey) {

        byte[] C1Byte = new byte[65];
        System.arraycopy(encryptData, 0, C1Byte, 0, C1Byte.length);

        ECPoint C1 = curve.decodePoint(C1Byte).normalize();

        /*
         * 计算椭圆曲线点 S = [h]C1 是否为无穷点
         */
        BigInteger h = ecc_bc_spec.getH();
        if (h != null) {
            ECPoint S = C1.multiply(h);
            if (S.isInfinity())
                throw new IllegalStateException();
        }
        /* 计算[dB]C1 = (x2, y2) */
        ECPoint dBC1 = C1.multiply(privateKey).normalize();

        /* 计算t = KDF(x2 || y2, klen) */
        byte[] merger = SMCommonUtils.byteMerger(dBC1.getXCoord().toBigInteger().toByteArray(), dBC1.getYCoord().toBigInteger().toByteArray());
        int klen = encryptData.length - C1Byte.length - DIGEST_LENGTH;
        byte[] t = Sm2Utils.KDF(merger, klen);

        if (allZero(t)) {
            System.err.println("all zero");
            throw new IllegalStateException();
        }

        /* 5 计算M'=C2⊕t */
        byte[] M = new byte[klen];
        for (int i = 0; i < M.length; i++) {
            M[i] = (byte) (encryptData[C1Byte.length + DIGEST_LENGTH + i] ^ t[i]);
        }

        /* 6 计算 u = Hash(x2 || M' || y2) 判断 u == C3是否成立 */
        byte[] u = Sm2Utils.sm3hash(dBC1.getXCoord().toBigInteger().toByteArray(), M,
                dBC1.getYCoord().toBigInteger().toByteArray());

        byte[] C3 = new byte[DIGEST_LENGTH];
        System.arraycopy(encryptData, C1Byte.length, C3, 0, DIGEST_LENGTH);

        if (Arrays.equals(u, C3)) {
            try {
                return new String(M, UTF_8);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return null;
        } else {
            return null;
        }

    }
    /**
     * 私钥解密 16进制
     *
     * @param encryptData 密文数据字节数组
     * @param privateKeyHex  解密私钥
     * @return
     */
    public String decryptHex(byte[] encryptData, String privateKeyHex) {
        BigInteger bi = new BigInteger(privateKeyHex,16);
        return decrypt(encryptData, bi);
    }
    /**
     * 签名
     *
     * @param M       签名信息
     * @param IDA     签名方唯一标识
     * @param keyPair 签名方密钥对
     * @return 签名实体类
     */
    public Signature sign(String M, String IDA, SM2KeyPair keyPair) throws Exception{
        byte[] ZA = ZA(IDA, keyPair.getPublicKey());
        byte[] M_ = SMCommonUtils.join(ZA, M.getBytes(UTF_8));
        BigInteger e = new BigInteger(1, Sm2Utils.sm3hash(M_));
        BigInteger k, r, s;
        do {
            // 随机数
            k = SMCommonUtils.randomInN(n);
//            k = new BigInteger("6CB28D99385C175C94F94E934817663FC176D925DD72B727260DBAAE1FB2F96F",16);
            ECPoint p1 = G.multiply(k).normalize();
            BigInteger x1 = p1.getXCoord().toBigInteger();
            // r=(e + x1) mod n
            r = e.add(x1).mod(n);
            // s=((l + dA)^-1 * (k - r * dA)) mod n
            s = ((keyPair.getPrivateKey().add(BigInteger.ONE).modInverse(n))
                    .multiply((k.subtract(r.multiply(keyPair.getPrivateKey()))).mod(n))).mod(n);
        } while (r.equals(BigInteger.ZERO) || r.add(k).equals(n) || s.equals(BigInteger.ZERO));

        return new Signature(r, s);
    }

    /**
     * 验签
     *
     * @param M          签名信息
     * @param signature  签名
     * @param IDA        签名方唯一标识
     * @param aPublicKey 签名方公钥
     * @return true or false
     */
    public boolean verify(String M, Signature signature, String IDA, ECPoint aPublicKey) throws Exception{
        if (!between(signature.r, BigInteger.ONE, n))
            return false;
        if (!between(signature.s, BigInteger.ONE, n))
            return false;

        byte[] M_ = SMCommonUtils.join(ZA(IDA, aPublicKey), M.getBytes(UTF_8));
        BigInteger e = new BigInteger(1, Sm2Utils.sm3hash(M_));
        BigInteger t = signature.r.add(signature.s).mod(n);

        if (t.equals(BigInteger.ZERO))
            return false;

        ECPoint p1 = G.multiply(signature.s).normalize();
        ECPoint p2 = aPublicKey.multiply(t).normalize();
        BigInteger x1 = p1.add(p2).normalize().getXCoord().toBigInteger();
        BigInteger R = e.add(x1).mod(n);
        if (R.equals(signature.r))
            return true;
        return false;
    }

    /**
     * 判断字节数组是否全0
     *
     * @param buffer
     * @return
     */
    private boolean allZero(byte[] buffer) {
        for (int i = 0; i < buffer.length; i++) {
            if (buffer[i] != 0)
                return false;
        }
        return true;
    }

    /**
     * 判断是否在范围内
     *
     * @param param
     * @param min
     * @param max
     * @return
     */
    private boolean between(BigInteger param, BigInteger min, BigInteger max) {
        if (param.compareTo(min) >= 0 && param.compareTo(max) < 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断生成的公钥是否合法
     *
     * @param publicKey
     * @return
     */
    private boolean checkPublicKey(ECPoint publicKey) {
        if (!publicKey.isInfinity()) {
            BigInteger x = publicKey.getXCoord().toBigInteger();
            BigInteger y = publicKey.getYCoord().toBigInteger();

            if (between(x, BigInteger.ZERO, p) && between(y, BigInteger.ZERO, p)) {
                BigInteger xResult = x.pow(3).add(a.multiply(x)).add(b).mod(p);
                BigInteger yResult = y.pow(2).mod(p);
                if (yResult.equals(xResult) && publicKey.multiply(n).isInfinity()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 取得用户标识字节数组
     * 杂凑值ZA
     *
     * @param IDA
     * @param aPublicKey
     * @return
     */
    private static byte[] ZA(String IDA, ECPoint aPublicKey) throws Exception {
        byte[] idaBytes = IDA.getBytes(UTF_8);
        int entlenA = idaBytes.length * 8;
        byte[] ENTLA = new byte[]{(byte) (entlenA & 0xFF00), (byte) (entlenA & 0x00FF)};
        // ZA = H256(ENTLA||IDA||a||b||xG||yG||xA||yA)
        byte[] ZA = Sm2Utils.sm3hash(ENTLA, idaBytes, a.toByteArray(), b.toByteArray(), gx.toByteArray(), gy.toByteArray(),
                aPublicKey.getXCoord().toBigInteger().toByteArray(),
                aPublicKey.getYCoord().toBigInteger().toByteArray());
        return ZA;
    }

    /**
     * 导出公钥到本地
     *
     * @param publicKey
     * @param path
     */
    public void exportPublicKey(ECPoint publicKey, String path) {
        File file = new File(path);
        FileOutputStream fos = null;
        try {
            if (!file.exists())
                file.createNewFile();
            byte buffer[] = publicKey.getEncoded(false);

            System.out.println("公钥:"+ SMCommonUtils.printHexString(buffer));

            fos = new FileOutputStream(file);
            fos.write(buffer);

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 从本地导入公钥
     *
     * @param path
     * @return
     */
    public ECPoint importPublicKey(String path) {
        File file = new File(path);
        FileInputStream fis = null;
        try {
            if (!file.exists())
                return null;
            fis = new FileInputStream(file);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            byte buffer[] = new byte[16];
            int size;
            while ((size = fis.read(buffer)) != -1) {
                baos.write(buffer, 0, size);
            }
            return curve.decodePoint(baos.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 导出私钥到本地
     *
     * @param privateKey
     * @param path
     */
    public void exportPrivateKey(BigInteger privateKey, String path) {
        System.out.println("私钥:"+ SMCommonUtils.printHexString(privateKey.toByteArray()));

        File file = new File(path);
        ObjectOutputStream oos = null;
        try {
            if (!file.exists())
                file.createNewFile();
            oos = new ObjectOutputStream(new FileOutputStream(file));
            oos.writeObject(privateKey);

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 从本地导入私钥
     *
     * @param path
     * @return
     */
    public BigInteger importPrivateKey(String path) {
        File file = new File(path);
        ObjectInputStream ois = null;
        try {
            if (!file.exists())
                return null;
            ois = new ObjectInputStream(new FileInputStream(file));
            BigInteger res = (BigInteger) (ois.readObject());
            return res;
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (ois != null) {
                try {
                    ois.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 数字签名类(r, s)
     */
    public static class Signature {
        BigInteger r;
        BigInteger s;

        public Signature(BigInteger r, BigInteger s) {
            this.r = r;
            this.s = s;
        }

        public BigInteger getR() {
            return r;
        }

        public BigInteger getS() {
            return s;
        }

        public String toString() {
            return "r:" + r.toString(16) + "\ns:" + s.toString(16);
        }
    }

    /**
     * 传输实体类
     *
     * @author Potato
     */
    public static class TransportEntity implements Serializable {
        final byte[] R; //R点
        final byte[] S; //验证S
        final byte[] Z; //用户标识
        final byte[] K; //公钥

        public TransportEntity(byte[] r, byte[] s, byte[] z, ECPoint pKey) {
            R = r;
            S = s;
            Z = z;
            K = pKey.getEncoded(false);
        }
    }

    /**
     * 密钥协商辅助类
     *
     * @author Potato
     */
    public static class KeyExchange {
        private BigInteger rA;
        private ECPoint RA;
        private ECPoint V;
        private byte[] Z, key;  // key为最终协商出来的秘钥
        // 协商秘钥长度
        private int klen;
        private SM2KeyPair keyPair;

        public byte[] getKey() {
            return key;
        }

        public KeyExchange(String ID, SM2KeyPair keyPair, int klen) throws Exception{
            this.keyPair = keyPair;
            this.klen = klen;
            this.Z = ZA(ID, keyPair.getPublicKey());
        }

        /**
         * 密钥协商发起第一步
         * 密钥交换步骤A1-A3
         *
         * @return
         */
        public TransportEntity keyExchange_1() {
            // 用户A的随机数
            rA = SMCommonUtils.randomInN(n);
//            rA = new BigInteger("83A2C9C8B96E5AF70BD480B472409A9A327257F1EBB73F5B073354B248668563",16);
            RA = G.multiply(rA).normalize();
            return new TransportEntity(RA.getEncoded(false), null, Z, keyPair.getPublicKey());
        }

        /**
         * 密钥协商响应方
         * 密钥交换步骤B1-B9
         *
         * @param entity 传输实体
         * @return
         */
        public TransportEntity keyExchange_2(TransportEntity entity) {
            // 用户B的随机数
            BigInteger rB = SMCommonUtils.randomInN(n);
//             BigInteger rB = new BigInteger("33FE2194 0342161C 55619C4A 0C060293 D543C80A F19748CE 176D8347 7DE71C80".replace(" ", ""),16);
            ECPoint RB = G.multiply(rB).normalize();

            this.rA = rB;
            this.RA = RB;

            BigInteger x2 = RB.getXCoord().toBigInteger();
            x2 = _2w.add(x2.and(_2w.subtract(BigInteger.ONE)));

            BigInteger tB = keyPair.getPrivateKey().add(x2.multiply(rB)).mod(n);
            ECPoint RA = curve.decodePoint(entity.R).normalize();

            BigInteger x1 = RA.getXCoord().toBigInteger();
            x1 = _2w.add(x1.and(_2w.subtract(BigInteger.ONE)));

            ECPoint aPublicKey = curve.decodePoint(entity.K).normalize();
            ECPoint temp = aPublicKey.add(RA.multiply(x1).normalize()).normalize();
            ECPoint V = temp.multiply(ecc_bc_spec.getH().multiply(tB)).normalize();
            if (V.isInfinity())
                throw new IllegalStateException();
            this.V = V;

            byte[] xV = V.getXCoord().toBigInteger().toByteArray();
            byte[] yV = V.getYCoord().toBigInteger().toByteArray();
            byte[] KB = Sm2Utils.KDF(SMCommonUtils.join(xV, yV, entity.Z, this.Z), klen / 8);
            key = KB;
            System.out.println("协商得B密钥:\n" + SMCommonUtils.printHexString(KB));
            byte[] sB = Sm2Utils.sm3hash(new byte[]{0x02}, yV,
                    Sm2Utils.sm3hash(xV, entity.Z, this.Z, RA.getXCoord().toBigInteger().toByteArray(),
                            RA.getYCoord().toBigInteger().toByteArray(), RB.getXCoord().toBigInteger().toByteArray(),
                            RB.getYCoord().toBigInteger().toByteArray()));

            return new TransportEntity(RB.getEncoded(false), sB, this.Z, keyPair.getPublicKey());
        }

        /**
         * 密钥协商发起方第二步
         * 密钥交换步骤A4-A10
         *
         * @param entity 传输实体
         */
        public TransportEntity keyExchange_3(TransportEntity entity) {
            BigInteger x1 = RA.getXCoord().toBigInteger();
            x1 = _2w.add(x1.and(_2w.subtract(BigInteger.ONE)));

            BigInteger tA = keyPair.getPrivateKey().add(x1.multiply(rA)).mod(n);
            ECPoint RB = curve.decodePoint(entity.R).normalize();

            BigInteger x2 = RB.getXCoord().toBigInteger();
            x2 = _2w.add(x2.and(_2w.subtract(BigInteger.ONE)));

            ECPoint bPublicKey = curve.decodePoint(entity.K).normalize();
            ECPoint temp = bPublicKey.add(RB.multiply(x2).normalize()).normalize();
            ECPoint U = temp.multiply(ecc_bc_spec.getH().multiply(tA)).normalize();
            if (U.isInfinity())
                throw new IllegalStateException();
            this.V = U;

            byte[] xU = U.getXCoord().toBigInteger().toByteArray();
            byte[] yU = U.getYCoord().toBigInteger().toByteArray();
            byte[] KA = Sm2Utils.KDF(SMCommonUtils.join(xU, yU,
                    this.Z, entity.Z), klen / 8);
            key = KA;
            System.out.println("协商得A密钥:\n" + SMCommonUtils.printHexString(KA));
            byte[] s1 = Sm2Utils.sm3hash(new byte[]{0x02}, yU,
                    Sm2Utils.sm3hash(xU, this.Z, entity.Z, RA.getXCoord().toBigInteger().toByteArray(),
                            RA.getYCoord().toBigInteger().toByteArray(), RB.getXCoord().toBigInteger().toByteArray(),
                            RB.getYCoord().toBigInteger().toByteArray()));
            if (Arrays.equals(entity.S, s1)) {
                System.out.println("B->A 密钥确认成功");
            } else {
                System.out.println("B->A 密钥确认失败");
            }
            byte[] sA = Sm2Utils.sm3hash(new byte[]{0x03}, yU,
                    Sm2Utils.sm3hash(xU, this.Z, entity.Z, RA.getXCoord().toBigInteger().toByteArray(),
                            RA.getYCoord().toBigInteger().toByteArray(), RB.getXCoord().toBigInteger().toByteArray(),
                            RB.getYCoord().toBigInteger().toByteArray()));

            return new TransportEntity(RA.getEncoded(false), sA, this.Z, keyPair.getPublicKey());
        }

        /**
         * 密钥确认最后一步
         * 密钥交换步骤B10
         *
         * @param entity 传输实体
         */
        public void keyExchange_4(TransportEntity entity) {
            byte[] xV = V.getXCoord().toBigInteger().toByteArray();
            byte[] yV = V.getYCoord().toBigInteger().toByteArray();
            ECPoint RA = curve.decodePoint(entity.R).normalize();
            byte[] s2 = Sm2Utils.sm3hash(new byte[]{0x03}, yV,
                    Sm2Utils.sm3hash(xV, entity.Z, this.Z, RA.getXCoord().toBigInteger().toByteArray(),
                            RA.getYCoord().toBigInteger().toByteArray(), this.RA.getXCoord().toBigInteger().toByteArray(),
                            this.RA.getYCoord().toBigInteger().toByteArray()));
            if (Arrays.equals(entity.S, s2)) {
                System.out.println("A->B 密钥确认成功");
            } else {
                System.out.println("A->B 密钥确认失败");
            }
        }
    }

    public static void main(String[] args)throws Exception {
        SM2 sm02 = getInstance();
        SM2KeyPair keyPair = sm02.generateKeyPair();
        ECPoint publicKey = keyPair.getPublicKey();
        BigInteger privateKey = keyPair.getPrivateKey();

//        sm02.exportPublicKey(publicKey, "D:/test/publickey.pem");
//        sm02.exportPrivateKey(privateKey, "D:/test/privatekey.pem");
//
//        ECPoint publicKey1 = sm02.importPublicKey("D:/test/publickey.pem");
//        BigInteger privateKey1 = sm02.importPrivateKey("D:/test/privatekey.pem");

//        BigInteger bi = new BigInteger("0820e6baf23a0918fa746318d1e6100244ce50d4a094652ba1b4936bc7a1668f",16);
//        System.out.println(bi);
        String source = "我好";
        String go = "04c11c99cb4e893c2e8d82d960c148cd454bd33b31b0f2b2c1ef41d3105495c13ad56f0096faa2cd43b581eaec39229db7556a393e8f56260c0dacf97fa77f7423";
        String si = "0820e6baf23a0918fa746318d1e6100244ce50d4a094652ba1b4936bc7a1668f";
        byte[] sourceByte = sm02.encryptHex(source,go);

        String re = sm02.decryptHex(sourceByte,si);
        System.out.println("re:"+re);


    }
}
