package com.qtec.src.pairing.f.accumulator;


import com.qtec.src.api.Element;
import com.qtec.src.api.PairingPreProcessing;
import com.qtec.src.util.concurrent.accumultor.Accumulator;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 * @since 2.0.0
 */
public interface PairingAccumulator extends Accumulator<Element> {

    public PairingAccumulator addPairing(Element e1, Element e2);

    public PairingAccumulator addPairingInverse(Element e1, Element e2);

    public PairingAccumulator addPairing(PairingPreProcessing pairingPreProcessing, Element e2);

}
