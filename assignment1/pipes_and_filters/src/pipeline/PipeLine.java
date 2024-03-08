package pipeline;

import filter.Filter;

public interface PipeLine<DATA> {
    void addFilter(Filter<DATA> filter);
    void execute(DATA data);
}
