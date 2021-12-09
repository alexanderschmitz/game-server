package de.tu_berlin.pjki_server.server_interface;

import java.util.ArrayList;

import com.google.gson.Gson;

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
	private ArrayList<? extends Game> lobby;
	
	
	public ServerProtocol(ArrayList<? extends Game> lobby) {
		this.lobby = lobby;
	}


	public String processInput(String input) {
		Gson input = new Gson(input);
		String output = null;
		switch(state) {
			case WAITING:
				output = new Gson().toJson(lobby);
				state = SENTLOBBIES;
				break;
			case SENTLOBBIES:
				
				break;
			case JOINEDGAME:
			
				break;
			case GAMERUNNING:
			
				break;
			case GAMEOVER:
			
				break;
			case SENTHISTORY:
			
				break;
			case OTHER:
				
				break;
		}
		
		return output;
		
	}
	

}
