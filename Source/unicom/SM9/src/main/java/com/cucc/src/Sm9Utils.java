package com.cucc.src;

import com.cucc.src.util.HexUtils;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.SM3Digest;
import com.cucc.gmhelper.SM4Util;
import com.cucc.src.api.Element;
import com.cucc.src.api.PairingParametersGenerator;
import com.cucc.src.api.Point;
import com.cucc.src.field.curve.CurveElement;
import com.cucc.src.field.curve.CurveField;
import com.cucc.src.pairing.f.TypeFCurveGenerator;
import com.cucc.src.pairing.f.TypeFPairing;
import com.cucc.src.sm3.SM3;
import com.cucc.src.sm4.SM4;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Arrays;

/**
 * Created by mzy on 2017/4/17.
 * SM9中使用的各种杂凑函数、派生函数
 */
public class Sm9Utils {
    // 密码杂凑函数Hv()
    private static final Digest digest;
    private static final int DIGEST_SIZE;
    private static final int V;
    private static final double LOG2;
    private static final SM4 sm4;
    public static final String UTF_8 = "UTF-8";
    private static PairingParametersGenerator pairingParametersGenerator ;
    private static TypeFPairing pairing ;
    static {
        pairingParametersGenerator = new TypeFCurveGenerator(256);
        pairing = new TypeFPairing(pairingParametersGenerator.generate());
        digest = new SM3Digest();
        DIGEST_SIZE = digest.getDigestSize();
        V = DIGEST_SIZE << 3;
        LOG2 = Math.log(2.0);
        sm4 = new SM4();
    }

    /**
     * 由密码杂凑函数Hv()派生的密码函数H1()
     *
     * @param z 比特串Z
     * @param n 整数n
     * @return 整数[1, n-1]
     */
    public static BigInteger h1(byte[] z, BigInteger n) {
        double log2n = Math.log(n.doubleValue()) / LOG2;
        double hlen = 8 * Math.ceil((5 * log2n) / 32);
        int counts = (int) Math.ceil(hlen / V);
        byte[] Ha = new byte[counts * DIGEST_SIZE];
        int zLength = z.length;
        for (int ct = 1; ct < counts; ct++) {
            digest.reset();
            digest.update((byte) 0x01);
            digest.update(z, 0, zLength);
            digest.update((byte) (ct >> 24 & 0xff));
            digest.update((byte) (ct >> 16 & 0xff));
            digest.update((byte) (ct >> 8 & 0xff));
            digest.update((byte) (ct & 0xff));
            digest.doFinal(Ha, (ct - 1) * DIGEST_SIZE);
        }
        byte[] temp = new byte[DIGEST_SIZE];
        digest.reset();
        digest.update((byte) 0x01);
        digest.update(z, 0, zLength);
        digest.update((byte) (counts >> 24 & 0xff));
        digest.update((byte) (counts >> 16 & 0xff));
        digest.update((byte) (counts >> 8 & 0xff));
        digest.update((byte) (counts & 0xff));
        digest.doFinal(temp, 0);

        BigInteger Hanum1;
        if (hlen % V > 0) {
            int nbits = (int) (hlen - (V * Math.floor(hlen / V)));
            int right = V - nbits;
            System.arraycopy(temp, 0, Ha, (counts - 1) * DIGEST_SIZE, DIGEST_SIZE);
            Hanum1 = new BigInteger(1, Ha);
            Hanum1 = Hanum1.shiftRight(right);
        } else {
            System.arraycopy(temp, 0, Ha, (counts - 1) * DIGEST_SIZE, DIGEST_SIZE);
            Hanum1 = new BigInteger(1, Ha);
        }
        return Hanum1.mod(n.subtract(BigInteger.ONE)).add(BigInteger.ONE);
    }

