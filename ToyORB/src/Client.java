import mathapp.MathOperation;
import toyORB.ToyORB;

public class Client {
    public static void main(String[]args) {
//        CityInfo cityInfo = (CityInfo) ToyORB.getObjectReference("TimisInfo");
//        String roadInfo = cityInfo.getRoadInfo(12);
//        System.out.println("Information is " + roadInfo);

        MathOperation mathOperation = (MathOperation) ToyORB.getObjectReference("MathServer");
        float result = mathOperation.doAdd(10f, 14f);
        System.out.println("Result is " + result);
    }
}