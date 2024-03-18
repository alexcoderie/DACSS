package furniturefactory.events;

import eventservice.Event;
import furniturefactory.Chair;

public class DoneAssembleStabilizer implements Event {
    public Chair chairInProgress;
    public DoneAssembleStabilizer(Chair chair) {
        chairInProgress = chair;
        chair.assembleStabilizer();
        System.out.println("Done assembling stabilizer bar");
    }
}
