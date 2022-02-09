package de.tu_berlin.pjki_server.game_engine;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import de.tu_berlin.pjki_server.game_engine.entities.AbstractPlayer;
import de.tu_berlin.pjki_server.game_engine.entities.Player;
import de.tu_berlin.pjki_server.game_engine.exception.IllegalMoveException;
import de.tu_berlin.pjki_server.game_engine.exception.MaximumPlayerNumberExceededException;
import de.tu_berlin.pjki_server.persistence.Controller;

/**
 * 
 */
@Entity
public abstract class AbstractGame implements Subject, JsonSerializer<AbstractGame>, Cloneable, Serializable {
	
	private static final long serialVersionUID = 1L;

	@Transient
	protected Logger log = Logger.getLogger(this.getClass().getName());
	
	/** The List with the observers subscribed to this game*/
	@Transient
	private List<Observer> observerList;
	
	/** The list of the current players*/
	@OneToMany
	private List<AbstractPlayer> activePlayerList;
	
	/** The unique id of the game*/
	@Id 
	@GeneratedValue
	private long ID; 
	
	@OneToMany
	private List<String> moveHistory;
	
	private int maxPlayerNumber;
	
	@Transient
	private AbstractPlayer currentPlayer;
	
	private AbstractPlayer winner;
	
	@Transient
	private State state;
	
	private State initialState;
	
	private boolean over;
	private boolean draw;
	
	public AbstractGame(State initialState) {
		this.state = initialState;
		this.initialState = initialState;
		maxPlayerNumber = 2;
		winner = null;
		observerList = new ArrayList<Observer>();
		activePlayerList = Collections.synchronizedList(new ArrayList<AbstractPlayer>());
		moveHistory = new ArrayList<>();
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
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
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
	public void addActivePlayer(AbstractPlayer player) throws MaximumPlayerNumberExceededException {
		if (isFull()) {
			throw new MaximumPlayerNumberExceededException("The maximum number of players has been reached.");
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
	
	public abstract void move(AbstractPlayer player, String move) throws IllegalMoveException;
	
	public void executeMove(AbstractPlayer player, String move) throws IllegalMoveException{
		try {
			move(player, move);
			moveHistory.add(move);
		} catch (IllegalMoveException e) {
			throw(e);
		}
		endTurn();
		notifyAllObservers();
		if (checkIfOver()) {
			Controller controller = Controller.getController();
			controller.persistGame(this);
		}
	};	
	
	public abstract boolean checkIfOver();
	
	public abstract boolean checkIfDraw();
	
	public boolean isOver() {
		if (over) {
			return over;
		} else {
			return checkIfOver();
		}
	}
	
//	public void setOver(boolean over) {
//		this.over = over;
//	}
	
	public boolean isDraw() {
		if (draw) {
			return draw;
		} else {
			return checkIfDraw();
		}
	}
	
//	public void setDraw(boolean draw) {
//		this.draw = draw;
//	}
	
	
	/****************************************************************************
	*	general 
	 * @param <T>
	****************************************************************************/

	public abstract AbstractGame getNewInstance();
	
	@Override
	public JsonElement serialize(AbstractGame src, Type typeOfSrc, JsonSerializationContext context) {
		JsonObject jsonGame = new JsonObject();
		jsonGame.addProperty("name", src.getClass().getSimpleName());
		jsonGame.addProperty("id", src.getID());
		
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

	public List<Observer> getObserverList() {
		return observerList;
	}

	public void setObserverList(List<Observer> observerList) {
		this.observerList = observerList;
	}

	public long getID() {
		return ID;
	}

	public void setID(long iD) {
		ID = iD;
	}

	public void setCurrentPlayer(AbstractPlayer currentPlayer) {
		this.currentPlayer = currentPlayer;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}
}
