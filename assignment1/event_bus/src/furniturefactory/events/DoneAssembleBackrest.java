package furniturefactory.events;

import eventservice.Event;
import furniturefactory.Chair;

public class DoneAssembleBackrest implements Event {
    public Chair chairInProgress;
    public DoneAssembleBackrest(Chair chair) {
        chairInProgress = chair;
        chair.assembleBackrest();
        System.out.println("Done assembling backrest");
    }
}
