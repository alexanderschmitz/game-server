package de.tu_berlin.pjki_server.game_engine.entities;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.IconifyAction;

import de.tu_berlin.pjki_server.game_engine.AbstractGame;
import de.tu_berlin.pjki_server.game_engine.MCTS;
import de.tu_berlin.pjki_server.game_engine.exception.IllegalMoveException;

public class Bot extends AbstractPlayer{
	
	private Logger log = Logger.getLogger(this.getClass().getName());
	
	public Bot(String name) {
		super(name);
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

	@Override
	public void update(AbstractGame game) {
		if (game.getCurrentPlayer().equals(this) && !game.isOver()) {
			try {
				String move = calculateNextMove(game);
				log.info("%s - Move: %s".formatted(getPlayerName(), move));
				game.move(move);				
			} catch (IllegalMoveException e) {
				e.printStackTrace();
			}
		}
	}
}
