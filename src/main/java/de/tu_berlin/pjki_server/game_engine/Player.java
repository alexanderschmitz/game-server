package de.tu_berlin.pjki_server.game_engine;

import java.util.UUID;

import javax.websocket.Session;

public class Player{

	private String userName;
	private Session session;
	private UUID playerID;
	private UUID gameID;
	
	public Player(String userName, Session session, UUID playerID) {
		super();
		this.userName = userName;
		this.session = session;
		this.playerID = playerID;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	public UUID getGameID() {
		return gameID;
	}

	public void setGameID(UUID gameID) {
		this.gameID = gameID;
	}

	public UUID getPlayerID() {
		return playerID;
	}

	@Override
	public String toString() {
		return "Player [userName=" + userName + ", session=" + session + ", playerID=" + playerID + ", gameID=" + gameID
				+ "]";
	}
	
	
}
