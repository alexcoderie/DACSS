package filter;

import chair.Chair;

public class AssembleBackrest implements Filter<Chair> {
    @Override
    public Chair process(Chair chair) {
        if(!chair.isCutSeat()) {
            throw new IllegalArgumentException("Incorrect order: Assembling the backrest should be dome after the seat was cut");
        }
        chair.assembleBackrest();
        System.out.println("Backseat was assembled");
        return chair;
    }
}
