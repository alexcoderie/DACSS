import pipeline.PipeLine;
import product.Chair;
import filter.*;
import pipeline.ChairPipeLine;

public class Main {
    // TO-DO
    // make the checking of the order be done by a pipeline manager
    public static void main(String[] args) {
        Chair chair = new Chair();
        PipeLine chairPipeLine = new ChairPipeLine();

        chairPipeLine.addFilter(new CutSeat());
        chairPipeLine.addFilter(new AssembleStabilizerBar());
        chairPipeLine.addFilter(new AssembleFeet());
        chairPipeLine.addFilter(new AssembleBackrest());
        chairPipeLine.addFilter(new PackageChair());

        chairPipeLine.execute(chair);

    }
}