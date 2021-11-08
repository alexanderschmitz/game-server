package de.tu_berlin.server_interface;

import de.tu_berlin.game_engine.GameInterface;

public class ServerProtocol {

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
	
	public String processInput(GameInterface rule) {
		return null;
		
	}
	

}
