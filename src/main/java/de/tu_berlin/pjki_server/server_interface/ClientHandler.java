package de.tu_berlin.pjki_server.server_interface;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.google.gson.Gson;

import de.tu_berlin.pjki_server.TicTacToeExample;
import de.tu_berlin.pjki_server.game_engine.Game;
import de.tu_berlin.pjki_server.game_engine.Observer;

class ClientHandler implements Observer, Runnable {
	
	private Game game;
	private final Socket clientSocket;
	List<Game> lobby;
	
	public ClientHandler(Socket clientSocket, List<Game> lobby) {
		this.clientSocket = clientSocket;
		this.lobby = lobby;
	}

	@Override
	public void run() {
		 PrintWriter out = null;
         BufferedReader in = null;
         try {
             out = new PrintWriter(clientSocket.getOutputStream(), true);
             in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

             String line;
             while ((line = in.readLine()) != null) {
            	 try {
            		 
            	 }
            	 Request request = new Gson().fromJson(line, Request.class);
                 System.out.printf(" Sent from the client: %s\n", new Gson().toJson(request));
                 out.println(line);
             }
         } catch (IOException e) {
             e.printStackTrace();
         } finally {
             try {
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
	public void update(Map<String, String> state) {
		//TODO: update the clients about the game state change

	}
	
	
	/**
	 * @param uuid The UUID of the game, the client wants to join
	 * @return the game the player wants to join if one is found with matching uuid. 
	 * @return or the game with most players
	 * @return null if there are no games in the lobby
	 */
	private Game joinGame(UUID uuid) {
		for (Game game: lobby) {
			if (game.getId().equals(uuid)) {
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
	
}
