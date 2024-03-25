import eventservice.EventService;
import furniturefactory.events.DoneInitChair;
import furniturefactory.workers.*;

public class Main {
    private static CutSeat cutSeat;
    private static AssembleFeet assembleFeet;
    private static AssembleBackrest assembleBackrest;
    private static AssembleStabilizerFeet assembleStabilizerFeet;
    private static PackageChair packageChair;
    public static void main(String[] args) {
        cutSeat = new CutSeat();
//        assembleFeet = new AssembleFeet();
//        assembleBackrest = new AssembleBackrest();
//        assembleStabilizerFeet = new AssembleStabilizerFeet();
//        packageChair = new PackageChair();

        EventService.instance().publish(new DoneInitChair());
    }
}