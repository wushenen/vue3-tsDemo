package com.cucc.src.field.poly;


import com.cucc.src.api.Element;
import com.cucc.src.api.Field;
import com.cucc.src.field.base.AbstractFieldOver;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.List;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class PolyModField<F extends Field> extends AbstractFieldOver<F, PolyModElement> {
    protected PolyElement irreduciblePoly;
    protected PolyModElement nqr;
    protected BigInteger order;
    protected int n;
    protected int fixedLengthInBytes;

    protected PolyModElement[] xpwr;


    public PolyModField(SecureRandom random, F targetField, int cyclotomicPolyDegree) {
        super(random, targetField);

        PolyField polyField = new PolyField(random, targetField);
        irreduciblePoly = polyField.newElement();

        List<Element> coefficients = irreduciblePoly.getCoefficients();
        coefficients.add(polyField.getTargetField().newElement().setToOne());
        for (int i = 1; i < cyclotomicPolyDegree; i++) {
            coefficients.add(polyField.getTargetField().newZeroElement());
        }
        coefficients.add(polyField.getTargetField().newElement().setToOne());

        init(null);
    }

    public PolyModField(SecureRandom random, PolyElement irreduciblePoly) {
        this(random, irreduciblePoly, null);
    }

    public PolyModField(SecureRandom random, PolyElement irreduciblePoly, BigInteger nqr) {
        super(random, (F) irreduciblePoly.getField().getTargetField());

        this.irreduciblePoly = irreduciblePoly;
        init(nqr);
    }

    
    protected void init(BigInteger nqr) {
        this.n = irreduciblePoly.getDegree();

        this.order = targetField.getOrder().pow(irreduciblePoly.getDegree());
        if (nqr != null) {
            this.nqr = newElement();
            this.nqr.getCoefficient(0).set(nqr);
        }

            computeXPowers();

        if (targetField.getLengthInBytes() < 0) {
            //f->length_in_bytes = fq_length_in_bytes;
            fixedLengthInBytes = -1;
        } else {
            fixedLengthInBytes = targetField.getLengthInBytes() * n;
        }
    } 
    

    public PolyModElement newElement() {
        return new PolyModElement(this);
    }

    public BigInteger getOrder() {
        return order;
    }

    public PolyModElement getNqr() {
        return nqr;
    }

    public int getLengthInBytes() {
        return fixedLengthInBytes;
    }

    public int getN() {
        return n;
    }


    /**
     * compute x^n,...,x^{2n-2} mod poly
     */
    protected void computeXPowers() {
        xpwr = new PolyModElement[n];

        for (int i = 0; i < n; i++) {
            xpwr[i] = newElement();
        }

        xpwr[0].setFromPolyTruncate(irreduciblePoly).negate();
        PolyModElement p0 = newElement();

        for (int i = 1; i < n; i++) {
            List<Element> coeff = xpwr[i - 1].getCoefficients();
            List<Element> coeff1 = xpwr[i].getCoefficients();

            coeff1.get(0).setToZero();

            for (int j = 1; j < n; j++) {
                coeff1.get(j).set(coeff.get(j - 1));
            }
            p0.set(xpwr[0]).polymodConstMul(coeff.get(n - 1));

            xpwr[i].add(p0);
        }


    }

}
