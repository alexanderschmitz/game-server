package de.tu_berlin.pjki_server.game_engine;

import java.util.List;

public interface Subject {
	
	//observer related functions
	public void registerObserver(Observer o);
	public void unregisterObserver(Observer o) throws Exception;
    public void notifyAllObservers();
    public void notifyObservers(List<Observer> o);
    public void notifyObserver(Observer o);
    
}
