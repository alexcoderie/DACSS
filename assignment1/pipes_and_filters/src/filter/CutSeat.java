package filter;

public class CutSeat implements Filter<Integer, Integer>{
    @Override
    public Integer process(Integer value) {
        if(value != 0) {
            throw new IllegalArgumentException("Incorrect order: Cutting the seat should be the first operation");
        }
        return 1;
    }
}
