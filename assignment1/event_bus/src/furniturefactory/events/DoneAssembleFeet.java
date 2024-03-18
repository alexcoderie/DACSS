package furniturefactory.events;

import eventservice.Event;
import furniturefactory.Chair;

public class DoneAssembleFeet implements Event {
    public Chair chairInProgress;
    public DoneAssembleFeet(Chair chair) {
        chairInProgress = chair;
        chair.assembleFeet();
        System.out.println("Done assembling feet");
    }
}
