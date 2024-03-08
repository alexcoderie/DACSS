package filter;

import product.Chair;

public class AssembleBackrest implements Filter<Chair> {
    @Override
    public Chair process(Chair chair) {
        chair.assembleBackrest();
        return chair;
    }
}
