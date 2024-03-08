package pipeline;

import filter.*;
import product.Chair;

import java.util.ArrayList;
import java.util.List;

public class ChairPipeLine implements PipeLine<Chair> {
    private List<Filter<Chair>> pipeline;

    public ChairPipeLine() {
        pipeline = new ArrayList<>();
    }

    @Override
    public void addFilter(Filter<Chair> newFilter) {
        pipeline.add(newFilter);
    }

    @Override
    public void execute(Chair chair) {
        for(Filter f : pipeline) {
            if(chair.isCutSeat() && f instanceof CutSeat) {
               throw new IllegalArgumentException("Chair is already cut!");
            } else if(!chair.isCutSeat() && f instanceof AssembleFeet) {
                throw new IllegalArgumentException("Seat needs to be cut before assembling the feet");
            } else if(!chair.isCutSeat() && f instanceof AssembleBackrest) {
                throw new IllegalArgumentException("Seat needs to be cut before assembling the backrest");
            } else if(!chair.isAssembleFeet() && f instanceof AssembleStabilizerBar) {
                throw new IllegalArgumentException("Feet need to be assembled before assembling the stabilizer bar");
            } else if(!f.equals(pipeline.get(pipeline.size() - 1)) && f instanceof PackageChair) {
                throw new IllegalArgumentException("Packaging needs to be the last operation");
            }
            f.process(chair);
        }
    }
}
