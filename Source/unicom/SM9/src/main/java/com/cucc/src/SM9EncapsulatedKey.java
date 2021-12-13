package com.cucc.src;

import com.cucc.src.field.curve.CurveElement;

/**
 * Created by mzy on 2017/4/24.
 * 密钥封装bean
 * K 是被封装的密钥，C 是封装密文
 */
public class SM9EncapsulatedKey {
    private byte[] k;
    private CurveElement c;

    public SM9EncapsulatedKey(byte[] k, CurveElement c) {
        this.k = k;
        this.c = c;
    }

    public byte[] getK() {
        return k;
    }

    public CurveElement getC() {
        return c;
    }

    public String toString() {
        return "K:" + SMCommonUtils.printHexString(k) + "\nC:" + SMCommonUtils.printHexString(c.toBytes());
    }
}
