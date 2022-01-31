package de.tu_berlin.pjki_server.game_engine;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.logging.Logger;

import de.tu_berlin.pjki_server.game_engine.exception.IllegalMoveException;

public class Bot extends Player implements Observer {
	
	Logger log = Logger.getLogger(this.getClass().getName());
	
	public Bot(String name, UUID botID) {
		super(name, null, botID);
	}

	@Override
	public void update(AbstractGame game) {
		if (game.getCurrentPlayer() != this) {
			return;
		} else {
			List<Player> playerList = new ArrayList<Player>();
			playerList.add(this);
			update(game, playerList);
		}
		
		
	}

	@Override
	public void update(AbstractGame game, List<Player> players) {
		if (!players.contains(this)) {
			return;
		} else {
			try {
				String move = calculateNextMove(game);
				game.move(move);
				log.info("%s - Move: %s".formatted(getUserName(), move));
			} catch (IllegalMoveException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	private String calculateNextMove(AbstractGame game) {
		if (!MCTS.class.isAssignableFrom(game.getClass()) ) {
			System.out.println("Game does not implement MCTS interface");
			return null;
		} else {
			List<String> legalMoves = ((MCTS) game).listMoves();
			Random random = new Random();
			return legalMoves.get(random.nextInt(legalMoves.size()));
		}
	}
	
	
}
