package filter;

import product.Chair;

public class CutSeat implements Filter<Chair>{
    @Override
    public Chair process(Chair chair) {
        chair.cutSeat();
        return chair;
    }
}
