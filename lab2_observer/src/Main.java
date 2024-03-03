import java.util.Date;

public class Main {
    public static void main(String[] args) {
        TemperatureSensor sensor1 = new TemperatureSensor(10, 1, "Living Room", new Date(2023, 10, 21), 90.0);
        TemperatureSensor sensor2 = new TemperatureSensor(20, 2, "Kitcher", new Date(2023, 11, 15), 80.0);
        NumericDisplay o1 = new NumericDisplay();
        TextDisplay o2 = new TextDisplay();
        AverageDisplay o3 = new AverageDisplay();
        AverageDisplay o4 = new AverageDisplay();

        sensor1.attach(o1);
        sensor1.attach(o2);
        sensor1.attach(o3);
        sensor1.attach(o4);

        sensor2.attach(o1);
        sensor2.attach(o2);
        sensor2.attach(o3);
        sensor2.attach(o4);

        sensor1.setTemperature(15);
        sensor2.setTemperature(23);

        sensor1.setID(10);
        sensor1.deattach(o4);

        sensor1.deattach(o2);
        sensor1.setTemperature(29);
        sensor1.attach(o4);
        sensor1.setTemperature(10);

    }
}