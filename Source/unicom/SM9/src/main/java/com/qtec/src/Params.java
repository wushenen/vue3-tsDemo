package com.qtec.src;

import com.qtec.src.api.PairingParameters;
import com.qtec.src.api.Point;
import com.qtec.src.field.curve.CurveElement;
import com.qtec.src.field.curve.CurveField;
import com.qtec.src.pairing.f.TypeFCurveGenerator;
import com.qtec.src.pairing.f.TypeFPairing;

import java.math.BigInteger;

/**
 * 国密办SM9文件中推荐的椭圆曲线相关参数-256位的BN曲线
 * 椭圆曲线方程：y^2 = x^3 + b
 * @author Potato
 *
 */
public class Params {
	/*
	 * t、tr、q、N参数在TypeFCurveGenerator.generate()中产生
	 * 参数群的阶N为256位(bit)，使用十六进制表示是64个字符，32字节(Byte)=256/8。
	 */
	// 参数t
	public static final BigInteger t = new BigInteger("600000000058F98A",16);
	// 方程参数b
	public static final BigInteger b = BigInteger.valueOf(5L);
	// 群G1的生成元P1 = (xp1 , yp1)
	public static final BigInteger g1xp1 = new BigInteger("93DE051D62BF718FF5ED0704487D01D6E1E4086909DC3280E8C4E4817C66DDDD", 16);
	public static final BigInteger g1yp1 = new BigInteger("21FE8DDA4F21E607631065125C395BBC1C1C00CBFA6024350C464CD70A3EA616", 16);
	// 群G2的生成元P2 = (xp2, yp2)	-- 这里的x和y对比推荐参数对调
	public static final BigInteger g2xp2x = new BigInteger("3722755292130B08D2AAB97FD34EC120EE265948D19C17ABF9B7213BAF82D65B", 16);
	public static final BigInteger g2xp2y = new BigInteger("85AEF3D078640C98597B6027B441A01FF1DD2C190F5E93C454806C11D8806141", 16);
	public static final BigInteger g2yp2x = new BigInteger("A7CF28D519BE3DA65F3170153D278FF247EFBA98A71A08116215BBA5C999A7C7", 16);
	public static final BigInteger g2yp2y = new BigInteger("17509B092E845C1266BA0D262CBEE6ED0736A96FA347C8BD856DC76B84EBEB96", 16);

	// Type F曲线参数集
	public static final PairingParameters parameters = (new TypeFCurveGenerator(256)).generate();

	// 曲线curve1, curve2
	public static CurveField curve1, curve2;
	// 下面这个gt变量未被使用，所以注释掉
	public static TypeFPairing pairing = new TypeFPairing(Params.parameters);

	// 群G1，群G2，签名主公钥 Ppub-s, 加密主公钥 Ppub-e
	public static CurveElement g1, g2, ppubs, ppube;
	// pairing.getR()得到群的阶N
	public static BigInteger N;
	static{
		curve1 = (CurveField) pairing.getG1();
		curve2 = (CurveField) pairing.getG2();
		g1 = curve1.newElement();
		g1.getX().set(Params.g1xp1);
		g1.getY().set(Params.g1yp1);
		g1.setInfFlag(0);

		g2 = curve2.newElement();
		Point g2x = (Point) g2.getX().getField().newElement();
		Point g2y = (Point) g2.getX().getField().newElement();
		g2x.getX().set(Params.g2xp2x);
		g2x.getY().set(Params.g2xp2y);
		g2y.getX().set(Params.g2yp2x);
		g2y.getY().set(Params.g2yp2y);
		g2.getX().set(g2x);
		g2.getY().set(g2y);
		g2.setInfFlag(0);
		N = pairing.getR();
	}


}
