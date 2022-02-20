package de.tu_berlin.pjki_server.server_interface;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.glassfish.tyrus.server.Server;

public class WebSocketServer {

	public static void main(String[] args) {
		runServer();
	}

	public static void runServer() {
		Server server = new Server("localhost", 8025, "/websockets", GameServerEndpoint.class);
		
		try {
			server.start();
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
			System.out.print("Please press a key to stop the server");
			bufferedReader.readLine();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			server.stop();
		}
		
	}
}
