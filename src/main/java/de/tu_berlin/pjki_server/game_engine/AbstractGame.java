package de.tu_berlin.pjki_server.game_engine;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.annotations.Expose;

import de.tu_berlin.pjki_server.game_engine.entities.AbstractPlayer;
import de.tu_berlin.pjki_server.game_engine.entities.Player;
import de.tu_berlin.pjki_server.game_engine.exception.IllegalMoveException;

/**
 * 
 */
public abstract class AbstractGame implements Subject, JsonSerializer<AbstractGame> {

	@Expose(serialize = false)
	protected Logger log = Logger.getLogger(this.getClass().getName());
	
	/** The List with the observers subscribed to this game*/
	@Expose(serialize = false)
	private List<Observer> observerList;
	
	/** The list of the current players*/
	//TODO: serialize the playerList to a List of the player names
	@Expose(serialize = false)
	private List<AbstractPlayer> activePlayerList;
	
	/** The unique id of the game*/
	@Expose(serialize = false)
	private final UUID ID = UUID.randomUUID(); 
	
	@Expose(serialize = false)
	private int maxPlayerNumber;
	@Expose(serialize = false)
	private AbstractPlayer currentPlayer;
	@Expose(serialize = false)
	private AbstractPlayer winner;
	@Expose(serialize = false)
	public State state;
	
	public AbstractGame() {
		maxPlayerNumber = 2;
		winner = null;
		observerList = new ArrayList<Observer>();
		activePlayerList = Collections.synchronizedList(new ArrayList<AbstractPlayer>());
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
	public void notifyAllObservers() {
		for (Observer o: observerList){
			o.update(this);
		}
		
	}
	
	@Override
	public void notifyObserver(Observer o) {
		if (observerList.contains(o)) {
			o.update(this);
		}
		
	}
	
	@Override
	public void notifyObservers(List<Observer> o) {
		for (Observer observer: o) {
			notifyObserver(observer);
		}
		
	}


	/****************************************************************************
	*	game logic related methods
	****************************************************************************/


	/**
	 * Ends the turn of current player and changes "currentPlayer" to the next player.
	 * For custom behaviour, override this method.
	 */
	public void endTurn() {
		int indexOfCurrentPlayer = activePlayerList.indexOf(currentPlayer);
		if (indexOfCurrentPlayer >= maxPlayerNumber - 1) {
			currentPlayer = activePlayerList.get(0);
		} else {
			currentPlayer = activePlayerList.get(indexOfCurrentPlayer + 1);
		}
	}
	
	/**
	 * Checks if the number of active players is equal to the maximum number of players.
	 * @return true if equal, false otherwise
	 */
	public boolean isFull() {
		return (activePlayerList.size() >= maxPlayerNumber);
	}

	/**
	 * Adds a player to the active player list.
	 * Notifies observers if the list is full after adding the player.
	 * @param Player, the player to be added
	 * @throws Exception if the list is already full 
	 */
	public void addActivePlayer(AbstractPlayer player) throws Exception {
		if (isFull()) {
			throw new Exception("The maximum number of players has been reached.");
		} else if (activePlayerList.size() == 0) {
			currentPlayer = player;
		}
		activePlayerList.add(player);
		if (isFull()) {
			notifyAllObservers();
		}
	}
	
	public void removeActivePlayer(Player player) {
		activePlayerList.remove(player);
	}
	
	public abstract void move(String move) throws IllegalMoveException;	
	
	public abstract boolean isOver();
	
	public abstract boolean isDraw();
	
	/****************************************************************************
	*	general 
	****************************************************************************/

	public abstract AbstractGame getNewInstance();

	//public abstract String toJson();
	
	@Override
	public JsonElement serialize(AbstractGame src, Type typeOfSrc, JsonSerializationContext context) {
		JsonObject jsonGame = new JsonObject();
		jsonGame.addProperty("name", src.getClass().getSimpleName());
		jsonGame.addProperty("id", src.getID().toString());
		
		JsonArray players = new JsonArray();
		for (AbstractPlayer player: activePlayerList) {
			players.add(player.getPlayerName());
		}
		jsonGame.add("players", players);
		JsonElement jsonState = new Gson().toJsonTree(state);
		jsonGame.add("state", jsonState);
		
		return jsonGame;
	}	
	
	// GETTERS AND SETTERS

	public List<AbstractPlayer> getActivePlayerList() {
		return activePlayerList;
	}

	public void setActivePlayerList(List<AbstractPlayer> activePlayerList) {
		this.activePlayerList = activePlayerList;
	}

	public int getMaxPlayerNumber() {
		return maxPlayerNumber;
	}

	public void setMaxPlayerNumber(int maxPlayerNumber) {
		this.maxPlayerNumber = maxPlayerNumber;
	}

	public AbstractPlayer getCurrentPlayer() {
		return currentPlayer;
	}

	public void setCurrentPlayer(Player currentPlayer) {
		this.currentPlayer = currentPlayer;
	}

	public AbstractPlayer getWinner() {
		return winner;
	}

	public void setWinner(AbstractPlayer abstractPlayer) {
		this.winner = abstractPlayer;
	}

	public UUID getID() {
		return ID;
	}
	
	
	
}
