package filter;

import product.Chair;

public class AssembleFeet implements Filter<Chair>{
    @Override
    public Chair process(Chair chair) {
        chair.assembleFeet();
        return chair;
    }
}
