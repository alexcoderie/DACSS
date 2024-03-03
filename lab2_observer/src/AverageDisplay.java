import java.util.ArrayList;

public class AverageDisplay implements Observer{
    private int temperature;
    private ArrayList<Integer> pastTemperatures = new ArrayList<Integer>();
    @Override
    public void update(Subject s) {
        if(s instanceof TemperatureSensor) {
            TemperatureSensor tempSensor = (TemperatureSensor) s;
            pastTemperatures.add(((TemperatureSensor) s).getTemperature());

            System.out.println("Temperature: " + ((TemperatureSensor) s).getTemperature());
            System.out.println("ID: " + ((TemperatureSensor) s).getId());
            System.out.println("Location: " + ((TemperatureSensor) s).getLocation());
            System.out.println("Technical Revision date: " + ((TemperatureSensor) s).getTechnicalRevisionDate());
            System.out.println("Meteorological precision: " + ((TemperatureSensor) s).getMeteorologicalPrecision());
            System.out.println("Average temperature: " + pastTemperatures.stream()
                    .mapToDouble(temperature -> temperature)
                    .average()
                    .orElse(0.0)
            );
        }
    }
}
