package de.tu_berlin.pjki_server.server_interface;

import java.util.UUID;

class Request {
	private UUID playerID;
	private UUID gameID;
	private Intent intent;
	private String[] move;
		
	enum Intent{
		JOINGAME, GETGAMES, MOVE
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


	public String[] getMove() {
		return move;
	}


	public void setMove(String[] move) {
		this.move = move;
	}	
	
}
