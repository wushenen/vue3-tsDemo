package com.qtec.src;

import com.qtec.src.api.Element;
import com.qtec.src.field.curve.CurveElement;
import com.qtec.src.field.curve.CurveField;

import java.math.BigInteger;

/**
 * Created by mzy on 2017/4/7.
 * SM9的数字签名 + 验证
 */
public class SM9Signer {
    private SM9KeyGenerationCenter kgc;
    private BigInteger N;
    private CurveElement p1, p2, ppubs;
    private byte[] hid;
    private SM9SignPrivateKey privatekey;
    private String id;
    // 下面变量为kgc.pair(in1, in2)操作返回值。这里保证同一SM9Signer对象，该操作只执行一次
    private Element ENC_GT_G;

    public SM9Signer(SM9KeyGenerationCenter kgc, byte[] hid) {
        this.kgc = kgc;
        this.N = kgc.getN();
        this.p1 = kgc.getG1();
        this.p2 = kgc.getG2();
        this.ppubs = kgc.getPpubs();
        this.hid = hid;
    }

    public void initSign(SM9SignPrivateKey privatekey) {
        this.privatekey = privatekey;
    }

    public void initVerify(String id) {
        this.id = id;
    }

    public Signature generateSignature(byte[] message) throws Exception {
        if (privatekey == null) {
            throw new Exception("not initial for sign");
        }
        // 计算群Gt 中的元素g = e(P1 , Ppub-s)
        if (null == ENC_GT_G){
            ENC_GT_G = kgc.pair(p1, ppubs);
        }
        BigInteger l, h;
        do {
            // 产生随机数
            BigInteger r = SMCommonUtils.randomInN(N);
            // 计算群Gt 中的元素w = g^r
            Element w = ENC_GT_G.duplicate().pow(r);

            byte[] wb = SMCommonUtils.GTFiniteElementToByte(w);
            byte[] merge = SMCommonUtils.byteMerger(message, wb);
            // 计算h = H2(M||w, N)
            h = Sm9Utils.h2(merge, N);
            // 计算l = ( r - h ) mod N
            l = r.subtract(h).mod(N);
        } while (l.equals(BigInteger.ZERO));

        CurveElement ds = privatekey.getDs();
        // 计算群G1 中的元素S = [l]dsA = (xS , yS)：
        CurveElement s = ds.duplicate().mul(l);
        // 消息M 的签名为(h, S)
        return new Signature(h, s);
    }

    public boolean verifySignature(byte[] message, Signature signature) throws Exception {
        if (id == null) {
            throw new Exception("not initial for verify");
        }
        if (signature.getH().compareTo(BigInteger.ONE) < 0 || signature.getH().compareTo(N) >= 0) {
            return false;
        }
        if (!signature.getS().isValid()) {
            return false;
        }
        // 计算群Gt 中的元素g = e(P1 , Ppub-s)：
        if (null == ENC_GT_G){
            ENC_GT_G = kgc.pair(p1, ppubs);
        }
        // 计算群Gt 中的元素t = g^h’
        Element t = ENC_GT_G.duplicate().pow(signature.getH());

        byte[] merge = SMCommonUtils.byteMerger(id.getBytes("UTF-8"), hid);
        // 计算h1 = H1(IDA||hid, N)
        BigInteger h1 = Sm9Utils.h1(merge, N);

        // 计算群G2 中的元素P = [h1]P2 + Ppub-s = (xP , yP)
        CurveElement p = p2.duplicate().mul(h1).add(ppubs);
        // 计算群Gt 中的元素u = e (S’ , P)
        Element u = kgc.pair(signature.getS(), p);
        // 计算群Gt 中的元素w' = u * t
        Element w = u.mul(t);

        byte[] wb2 = SMCommonUtils.GTFiniteElementToByte(w);
        byte[] merge2 = SMCommonUtils.byteMerger(message, wb2);
        // 计算h2 = H2(M’||w’, N)
        BigInteger h2 = Sm9Utils.h2(merge2, N);
        // 如果h2 = h,验证通过
        return h2.equals(signature.getH());
    }

    /**
     * byte 转 CurveElement
     * @param xy
     * @return
     * @throws Exception
     */
    public CurveElement getSByByte(byte[] xy) throws Exception {
        CurveField curve1 = (CurveField)this.kgc.getPairing().getG1();
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
     * Created by mzy on 2017/4/19.
     * 消息M 的签名为(h, S)的封装bean
     */
    public static class Signature {
        private BigInteger h;
        private CurveElement s;

        public Signature(BigInteger h, CurveElement s) {
            this.h = h;
            this.s = s;
        }

        public BigInteger getH() {
            return h;
        }

        public CurveElement getS() {
            return s;
        }

        public String toString() {
            return "h:" + h.toString(16) + "\nS:" + SMCommonUtils.printHexString(s.toBytes());
        }
    }

}
