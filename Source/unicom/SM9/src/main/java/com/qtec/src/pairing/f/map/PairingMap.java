package com.qtec.src.pairing.f.map;


import com.qtec.src.api.Element;
import com.qtec.src.api.PairingPreProcessing;
import com.qtec.src.api.Point;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public interface PairingMap {

    Element pairing(Point in1, Point in2);

    boolean isProductPairingSupported();

    Element pairing(Element[] in1, Element[] in2);


    void finalPow(Element element);

    boolean isAlmostCoddh(Element a, Element b, Element c, Element d);


    int getPairingPreProcessingLengthInBytes();

    PairingPreProcessing pairing(Point in1);

    PairingPreProcessing pairing(byte[] source, int offset);


}
