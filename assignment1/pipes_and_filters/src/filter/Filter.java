package filter;

public interface Filter<DATA> {
    DATA process(DATA input);
}
