package de.tu_berlin.pjki_server.game_engine;

public interface Subject {
	
	//observer related functions
	public void registerObserver(Observer o);
	public void unregisterObserver(Observer o) throws Exception;
    public void notifyObservers();
    
}
