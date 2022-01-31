package de.tu_berlin.pjki_server.game_engine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import de.tu_berlin.pjki_server.game_engine.exception.IllegalMoveException;

/**
 * 
 */
public abstract class AbstractGame implements Subject {

	/** The List with the observers subscribed to this game*/
	private List<Observer> observerList;
	
	/** The list of the current players*/
	private List<Player> activePlayerList;
	
	/** The unique id of the game*/
	private final UUID ID = UUID.randomUUID(); 
	
	private int maxPlayerNumber;
	private Player currentPlayer;
	private Player winner;
	public State state;
	
	public AbstractGame() {
		maxPlayerNumber = 2;
		winner = null;
		observerList = new ArrayList<Observer>();
		activePlayerList = Collections.synchronizedList(new ArrayList<Player>());
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
	public void addActivePlayer(Player player) throws Exception {
		if (isFull()) {
			throw new Exception("The maximum number of players has been reached.");
		} else if (activePlayerList.size() == 0) {
			currentPlayer = player;
		}
		activePlayerList.add(player);
		if (isFull()) {
			notifyObservers();
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
	
	// GETTERS AND SETTERS

	public List<Player> getActivePlayerList() {
		return activePlayerList;
	}

	public void setActivePlayerList(List<Player> activePlayerList) {
		this.activePlayerList = activePlayerList;
	}

	public int getMaxPlayerNumber() {
		return maxPlayerNumber;
	}

	public void setMaxPlayerNumber(int maxPlayerNumber) {
		this.maxPlayerNumber = maxPlayerNumber;
	}

	public Player getCurrentPlayer() {
		return currentPlayer;
	}

	public void setCurrentPlayer(Player currentPlayer) {
		this.currentPlayer = currentPlayer;
	}

	public Player getWinner() {
		return winner;
	}

	public void setWinner(Player winner) {
		this.winner = winner;
	}

	public UUID getID() {
		return ID;
	}
	
	
	
}
