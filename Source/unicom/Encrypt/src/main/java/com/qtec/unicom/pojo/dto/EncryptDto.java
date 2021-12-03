package com.qtec.unicom.pojo.dto;
//                        _oo0oo_
//                       o8888888o
//                       88" . "88
//                       (| -_- |)
//                       0\  =  /0
//                    ____/`---`\____
//                  .'  \\|     |//  '.
//                 /  \\|||  :  |||//  \
//                /  _||||| _:_ |||||_  \
//               |    | \\\  -  /// |    |
//               |  \_|  ''\---/''  |_/  |
//               \   .-\__  '-'  __/-.   /
//             ___`.  .'  /--.--\  '.  .`___
//          ."" '<   '.___\_<|>_/___.'   >' "".
//         |  | :  `- \`.;`\ _ /`;.`/ -`  : |  |
//         \   \ `-.   \_ __\ /__ _/   .-` /   /
// =========`-.____`-.___\_______/___.-`____.-`=========
//                        `=---=`
// ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
//              佛祖保佑            永无BUG
//       本模块已经经过开光处理，绝无可能再产生bug


import java.io.Serializable;

/**
 * @Author 李震宇
 * @Description
 * @Time 2018年06月26日
 * @Version
 */
public class EncryptDto implements Serializable{
    private static final long serialVersionUID = -5891302685780549667L;
    //对称密钥
    private String key;
    //来源数据（明文或密文）
    private String source;
    //算法名称SM4
    private String algo;
    //SM4算法模式：SM4/CBC/PKCS5PADDING  SM4/ECB/NOPADDING
    private String mode;
    //SM4算法CBC模式时的向量
    private String iv;
    //非对称密钥公钥
    private String pubKey;
    //非对称密钥私钥
    private String priKey;
    //签名数据
    private String signData;
    //签名预处理ID
    private String signId;

    public String getSignId() {
        return signId;
    }

    public void setSignId(String signId) {
        this.signId = signId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getAlgo() {
        return algo;
    }

    public void setAlgo(String algo) {
        this.algo = algo;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getIv() {
        return iv;
    }

    public void setIv(String iv) {
        this.iv = iv;
    }

    public String getPubKey() {
        return pubKey;
    }

    public void setPubKey(String pubKey) {
        this.pubKey = pubKey;
    }

    public String getPriKey() {
        return priKey;
    }

    public void setPriKey(String priKey) {
        this.priKey = priKey;
    }

    public String getSignData() {
        return signData;
    }

    public void setSignData(String signData) {
        this.signData = signData;
    }
}
