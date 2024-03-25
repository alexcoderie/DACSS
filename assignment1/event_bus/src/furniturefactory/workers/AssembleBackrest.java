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
//        EventService.instance().subscribe(this, "onDoneCutSeat", null);
        EventService.instance().subscribe(this, "onAssembleFeet", null);
//        EventService.instance().subscribe(this,"onAssembleStabilizer", null);
    }

//    public void onDoneCutSeat(DoneCutSeat event) {
//        Chair chair = event.chairInProgress;
//        triggerPublication(new DoneAssembleBackrest(chair));
//    }

    public void onAssembleFeet(DoneAssembleFeet event) {
        Chair chair = event.chairInProgress;
        triggerPublication(new DoneAssembleBackrest(chair));
    }

//    public void onAssembleStabilizer(DoneAssembleStabilizer event) {
//        Chair chair = event.chairInProgress;
//        triggerPublication(new DoneAssembleBackrest(chair));
//    }
    public static void triggerPublication(Event event) {
        EventService.instance().publish(event);
    }

}
