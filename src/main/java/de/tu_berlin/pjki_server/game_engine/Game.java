/**
 * 
 */
package de.tu_berlin.pjki_server.game_engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public abstract class Game implements Subject {

	private Map<String, String> state;
	private ArrayList<Observer> observerList;
	
	public Game() {
		state = new HashMap<>();
		state.put("maxPlayerNumber", "2");
		state.put("currentPlayer", "0");
		state.put("draw", Boolean.toString(false));
		state.put("winner", null);
		observerList = new ArrayList<Observer>();
	}

	//observer interface related methods
	@Override
	public void registerObserver(Observer o) {
		observerList.add(o);
		
	}

	@Override
	public void unregisterObserver(Observer o) throws Exception {
		if (!observerList.remove(o)) {
			throw new Exception("Observer not in ObserverList");
		}
		
	}

	@Override
	public void notifyObservers() {
		for (Iterator<Observer> it = observerList.iterator(); it.hasNext();){
			Observer o = it.next();
			o.update(state);
		}

	}

	/****************************************************************************
	*	game logic related methods
	****************************************************************************/

	public Map<String, String> getState() {
		return state;
	}

	public void setState(Map<String, String> state) {
		this.state = state;
	}

	public void setValue(String key, String value) {
		state.put(key, value);
	}
	
	public String getValue(String key) {
		return state.get(key);
	}
	
	public void endTurn() {
		int currentPlayer = Integer.parseInt(state.get("currentPlayer"));
		if (currentPlayer+1 >= Integer.parseInt(state.get("maxPlayerNumber"))) {
			setValue("currentPlayer", "0");
		} else {
			setValue("currentPlayer", Integer.toString(currentPlayer + 1));
		}
	}
}