    /**
     * 由密码杂凑函数Hv()派生的密码函数H2()
     *
     * @param z 比特串Z
     * @param n 整数n
     * @return 整数[1, n-1]
     */
    public static BigInteger h2(byte[] z, BigInteger n) {
        double log2n = Math.log(n.doubleValue()) / LOG2;
        double hlen = 8 * Math.ceil((5 * log2n) / 32);
        int counts = (int) Math.ceil(hlen / V);
        byte[] Ha = new byte[counts * DIGEST_SIZE];
        int zLength = z.length;
        for (int ct = 1; ct < counts; ct++) {
            digest.reset();
            digest.update((byte) 0x02);
            digest.update(z, 0, zLength);
            digest.update((byte) (ct >> 24 & 0xff));
            digest.update((byte) (ct >> 16 & 0xff));
            digest.update((byte) (ct >> 8 & 0xff));
            digest.update((byte) (ct & 0xff));
            digest.doFinal(Ha, (ct - 1) * DIGEST_SIZE);
        }
        byte[] temp = new byte[DIGEST_SIZE];
        digest.reset();
        digest.update((byte) 0x02);
        digest.update(z, 0, zLength);
        digest.update((byte) (counts >> 24 & 0xff));
        digest.update((byte) (counts >> 16 & 0xff));
        digest.update((byte) (counts >> 8 & 0xff));
        digest.update((byte) (counts & 0xff));
        digest.doFinal(temp, 0);

        BigInteger Hanum2;
        if (hlen % V > 0) {
            int nbits = (int) (hlen - (V * Math.floor(hlen / V)));
            int right = V - nbits;
            System.arraycopy(temp, 0, Ha, (counts - 1) * DIGEST_SIZE, DIGEST_SIZE);
            Hanum2 = new BigInteger(1, Ha);
            Hanum2 = Hanum2.shiftRight(right);
        } else {
            System.arraycopy(temp, 0, Ha, (counts - 1) * DIGEST_SIZE, DIGEST_SIZE);
            Hanum2 = new BigInteger(1, Ha);
        }
        return Hanum2.mod(n.subtract(BigInteger.ONE)).add(BigInteger.ONE);
    }

    /**
     * 密钥派生函数，需要调用密码杂凑函数Hv()
     * 作用：在密钥交换所获共享的秘密比特串上，从中产生所需的会话密钥或进一步加密所需的密钥数据
     *
     * @param z    比特串Z——双方共享的数据
     * @param klen 整数klen——表示要获得的密钥数据的比特长度，要求该值小于(2^32 -1)v，v为杂凑值的长度
     * @return 长度为klen 的密钥数据比特串
     */
    public static byte[] KDF(byte[] z, long klen) {
        assert (klen < 4294967295L * V);
        int reminder = (int) (klen % V);
        int counts = (int) (klen / V) + (reminder == 0 ? 0 : 1);
        byte[] ha = new byte[counts * DIGEST_SIZE];
        int zLength = z.length;
        for (int ct = 1; ct <= counts; ct++) {
            digest.reset();
            digest.update(z, 0, zLength);
            digest.update((byte) (ct >> 24 & 0xff));
            digest.update((byte) (ct >> 16 & 0xff));
            digest.update((byte) (ct >> 8 & 0xff));
            digest.update((byte) (ct & 0xff));
            digest.doFinal(ha, (ct - 1) * DIGEST_SIZE);
        }

        if (klen % V > 0) {
            int lbits = (int) (klen - (V * Math.floor(klen / V)));
            int shiftbit = V - lbits;
            BigInteger hanum = new BigInteger(1, ha);
            hanum = hanum.shiftRight(shiftbit);
            byte[] k = hanum.toByteArray();
            if (ha[0] == 0){
                byte[] temp = new byte[k.length + 1];
                System.arraycopy(k, 0, temp, 1, k.length);
                k = temp;
            }
            // 此处去掉多出1字节时前面出现的0值
            System.arraycopy(k, k.length - ((int) klen >> 3), k, 0, (int) klen >> 3);
            return k;
        } else {
            return ha;
        }
    }

    /**
     * 消息认证码函数MAC(K2, Z)
     *
     * @param k2 比特串K2(比特长度为K2_len 的密钥)
     * @param z  比特串Z(待求取消息认证码的消息)
     * @return 长度为v(256) 的消息认证码数据比特串
     */
    public static byte[] MAC(byte[] k2, byte[] z) {
        byte[] k = new byte[DIGEST_SIZE];
        digest.reset();
        digest.update(z, 0, z.length);
        digest.update(k2, 0, k2.length);
        digest.doFinal(k, 0);
        return k;
    }

