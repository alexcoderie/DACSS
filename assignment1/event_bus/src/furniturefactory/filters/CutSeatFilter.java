package furniturefactory.filters;

import com.sun.jdi.Field;
import eventservice.Event;
import eventservice.Filter;
import furniturefactory.Chair;
import furniturefactory.events.DoneAssembleBackrest;
import furniturefactory.events.DoneAssembleFeet;

public class CutSeatFilter implements Filter {
    private static int counter = 0;
    public boolean filterAssembleBackrest(DoneAssembleBackrest event) {
        Chair chair =  event.chairInProgress;
        if(chair.isCutSeat() && counter == 0) {
            counter++;
            return true;
        }
        return false;
    }

    public boolean filterAssembleFeet(DoneAssembleFeet event) {
        Chair chair =  event.chairInProgress;
        if(chair.isCutSeat() && counter == 0) {
            counter++;
            return true;
        }

        return false;
    }
}
