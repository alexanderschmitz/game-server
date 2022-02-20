package de.tu_berlin.pjki_server.game_engine;

import java.io.Serializable;
import java.util.Map;

import org.apache.commons.lang3.SerializationUtils;


public class State implements Cloneable, Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5273988178240543934L;
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
		return SerializationUtils.clone(this);
	}
	
	
}
