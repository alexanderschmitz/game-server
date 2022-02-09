package de.tu_berlin.pjki_server.game_engine;

import java.util.Map;

public class State {
	
	private Map<String, Object> state;

	public State(Map<String, Object> state) {
		this.state = state;
	}
	
	public Object get(String key) {
		return state.get(key);
	}
	
	public void put(String key, Object object) {
		state.put(key, object);
	}
}
