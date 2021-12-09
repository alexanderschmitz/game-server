package de.tu_berlin.pjki_server.server_interface;

import java.util.UUID;

class Request {
	private UUID playerID;
	private UUID gameID;
	private Intent intent;
		
	private enum Intent{
		JOINGAME, GETGAMES
	}


	public UUID getPlayerID() {
		return playerID;
	}


	public void setPlayerID(UUID userID) {
		this.playerID = userID;
	}


	public Intent getIntent() {
		return intent;
	}


	public void setIntent(Intent intent) {
		this.intent = intent;
	}


	public UUID getGameID() {
		return gameID;
	}


	public void setGameID(UUID gameID) {
		this.gameID = gameID;
	}
	
	
	
}
