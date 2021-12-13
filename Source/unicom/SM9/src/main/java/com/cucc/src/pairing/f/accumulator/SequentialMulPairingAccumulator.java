package com.cucc.src.pairing.f.accumulator;

import com.cucc.src.api.Element;
import com.cucc.src.api.Pairing;
import com.cucc.src.api.PairingPreProcessing;
import com.cucc.src.util.concurrent.Pool;
import com.cucc.src.util.concurrent.accumultor.Accumulator;

import java.util.concurrent.Callable;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 * @since 2.0.0
 */
public class SequentialMulPairingAccumulator implements PairingAccumulator {

    public static final String NOT_SUPPORTED = "Not supported!!!";
    private Pairing pairing;
    private Element value;


    public SequentialMulPairingAccumulator(Pairing pairing) {
        this.pairing = pairing;
        this.value = pairing.getGT().newOneElement();
    }

    public SequentialMulPairingAccumulator(Pairing pairing, Element value) {
        this.pairing = pairing;
        this.value = value;
    }

    public Accumulator<Element> accumulate(Callable<Element> callable) {
        throw new IllegalStateException(NOT_SUPPORTED);
    }

    public Accumulator<Element> awaitTermination() {
        return this;
    }

    public Element getResult() {
        return value;
    }

    public Pool submit(Callable<Element> callable) {
        throw new IllegalStateException(NOT_SUPPORTED);
    }

    public Pool submit(Runnable runnable) {
        throw new IllegalStateException(NOT_SUPPORTED);
    }

    public PairingAccumulator addPairing(Element e1, Element e2) {
        value.mul(pairing.pairing(e1, e2));

        return this;
    }

    public PairingAccumulator addPairing(PairingPreProcessing pairingPreProcessing, Element e2) {
        value.mul(pairingPreProcessing.pairing(e2));

        return this;
    }

    public PairingAccumulator addPairingInverse(Element e1, Element e2) {
        value.mul(pairing.pairing(e1, e2).invert());

        return this;
    }

    public Element awaitResult(){
        return value;
    }

}
