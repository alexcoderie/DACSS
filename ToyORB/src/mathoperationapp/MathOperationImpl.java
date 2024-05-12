package mathoperationapp;

public class MathOperationImpl implements MathOperation {
    @Override
    public String doAdd(float a, float b) {
        float result = a + b;
        return String.valueOf(result);
    }

    @Override
    public String doSqr(float a) {
        return String.valueOf(Math.sqrt(a));
    }
}
