import java.util.ArrayList;
import java.util.Date;

public class TemperatureSensor implements Subject{

    private int temperature;
    private int ID;
    private String location;
    private Date technicalRevisionDate;
    private double meteorologicalPrecision;
    private ArrayList<Observer> observers;
    public TemperatureSensor(int temperature, int ID, String location, Date technicalRevisionDate, double meteorologicalPrecision) {
        this.temperature = temperature;
        this.ID = ID;
        this.location = location;
        this.technicalRevisionDate = technicalRevisionDate;
        this.meteorologicalPrecision = meteorologicalPrecision;
        observers = new ArrayList<Observer>();
    }

    public int getTemperature() {
        return this.temperature;
    }

    public int getId() {
        return this.ID;
    }

    public String getLocation() {
        return this.location;
    }

    public Date getTechnicalRevisionDate() {
        return this.technicalRevisionDate;
    }

    public double getMeteorologicalPrecision() {
        return this.meteorologicalPrecision;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
        notifyObservers();
    }
    public void setID(int ID) {
        this.ID = ID;
        notifyObservers();
    }

    public void setLocation(String location) {
        this.location = location;
        notifyObservers();
    }

    public void setTechnicalRevisionDate(Date technicalRevisionDate) {
        this.technicalRevisionDate = technicalRevisionDate;
        notifyObservers();
    }

    public void setMeteorologicalPrecision(double meteorologicalPrecision) {
        this.meteorologicalPrecision = meteorologicalPrecision;
        notifyObservers();
    }
    @Override
    public void attach(Observer o) {
        observers.add(o);
    }

    @Override
    public void deattach(Observer o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers() {
        for(Observer o : observers) {
           o.update(this);
        }
    }
}
