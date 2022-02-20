package de.tu_berlin.pjki_server.game_engine.entities;

import java.util.UUID;

import javax.websocket.Session;

import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import de.tu_berlin.pjki_server.examples.TicTacToeTest;
import de.tu_berlin.pjki_server.server_interface.GameServerEndpoint;

public class PlayerTest {

	@Test
	public void testPlayerDeserializazion() {
		Gson gson = new GsonBuilder()
				  .excludeFieldsWithoutExposeAnnotation()
				  .create();
		Player player = new Player("test", null, new GameServerEndpoint());
		System.out.println(gson.toJson(player));
		MCTSBot<TicTacToeTest> bot = new MCTSBot<>("bot", TicTacToeTest.class);
		System.out.println(gson.toJson(bot));
		Spectator spectator = new Spectator("test", null, new GameServerEndpoint());
		System.out.println(gson.toJson(spectator));
		
		for (int i = 0; i < 10; i++) {
			player = new Player("test", null, new GameServerEndpoint());
			System.out.println(gson.toJson(player));
		}

	}
}
