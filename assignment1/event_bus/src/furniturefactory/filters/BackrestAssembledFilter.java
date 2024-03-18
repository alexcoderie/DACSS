package furniturefactory.filters;

import eventservice.Event;
import eventservice.Filter;
import furniturefactory.Chair;
import furniturefactory.events.DoneAssembleStabilizer;

public class BackrestAssembledFilter implements Filter {
    @Override
    public boolean apply(Event event) {
        if(event instanceof DoneAssembleStabilizer) {
            Chair chair = ((DoneAssembleStabilizer) event).chairInProgress;
            if(chair.isAssembleBackrest()) {
                return true;
            }
        }
        return false;
    }
}
