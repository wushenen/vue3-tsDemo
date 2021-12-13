package com.cucc.src;

import com.cucc.src.api.Element;
import com.cucc.src.field.curve.CurveElement;

import java.math.BigInteger;

/**
 * Created by mzy on 2017/4/24.
 * 秘钥封装 + 解封
 */
public class SM9KEM {

    private SM9KeyGenerationCenter kgc;
    private byte[] hid;
    // 下面变量为kgc.pair(in1, in2)操作返回值。这里保证同一SM9KEM对象，该操作只执行一次
    private Element ENC_GT_G;

    public SM9KEM(SM9KeyGenerationCenter kgc, byte[] hid) {
        this.kgc = kgc;
        this.hid = hid;
    }

    /**
     * 秘钥封装
     *
     * @param id   接收端的用户id
     * @param klen 协商好封装密钥的长度
     * @return 秘钥K 自己留下，密文C 发送给接收端
     */
    public SM9EncapsulatedKey encapsulate(byte[] id, long klen) {
        BigInteger N = kgc.getN();
        CurveElement g1, g2, ppube;
        g1 = kgc.getG1();
        g2 = kgc.getG2();
        ppube = kgc.getPpube();

        byte[] merge = SMCommonUtils.byteMerger(id, hid);
        BigInteger h1 = Sm9Utils.h1(merge, N);
        CurveElement qb = g1.duplicate().mul(h1).add(ppube);

        byte[] k;
        CurveElement c;
        do {
            // 随机数
            BigInteger r = SMCommonUtils.randomInN(N);
            c = qb.mul(r);
            byte[] cb = c.toBytes();

            if (null == ENC_GT_G){
                ENC_GT_G = kgc.pair(ppube, g2);
            }
            Element w = ENC_GT_G.duplicate().pow(r);
            byte[] wb = SMCommonUtils.GTFiniteElementToByte(w);
            byte[] join = SMCommonUtils.join(cb, wb, id);
            k = Sm9Utils.KDF(join, klen);
        } while (testZeros(k));

        return new SM9EncapsulatedKey(k, c);
    }

    /**
     * 秘钥解封
     *
     * @param c    密文C
     * @param id   自己的用户id
     * @param de   自己的加密私钥
     * @param klen 协商好封装密钥的长度
     * @return 秘钥K的byte数组
     */
    public byte[] decapsulate(CurveElement c, byte[] id, SM9EncryptPrivateKey de, long klen) throws Exception {
        if (!c.isValid()) {
            throw new Exception("invalid content");
        }
        // 计算群GT 中的元素w’=e(C, deB)
        Element w = kgc.pair(c, de.getDe());
        byte[] wb = SMCommonUtils.GTFiniteElementToByte(w);
        byte[] cb = c.toBytes();
        byte[] join = SMCommonUtils.join(cb, wb, id);
        // 计算封装的密钥K’=KDF(C||w’||IDB, klen)
        byte[] k = Sm9Utils.KDF(join, klen);
        if (testZeros(k)) {
            throw new Exception("k is zeroo");
        }
        return k;
    }

    private boolean testZeros(byte[] in) {
        for (byte b : in) {
            if (b != 0)
                return false;
        }
        return true;
    }
}