    /**
     * SM4 用密钥K1对明文m进行加密
     *
     * @param k1 密钥byte数组
     * @param m  明文byte数组，长度需要为32的倍数，长度不足的使用0C填充
     * @return SM4的ECB模式加密后的密文比特串
     * @throws Exception
     */
    /*public static byte[] Enc(byte[] k1, byte[] m) throws Exception {
        int p;
        String oc;
        if (m.length % 16 == 0) {   // 明文长度正确的，直接填充32位
            p = 16;
            oc = "10";  // 十六进制的10，等于十进制的16
        } else {
            p = 16 - m.length % 16;
            // 计算需要填充的位数，转为16进制字符串后，作为填充值。
            int v = (int) Math.ceil(m.length / 16.0) << 4;
            oc = String.format("%02x", v - m.length);
        }
        String inputHex = SMCommonUtils.printHexString(m) + oc;
        StringBuffer stringBuffer = new StringBuffer(inputHex);
        for (int i = 0, length = p - 1; i < length; i++) {
            stringBuffer.append(oc);
        }
        String ecb = sm4.encryptDataECB(stringBuffer.toString(), SMCommonUtils.printHexString(k1), true);
        return SMCommonUtils.hexString2Bytes(ecb);
//        return sm4.encryptDataECB(stringBuffer.toString().getBytes(),SMCommonUtils.printHexString(k1));

    }*/
    public static byte[] Enc(byte[] k1, byte[] m) throws Exception {
        int p;
        String oc;
        if (m.length % 16 == 0) {   // 明文长度正确的，直接填充32位
            p = 16;
            oc = "10";  // 十六进制的10，等于十进制的16
        } else {
            p = 16 - m.length % 16;
            // 计算需要填充的位数，转为16进制字符串后，作为填充值。
            int v = (int) Math.ceil(m.length / 16.0) << 4;
            oc = String.format("%02x", v - m.length);
        }
        System.out.println("SMCommonUtils.printHexString(m):"+SMCommonUtils.printHexString(m));
        System.out.println("SMCommonUtils.printHexString(k1):"+SMCommonUtils.printHexString(k1));

        String inputHex = SMCommonUtils.printHexString(m) + oc;
        StringBuffer stringBuffer = new StringBuffer(inputHex);
        for (int i = 0, length = p - 1; i < length; i++) {
            stringBuffer.append(oc);
        }
        System.out.println("stringBuffer:"+stringBuffer);

//        String ecb = sm4.encryptDataECB(stringBuffer.toString(), SMCommonUtils.printHexString(k1), true);
        byte[] ecbByte = SM4Util.encrypt_ECB_Padding(k1,HexUtils.hexStringToBytes(stringBuffer.toString()));
        String ecb = HexUtils.bytesToHexString(ecbByte);
        System.out.println("ecb:"+ecb);

        return SMCommonUtils.hexString2Bytes(ecb);
    }
    /**
     * SM4 用密钥K1对密文C2进行解密
     *
     * @param k1 密钥byte数组
     * @param c2 密文byte数组
     * @return SM4的ECB模式解密后的明文比特串
     * @throws Exception
     */
    /*public static byte[] Dec(byte[] k1, byte[] c2) throws Exception {
        String plainText = sm4.decryptDataECB(SMCommonUtils.printHexString(c2), SMCommonUtils.printHexString(k1), false);
        // 找出最后2字符（1字节）填充值
        String substring = plainText.substring(plainText.length() - 2);
        // 由上面找出的填充值可以截取明文实际值
        String ecb = plainText.substring(0, plainText.length() - Integer.parseInt(substring, 16) * 2);
        return SMCommonUtils.hexString2Bytes(ecb);
    }*/
    public static byte[] Dec(byte[] k1, byte[] c2) throws Exception {

        byte[] plainByte = SM4Util.decrypt_ECB_NoPadding(k1, c2);
        String plainText = HexUtils.bytesToHexString(plainByte);
        // 找出最后2字符（1字节）填充值
        String substring = plainText.substring(plainText.length() - 2);

        // 由上面找出的填充值可以截取明文实际值
        String ecb = plainText.substring(0, plainText.length() - Integer.parseInt(substring, 16) * 2);

        return SMCommonUtils.hexString2Bytes(ecb);
    }
    /**
     * 传输实体类
     *
     * @author Potato
     */
    public static class TransportEntity implements Serializable {
        // RA或RB
        final CurveElement R;
        // SA或SB
        final byte[] S;
        final Element g2;
        final Element g1A;

        public TransportEntity(CurveElement R, byte[] S, Element g2, Element g1A) {
            this.R = R;
            this.S = S;
            this.g2 = g2;
            this.g1A = g1A;
        }

    }

    /**
     * 密钥交换协议辅助类
     *
     * @author Potato
     */
    public static class SM9KeyExchange {
        private SM9KeyGenerationCenter kgc;
        private String ID;
        private byte[] hid;
        // 加密主公钥
        private CurveElement ppube;
        // 用户A或B的加密私钥
        private CurveElement deX;
        private BigInteger N;
        private CurveElement p1;
        private CurveElement p2;
        // 交换秘钥的长度
        private long klen;

        private BigInteger rA;      // A用户产生的随机数
        private CurveElement RA;
        private Element g1A;
        private byte[] Padding82 = {(byte) 0x82};
        private byte[] Padding83 = {(byte) 0x83};
        private byte[] S2;      // B用户产生的选项

