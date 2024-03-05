package filter;

import chair.Chair;

public class CutSeat implements Filter<Chair, Chair>{
    @Override
    public Chair process(Chair chair) {
        if(chair.isCutSeat() == true) {
            throw new IllegalArgumentException("Seat is already cut");
        }

        if(chair.isCutSeat() == false && (chair.isAssembleStabilizer() || chair.isAssembleBackrest() || chair.isAssembleFeet() || chair.isPackageChair())) {
            throw new IllegalArgumentException("Cutting seat should be done first");
        }

        chair.cutSeat();
        System.out.println("Seat was cut");

        return chair;
    }
}
