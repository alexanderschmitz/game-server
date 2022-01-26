package de.tu_berlin.pjki_server.game_engine;

import java.util.List;
import java.util.UUID;

public interface Observer {

	public void update(AbstractGame game);
	public void update(AbstractGame game, List<UUID> players);

}
