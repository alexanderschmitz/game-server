package de.tu_berlin.pjki_server.server_interface;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
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
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import de.tu_berlin.pjki_server.TicTacToeExample;
import de.tu_berlin.pjki_server.game_engine.Game;
import de.tu_berlin.pjki_server.game_engine.Observer;

@ServerEndpoint(value="/game")
@Singleton
public class GameServerEndpoint implements Observer {
	
	static Set<Game> lobby = Collections.synchronizedSet(new HashSet<Game>());
	static Set<Session> sessions = Collections.synchronizedSet(new HashSet<Session>());
	private Logger logger = Logger.getLogger(this.getClass().getName());
	static Map<UUID, Session> playerMap = Collections.synchronizedMap(new HashMap<UUID, Session>()); //Map from player UUID to a Session. This way players can quit the session but rejoin his game if he sends his UUID in the request
	
	@OnOpen
	public void onOpen(Session session) {
		logger.info("Connected. Session ID: " + session.getId());
		sessions.add(session);
		
		//rejoining
		//NOTE maybe rejoining has to be handled in @OnMessage and not on first connection
//		for (Game game: lobby) {
//			if (game.getPlayerList().contains(UUIDfromJSON)) {
//				playerMap.put(UUIDfromJSON, session);
//				logger.info(String.format("Player %s rejoined game %s.", UUIDfromJSON, game.ID));
//			}
//		}
//		
		// PROTOTYPING:
		joinGame(UUID.randomUUID(), session);
		
	}

	@OnClose
	public void onClose(Session session, CloseReason closeReason) {
		logger.info(String.format("Session %s closed because of %s", session.getId(), closeReason));
		removeEntriesFromMap(session);
		sessions.remove(session);
	}
	
	@OnMessage
	public String onMessage(String message, Session session) {
		Message parsedMessage = new Gson().fromJson(message, Message.class);
		logger.info("Parsed message: " + parsedMessage.header.playerID);
		UUID playerID = UUID.fromString(parsedMessage.header.playerID);
		UUID gameID = UUID.fromString(parsedMessage.game.id);
		String move = parsedMessage.game.move;
		
		
		return "not implemented";
	}
	
	@OnError
	public void onError(Session session, Throwable t) {
		t.printStackTrace();
		logger.warning("Server error: " + t.getMessage());
		try {
			session.getBasicRemote().sendText("ERROR");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void update(Game updatedGame) {
		for (UUID playerID: updatedGame.getPlayerList()) {
			logger.info("Player to update: " + playerID.toString());
			Session session = playerMap.get(playerID);
			try {
				Gson gson = new Gson();
				JsonObject answer = new JsonObject();
				answer = addHeader(answer, playerID);
				answer = addBody(answer, updatedGame, playerID);
				logger.info("Server answer: " + gson.toJson(answer));
				session.getBasicRemote().sendText(gson.toJson(answer));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
		
	/****************************************************************************
	*	utility
	****************************************************************************/
	
	private void joinGame(UUID playerID, Session session) {
		Game gameToJoin = null;
		for (Game game: lobby) {
			if (!game.isFull()) {
				gameToJoin = game;
			}
		}
		if (gameToJoin == null) {
			gameToJoin = new TicTacToeExample();
			logger.info("Created new Game: " + gameToJoin.ID.toString());
			lobby.add(gameToJoin);
			gameToJoin.registerObserver(this);
		}
		playerMap.put(playerID, session);
		gameToJoin.addActivePlayer(playerID);
		
		int fullGames = lobby.stream().map(x -> (x.isFull() ? 1 : 0)).reduce(0, Integer::sum);
		logger.info(String.format("Of %s games in the Lobby, there are %s full games ", lobby.size(), fullGames));	
	}
	
	private JsonObject addHeader(JsonObject answer, UUID playerID) {
		JsonObject header = new JsonObject();
		header.addProperty("playerID", playerID.toString());
		answer.add("header", header);
		return answer;
	}
	
	private JsonObject addBody(JsonObject answer, Game game, UUID playerID) {
		JsonObject body = new JsonObject();
		boolean yourTurn = false;
		if (playerID == game.getCurrentPlayer()) {
			yourTurn = true;
		}
		body.addProperty("id", game.ID.toString());
		body.addProperty("yourTurn", yourTurn);
		body.add("state", stateToJsonObject(game.getState()));
		answer.add("game", body);
		return answer;
	}
	
	private JsonObject stateToJsonObject(Map<String, String> state) {
		JsonObject jsonMap = new JsonObject();
		for (Entry<String, String> entry: state.entrySet()) {
			jsonMap.addProperty(entry.getKey(), entry.getValue());
		}
		return jsonMap;
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
	
//	private void parseMessage(String message) {
//		Json json = new Gson().toJson(message);
//	}
	
}
