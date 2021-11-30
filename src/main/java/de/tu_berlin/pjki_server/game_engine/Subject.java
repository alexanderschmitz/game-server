package de.tu_berlin.pjki_server.game_engine;

import de.tu_berlin.pjki_server.game_engine.exception.IllegalMoveException;

public interface Subject {
	
	//observer related functions
	public void registerObserver(Observer o);
	public void unregisterObserver(Observer o) throws Exception;
    public void notifyObservers();
	
	//game related functions
	public void setup(String[] args);
	public void move(String[] args) throws IllegalMoveException;	
	public boolean isOver(String[] args);
	
	

}
