package com.cucc.src.pairing.f.accumulator;


import com.cucc.src.api.Element;
import com.cucc.src.api.Pairing;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 * @since 2.0.0
 */
public class MultiThreadedMulPairingAccumulator extends AbstractPairingAccumulator {


    public MultiThreadedMulPairingAccumulator(Pairing pairing) {
        super(pairing);
    }

    public MultiThreadedMulPairingAccumulator(Pairing pairing, Element value) {
        super(pairing, value);
    }


    protected void reduce(Element value) {
        this.result.mul(value);
    }
}
