package de.tu_berlin.pjki_server.game_engine.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import de.tu_berlin.pjki_server.game_engine.Observer;

@Entity
public abstract class AbstractPlayer implements Observer{
	
	
	private String playerName;
	
	@Id
	@GeneratedValue
	private long playerID;
	
	public AbstractPlayer(String playerName) {
		super();
		this.playerName = playerName;
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public long getPlayerID() {
		return playerID;
	}

	public void setPlayerID(long playerID) {
		this.playerID = playerID;
	}

	@Override
	public String toString() {
		return "Player: %s".formatted(playerName);
	}	
	
	
	
}
