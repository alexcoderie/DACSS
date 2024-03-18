package furniturefactory.workers;

import eventservice.Event;
import eventservice.EventService;
import eventservice.Subscriber;
import furniturefactory.Chair;
import furniturefactory.events.DoneAssembleBackrest;
import furniturefactory.events.DoneAssembleFeet;
import furniturefactory.events.DoneAssembleStabilizer;
import furniturefactory.events.DoneCutSeat;
import furniturefactory.filters.CutSeatFilter;

public class AssembleBackrest implements Subscriber {
    public AssembleBackrest() {
        EventService.instance().subscribe(DoneCutSeat.class, null, this);
        EventService.instance().subscribe(DoneAssembleFeet.class, new CutSeatFilter(), this);
        EventService.instance().subscribe(DoneAssembleStabilizer.class, null, this);
    }

    public static void triggerPublication(Event event) {
        EventService.instance().publish(event);
    }

    @Override
    public void inform(Event event) {
        if(event instanceof DoneCutSeat) {
            Chair chair = ((DoneCutSeat) event).chairInProgress;
            triggerPublication(new DoneAssembleBackrest(chair));
        }
    }
}
