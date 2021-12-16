package de.tu_berlin.pjki_server.server_interface;

import javax.websocket.EncodeException;

import java.util.Iterator;
import java.util.Map;

import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;
import javax.json.spi.JsonProvider;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

import de.tu_berlin.pjki_server.TicTacToeExample;
import de.tu_berlin.pjki_server.game_engine.Game;

class MessageEncoder implements Encoder.Text<TicTacToeExample> {

	@Override
	public void init(EndpointConfig config) {
		// TODO Auto-generated method stub

	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public String encode(TicTacToeExample game) throws EncodeException {
		JsonProvider provider = JsonProvider.provider();
		JsonObject jsonGame = provider.createObjectBuilder()
				.add("action", "add")
				.add("state", mapToJson(game.getState()))
				.build();
		System.out.println(jsonGame.toString());
		return jsonGame.toString();
	}
	
	private JsonValue mapToJson(Map<String, String> map) {
		JsonProvider provider = JsonProvider.provider();
		JsonObjectBuilder jsonBuilder = provider.createObjectBuilder();
		Iterator <String> it = map.keySet().iterator();  
		while (it.hasNext()) {
			String key = it.next();
			String value = map.get(key);
			jsonBuilder.add(key, value);
		}
		return jsonBuilder.build();
	}


}
