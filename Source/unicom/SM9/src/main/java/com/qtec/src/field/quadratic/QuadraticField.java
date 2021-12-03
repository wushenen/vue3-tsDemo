package com.qtec.src.field.quadratic;


import com.qtec.src.api.Field;
import com.qtec.src.field.base.AbstractFieldOver;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class QuadraticField<F extends Field, E extends QuadraticElement> extends AbstractFieldOver<F, E> {
    protected BigInteger order;
    protected int fixedLengthInBytes;


    public QuadraticField(SecureRandom random, F targetField) {
        super(random, targetField);

        this.order = targetField.getOrder().multiply(targetField.getOrder());

        if (targetField.getLengthInBytes() < 0) {
            fixedLengthInBytes = -1;
        } else {
            fixedLengthInBytes = 2 * targetField.getLengthInBytes();
        }
    }


    public E newElement() {
        return (E) new QuadraticElement(this);
    }

    public BigInteger getOrder() {
        return order;
    }

    public E getNqr() {
        throw new IllegalStateException("Not implemented yet!!!");
    }

    public int getLengthInBytes() {
        return fixedLengthInBytes;
    }
    
}