/**
 * 
 */
package de.tu_berlin.pjki_server.game_engine;

import java.util.ArrayList;
import java.util.Iterator;

public abstract class Game implements Subject {

	private String state;
	private ArrayList<Observer> observerList;

	public Game(String state) {
		super();
		this.state = state;
		//TODO add Observers
		//this.observerList = observerList;
	}
	
	//observer interface related methods
	@Override
	public void registerObserver(Observer o) {
		observerList.add(o);
		
	}

	@Override
	public void unregisterObserver(Observer o) {
		observerList.remove(observerList.indexOf(o));
		
	}

	@Override
	public void notifyObservers() {
		for (Iterator<Observer> it =
	              observerList.iterator(); it.hasNext();)
	        {
	            Observer o = it.next();
	            o.update(state);
	        }
		
	}

	
	//game logic related methods

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

}
