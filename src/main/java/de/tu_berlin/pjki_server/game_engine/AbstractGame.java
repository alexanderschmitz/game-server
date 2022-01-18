package de.tu_berlin.pjki_server.game_engine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 
 */
public abstract class AbstractGame implements Subject {

	private Map<String, String> state;
	private ArrayList<Observer> observerList;
	public final UUID ID = UUID.randomUUID(); 
	private List<UUID> activePlayerList;
	private List<UUID> registeredPlayerList;
	
	/**
	 * Constructor for Game.
	 * Creates the state attribute. The state stores all critical information about a game in a Map.
	 * Standard entries are maxPlayerNumber, currentPlayer, draw, winner. (All Strings)	 * 
	 */
	public AbstractGame() {
		state = new HashMap<>();
		state.put("maxPlayerNumber", "2");
		state.put("currentPlayer", "0");
		state.put("draw", Boolean.toString(false));
		state.put("winner", null);
		observerList = new ArrayList<Observer>();
		activePlayerList = Collections.synchronizedList(new ArrayList<UUID>());
		registeredPlayerList = Collections.synchronizedList(new ArrayList<UUID>());
	}
		
	/****************************************************************************
	*	observer interface related methods
	****************************************************************************/
	
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
		for (Observer o: observerList){
			o.update(this);
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
	
	public boolean isFull() {
		int maxPlayers = Integer.parseInt(state.get("maxPlayerNumber"));
		return (activePlayerList.size() >= maxPlayers);
	}
	
	public List<UUID> getPlayerList() {
		return activePlayerList;
	}
	
	public void addActivePlayer(UUID playerID) {
		activePlayerList.add(playerID);
		if (isFull()) {
			notifyObservers();
		}
	}
	
	public void removeActivePlayer(UUID playerID) {
		activePlayerList.remove(playerID);
	}
	
	public UUID getCurrentPlayer() {
		int index = Integer.parseInt(getValue("currentPlayer"));
		return activePlayerList.get(index);
	}
		
	/****************************************************************************
	*	general 
	****************************************************************************/

	public abstract AbstractGame getNewInstance();
	
	public void registerPlayer(UUID playerID) {
		registeredPlayerList.add(playerID);
	}
	
	public void unregisterPlayer(UUID playerID) {
		registeredPlayerList.remove(playerID);
	}

	
}
