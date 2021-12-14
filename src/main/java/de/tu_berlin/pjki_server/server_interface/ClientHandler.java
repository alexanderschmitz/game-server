package de.tu_berlin.pjki_server.server_interface;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.UUID;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import de.tu_berlin.pjki_server.TicTacToeExample;
import de.tu_berlin.pjki_server.game_engine.Game;
import de.tu_berlin.pjki_server.game_engine.Observer;
import de.tu_berlin.pjki_server.game_engine.exception.IllegalMoveException;

class ClientHandler implements Observer, Runnable {
	
	private Game game;
	private final Socket clientSocket;
	List<Game> lobby;
	PrintWriter out = null;
    BufferedReader in = null;
    UUID playerID = null;
	
	public ClientHandler(Socket clientSocket, List<Game> lobby) {
		this.clientSocket = clientSocket;
		this.lobby = lobby;
	}

	@Override
	public void run() {
		JsonObject answer = new JsonObject();
		JsonObject body = new JsonObject();
		answer = addHeader(answer);
		try {
			out = new PrintWriter(clientSocket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

			String line;
			while ((line = in.readLine()) != null) {
				Request request = null;
				Request.Intent intent = null;
				try {	//parsing the request from the client
					//TODO: check validity compared to json schema
					request = new Gson().fromJson(line, Request.class);
					intent = request.getIntent();
				} catch (JsonSyntaxException jsonSyntaxException) {
					body.addProperty("ERROR", "Invalid JSON Syntax");
					answer.add("body", body);
					out.println(answer);
					continue;
				} catch (NullPointerException nullPointerException) {
					body.addProperty("ERROR", "Invalid Request");
					answer.add("body", body);
					out.println(answer);
					continue;
				}
				System.out.printf(" Sent from the client: %s\n", new Gson().toJson(request));
				
				switch(intent) {
				case GETGAMES:
					body.addProperty("games", new Gson().toJson(lobby));

					break;
				case JOINGAME:
					game = joinGame(request.getGameID());
					if (game == null) {
						game = new TicTacToeExample();
					}
					body.addProperty("gameID", game.ID.toString());
					body.addProperty("state", new Gson().toJson(game.getState()));
					game.registerObserver(this);
					break;
				case MOVE:
					try {
						game.move(request.getMove());
						game.notifyObservers();
					} catch (IllegalMoveException e) {
						body.addProperty("ERROR", "IllegalMoveException");
					}
				default:
					body.addProperty("ERROR", "Invalid Intent");
					
				}
				//TODO: let player join a game type of his choice -> Game type has to be handed in with request
				answer.add("body", body);
				out.println(answer);
			}

		} catch (IOException e) {
			body.addProperty("ERROR", "Internal Server Error: IOException");
			answer.add("body", body);
			out.println(answer);
			e.printStackTrace();
		} finally {
			try { //close the socket
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
					clientSocket.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void update(Game game) {
		try {
			String answer = new Gson().toJson(game.getState());
			out.println(answer);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	
	/**
	 * @param uuid The UUID of the game, the client wants to join
	 * @return the game the player wants to join if one is found with matching uuid. 
	 * @return or the game with most players
	 * @return null if there are no games in the lobby
	 */
	private Game joinGame(UUID uuid) {
		for (Game game: lobby) {
			if (game.ID.equals(uuid)) {
				return game;
			}
		}
		int maxActivePlayers = 0;
		Game fullestGame = null;
		for (Game game: lobby) {
			int activePlayers = Integer.parseInt(game.getValue("activePlayers"));
			if (activePlayers >= maxActivePlayers) {
				maxActivePlayers = activePlayers;
				fullestGame = game;
			}
		}
		return fullestGame;
	}
	
	/****************************************************************************
	*	utility
	****************************************************************************/
	
	private JsonObject addHeader(JsonObject answer) {
		JsonObject header = new JsonObject();
		if (playerID == null) {
			playerID = UUID.randomUUID();
		}
		header.addProperty("playerID", playerID.toString());
		answer.add("header", header);
		return answer;
	}
	
}
