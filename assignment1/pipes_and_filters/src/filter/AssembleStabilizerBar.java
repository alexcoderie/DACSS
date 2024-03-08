package filter;

import product.Chair;

public class AssembleStabilizerBar implements Filter<Chair> {
    @Override
    public Chair process(Chair chair) {
        chair.assembleStabilizer();
        return chair;
    }
}
