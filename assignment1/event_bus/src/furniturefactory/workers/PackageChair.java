package furniturefactory.workers;

import eventservice.Event;
import eventservice.EventService;
import eventservice.Subscriber;
import furniturefactory.Chair;
import furniturefactory.events.DoneAssembleBackrest;
import furniturefactory.events.DoneAssembleStabilizer;
import furniturefactory.events.DonePackage;
import furniturefactory.filters.BackrestAssembledFilter;
import furniturefactory.filters.StabilizerAssembledFilter;

public class PackageChair implements Subscriber {
    public PackageChair() {
        EventService.instance().subscribe(this, "onAssembleStabilizer", new BackrestAssembledFilter());
    }

    public void onAssembleBackrest(DoneAssembleBackrest event) {
        Chair chair = event.chairInProgress;
        triggerPublication(new DonePackage(chair));
    }

    public void onAssembleStabilizer(DoneAssembleStabilizer event) {
        Chair chair = event.chairInProgress;
        triggerPublication(new DonePackage(chair));
    }
    public static void triggerPublication(Event event) {

        EventService.instance().publish(event);
    }

}
