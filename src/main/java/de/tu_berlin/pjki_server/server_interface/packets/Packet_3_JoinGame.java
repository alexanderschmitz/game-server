package de.tu_berlin.pjki_server.server_interface.packets;

public class Packet_3_JoinGame extends AbstractPacket {

	private String username;
	private String playerID;
	private long gameID;
	
	
	public Packet_3_JoinGame(Type type) {
		super(Type.JOIN);
	}


	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}


	public String getPlayerID() {
		return playerID;
	}


	public void setPlayerID(String playerID) {
		this.playerID = playerID;
	}


	public long getGameID() {
		return gameID;
	}


	public void setGameID(long gameID) {
		this.gameID = gameID;
	}

	
	
}
