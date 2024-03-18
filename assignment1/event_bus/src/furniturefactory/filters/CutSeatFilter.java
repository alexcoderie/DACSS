package furniturefactory.filters;

import com.sun.jdi.Field;
import eventservice.Event;
import eventservice.Filter;
import furniturefactory.Chair;
import furniturefactory.events.DoneAssembleBackrest;
import furniturefactory.events.DoneAssembleFeet;

public class CutSeatFilter implements Filter {
    @Override
    public boolean apply(Event event) {
        if(event instanceof DoneAssembleBackrest) {
            Chair chair = ((DoneAssembleBackrest) event).chairInProgress;
            if(chair.isCutSeat()) {
                return true;
            }
        }
        if(event instanceof DoneAssembleFeet) {
            Chair chair = ((DoneAssembleFeet) event).chairInProgress;
            if(chair.isCutSeat()) {
                return true;
            }
        }
        return false;
    }
}
