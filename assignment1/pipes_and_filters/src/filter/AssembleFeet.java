package filter;

public class AssembleFeet implements Filter<Integer, Integer>{
    @Override
    public Integer process(Integer value) {
        if(value != 1 || value != 3) {
            throw new IllegalArgumentException("Incorrect order: Assembling the feet should be dome after the seat was cut");
        }
        return 2;
    }
}
