package de.tu_berlin.pjki_server.server_interface;

class Message {
	
	public Header header;
	public Game game;

	public class Header {
		public String playerID;
	}
	
	public class Game {
		public String id;
		public String move;
	}
	
}
