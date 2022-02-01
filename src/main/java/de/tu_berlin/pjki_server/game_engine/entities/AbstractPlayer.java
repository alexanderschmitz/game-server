package de.tu_berlin.pjki_server.game_engine.entities;

import java.lang.reflect.Type;
import java.util.UUID;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import de.tu_berlin.pjki_server.game_engine.Observer;

public abstract class AbstractPlayer implements Observer{

	private String playerName;
	private final UUID playerID = UUID.randomUUID();
	
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

	public UUID getPlayerID() {
		return playerID;
	}	
}
