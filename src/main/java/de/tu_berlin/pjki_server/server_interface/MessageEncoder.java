package de.tu_berlin.pjki_server.server_interface;

import javax.websocket.EncodeException;

import java.io.Writer;
import java.util.Iterator;
import java.util.Map;

import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;
import javax.json.JsonWriter;
import javax.json.spi.JsonProvider;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

import de.tu_berlin.pjki_server.game_engine.Game;

class MessageEncoder<T extends Game> implements Encoder.TextStream<T> {

	@Override
	public void init(EndpointConfig config) {
		// TODO Auto-generated method stub

	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void encode(T game, Writer writer) throws EncodeException {
		JsonProvider provider = JsonProvider.provider();
		JsonObject jsonGame = provider.createObjectBuilder()
				.add("action", "add")
				.add("state", mapToJson(game.getState()))
				.build();
		try (JsonWriter jsonWriter = provider.createWriter(writer)) {
		      jsonWriter.write(jsonGame);
		}
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
