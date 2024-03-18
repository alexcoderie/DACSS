package furniturefactory.workers;

import eventservice.Event;
import eventservice.EventService;
import eventservice.Subscriber;
import furniturefactory.Chair;
import furniturefactory.events.DoneAssembleBackrest;
import furniturefactory.events.DoneAssembleFeet;
import furniturefactory.events.DoneCutSeat;
import furniturefactory.events.DoneInitChair;
import furniturefactory.filters.CutSeatFilter;

public class AssembleFeet implements Subscriber {
    public AssembleFeet() {
        EventService.instance().subscribe(DoneCutSeat.class, null, this);
        EventService.instance().subscribe(DoneAssembleBackrest.class, new CutSeatFilter(), this);
    }

    public static void triggerPublication(Event event) {
        EventService.instance().publish(event);
    }

    @Override
    public void inform(Event event) {
        if (event instanceof DoneCutSeat) {
            Chair chair = ((DoneCutSeat) event).chairInProgress;
            triggerPublication(new DoneAssembleFeet(chair));
        }
    }
}
