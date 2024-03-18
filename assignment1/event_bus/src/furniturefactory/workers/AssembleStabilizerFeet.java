package furniturefactory.workers;

import eventservice.Event;
import eventservice.EventService;
import eventservice.Subscriber;
import furniturefactory.Chair;
import furniturefactory.events.DoneAssembleBackrest;
import furniturefactory.events.DoneAssembleFeet;
import furniturefactory.events.DoneAssembleStabilizer;
import furniturefactory.events.DoneCutSeat;
import furniturefactory.filters.FeetAssembledFilter;

public class AssembleStabilizerFeet implements Subscriber {
    public AssembleStabilizerFeet() {
        EventService.instance().subscribe(DoneAssembleFeet.class, null, this);
        EventService.instance().subscribe(DoneAssembleBackrest.class, new FeetAssembledFilter(), this);
    }

    public static void triggerPublication(Event event) {
        EventService.instance().publish(event);
    }

    @Override
    public void inform(Event event) {
       if(event instanceof DoneAssembleBackrest) {
           Chair chair = ((DoneAssembleBackrest) event).chairInProgress;
           triggerPublication(new DoneAssembleStabilizer(chair));
       }
    }
}
