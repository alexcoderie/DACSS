package filter;

public class AssembleBackrest implements Filter<Integer, Integer> {
    @Override
    public Integer process(Integer value) {
        if(value != 1 || value != 2) {
            throw new IllegalArgumentException("Incorrect order: Assembling the backrest should be dome after the seat was cut");
        }
        return 3;
    }
}
