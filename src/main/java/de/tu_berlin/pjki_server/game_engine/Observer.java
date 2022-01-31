package de.tu_berlin.pjki_server.game_engine;

import java.util.List;
import java.util.UUID;

public interface Observer {

	
	/** Update all Observers about a change in the game state*/
	public void update(AbstractGame game);
	
	/** Update a subset of players registered to this game about a change in the game state*/
	public void update(AbstractGame game, List<Player> players);

}
