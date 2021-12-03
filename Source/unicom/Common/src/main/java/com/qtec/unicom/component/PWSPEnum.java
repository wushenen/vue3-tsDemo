package com.qtec.unicom.component;

/**
 * Created by wuzh on 2020/2/27.
 * Description: 统一项目的各个常量值
 */
public enum PWSPEnum {
    /** service_log表，系统日志数据相关常量 **/
    /* type-日志类别 */
    TYPE_ACCESS_MANAGE("接入管理", 1),
    TYPE_KEY_SERVICE("密钥服务", 2),
    TYPE_SELF_CHECK("自检管理", 3),
    TYPE_SM9_SERVICE("SM9相关服务", 4),
    /* subtype-日志具体类型 */
    SUBTYPE_SIGN_IN("登入", 1001),
    SUBTYPE_SIGN_OUT("登出", 1002),
    SUBTYPE_CREATE_KEY("新建密钥", 2001),
    SUBTYPE_GET_KEY("获取密钥", 2002),
    SUBTYPE_GET_RANDOM("获取量子随机数", 2003),
    SUBTYPE_UPDATE_KEY_START("更新存储密钥开始", 2005),
    SUBTYPE_UPDATE_KEY_END("更新存储密钥结束", 2006),
    SUBTYPE_SELF_CHECK("周期自检", 3001),
    SUBTYPE_SM9_SERVICE("获取SM9公开参数和用户私钥", 4001),
    /* result-业务处理结果 */
    RESULT_SUCCESS("成功", 1),
    RESULT_FAIL("失败", 0),

    /** service_key表，量子密钥数据相关常量 **/
    /* key_type-密钥类型 */
    KEY_TYPE_SYMMETRY("对称密钥", 0x00000102),
    /* chkv_alg-校验算法标识 */
    CHKV_ALG_SM3("SM3", 0x00000001),

    ;

    private String msg;
    private int code;
    PWSPEnum(String msg, int code) {
        this.msg = msg;
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public int getCode(){
        return code;
    }
}
