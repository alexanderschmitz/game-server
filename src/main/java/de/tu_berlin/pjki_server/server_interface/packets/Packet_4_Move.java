package de.tu_berlin.pjki_server.server_interface.packets;

public class Packet_4_Move extends AbstractPacket {

	private String username;
	private String playerID;
	private long gameID;
	private String move;
	
	
	public Packet_4_Move(Type type) {
		super(Type.MOVE);
		// TODO Auto-generated constructor stub
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


	public void setGameID(Long gameID) {
		this.gameID = gameID;
	}


	public String getMove() {
		return move;
	}


	public void setMove(String move) {
		this.move = move;
	}
	
	

}
