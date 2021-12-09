package de.tu_berlin.pjki_server.server_interface;

import static org.junit.Assert.assertEquals;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class RequestTest {
	
	private Request actual;
	private Gson gson;
	private JsonObject jsonObject;
	
	@Before
	public void beforeEach() {
		gson = new Gson();
		jsonObject = new JsonObject();
	}
	
	@Test
	public void jsonToObjectOne() {
		String playerID = UUID.randomUUID().toString();
		String gameID = UUID.randomUUID().toString();
		
		jsonObject.addProperty("playerID", playerID);
		jsonObject.addProperty("gameID", gameID);
		jsonObject.addProperty("intent", "JOINGAME");
		
		actual = gson.fromJson(jsonObject, Request.class);
		
		assertEquals(playerID, actual.getPlayerID().toString());
		assertEquals(gameID, actual.getGameID().toString());
		assertEquals(Request.Intent.JOINGAME, actual.getIntent());
	}

}
