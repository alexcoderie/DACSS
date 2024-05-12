import cityinfoapp.CityInfoImpl;
import mathoperationapp.MathOperationImpl;
import toyORB.ToyORB;

public class Server {
    public static void main(String args[]) {
//        CityInfoImpl cityInfo = new CityInfoImpl();
//        ToyORB.register("TimisInfo", cityInfo, 1111);

        MathOperationImpl mathOperation = new MathOperationImpl();
        ToyORB.register("MathServer", mathOperation, 1112);
    }
}