public class TextDisplay implements Observer{
    @Override
    public void update(Subject s) {
        if(s instanceof TemperatureSensor) {
            TemperatureSensor tempSensor = (TemperatureSensor) s;
            System.out.println(tempSensor.getTemperature() >= 20 ? "Warm" : "Cold");
            System.out.println(tempSensor.getLocation());
        }
    }
}
