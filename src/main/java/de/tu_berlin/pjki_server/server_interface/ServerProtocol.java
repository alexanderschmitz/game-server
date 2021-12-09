package de.tu_berlin.pjki_server.server_interface;

import java.util.List;
import com.google.gson.Gson;

import de.tu_berlin.pjki_server.TicTacToeExample;
import de.tu_berlin.pjki_server.game_engine.Game;

public class ServerProtocol{

	//define states that a communication can be found in
	private static final int WAITING = 0;
	private static final int SENTLOBBIES = 1;
	private static final int JOINEDGAME = 2; //this state can be reached after creating or joining a game
	private static final int GAMERUNNING = 3;
	private static final int GAMEOVER = 4;
	private static final int SENTHISTORY = 5;
	private static final int OTHER = 6;
	//states for user creation and management are missing
	
	private int state = WAITING;
	private List<Game> lobby;
	
	
	public ServerProtocol(List<Game> lobby) {
		this.lobby = lobby;
	}


	public String processInput(String input) {
		Request request = new Gson().fromJson(input, Request.class);
		String output = null;
		switch(request.getIntent()) {
			case GETGAMES:
				output = new Gson().toJson(lobby);
				break;
			case JOINGAME:
				Game game = getAvailableGame();
				if (game == null) {
					game = new TicTacToeExample();
				}
				break;
			default:
				
		}
		
		return output;
		
	}
	
	public Game getAvailableGame() {
		Game game = null;
		
		for (Game g:lobby) {
			int activePlayers = Integer.parseInt(g.getValue("activePlayers"));
			int maxPlayerNumber = Integer.parseInt(g.getValue("maxPlayerNumber"));
			if (activePlayers < maxPlayerNumber) {
				game = g;
			}
		}
		
		return game;
	}
	

}
