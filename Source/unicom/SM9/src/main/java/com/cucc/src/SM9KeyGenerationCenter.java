package com.cucc.src;


import com.cucc.src.api.Element;
import com.cucc.src.field.curve.CurveElement;
import com.cucc.src.field.curve.CurveField;
import com.cucc.src.pairing.f.TypeFPairing;

import java.math.BigInteger;

/**
 * Created by mzy on 2017/4/17.
 * 密钥生成中心key generation center——KGC
 * 生产签名私钥 + 加密私钥
 */
public class SM9KeyGenerationCenter {

    // 签名主私钥 ks
    private BigInteger ks;  //master sign private key
    // 加密主私钥 ke
    private BigInteger ke;  //master encrypt private key
    private  CurveElement ppubs, ppube;
    private static SM9KeyGenerationCenter THIS;

    public BigInteger getKs() {
        return ks;
    }

    public void setKs(BigInteger ks) {
        this.ks = ks;
    }

    public BigInteger getKe() {
        return ke;
    }

    public void setKe(BigInteger ke) {
        this.ke = ke;
    }

    /**
     * 由默认的SM9文档中参数，生成密钥生成中心对象。各个参数设置在src.Params类中。
     */
    public SM9KeyGenerationCenter() {
        this.ks = SMCommonUtils.randomInN(Params.N);
        this.ke = SMCommonUtils.randomInN(Params.N);
        this.ppubs = Params.g2.duplicate().mul(ks);
        this.ppube = Params.g1.duplicate().mul(ke);
    }

    public static SM9KeyGenerationCenter getInstance() {
        if (THIS == null) {
            THIS = new SM9KeyGenerationCenter();
        }
        return THIS;
    }

    /** 服务端启动时从数据库获取私钥等参数，初始化缓存中的单例kgc对象 **/
    public static void initFromDB(SM9KeyGenerationCenter kgc) {
        if (null == kgc) throw new RuntimeException("SM9KeyGenerationCenter initFromDB error.");
        THIS = kgc;
    }
    /**
     * 由给定的SM9参数，生成密钥生成中心对象。各个参数都是十六进制的字符串形式
     * n为十六进制的64个字符 = 256位(bit)
     */
    public SM9KeyGenerationCenter(int ring) {
    }

    /**
     * 以主公钥构造kgc对象 0-加密主公钥 1-签名主公钥 2-都有
     * @param SM9_Ppube
     * @param SM9_Ppubs
     * @param mark
     * @throws Exception
     */
    public SM9KeyGenerationCenter( String SM9_Ppube,String SM9_Ppubs, int mark) throws Exception{
        switch (mark){
            case 1://1-签名主公钥
                this.ppubs = Sm9Utils.getSignByByte(SMCommonUtils.hexString2Bytes(SM9_Ppubs));
                break;
            case 2://2-都有
                this.ppubs = Sm9Utils.getSignByByte(SMCommonUtils.hexString2Bytes(SM9_Ppubs));
                this.ppube = Sm9Utils.getSByByte(SMCommonUtils.hexString2Bytes(SM9_Ppube));
                break;
            default://0-加密主公钥
                this.ppube = Sm9Utils.getSByByte(SMCommonUtils.hexString2Bytes(SM9_Ppube));
        }
    }

    /**
     * 由给定的SM9参数，生成密钥生成中心对象。各个参数都是十六进制的字符串形式
     * @param prikey   私钥16进制  可以主私钥，可以用户私钥
     * @param type    1-加密；2-签名
     */
    public SM9KeyGenerationCenter(String prikey, int type) {
        switch (type){
            case 1:
                this.ke = new BigInteger(prikey, 16);
                this.ppube = Params.g1.duplicate().mul(ke);
                break;
            case 2:
                this.ks = new BigInteger(prikey, 16);
                this.ppubs = Params.g2.duplicate().mul(ks);
                break;
             default:
            //默认加密
                 this.ke = new BigInteger(prikey, 16);
                 this.ppube = Params.g1.duplicate().mul(ke);
        }

    }

    /**
     * 生产签名私钥
     *
     * @param id  用户标识
     * @param hid 签名私钥生成函数识别符
     * @return 签名私钥ds
     * @throws Exception
     */
    public SM9SignPrivateKey generateSignPrivatekey(String id, byte[] hid) throws Exception {
        byte[] temp = SMCommonUtils.byteMerger(id.getBytes("UTF-8"), hid);
        // 在有限域FN上计算t1=H1(IDA||hid, N)+ks
        BigInteger t1 = Sm9Utils.h1(temp, Params.N).add(ks);
        t1 = t1.mod(Params.N);
        if (t1.equals(BigInteger.ZERO)) {
            throw new Exception("need to update the master sign private key ");
        }
        // t2 = ks * t1^-1 mod N
        BigInteger t2 = ks.multiply(t1.modInverse(Params.N)).mod(Params.N);
        //ds = [t2]P1
        CurveElement ds = Params.g1.duplicate().mul(t2);
        return new SM9SignPrivateKey(ds);
    }

    /**
     * 生产加密私钥
     *
     * @param id  用户标识
     * @param hid 加密私钥生成函数识别符
     * @return 加密私钥de
     * @throws Exception
     */
    public SM9EncryptPrivateKey generateEncrypyPrivateKey(String id, byte[] hid) throws Exception {
        byte[] merge = SMCommonUtils.byteMerger(id.getBytes("UTF-8"), hid);
        // 在有限域FN上计算t1=H1(IDA||hid, N)+ke
        BigInteger t1 = Sm9Utils.h1(merge, Params.N).add(ke);
        t1 = t1.mod(Params.N);
        if (t1.equals(BigInteger.ZERO)) {
            throw new Exception("need to update the master encrypt private key");
        }
        // t2 = ke * t1^-1 mod N
        BigInteger t2 = ke.multiply(t1.modInverse(Params.N)).mod(Params.N);
        //de = [t2]P2
        CurveElement de = Params.g2.duplicate().mul(t2);
        return new SM9EncryptPrivateKey(de);
    }

    public Element pair(CurveElement p1, CurveElement p2) {
        return Params.pairing.pairing(p1, p2);
    }

    public CurveElement getPpubs() {
        return this.ppubs;
    }

    public CurveElement getPpube() {
        return this.ppube;
    }

    public CurveElement getG1() {
        return Params.g1;
    }

    public CurveElement getG2() {
        return Params.g2;
    }

    public BigInteger getN() {
        return Params.N;
    }

    public CurveField getCurve1() {
        return Params.curve1;
    }

    public CurveField getCurve2() {
        return Params.curve2;
    }

    public TypeFPairing getPairing() {
        return Params.pairing;
    }
}
