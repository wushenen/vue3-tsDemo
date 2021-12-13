package com.cucc.unicom.pojo;

public class QkmVersion {
    private String id;
    /**
     * JSON字符串
     * {"MYSQL版本":"5.6.36","SM9 KGC版本":"1.1.0","管理工具版本":"V1.0.1","密码卡版本":"V3.7.1","USB-KEY版本":"V1.2 "}
     */
    private String version;
    /**
     * 系统状态
     * 0-初始；1-就绪
     */
    private int state;
    /**
     * 状态信息描述
     */
    private String param;
    /**
     * 私钥控制码
     */
    private String keyControlCode;
    /**
     * 服务器名
     */
    private String name;
    /**
     * 系统编号
     */
    private String number;
    /**
     * 校验两个war的哈希
     */
    private String verifyWar;
    /**
     * 签名公钥
     */
    private String publicKey;
    /**
     * 签名
     */
    private String signWar;
    /**
     * mac地址
     */
    private String macAddr;

    public String getMacAddr() {
        return macAddr;
    }

    public void setMacAddr(String macAddr) {
        this.macAddr = macAddr;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public String getKeyControlCode() {
        return keyControlCode;
    }

    public void setKeyControlCode(String keyControlCode) {
        this.keyControlCode = keyControlCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getVerifyWar() {
        return verifyWar;
    }

    public void setVerifyWar(String verifyWar) {
        this.verifyWar = verifyWar;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getSignWar() {
        return signWar;
    }

    public void setSignWar(String signWar) {
        this.signWar = signWar;
    }
}
