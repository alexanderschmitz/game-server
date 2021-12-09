package de.tu_berlin.pjki_server.server_interface;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.tu_berlin.pjki_server.TicTacToeExample;
import de.tu_berlin.pjki_server.game_engine.Game;

public class Main {
	
	public static void main(String[] args) throws IOException {

		System.out.println("Hello World");
		ServerSocket server = null;
		List<Game> lobby = Collections.synchronizedList(new ArrayList<Game>());

		try {
			server = new ServerSocket(1234); // server is listening on port 1234
			server.setReuseAddress(true);

			while (true) {
				Socket client = server.accept();
				System.out.println("New client connected" + client.getInetAddress().getHostAddress());
				ClientHandler clientSocket = new ClientHandler(client, lobby);
				new Thread(clientSocket).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (server != null) {
				try {
					server.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}	
	}
	
}
