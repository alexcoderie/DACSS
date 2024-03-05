package filter;

import chair.Chair;

public class AssembleStabilizerBar implements Filter<Chair> {
    @Override
    public Chair process(Chair chair) {
        if(!chair.isAssembleFeet()) {
            throw new IllegalArgumentException("Incorrect order: Assembling the stabilizer bar should be dome after the feet were assembled");
        }

        chair.assembleStabilizer();
        System.out.println("Stabilizer bar was assembled");
        return chair;
    }
}
