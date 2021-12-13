package com.cucc.src.field.poly;


import com.cucc.src.api.Element;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class ImmutablePolyModElement<E extends Element> extends PolyModElement<E> {

    public static final String CALL_ON_AN_IMMUTABLE_ELEMENT = "Invalid call on an immutable element";

    public ImmutablePolyModElement(PolyModElement<E> element) {
        super(element.getField());

        coefficients.clear();
        for (int i = 0; i < field.n; i++) {
            coefficients.add((E) element.getCoefficient(i).getImmutable());
        }
        this.immutable = true;
    }

    @Override
    public PolyModElement<E> duplicate() {
        return super.duplicate();
    }

    @Override
    public Element getImmutable() {
        return this;
    }

    @Override
    public PolyModElement<E> set(Element e) {
        throw new IllegalStateException(CALL_ON_AN_IMMUTABLE_ELEMENT);
    }

    @Override
    public PolyModElement<E> set(int value) {
        throw new IllegalStateException(CALL_ON_AN_IMMUTABLE_ELEMENT);
    }

    @Override
    public PolyModElement<E> set(BigInteger value) {
        throw new IllegalStateException(CALL_ON_AN_IMMUTABLE_ELEMENT);
    }

    @Override
    public PolyModElement<E> setToRandom() {
        throw new IllegalStateException(CALL_ON_AN_IMMUTABLE_ELEMENT);
    }

    @Override
    public PolyModElement<E> setFromHash(byte[] source, int offset, int length) {
        throw new IllegalStateException(CALL_ON_AN_IMMUTABLE_ELEMENT);
    }

    @Override
    public PolyModElement<E> setToZero() {
        throw new IllegalStateException(CALL_ON_AN_IMMUTABLE_ELEMENT);
    }

    @Override
    public PolyModElement<E> setToOne() {
        throw new IllegalStateException(CALL_ON_AN_IMMUTABLE_ELEMENT);
    }

    @Override
    public PolyModElement<E> map(Element e) {
        throw new IllegalStateException(CALL_ON_AN_IMMUTABLE_ELEMENT);
    }

    @Override
    public PolyModElement<E> twice() {
        return (PolyModElement<E>) super.duplicate().twice().getImmutable();    
    }

    @Override
    public PolyModElement<E> square() {
        return (PolyModElement<E>) super.duplicate().square().getImmutable();    
    }

    @Override
    public PolyModElement<E> invert() {
        return (PolyModElement<E>) super.duplicate().invert().getImmutable();    
    }

    @Override
    public PolyModElement<E> negate() {
        return (PolyModElement<E>) super.duplicate().negate().getImmutable();    
    }

    @Override
    public PolyModElement<E> add(Element e) {
        return (PolyModElement<E>) super.duplicate().add(e).getImmutable();    
    }

    @Override
    public PolyModElement<E> sub(Element e) {
        return (PolyModElement<E>) super.duplicate().sub(e).getImmutable();    
    }

    @Override
    public PolyModElement<E> mul(Element e) {
        return (PolyModElement<E>) super.duplicate().mul(e).getImmutable();    
    }

    @Override
    public PolyModElement<E> mul(int z) {
        return (PolyModElement<E>) super.duplicate().mul(z).getImmutable();    
    }

    @Override
    public PolyModElement<E> mul(BigInteger n) {
        return (PolyModElement<E>) super.duplicate().mul(n).getImmutable();    
    }

    @Override
    public Element pow(BigInteger n) {
        return (PolyModElement<E>) super.duplicate().pow(n).getImmutable();
    }

    @Override
    public PolyModElement<E> powZn(Element e) {
        return (PolyModElement<E>) super.duplicate().powZn(e).getImmutable();    
    }

    @Override
    public PolyModElement<E> sqrt() {
        return (PolyModElement<E>) super.duplicate().sqrt().getImmutable();    
    }

    @Override
    public int setFromBytes(byte[] source) {
        throw new IllegalStateException(CALL_ON_AN_IMMUTABLE_ELEMENT);
    }

    @Override
    public int setFromBytes(byte[] source, int offset) {
        throw new IllegalStateException(CALL_ON_AN_IMMUTABLE_ELEMENT);
    }

    @Override
    public Element halve() {
        return (PolyModElement<E>) super.duplicate().halve().getImmutable();   
    }

    @Override
    public Element div(Element element) {
        return (PolyModElement<E>) super.duplicate().div(element).getImmutable();   
    }

    @Override
    public Element mulZn(Element z) {
        return (PolyModElement<E>) super.duplicate().mulZn(z).getImmutable();   
    }

}
