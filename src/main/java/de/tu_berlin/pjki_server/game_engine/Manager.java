package de.tu_berlin.pjki_server.game_engine;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.inject.Singleton;
import javax.websocket.Session;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import de.tu_berlin.pjki_server.game_engine.entities.AbstractPlayer;
import de.tu_berlin.pjki_server.game_engine.entities.Player;

/**
 * This class manages the high level logic of the Game Server.
 * <p> Active Games in the lobby
 * <p> Active Players in the players list
 * <p> Active Clients with the sessions
 * <p> Registered Games in the gameMap
 *
 */
@Singleton
public final class Manager {

	private static Manager INSTANCE;
	static Set<AbstractGame> lobby = Collections.synchronizedSet(new HashSet<AbstractGame>());
	static Set<Session> sessions = Collections.synchronizedSet(new HashSet<Session>());
	static Set<Player> players = Collections.synchronizedSet(new HashSet<Player>());
	static ConcurrentHashMap<String, Class<? extends AbstractGame>> gameMap = new ConcurrentHashMap<>(); 
	static GsonBuilder gsonBuilder = new GsonBuilder();
	
	private Manager() {
		
	}
	
	public synchronized static Manager getManager() {
		if (INSTANCE == null) {
			INSTANCE = new Manager();
		}
		return INSTANCE;
	}

	public Set<AbstractGame> getLobby() {
		return lobby;
	}

	public Set<Session> getSessions() {
		return sessions;
	}

	public Set<Player> getPlayers() {
		return players;
	}

	public ConcurrentHashMap<String, Class<? extends AbstractGame>> getGameMap() {
		return gameMap;
	}

	public AbstractGame addGameToLobby(String gameName) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		AbstractGame game = gameMap.get(gameName).getConstructor().newInstance().getNewInstance();
		lobby.add(game);
		return game;
	}
	
	public void addGameType(Class<? extends AbstractGame> gameClass) {
		try {
			AbstractGame game = gameClass.getConstructor().newInstance().getNewInstance();
			gsonBuilder.registerTypeHierarchyAdapter(AbstractGame.class, game);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
		gameMap.put(gameClass.getSimpleName(), gameClass);
	}
	
	public void removeGameType(Class<? extends AbstractGame> gameClass) {
		gameMap.remove(gameClass.getSimpleName());
	}
	
	public Player getPlayerByID(Long playerID) {
		if (playerID == null) return null;		
		for (Player player: players) {
			if (player.getPlayerID() == playerID) {
				return player;
			}
		}
		return null;
	}

	public Player getPlayerByUsername(String username) {
		if (username == null) return null;
		for (Player player: players) {
			if (player.getPlayerName().equals(username)) {
				return player;
			}
		}
		return null;
	}
	
	public Player getPlayerBySession(Session session) {
		if (session == null) return null;
		for (Player player: players) {
			if (player.getSession().equals(session)) {
				return player;
			}
		}
		return null;
	}
	
	public AbstractGame getGameByID(long gameID) {
		for (AbstractGame game: lobby) {
			if (game.getID() == gameID) {
				return game;
			}
		}
		return null;
	}
	
	public AbstractGame getGameByPlayer(AbstractPlayer player) {
		for (AbstractGame game: lobby) {
			for (AbstractPlayer checkPlayer: game.getActivePlayerList()) {
				if (checkPlayer.equals(player)) {
					return game;
				}
			}
		}
		return null;
	}
	
	public Gson getGson() {
		return gsonBuilder.create();
	}	
}
