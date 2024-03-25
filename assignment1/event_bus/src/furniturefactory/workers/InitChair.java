package furniturefactory.workers;

import eventservice.Event;
import eventservice.EventService;
import eventservice.Subscriber;
import furniturefactory.events.DoneInitChair;

public class InitChair implements Subscriber {
    public void initChair(DoneInitChair e) {
        EventService.instance().publish(e);
    }
}
