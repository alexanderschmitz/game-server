package de.tu_berlin.pjki_server.game_engine;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import javax.inject.Singleton;
import javax.websocket.Session;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

@Singleton
public final class Manager {

	private static Manager INSTANCE;
	static Set<AbstractGame> lobby = Collections.synchronizedSet(new HashSet<AbstractGame>());
	static Set<Session> sessions = Collections.synchronizedSet(new HashSet<Session>());
	static Set<Player> players = Collections.synchronizedSet(new HashSet<Player>());
	static ConcurrentHashMap<String, Class<? extends AbstractGame>> gameMap = new ConcurrentHashMap<>(); 
	
	
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
		gameMap.put(gameClass.getSimpleName(), gameClass);
	}
	
	public void removeGameType(Class<? extends AbstractGame> gameClass) {
		gameMap.remove(gameClass.getSimpleName());
	}
	
	public Player getPlayerByID(UUID playerID) {
		if (playerID == null) return null;		
		for (Player player: players) {
			if (player.getPlayerID().equals(playerID)) {
				return player;
			}
		}
		return null;
	}

	public Player getPlayerByUsername(String username) {
		if (username == null) return null;
		for (Player player: players) {
			if (player.getUserName().equals(username)) {
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
	
	public AbstractGame getGameByID(UUID gameID) {
		if (gameID == null) return null;
		for (AbstractGame game: lobby) {
			if (game.ID.equals(gameID)) {
				return game;
			}
		}
		return null;
	}

	public String lobbyToJson() {
		List<String> games = new ArrayList<>();
		Gson g = new Gson();
		for (AbstractGame game: lobby) {
			games.add(game.toJson());
		}
		return new Gson().toJson(games);
	}
	
}
