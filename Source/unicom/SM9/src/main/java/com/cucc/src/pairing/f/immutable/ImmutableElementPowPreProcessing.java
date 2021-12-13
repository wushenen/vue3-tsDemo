package com.cucc.src.pairing.f.immutable;


import com.cucc.src.api.Element;
import com.cucc.src.api.ElementPowPreProcessing;
import com.cucc.src.api.Field;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 * @since 2.0.0
 */
public class ImmutableElementPowPreProcessing implements ElementPowPreProcessing {

    private ElementPowPreProcessing elementPowPreProcessing;
    private Field immutableField;

    public ImmutableElementPowPreProcessing(ImmutableField immutableField, ElementPowPreProcessing elementPowPreProcessing){
        this.immutableField = immutableField;
        this.elementPowPreProcessing = elementPowPreProcessing;
    }

    public Field getField() {
        return immutableField;
    }

    public Element pow(BigInteger n) {
        return elementPowPreProcessing.pow(n).getImmutable();
    }

    public Element powZn(Element n) {
        return elementPowPreProcessing.powZn(n).getImmutable();
    }

    public byte[] toBytes() {
        return elementPowPreProcessing.toBytes();
    }
}
