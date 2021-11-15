package de.tu_berlin.pjki_server.game_engine;

public interface Subject {
	
	//observer related functions
	public void registerObserver(Observer o);
	public void unregisterObserver(Observer o);
    public void notifyObservers();
	
	//game related functions
	public void setup(String[] args);
	public String move(String[] args);	
	public boolean isOver(String[] args);
	
	

}
