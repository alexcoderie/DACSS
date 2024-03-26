package furniturefactory.workers;

import eventservice.Event;
import eventservice.EventService;
import eventservice.Subscriber;
import furniturefactory.Chair;
import furniturefactory.events.DoneCutSeat;
import furniturefactory.events.DoneInitChair;

public class CutSeat implements Subscriber {
    public static void triggerPublication(Event event) {
        EventService.instance().publish(event);
    }

    public CutSeat() {
        EventService.instance().subscribe(this, "onInitChair", null, null);
    }

    public void onInitChair(DoneInitChair event) {
        Chair chair = event.chairInProgress;
        triggerPublication(new DoneCutSeat(chair));
    }
}
