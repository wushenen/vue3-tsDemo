package com.qtec.src.field.base;

import com.qtec.src.api.Element;
import com.qtec.src.api.Point;
import com.qtec.src.api.Vector;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public abstract class AbstractPointElement<E extends Element, F extends AbstractFieldOver> extends AbstractElement<F> implements Point<E>, Vector<E> {

    public static final String NOT_IMPLEMENTED_YET = "Not Implemented yet!";
    protected E  x, y;


    protected AbstractPointElement(F field) {
        super(field);
    }


    public int getSize() {
        return 2;
    }

    public E getAt(int index) {
        return (index == 0) ? x : y;
    }

    public E getX() {
        return x;
    }

    public E getY() {
        return y;
    }


    public int getLengthInBytesCompressed() {
        throw new IllegalStateException(NOT_IMPLEMENTED_YET);
    }

    public byte[] toBytesCompressed() {
        throw new IllegalStateException(NOT_IMPLEMENTED_YET);
    }

    public int setFromBytesCompressed(byte[] source) {
        throw new IllegalStateException(NOT_IMPLEMENTED_YET);
    }

    public int setFromBytesCompressed(byte[] source, int offset) {
        throw new IllegalStateException(NOT_IMPLEMENTED_YET);
    }

    public int getLengthInBytesX() {
        throw new IllegalStateException(NOT_IMPLEMENTED_YET);
    }

    public byte[] toBytesX() {
        throw new IllegalStateException(NOT_IMPLEMENTED_YET);
    }

    public int setFromBytesX(byte[] source) {
        throw new IllegalStateException(NOT_IMPLEMENTED_YET);
    }

    public int setFromBytesX(byte[] source, int offset) {
        throw new IllegalStateException(NOT_IMPLEMENTED_YET);
    }

}