        // 协商后获得的最终秘钥
        byte[] key;

        public byte[] getKey() {
            return key;
        }

        public SM9KeyExchange(SM9KeyGenerationCenter kgc, String ID, byte[] hid, long klen) throws Exception {
            this.kgc = kgc;
            this.ID = ID;
            this.hid = hid;
            this.ppube = kgc.getPpube();
            this.deX = kgc.generateEncrypyPrivateKey(ID, hid).getDe();
            this.N = kgc.getN();
            this.p1 = kgc.getG1();
            this.p2 = kgc.getG2();
            this.klen = klen;
        }

        /**
         * 密钥协商发起第一步
         * 密钥交换步骤A1-A4
         *
         * @return
         */
        public TransportEntity keyExchange_1(String IDB) throws Exception {
            BigInteger h1 = h1(SMCommonUtils.byteMerger(IDB.getBytes(UTF_8), hid), N);
            // 计算群G1 中的元素QB =[H1(IDB||hid, N)]P1 +Ppub-e
            CurveElement QB = p1.duplicate().mul(h1).add(ppube);
            // 随机数
            rA = SMCommonUtils.randomInN(N);
//            rA = new BigInteger("5879 DD1D51E1 75946F23 B1B41E93 BA31C584 AE59A426 EC1046A4 D03B06C8".replace(" ",""),16);
            g1A = kgc.pair(ppube, p2).pow(rA);
            RA = QB.duplicate().mul(rA);
            return new TransportEntity(RA, null, null, g1A);
        }

        /**
         * 密钥协商发起第二步
         * 密钥交换步骤B1-B7
         *
         * @return
         */
        public TransportEntity keyExchange_2(TransportEntity entity, String IDA) throws Exception {
            BigInteger h1 = h1(SMCommonUtils.byteMerger(IDA.getBytes(UTF_8), hid), N);
            CurveElement QA = p1.duplicate().mul(h1).add(ppube);
            // 随机数
            BigInteger rB = SMCommonUtils.randomInN(N);
//            BigInteger rB = new BigInteger("018B98 C44BEF9F 8537FB7D 071B2C92 8B3BC65B D3D69E1E EE213564 905634FE".replace(" ",""),16);
            CurveElement RB = QA.duplicate().mul(rB);
            // 验证RA是否属于G1
            if (!entity.R.isValid()) {
                throw new Exception("invalid content");
            }
            // 计算群Gt 中的元素g1 = e(RA , deB)，下面2个pair操作耗时
            Element g1B = kgc.pair(entity.R, deX);
            Element g2 = kgc.pair(ppube, p2).pow(rB);
            Element g3 = g1B.pow(rB);
            byte[] merge1 = SMCommonUtils.join(IDA.getBytes(), ID.getBytes(), entity.R.toBytes(), RB.toBytes(), SMCommonUtils.GTFiniteElementToByte(entity.g1A), SMCommonUtils.GTFiniteElementToByte(g2), SMCommonUtils.GTFiniteElementToByte(g3));
            byte[] SKB = KDF(merge1, klen);
            key = SKB;
            System.out.println("协商得B密钥SKB:\n" + SMCommonUtils.printHexString(SKB));

            byte[] merge2 = SMCommonUtils.join(SMCommonUtils.GTFiniteElementToByte(g2), SMCommonUtils.GTFiniteElementToByte(g3), IDA.getBytes(), ID.getBytes(), entity.R.toBytes(), RB.toBytes());
            byte[] hash = SM3.hash(merge2);
            byte[] SB = SM3.hash(SMCommonUtils.join(Padding82, SMCommonUtils.GTFiniteElementToByte(entity.g1A), hash));
            S2 = SM3.hash(SMCommonUtils.join(Padding83, SMCommonUtils.GTFiniteElementToByte(entity.g1A), hash));
            return new TransportEntity(RB, SB, g2, null);
        }

