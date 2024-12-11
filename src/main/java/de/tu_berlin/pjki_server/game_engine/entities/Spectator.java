package de.tu_berlin.pjki_server.game_engine.entities;

import javax.websocket.Session;
import de.tu_berlin.pjki_server.game_engine.AbstractGame;
import de.tu_berlin.pjki_server.server_interface.GameServerEndpoint;

public class Spectator extends AbstractPlayer {
	
	private Session session;
	private GameServerEndpoint serverEndpoint;

	public Spectator(String userName, Session session, GameServerEndpoint serverEndpoint) {
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
	public void update(AbstractGame game) {
		if (serverEndpoint != null) {
			serverEndpoint.update(this, game);
		}
	}	
}
