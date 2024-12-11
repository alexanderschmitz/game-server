package de.tu_berlin.pjki_server.game_engine.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.google.gson.annotations.Expose;

import de.tu_berlin.pjki_server.game_engine.Observer;

@Entity
public abstract class AbstractPlayer implements Observer{
	
	@Expose
	private String playerName;
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="seq")
	@Expose
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
