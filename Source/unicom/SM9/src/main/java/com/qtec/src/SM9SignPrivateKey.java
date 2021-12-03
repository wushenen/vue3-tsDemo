package com.qtec.src;

import com.qtec.src.field.curve.CurveElement;


/**
 * Created by mzy on 2017/4/17.
 * SM9签名私钥ds的bean
 */
public class SM9SignPrivateKey {
    private CurveElement ds;

    public SM9SignPrivateKey(CurveElement point) {
        this.ds = point;
    }

    public CurveElement getDs() {
        return ds;
    }

    public String toString() {
        return "ds:" + SMCommonUtils.printHexString(ds.toBytes());
    }
}
