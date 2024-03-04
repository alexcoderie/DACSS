package filter;

public class AssembleStabilizerBar implements Filter<Integer, Integer> {
    @Override
    public Integer process(Integer value) {
        if(value != 2 || value != 3) {
            throw new IllegalArgumentException("Incorrect order: Assembling the stabilizer bar should be dome after the feet were assembled");
        }
        return 4;
    }
}
