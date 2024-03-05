package filter;

import chair.Chair;

public class AssembleFeet implements Filter<Chair>{
    @Override
    public Chair process(Chair chair) {
        if(!chair.isCutSeat()) {
            throw new IllegalArgumentException("Incorrect order: Assembling the feet should be dome after the seat was cut");
        }
        chair.assembleFeet();
        System.out.println("Feet were assembled");
        return chair;
    }
}
