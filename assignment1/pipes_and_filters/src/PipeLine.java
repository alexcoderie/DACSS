import filter.Filter;

public class PipeLine<DATA> {
    private final Filter<DATA> currentFilter;

    public PipeLine(Filter<DATA> currentFilter) {
        this.currentFilter = currentFilter;
    }

    PipeLine<DATA> addFilter(Filter<DATA> newFilter) {
        return new PipeLine<>(input -> newFilter.process(currentFilter.process(input)));
    }

    DATA execute(DATA input) {
        return currentFilter.process(input);
    }
}
