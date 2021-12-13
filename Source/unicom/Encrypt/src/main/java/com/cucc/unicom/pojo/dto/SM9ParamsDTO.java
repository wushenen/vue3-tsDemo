package com.cucc.unicom.pojo.dto;

import java.io.Serializable;

public class SM9ParamsDTO implements Serializable {
    private String pubSign;
    private String pubEnc;
    private String priSign;
    private String priEnc;

    public String getPubSign() {
        return pubSign;
    }

    public void setPubSign(String pubSign) {
        this.pubSign = pubSign;
    }

    public String getPubEnc() {
        return pubEnc;
    }

    public void setPubEnc(String pubEnc) {
        this.pubEnc = pubEnc;
    }

    public String getPriSign() {
        return priSign;
    }

    public void setPriSign(String priSign) {
        this.priSign = priSign;
    }

    public String getPriEnc() {
        return priEnc;
    }

    public void setPriEnc(String priEnc) {
        this.priEnc = priEnc;
    }
}
