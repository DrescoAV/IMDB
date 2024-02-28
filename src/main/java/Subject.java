package org.example;

public interface Subject {
    void registerObserver(Observer o);
    void removeObserver(Observer o);
    void removeAllObservers();
    void notifyObservers(String message);

}
