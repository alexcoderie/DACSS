import chair.Chair;
import filter.*;

public class Main {
    // TO-DO
    // change filter to take only one generic type
    // make the checking of the order be done by a pipeline manager
    public static void main(String[] args) {
        Chair chair = new Chair();
        var filters = new PipeLine<>(new CutSeat())
                .addFilter(new AssembleFeet())
                .addFilter(new AssembleStabilizerBar())
                .addFilter(new AssembleBackrest())
                .addFilter(new PackageChair());

        filters.execute(chair);
    }
}