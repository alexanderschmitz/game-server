package de.tu_berlin.pjki_server.game_engine.entities;

import java.util.UUID;

import javax.websocket.Session;

import de.tu_berlin.pjki_server.game_engine.AbstractGame;
import de.tu_berlin.pjki_server.server_interface.GameServerEndpoint;

public class Player extends AbstractPlayer{

	private Session session;
	private GameServerEndpoint serverEndpoint;
	
	public Player(String userName, Session session, UUID playerID, GameServerEndpoint serverEndpoint) {
		super(userName);
		this.serverEndpoint = serverEndpoint;
		this.session = session;
	}
	
	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	@Override
	public String toString() {
		return "Player [userName=" + getPlayerName() + ", session=" + session + ", playerID=" + getPlayerID() + "]";
	}

	@Override
	public void update(AbstractGame game) {
		if (serverEndpoint != null) {
			serverEndpoint.update(this, game);
		}
	}
	
}