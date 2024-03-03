public class NumericDisplay implements Observer{
    @Override
    public void update(Subject s) {
        if(s instanceof TemperatureSensor) {
            System.out.println( "Location: " + ((TemperatureSensor) s).getLocation());
            System.out.println( "Meteorological precision" + ((TemperatureSensor) s).getMeteorologicalPrecision());
            System.out.println("Temperature" + ((TemperatureSensor) s).getTemperature());
        }
    }
}
