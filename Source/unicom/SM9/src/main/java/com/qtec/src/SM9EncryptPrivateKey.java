package com.qtec.src;

import com.qtec.src.field.curve.CurveElement;

/**
 * Created by mzy on 2017/4/24.
 * SM9加密私钥de的bean
 */
public class SM9EncryptPrivateKey {
    private CurveElement de;

    public SM9EncryptPrivateKey(CurveElement de) {
        this.de = de;
    }

    public CurveElement getDe() {
        return de;
    }

    public String toString() {
        return "de:" + SMCommonUtils.printHexString(de.toBytes());
    }
}
