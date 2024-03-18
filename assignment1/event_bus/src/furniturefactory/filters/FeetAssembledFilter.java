package furniturefactory.filters;

import eventservice.Event;
import eventservice.Filter;
import furniturefactory.Chair;
import furniturefactory.events.DoneAssembleBackrest;

public class FeetAssembledFilter implements Filter {
    @Override
    public boolean apply(Event event) {
        if (event instanceof DoneAssembleBackrest) {
            Chair chair = ((DoneAssembleBackrest) event).chairInProgress;
            if (chair.isAssembleFeet()) {
                return true;
            }
        }

        return false;
    }
}
