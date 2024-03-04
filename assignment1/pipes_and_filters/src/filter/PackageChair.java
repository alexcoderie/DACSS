package filter;

public class PackageChair implements Filter<Integer, Integer> {
    @Override
    public Integer process(Integer value) {
        if(value != 4) {
            throw new IllegalArgumentException("Incorrect order: Packaging the step should be the last step");
        }
        return 5;
    }
}
