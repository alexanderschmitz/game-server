package de.tu_berlin.pjki_server.game_engine;

import java.util.Map;
import com.google.gson.Gson;

public class State implements Cloneable{
	
	
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

	@Override
	public State clone() throws CloneNotSupportedException {
//		Gson gson = new Gson();
//		String stateString = gson.toJson(this);
//		State clone = gson.fromJson(stateString, State.class);
		return (State) super.clone();
	}
	
	
}
