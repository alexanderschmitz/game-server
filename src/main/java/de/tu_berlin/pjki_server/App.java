package de.tu_berlin.pjki_server;

import de.tu_berlin.pjki_server.game_engine.Manager;
import de.tu_berlin.pjki_server.server_interface.WebSocketServer;

/*
 * Standard entry point
 * Usage: java de.tu_berlin.pjki_server.App <YourGame> <Port> 
 * 
 */
public class App {
	
	private static Manager manager = Manager.getManager();
	
	public static void main( String[] args ){
		
		manager.addGameType(TicTacToeExample.class);
		WebSocketServer.runServer();
		
	}
}
