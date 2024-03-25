package furniturefactory.workers;

import eventservice.Event;
import eventservice.EventService;
import eventservice.Subscriber;
import furniturefactory.Chair;
import furniturefactory.events.DoneAssembleBackrest;
import furniturefactory.events.DoneAssembleFeet;
import furniturefactory.events.DoneCutSeat;
import furniturefactory.events.DoneInitChair;
import furniturefactory.filters.BackrestAssembledFilter;
import furniturefactory.filters.CutSeatFilter;

public class AssembleFeet implements Subscriber {
    public AssembleFeet() {
        EventService.instance().subscribe(this, "onDoneCutSeat", null);
    }

    public void onDoneCutSeat(DoneCutSeat event) {
        Chair chair = event.chairInProgress;
        triggerPublication(new DoneAssembleFeet(chair));
    }

    public void onAssembleBackrest(DoneAssembleBackrest event) {
        Chair chair = event.chairInProgress;
        triggerPublication(new DoneAssembleFeet(chair));
    }
    public static void triggerPublication(Event event) {
        EventService.instance().publish(event);
    }

}
