package de.tu_berlin.pjki_server.server_interface;

import java.io.IOException;
import java.io.Reader;
import java.util.Map;

import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.spi.JsonProvider;
import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

import de.tu_berlin.pjki_server.TicTacToeExample;
import de.tu_berlin.pjki_server.game_engine.Game;

public class MessageDecoder<T extends Game> implements Decoder.TextStream<T> {
	
	@Override
	public void init(EndpointConfig config) {
		// TODO Auto-generated method stub

	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@SuppressWarnings("unchecked")
	@Override
	public T decode(Reader reader) throws DecodeException, IOException {
		JsonProvider provider = JsonProvider.provider();
		JsonReader jsonReader = provider.createReader(reader);
		JsonObject jsonSticker = jsonReader.readObject();
		T game = (T) new TicTacToeExample();
		return game;
	}

}
