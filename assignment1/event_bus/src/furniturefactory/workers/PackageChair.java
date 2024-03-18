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
    public static void triggerPublication(Event event) {

        EventService.instance().publish(event);
    }

    public PackageChair() {
        EventService.instance().subscribe(DoneAssembleBackrest.class, new StabilizerAssembledFilter(), this);
        EventService.instance().subscribe(DoneAssembleStabilizer.class, new BackrestAssembledFilter(), this);
    }
    @Override
    public void inform(Event event) {
        if(event instanceof DoneAssembleStabilizer) {
            Chair chair = ((DoneAssembleStabilizer) event).chairInProgress;
            triggerPublication(new DonePackage(chair));
        }
    }
}
