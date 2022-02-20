package de.tu_berlin.pjki_server.server_interface.packets;

public class Packet_5_Database extends AbstractPacket {

	private int gameCount;
	private long gameID;
	private String gameType;
	
	
	public Packet_5_Database() {
		super(Type.DATABASE);
	}


	public int getGameCount() {
		return gameCount;
	}


	public void setGameCount(int gameCount) {
		this.gameCount = gameCount;
	}


	public long getGameID() {
		return gameID;
	}


	public void setGameID(long gameID) {
		this.gameID = gameID;
	}


	public String getGameType() {
		return gameType;
	}


	public void setGameType(String gameType) {
		this.gameType = gameType;
	}

	
	
}
