package de.tu_berlin.pjki_server.server_interface;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.UUID;
import java.util.logging.Logger;

import javax.inject.Singleton;
import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import de.tu_berlin.pjki_server.game_engine.AbstractGame;
import de.tu_berlin.pjki_server.game_engine.Manager;
import de.tu_berlin.pjki_server.game_engine.Observer;
import de.tu_berlin.pjki_server.game_engine.Player;
import de.tu_berlin.pjki_server.game_engine.exception.IllegalMoveException;
import de.tu_berlin.pjki_server.server_interface.packets.AbstractPacket;
import de.tu_berlin.pjki_server.server_interface.packets.AbstractPacket.Type;
import de.tu_berlin.pjki_server.server_interface.packets.Packet_0_Login;
import de.tu_berlin.pjki_server.server_interface.packets.Packet_3_JoinGame;
import de.tu_berlin.pjki_server.server_interface.packets.Packet_4_Move;

@ServerEndpoint(value="/game")
@Singleton
public class GameServerEndpoint implements Observer {
	
	
	private Logger logger = Logger.getLogger(this.getClass().getName());
	private static Manager manager = Manager.getManager();
	
	@OnMessage
	public String onMessage(String message, Session session) {
		
		JsonObject jsonObject; 
		try {
			jsonObject = JsonParser.parseString(message).getAsJsonObject();
		} catch (Exception e) {
			return e.getLocalizedMessage();
		}
		logger.info(new Gson().toJson(jsonObject));
		Type packetType = AbstractPacket.getPacketType(jsonObject.get("type").getAsString());
		Gson gson = new Gson();
		switch (packetType) {
		case INVALID:
			return "invalid packet id";
		case LOGIN:
			return doLogin(session, message);			
		case GETGAMES:
			return gson.toJson(manager.getLobby());
		case CREATEGAME:
			if (manager.getPlayerBySession(session) == null) {
				return "Please login (packetID = 0) before creating a game";
			}
			String gameName = "TicTacToeExample";
			AbstractGame newGame;
			try {
				newGame = manager.addGameToLobby(gameName);
				return gson.toJson(newGame);
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException | SecurityException e) {
				return e.getLocalizedMessage();
			}
			
			
		case JOIN:
			Player player = manager.getPlayerBySession(session);
			if (player == null) {
				return "Please login (packetID = 0) before joining a game";
			}
			Packet_3_JoinGame parsedPacket_3 = gson.fromJson(message, Packet_3_JoinGame.class);
			UUID gameID = UUID.fromString(parsedPacket_3.getGameID());
			AbstractGame game = manager.getGameByID(gameID);
			player.setGameID(gameID);
			game.addActivePlayer(player.getPlayerID());
			return gson.toJson(game);
		case MOVE:
			player = manager.getPlayerBySession(session);
			if (player == null) {
				return "Please login (packetID = 0) before joining a game";
			}
			Packet_4_Move parsedPacket_4 = gson.fromJson(message, Packet_4_Move.class);
			gameID = UUID.fromString(parsedPacket_4.getGameID());
			if (!player.getGameID().equals(gameID)) {
				return "player id and game id don't match";
			}
			game = manager.getGameByID(gameID);
			try {
				game.move(new String[] {parsedPacket_4.getMove()});
			} catch (IllegalMoveException e) {
				return e.getLocalizedMessage();
			}
			
			break;
		default:
			break;
		
		}
		
		return "not implemented";
		
	}
	
	@OnOpen
	public void onOpen(Session session) {
		logger.info("Connected. Session ID: " + session.getId());
		manager.getSessions().add(session);
	}

	@OnClose
	public void onClose(Session session, CloseReason closeReason) {
		logger.info(String.format("Session %s closed because of %s", session.getId(), closeReason));
		manager.getPlayerBySession(session).setSession(null);
		manager.getSessions().remove(session);
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
	public void update(AbstractGame updatedGame) {
//		for (UUID playerID: updatedGame.getPlayerList()) {
//			logger.info("Player to update: " + playerID.toString());
//			Session session = manager.getPlayerByID(playerID).getSession();
//			try {
//				Gson gson = new Gson();
//				JsonObject answer = new JsonObject();
//				answer = addHeader(answer, playerID);
//				answer = addBody(answer, updatedGame, playerID);
//				logger.info("Server answer: " + gson.toJson(answer));
//				session.getBasicRemote().sendText(gson.toJson(answer));
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
	}
	
	private String doLogin(Session session, String message) {
		Gson gson = new Gson();
		Packet_0_Login parsedPacket_0 = gson.fromJson(message, Packet_0_Login.class);
		if (!isUserNameValid(parsedPacket_0.getUsername())) return "user name must contain between 3 and 20 letters";
		Player player = manager.getPlayerByUsername(parsedPacket_0.getUsername());
		if (player == null) { 
			//this username is still free
			logger.info("new user %s logs in".formatted(parsedPacket_0.getUsername()));
			player = new Player(parsedPacket_0.getUsername(), session, UUID.randomUUID());
			manager.getPlayers().add(player);
			return playerToJson(player);
		} else if (parsedPacket_0.getPlayerID() != null) {
			//if a playerID is send
			try {
				//check if it is a valid id
				if (!player.getPlayerID().equals(UUID.fromString(parsedPacket_0.getPlayerID()))) {
					return "Username already taken OR username and player id don't match";
				} else {
				//existing user logs in
				player.setSession(session);
				return playerToJson(player);
				}
			} catch (IllegalArgumentException e) {
				return e.getLocalizedMessage();
			}
			
		} 
		return "please provide your playerID if you are already logged in";
	}
	
	private boolean isUserNameValid(String username) {
		if (username == null) {
			return false;
		} else if (username.length() < 3 || username.length() > 20) {
			return false;
		} else {
			return true;
		}
	}
		
	/****************************************************************************
	*	utility
	****************************************************************************/
	
//	private void joinGame(UUID playerID, Session session) {
//		AbstractGame gameToJoin = null;
//		for (AbstractGame game: manager.getLobby()) {
//			if (!game.isFull()) {
//				gameToJoin = game;
//			}
//		}
//		if (gameToJoin == null) {
//			gameToJoin = new TicTacToeExample();
//			logger.info("Created new Game: " + gameToJoin.ID.toString());
//			manager.getLobby().add(gameToJoin);
//			gameToJoin.registerObserver(this);
//		}
//		playerMap.put(playerID, session);
//		gameToJoin.addActivePlayer(playerID);
//		
//		int fullGames = lobby.stream().map(x -> (x.isFull() ? 1 : 0)).reduce(0, Integer::sum);
//		logger.info(String.format("Of %s games in the Lobby, there are %s full games ", manager.getLobby().size(), fullGames));	
//	}
	
	private String playerToJson(Player player) {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("username", player.getUserName());
		jsonObject.addProperty("playerID", player.getPlayerID().toString());
		if (!(player.getGameID() == null)) {
			jsonObject.addProperty("gameID", player.getGameID().toString());
		}
		return new Gson().toJson(jsonObject);
	}
	
}
