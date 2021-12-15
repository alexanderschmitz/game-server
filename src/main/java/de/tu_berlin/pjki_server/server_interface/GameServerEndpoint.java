package de.tu_berlin.pjki_server.server_interface;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Logger;

import javax.inject.Singleton;
import javax.websocket.CloseReason;
import javax.websocket.CloseReason.CloseCodes;
import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.google.gson.JsonObject;

import de.tu_berlin.pjki_server.game_engine.Game;
import de.tu_berlin.pjki_server.game_engine.Observer;

@ServerEndpoint(
		value="/game",
		encoders = {MessageEncoder.class})
@Singleton
public class GameServerEndpoint implements Observer {
	
	Set<Game> lobby = Collections.synchronizedSet(new HashSet<Game>());
	Set<Session> sessions = Collections.synchronizedSet(new HashSet<Session>());
	private Logger logger = Logger.getLogger(this.getClass().getName());
	private Map<UUID, Session> playerMap; //Map from player UUID to a Session. This way players can quit the session but rejoin his game if he sends his UUID in the request
	
	@OnOpen
	public void onOpen(Session session) {
		logger.info("Connected. Session ID: " + session.getId());
		sessions.add(session);
	}
	
	@OnClose
	public void onClose(Session session, CloseReason closeReason) {
		logger.info(String.format("Session %s closed because of %s", session.getId(), closeReason));
		removeEntriesFromMap(session);
		sessions.remove(session);
	}
	
	@OnMessage
	public String onMessage(String message, Session session) {
		logger.info(String.format("Received following message: %s", message));
		//1. parse incoming message
		//2. determine message type
		//3. execute message query
		//4. answer accordingly
		
		switch (message) {
		case "quit":
			try {
				session.close(new CloseReason(CloseCodes.NORMAL_CLOSURE, "Game ended"));
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			break;
		}
		
		
		return "not implemented";
	}
	
	@Override
	public void update(Game updatedGame) {
		for (UUID playerID: updatedGame.getPlayerList()) {
			Session session = playerMap.get(playerID);
			try {
				session.getBasicRemote().sendObject(updatedGame);
			} catch (IOException | EncodeException e) {
				e.printStackTrace();
			} 
		}
		
	}
	
	
	
	
	/****************************************************************************
	*	utility
	****************************************************************************/
	
	private JsonObject addHeader(JsonObject answer, UUID playerID) {
		JsonObject header = new JsonObject();
		if (playerID == null) {
			playerID = UUID.randomUUID();
		}
		header.addProperty("playerID", playerID.toString());
		answer.add("header", header);
		return answer;
	}
	
	private void removeEntriesFromMap(Session session) {
		Set<UUID> keys = new HashSet<>();
	    for (Entry<UUID, Session> entry : playerMap.entrySet()) {
	        if (entry.getValue().equals(session)) {
	            keys.add(entry.getKey());
	        }
	    }
	    for (UUID key: keys) {
	    	playerMap.remove(key);
	    }
	}
	
}
