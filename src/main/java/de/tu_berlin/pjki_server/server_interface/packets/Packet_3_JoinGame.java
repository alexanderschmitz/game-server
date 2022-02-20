package de.tu_berlin.pjki_server.server_interface.packets;

public class Packet_3_JoinGame extends AbstractPacket {

	private String username;
	private String playerID;
	private int joinAsPlayer;
	private long gameID;
	
	
	public Packet_3_JoinGame() {
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


	public int getJoinAsPlayer() {
		return joinAsPlayer;
	}


	public void setJoinAsPlayer(int joinAsPlayer) {
		this.joinAsPlayer = joinAsPlayer;
	}

	
	
	
}
