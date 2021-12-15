package de.tu_berlin.pjki_server.server_interface;

import java.io.IOException;
import java.util.UUID;
import java.util.logging.Logger;

import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.CloseReason.CloseCodes;
import javax.websocket.server.ServerEndpoint;

import com.google.gson.JsonObject;

import de.tu_berlin.pjki_server.game_engine.Game;
import de.tu_berlin.pjki_server.game_engine.Observer;

@ServerEndpoint(value="/database")
public class DatabaseEndpoint implements Observer{

private Logger logger = Logger.getLogger(this.getClass().getName());
	
	@OnOpen
	public void onOpen(Session session) {
		logger.info("Connected. Session ID: " + session.getId());
	}
	
	@OnClose
	public void onClose(Session session, CloseReason closeReason) {
		logger.info(String.format("Session %s closed because of %s", session.getId(), closeReason));
	}
	
	@OnMessage
	public String onMessage(String message, Session session) {
		JsonObject answer = new JsonObject();
		JsonObject body = new JsonObject();
		
		
		switch (message) {
		case "quit":
			try {
				session.close(new CloseReason(CloseCodes.NORMAL_CLOSURE, "Game ended"));
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			break;
		}
		
		answer = addHeader(answer, UUID.randomUUID());
		
		return "Database Endpoint";
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

	@Override
	public void update(Game game) {
		// TODO Auto-generated method stub
		
	}
	
}
