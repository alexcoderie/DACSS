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
        EventService.instance().subscribe(this, "onAssembleBackrest", null);
    }

    public void onAssembleFeet(DoneAssembleFeet event) {
        Chair chair = event.chairInProgress;
        triggerPublication(new DoneAssembleStabilizer(chair));
    }
    public void onAssembleBackrest(DoneAssembleBackrest event) {
        Chair chair = event.chairInProgress;
        triggerPublication(new DoneAssembleStabilizer(chair));
    }
    public static void triggerPublication(Event event) {
        EventService.instance().publish(event);
    }

}
