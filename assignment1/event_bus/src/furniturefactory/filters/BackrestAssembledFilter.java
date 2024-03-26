package furniturefactory.filters;

import eventservice.Event;
import eventservice.Filter;
import furniturefactory.Chair;
import furniturefactory.events.DoneAssembleStabilizer;

public class BackrestAssembledFilter implements Filter {
    private static int counter = 0;

    public boolean filterAssembleStabilizer(DoneAssembleStabilizer event) {
            Chair chair =  event.chairInProgress;
            if(chair.isAssembleBackrest() && counter == 0) {
                counter++;
                return true;
            }
        return false;
    }
}
