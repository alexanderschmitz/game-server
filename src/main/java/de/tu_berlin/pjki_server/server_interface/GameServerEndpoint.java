package de.tu_berlin.pjki_server.server_interface;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
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
import de.tu_berlin.pjki_server.game_engine.entities.Player;
import de.tu_berlin.pjki_server.game_engine.entities.Spectator;
import de.tu_berlin.pjki_server.game_engine.exception.IllegalMoveException;
import de.tu_berlin.pjki_server.persistence.Controller;
import de.tu_berlin.pjki_server.server_interface.packets.AbstractPacket;
import de.tu_berlin.pjki_server.server_interface.packets.AbstractPacket.Type;
import de.tu_berlin.pjki_server.server_interface.packets.Packet_0_Login;
import de.tu_berlin.pjki_server.server_interface.packets.Packet_2_CreateGame;
import de.tu_berlin.pjki_server.server_interface.packets.Packet_3_JoinGame;
import de.tu_berlin.pjki_server.server_interface.packets.Packet_4_Move;
import de.tu_berlin.pjki_server.server_interface.packets.Packet_5_Database;

/**
 * The Endpoint class for the Websocket Interface to the game logic.
 *
 */
@ServerEndpoint(value="/game")
@Singleton
public class GameServerEndpoint {
	
	
	private Logger logger = Logger.getLogger(this.getClass().getName());
	private static Manager manager = Manager.getManager();
	Gson customGson;
	
	@OnMessage
	public String onMessage(String message, Session session) {
		logger.info("Raw received message: " + message);
		JsonObject jsonObject; 
		try {
			jsonObject = JsonParser.parseString(message).getAsJsonObject();
		} catch (Exception e) {
			return e.getMessage();
		}
		logger.info(new Gson().toJson(jsonObject));
		Type packetType = Type.INVALID;
		try {
			packetType = AbstractPacket.getPacketType(jsonObject.get("type").getAsString());
		} catch (Exception e) {
			return "invalid packet";
		}
		Gson gson = new Gson();
		Player player = manager.getPlayerBySession(session);
		switch (packetType) {
		case INVALID:
			return "invalid packet id";
		case LOGIN:
			return doLogin(session, message);			
		case GETGAMES:
			return manager.getGson().toJson(manager.getLobby());
		case CREATEGAME:
			Packet_2_CreateGame parsedPacket_2 = gson.fromJson(message, Packet_2_CreateGame.class);
			return doCreate(session, parsedPacket_2);			
		case JOIN: //3
			if (player == null) {
				return "Please login (packetID = 0) before joining a game";
			}
			Packet_3_JoinGame parsedPacket_3 = gson.fromJson(message, Packet_3_JoinGame.class);
			long gameID = parsedPacket_3.getGameID();
			AbstractGame game = manager.getGameByID(gameID);
			try {
				if (parsedPacket_3.getJoinAsPlayer() == 1) {
					game.addActivePlayer(player);
				}
				game.registerObserver(player);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			return manager.getGson().toJson(game);
		case MOVE: //4
			if (player == null) {
				return "Please login (packetID = 0) before joining a game";
			}
			Packet_4_Move parsedPacket_4 = gson.fromJson(message, Packet_4_Move.class);
			gameID = parsedPacket_4.getGameID();
			game = manager.getGameByID(gameID);
			if (!game.getActivePlayerList().contains(player)) {
				return "player id and game id don't match";
			}
			try {
				game.executeMove(player, parsedPacket_4.getMove());
			} catch (IllegalMoveException e) {
				return e.getMessage();
			}
			break;
		case DATABASE:
			Packet_5_Database parsedPacket_5 = gson.fromJson(message, Packet_5_Database.class);
			Controller controller = Controller.getController();
			Class<? extends AbstractGame> gameClass = manager.getGameMap().get(parsedPacket_5.getGameType()); 
			if (gameClass == null) {
				return "Specify a valid game type: ".concat(gson.toJson(manager.getGameMap().keys()));
			} else if (parsedPacket_5.getGameCount() <= 0) {
				return manager.getGson().toJson(controller.query(gameClass));
			} else {
				return manager.getGson().toJson(controller.query(gameClass, parsedPacket_5.getGameCount()));
			}
			
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
			session.getBasicRemote().sendText(t.getLocalizedMessage());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private String doLogin(Session session, String message) {
		Gson gson = new Gson();
		Packet_0_Login parsedPacket_0 = gson.fromJson(message, Packet_0_Login.class);
		if (!isUserNameValid(parsedPacket_0.getUsername())) return "user name must contain between 3 and 20 letters";
		Player player = manager.getPlayerByUsername(parsedPacket_0.getUsername());
		if (player == null) { 
			//this username is still free
			logger.info("new user %s logs in".formatted(parsedPacket_0.getUsername()));
			player = new Player(parsedPacket_0.getUsername(), session, this);
			manager.getPlayers().add(player);
			return gson.toJson(player);
		} else if (parsedPacket_0.getPlayerID() != 0) {
			//if a playerID is send
			try {
				//check if it is a valid id
				if (!(player.getPlayerID() == parsedPacket_0.getPlayerID())) {
					return "Username already taken OR username and player id don't match";
				} else {
				//existing user logs in
				player.setSession(session);
				return gson.toJson(player);
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
	
	private String doCreate(Session session, Packet_2_CreateGame parsedPacket) {
		Player player = manager.getPlayerBySession(session);
		if (player == null) {
			return "Please login (packetID = 0) before creating a game";
		}
		
		try {
			AbstractGame newGame = manager.addGameToLobby(parsedPacket.getGameName());
			newGame.registerObserver(player);
			return manager.getGson().toJson(newGame);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | NoSuchMethodException | SecurityException e) {
			return e.getMessage();
		}
		
	}
		
	/****************************************************************************
	*	utility
	****************************************************************************/

	public void update(Player player, AbstractGame game) {
		try {
			player.getSession().getBasicRemote().sendText(new Gson().toJson(game));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	public void update(Spectator spectator, AbstractGame game) {
		try {
			spectator.getSession().getBasicRemote().sendText(new Gson().toJson(game));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}

		
}
