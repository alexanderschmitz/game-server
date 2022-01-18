package de.tu_berlin.pjki_server.server_interface.packets;

public class Packet_0_Login extends AbstractPacket{

	private String username;
	private String playerID;
	
	public Packet_0_Login() {
		super(Type.LOGIN);
		
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
	
	
	
	

}
