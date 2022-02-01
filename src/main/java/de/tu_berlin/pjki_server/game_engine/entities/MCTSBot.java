package de.tu_berlin.pjki_server.game_engine.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.IconifyAction;

import de.tu_berlin.pjki_server.game_engine.AbstractGame;
import de.tu_berlin.pjki_server.game_engine.State;
import de.tu_berlin.pjki_server.game_engine.exception.IllegalMoveException;
import de.tu_berlin.pjki_server.game_engine.mcts.MCTS;
import de.tu_berlin.pjki_server.game_engine.mcts.Node;

public class MCTSBot<T extends AbstractGame & MCTS> extends AbstractPlayer{
	
	private Logger log = Logger.getLogger(this.getClass().getName());
	T game;
	Random random = new Random();
	
	public MCTSBot(String name) {
		super(name);
	}	
	
	@Override
	public void update(AbstractGame game) {
		if (game.getCurrentPlayer().equals(this) && !game.isOver()) {
			try {
				String move = calculateNextMove(game);
				log.info("%s - Move: %s".formatted(getPlayerName(), move));
				game.executeMove(move);				
			} catch (IllegalMoveException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	
	private String calculateNextMove(AbstractGame game) {
		if (!MCTS.class.isAssignableFrom(game.getClass())) {
			System.out.println("Game does not implement MCTS interface");
			return null;
		} else {
			this.game = (T) game;
			int opponents = game.getMaxPlayerNumber();
			int playerIndex = game.getActivePlayerList().indexOf(this);
			Node rootNode = new Node();
			rootNode.setState(game.getState());
			
			long end = System.currentTimeMillis();
			while (System.currentTimeMillis() < end) {
				
			}
			return null;
		}
	}

	
	public List<State> createLegalStates(State initialState){
		List<State> legalStates = new ArrayList<State>();
		for (String move: game.listMoves()) {
			game.setState(initialState);
			try {
				game.move(move);
			} catch (IllegalMoveException e) {
				continue;
			}
			legalStates.add(game.getState());
		}
		return legalStates;
	}
	
	public State getRandomState(State initialState) {
		List<State> legalStates = createLegalStates(initialState);
		return legalStates.get(random.nextInt(legalStates.size()));
	}
	
}
