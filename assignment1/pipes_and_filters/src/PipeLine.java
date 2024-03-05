import filter.Filter;

public class PipeLine<INPUT, OUTPUT> {
    private final Filter<INPUT, OUTPUT> currentFilter;

    public PipeLine(Filter<INPUT, OUTPUT> currentFilter) {
        this.currentFilter = currentFilter;
    }

    <K> PipeLine<INPUT, K> addFilter(Filter<OUTPUT, K> newFilter) {
        return new PipeLine<>(input -> newFilter.process(currentFilter.process(input)));
    }

    OUTPUT execute(INPUT input) {
        return currentFilter.process(input);
    }
}
