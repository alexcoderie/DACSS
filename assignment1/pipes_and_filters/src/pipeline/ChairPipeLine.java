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
        if (newFilter instanceof CutSeat && pipeline.stream().anyMatch(f -> f instanceof CutSeat)) {
            throw new IllegalArgumentException("CutSeat filter already exists in the pipeline");
        }
        if (newFilter instanceof AssembleFeet && !pipeline.stream().anyMatch(f -> f instanceof CutSeat)) {
            throw new IllegalArgumentException("Seat needs to be cut before adding AssembleFeet filter");
        }
        if (newFilter instanceof AssembleBackrest && !pipeline.stream().anyMatch(f -> f instanceof CutSeat)) {
            throw new IllegalArgumentException("Seat needs to be cut before adding AssembleBackrest filter");
        }
        if (newFilter instanceof AssembleStabilizerBar && !pipeline.stream().anyMatch(f -> f instanceof AssembleFeet)) {
            throw new IllegalArgumentException("Feet need to be assembled before adding AssembleStabilizerBar filter");
        }
        if (newFilter instanceof PackageChair) {
            if (pipeline.size() != pipeline.stream().filter(f -> !(f instanceof PackageChair)).count()) {
                throw new IllegalArgumentException("Packaging filter can only be added after all other filters are added");
            }
        } else {
            if (pipeline.stream().anyMatch(f -> f instanceof PackageChair)) {
                throw new IllegalArgumentException("Packaging filter should be added last");
            }
        }

        pipeline.add(newFilter);
    }

    @Override
    public void execute(Chair chair) {
        for(Filter f : pipeline) {
            f.process(chair);
        }
    }
}
