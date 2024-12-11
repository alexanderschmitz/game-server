package de.tu_berlin.pjki_server.server_interface.packets;

public class Packet_2_CreateGame extends AbstractPacket {

	private String username;
	private String playerID;
	private String gameName;
	
	public Packet_2_CreateGame() {
		super(Type.CREATEGAME);
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

	public String getGameName() {
		return gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}
}
