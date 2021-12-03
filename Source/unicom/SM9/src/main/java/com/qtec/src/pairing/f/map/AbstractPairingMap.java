package com.qtec.src.pairing.f.map;

import com.qtec.src.api.Element;
import com.qtec.src.api.Pairing;
import com.qtec.src.api.PairingPreProcessing;
import com.qtec.src.api.Point;
import com.qtec.src.pairing.f.accumulator.PairingAccumulator;
import com.qtec.src.pairing.f.accumulator.PairingAccumulatorFactory;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public abstract class AbstractPairingMap implements PairingMap {

    protected final Pairing pairing;

    protected AbstractPairingMap(Pairing pairing) {
        this.pairing = pairing;
    }


    public boolean isProductPairingSupported() {
        return false;
    }

    public Element pairing(Element[] in1, Element[] in2) {
        PairingAccumulator combiner = PairingAccumulatorFactory.getInstance().getPairingMultiplier(pairing);
        for(int i = 0; i < in1.length; i++)
            combiner.addPairing(in1[i], in2[i]);
        return combiner.awaitResult();
    }

    public int getPairingPreProcessingLengthInBytes() {
        return pairing.getG1().getLengthInBytes();
    }

    public PairingPreProcessing pairing(final Point in1) {
        return new DefaultPairingPreProcessing(pairing, in1);
    }

    public PairingPreProcessing pairing(byte[] source, int offset) {
        return new DefaultPairingPreProcessing(pairing, pairing.getG1(), source, offset);
    }

    public boolean isAlmostCoddh(Element a, Element b, Element c, Element d) {
        Element t0, t1;

        t0 = pairing((Point) a, (Point) d);
        t1 = pairing((Point) b, (Point) c);

        if (t0.isEqual(t1)) {
            return true;
        } else {
            t0.mul(t1);
            return t0.isOne();
        }
    }


    protected final void pointToAffine(Element Vx, Element Vy, Element z, Element z2, Element e0) {
        // Vx = Vx * z^-2
        Vx.mul(e0.set(z.invert()).square());
        // Vy = Vy * z^-3
        Vy.mul(e0.mul(z));

        z.setToOne();
        z2.setToOne();
    }

}
