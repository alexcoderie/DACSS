package filter;

public interface Filter<INPUT, OUTPUT> {
    OUTPUT process(INPUT input);
}
