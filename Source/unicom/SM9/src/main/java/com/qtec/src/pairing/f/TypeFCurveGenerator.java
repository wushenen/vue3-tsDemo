package com.qtec.src.pairing.f;


import com.qtec.src.Params;
import com.qtec.src.api.Field;
import com.qtec.src.api.PairingParameters;
import com.qtec.src.api.PairingParametersGenerator;
import com.qtec.src.api.Point;
import com.qtec.src.field.poly.PolyElement;
import com.qtec.src.field.poly.PolyField;
import com.qtec.src.field.quadratic.QuadraticField;
import com.qtec.src.field.z.ZrField;
import com.qtec.src.pairing.f.parameters.PropertiesParameters;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 * The curve is defined as E : y^2 = x^3 + b
 * for some b \in F_q.
 */
public class TypeFCurveGenerator implements PairingParametersGenerator {
    protected SecureRandom random;
    protected int rBits; // The number of bits in r, the order of the subgroup G1

    public TypeFCurveGenerator(int rBits) {
        this(new SecureRandom(), rBits);
    }

    public TypeFCurveGenerator(SecureRandom random, int rBits) {
        this.random = random;
        this.rBits = rBits;
    }

    public PairingParameters generate() {
        BigInteger t = Params.t;
        BigInteger b = Params.b;
        // 迹 tr = 6t^2 + 1
        BigInteger tr = t.multiply(t).multiply(BigInteger.valueOf(6L)).add(BigInteger.ONE);
        // 基域特征 q = 36t^4 + 36t^3 + 24t^2 + 6t + 1
        BigInteger q = tryPlusX(t);
        // 群的阶 N = 36t^4 + 36t^3 + 18t^2 + 6t + 1
        BigInteger N = q.subtract(tr).add(BigInteger.ONE);

        // Compute b
        Field Fq = new ZrField(random, q);
        // 扭曲线的参数 beta
        BigInteger beta = Fq.newElement(-2).toBigInteger();

        Field Fq2 = new QuadraticField(random, Fq);
        PolyField Fq2x = new PolyField(random, Fq2);
        PolyElement<Point> f = Fq2x.newElement();

        // Find an irreducible polynomial of the form f = x^6 + alpha.
        // Call poly_set_coeff1() first so we can use element_item() for the other
        // coefficients.
        f.ensureSize(7);
        f.getCoefficient(6).setToOne();
        Point tmp = (Point) Fq2.newElement();
        tmp.getX().setToZero();
        tmp.getY().setToOne();
        f.getCoefficient(0).set(tmp.negate());

        // Store parameters
        PropertiesParameters params = new PropertiesParameters();
        params.put("type", "f");
        params.put("t", t.toString());
        params.put("tr", tr.toString());
        params.put("q", q.toString());
        // r代表群的阶N
        params.put("r", N.toString());
        params.put("b", b.toString());
        params.put("beta", beta.toString());
        params.put("alpha0", f.getCoefficient(0).getX().toBigInteger().toString());
        params.put("alpha1", f.getCoefficient(0).getY().toBigInteger().toString());
        return params;
    }


    protected BigInteger tryMinusX(BigInteger x) {
        // 36x^4 - 36x^3 + 24x^2 - 6x + 1 = ((36(x - 1)x + 24)x - 6)x + 1
        return x.subtract(BigInteger.ONE)
                .multiply(x)
                .multiply(BigInteger.valueOf(36L))
                .add(BigInteger.valueOf(24L))
                .multiply(x)
                .subtract(BigInteger.valueOf(6L))
                .multiply(x)
                .add(BigInteger.ONE);
    }

    protected BigInteger tryPlusX(BigInteger x) {
        // 36x^4 + 36x^3 + 24x^2 + 6x + 1 = ((36(x + 1)x + 24)x + 6)x + 1
        return x.add(BigInteger.ONE)
                .multiply(x)
                .multiply(BigInteger.valueOf(36L))
                .add(BigInteger.valueOf(24L))
                .multiply(x)
                .add(BigInteger.valueOf(6L))
                .multiply(x)
                .add(BigInteger.ONE);
    }

    public static void main(String[] args) {
        Integer rBits = 256;
        PairingParametersGenerator generator = new TypeFCurveGenerator(rBits);
        PairingParameters curveParams = generator.generate();
        System.out.println(curveParams.toString(" "));
    }

}