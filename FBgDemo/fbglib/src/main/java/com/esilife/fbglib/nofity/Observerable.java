package com.esilife.fbglib.nofity;


public interface Observerable {
    public void registerOnlyOneObserver(Observer o);
    public void removeOnlyOneObserver();
    public void registerObserver(Observer o);
    public void removeObserver(Observer o);
    public void notifyObserver();
}
