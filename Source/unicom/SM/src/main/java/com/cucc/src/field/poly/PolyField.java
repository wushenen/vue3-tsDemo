package com.cucc.src.field.poly;


import com.cucc.src.api.Field;
import com.cucc.src.field.base.AbstractFieldOver;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class PolyField<F extends Field> extends AbstractFieldOver<F, PolyElement> {


    public static final String NOT_IMPLEMENTED_YET = "Not Implemented yet!";

    public PolyField(SecureRandom random, F targetField) {
        super(random, targetField);
    }

    public PolyField(F targetField) {
        super(new SecureRandom(), targetField);
    }


    public PolyElement newElement() {
        return new PolyElement(this);
    }

    public BigInteger getOrder() {
        throw new IllegalStateException(NOT_IMPLEMENTED_YET);
    }

    public PolyElement getNqr() {
        throw new IllegalStateException(NOT_IMPLEMENTED_YET);
    }

    public int getLengthInBytes() {
        throw new IllegalStateException(NOT_IMPLEMENTED_YET);
    }

}
