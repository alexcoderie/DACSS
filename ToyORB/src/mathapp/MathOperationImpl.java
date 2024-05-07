package mathapp;

public class MathOperationImpl implements MathOperation {
    @Override
    public float doAdd(float a, float b) {
        return a + b;
    }

    @Override
    public double doSqr(float a) {
        return Math.sqrt(a);
    }
}
