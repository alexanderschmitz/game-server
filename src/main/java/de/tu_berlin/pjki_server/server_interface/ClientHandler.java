package de.tu_berlin.pjki_server.server_interface;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Map;

import de.tu_berlin.pjki_server.game_engine.Game;
import de.tu_berlin.pjki_server.game_engine.Observer;

class ClientHandler<T extends Game> implements Observer, Runnable {
	
	private T game;
	private final Socket clientSocket;
	ArrayList<? extends Game> lobby;
	
	public ClientHandler(Socket clientSocket, ArrayList<? extends Game> lobby) {
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
                 System.out.printf(" Sent from the client: %s\n", line);
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

}
