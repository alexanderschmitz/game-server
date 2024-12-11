package de.tu_berlin.pjki_server.game_engine;

public interface Observer {
	
	/** Update all Observers about a change in the game state*/
	public void update(AbstractGame game);
}
