package com.qtec.src.sm2;

import java.math.BigInteger;

/**
 * SM2 国密办文件中推荐的椭圆曲线相关参数Fp-256
 * @author Potato	椭圆曲线方程为: y^2 = x^3 + ax + b
 *
 */
public class Sm2Params {
	// 阶n
	public static final BigInteger n = new BigInteger("FFFFFFFE" + "FFFFFFFF" + "FFFFFFFF" + "FFFFFFFF" + "7203DF6B" + "21C6052B" + "53BBF409" + "39D54123", 16);
	// 素数p
	public static final BigInteger p = new BigInteger("FFFFFFFE" + "FFFFFFFF" + "FFFFFFFF" + "FFFFFFFF" + "FFFFFFFF" + "00000000" + "FFFFFFFF" + "FFFFFFFF", 16);
	// 系数a
	public static final BigInteger a = new BigInteger("FFFFFFFE" + "FFFFFFFF" + "FFFFFFFF" + "FFFFFFFF" + "FFFFFFFF" + "00000000" + "FFFFFFFF" + "FFFFFFFC", 16);
	// 系数b
	public static final BigInteger b = new BigInteger("28E9FA9E9D9F5E344D5A9E4BCF6509A7F39789F515AB8F92DDBCBD414D940E93", 16);
	// 坐标xG
	public static final BigInteger gx = new BigInteger("32C4AE2C1F1981195F9904466A39C9948FE30BBFF2660BE1715A4589334C74C7", 16);
	// 坐标yG
	public static final BigInteger gy = new BigInteger("BC3736A2F4F6779C59BDCEE36B692153D0A9877CC62A474002DF32E52139F0A0", 16);
}
