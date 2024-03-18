package furniturefactory.events;

import eventservice.Event;
import furniturefactory.Chair;

public class DonePackage implements Event {
    public Chair chairInProgress;
    public DonePackage(Chair chair) {
        chairInProgress = chair;
        chair.packageChair();
        System.out.println("Done packaging the chair");
    }
}
