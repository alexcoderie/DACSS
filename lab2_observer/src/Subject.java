public interface Subject {
    void attach(Observer o);
    void deattach(Observer o);
    void notifyObservers();
}
