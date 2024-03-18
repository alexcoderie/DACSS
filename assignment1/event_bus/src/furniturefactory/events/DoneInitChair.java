package furniturefactory.events;

import eventservice.Event;
import furniturefactory.Chair;

public class DoneInitChair implements Event {
    public Chair chairInProgress;

    public DoneInitChair() {
        chairInProgress = new Chair();
        System.out.println("Chair initialized");
    }
}
