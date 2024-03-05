package filter;

import chair.Chair;

public class PackageChair implements Filter<Chair, Chair> {
    @Override
    public Chair process(Chair chair) {
        if(!chair.isCutSeat() || !chair.isAssembleFeet() || !chair.isAssembleStabilizer() || !chair.isAssembleBackrest()) {
            throw new IllegalArgumentException("Incorrect order: Packaging the step should be the last step");
        }

        chair.packageChair();
        System.out.println("Chair was packaged");
        return chair;
    }
}
