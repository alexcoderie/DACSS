package furniturefactory.events;

import eventservice.Event;
import eventservice.EventService;
import furniturefactory.Chair;

public class DoneCutSeat implements Event {
    public Chair chairInProgress;
    public DoneCutSeat(Chair chair) {
        chairInProgress = chair;
        chair.cutSeat();
        System.out.println("Done cutting seat");
    }
}
