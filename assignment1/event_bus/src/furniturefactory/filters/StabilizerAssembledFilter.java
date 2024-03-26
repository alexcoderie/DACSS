package furniturefactory.filters;

import eventservice.Event;
import eventservice.Filter;
import furniturefactory.Chair;
import furniturefactory.events.DoneAssembleBackrest;

public class StabilizerAssembledFilter implements Filter {
    private static int counter = 0;
    public boolean filterAssembleBackrest(DoneAssembleBackrest event) {
        Chair chair =  event.chairInProgress;
        if(chair.isAssembleStabilizer() && counter == 0) {
            counter++;
            return true;
        }

        return false;
    }
}