        /**
         * 密钥协商发起第三步
         * 密钥交换步骤A5-A8
         *
         * @return
         */
        public TransportEntity keyExchange_3(TransportEntity entity, String IDB) throws Exception {
            // 验证RB是否属于G1
            if (!entity.R.isValid()) {
                throw new Exception("invalid content");
            }
            Element g2A = kgc.pair(entity.R, deX);
            Element g3A = entity.g2.pow(rA);
            byte[] merge1 = SMCommonUtils.join(SMCommonUtils.GTFiniteElementToByte(g2A), SMCommonUtils.GTFiniteElementToByte(g3A), ID.getBytes(UTF_8), IDB.getBytes(UTF_8), RA.toBytes(), entity.R.toBytes());
            byte[] hash = SM3.hash(merge1);
            byte[] S1 = SM3.hash(SMCommonUtils.join(Padding82, SMCommonUtils.GTFiniteElementToByte(g1A), hash));
//            assertArrayEquals(S1, entity.S);
            if(Arrays.equals(S1, entity.S)){
                System.out.println("S1=SB,从B到A的密钥确认成功");
            }else{
                System.out.println("S1 != SB,从B到A的密钥失败");
            }

            byte[] merge2 = SMCommonUtils.join(ID.getBytes(), IDB.getBytes(), RA.toBytes(), entity.R.toBytes(), SMCommonUtils.GTFiniteElementToByte(g1A), SMCommonUtils.GTFiniteElementToByte(g2A), SMCommonUtils.GTFiniteElementToByte(g3A));
            byte[] SKA = KDF(merge2, klen);
            key = SKA;
            System.out.println("协商得A密钥SKA:\n" + SMCommonUtils.printHexString(SKA));
            byte[] SA = SM3.hash(SMCommonUtils.join(Padding83, SMCommonUtils.GTFiniteElementToByte(g1A), hash));
            return new TransportEntity(null, SA, null, null);
        }

        /**
         * 密钥协商发起第四步
         * 密钥交换步骤B8
         *
         * @return
         */
        public void keyExchange_4(TransportEntity entity) {
            if(Arrays.equals(S2, entity.S)){
                System.out.println("S2=SA,从A到B的密钥确认成功");
            }else{
                System.out.println("S2 != SA,从A到B的密钥失败");
            }
        }
    }
    /**
     * 加密相关 byte 转 CurveElement
     * @param xy
     * @return
     * @throws Exception
     */
    public static CurveElement getSByByte(byte[] xy) throws Exception {
        CurveField curve1 = (CurveField) pairing.getG1();
        CurveElement g1 = curve1.newElement();
        String hexString = SMCommonUtils.printHexString(xy);
        BigInteger xInt = new BigInteger(hexString.substring(0, 64), 16);
        BigInteger yInt = new BigInteger(hexString.substring(64), 16);
        g1.getX().set(xInt);
        g1.getY().set(yInt);
        g1.setInfFlag(0);
        return g1;
    }
    /**
     * 签名相关 byte 转 CurveElement
     * @param xy
     * @return
     * @throws Exception
     */
    public static CurveElement getSignByByte(byte[] xy) throws Exception {
        CurveField curve2 = (CurveField) pairing.getG2();
        CurveElement g2 = curve2.newElement();
        String hexString = SMCommonUtils.printHexString(xy);
        BigInteger xxInt = new BigInteger(hexString.substring(0, 64), 16);
        BigInteger xyInt = new BigInteger(hexString.substring(64,128), 16);
        BigInteger yxInt = new BigInteger(hexString.substring(128, 192), 16);
        BigInteger yyInt = new BigInteger(hexString.substring(192), 16);

        Point g2x = (Point) g2.getX().getField().newElement();
        Point g2y = (Point) g2.getX().getField().newElement();
        g2x.getX().set(xxInt);
        g2x.getY().set(xyInt);
        g2y.getX().set(yxInt);
        g2y.getY().set(yyInt);
        g2.getX().set(g2x);
        g2.getY().set(g2y);
        g2.setInfFlag(0);
        return g2;
    }
    /** SM9功能中，将256个字符的16进制字符串的user_prienc转换成CurveElement类型的用户私钥de **/
    public static CurveElement convertToPriEnc(String userPrienc){
        CurveElement de = ((CurveField) pairing.getG2()).newElement();
        Point priencx = (Point) de.getX().getField().newElement();
        Point priency = (Point) de.getX().getField().newElement();
        priencx.getX().set(new BigInteger(userPrienc.substring(0,64),16));
        priencx.getY().set(new BigInteger(userPrienc.substring(64,128),16));
        priency.getX().set(new BigInteger(userPrienc.substring(128,192),16));
        priency.getY().set(new BigInteger(userPrienc.substring(192),16));
        de.getX().set(priencx);
        de.getY().set(priency);
        de.setInfFlag(0);
        return de;
    }
    /** SM9功能中，将128个字符的16进制字符串的user_priSign转换成CurveElement类型的用户私钥de **/
    public static CurveElement convertToPriSign(String userPriSign){
        CurveElement de = ((CurveField) pairing.getG1()).newElement();
        de.getX().set(new BigInteger(userPriSign.substring(0,64),16));
        de.getY().set(new BigInteger(userPriSign.substring(64),16));
        de.setInfFlag(0);
        return de;
    }
}
