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
        EventService.instance().subscribe(DoneInitChair.class, null, this);
    }

    @Override
    public void inform(Event event) {
        if(event instanceof DoneInitChair) {
            Chair chair = ((DoneInitChair) event).chairInProgress;
            DoneCutSeat doneCutSeat = new DoneCutSeat(chair);
            triggerPublication(doneCutSeat);
        }
    }
}
