package de.tu_berlin.pjki_server;

import static org.junit.Assert.*;

import org.junit.Test;

import de.tu_berlin.pjki_server.examples.TicTacToeTest;
import de.tu_berlin.pjki_server.game_engine.AbstractGame;
import de.tu_berlin.pjki_server.game_engine.Observer;
import de.tu_berlin.pjki_server.game_engine.entities.MCTSBot;
import de.tu_berlin.pjki_server.game_engine.exception.MaximumPlayerNumberExceededException;

public class MCTSBotTest implements Observer{

	@Test
	public void testNodeGetLegalMoves() {
		TicTacToeTest tttExample = new TicTacToeTest();
		MCTSBot<TicTacToeTest> bot1 = new MCTSBot<TicTacToeTest>("bot1", TicTacToeTest.class);
		MCTSBot<TicTacToeTest> bot2 = new MCTSBot<TicTacToeTest>("bot2", TicTacToeTest.class);
		tttExample.registerObserver(bot1);
		tttExample.registerObserver(bot2);
		tttExample.registerObserver(this);
		try {
			tttExample.addActivePlayer(bot1);
			tttExample.addActivePlayer(bot2);
		} catch (MaximumPlayerNumberExceededException e) {
			fail(e.getMessage());
		}		
	}

	@Override
	public void update(AbstractGame game) {
		System.out.println("Update received");
		System.out.println(game.toString());
		
	}
	
	
//	@Test
//	public void testCalculateNextMoveShouldReturnMove() {
//		TicTacToeExample tttExample = new TicTacToeExample();
//		MCTSBot<TicTacToeExample> bot1 = new MCTSBot<TicTacToeExample>("bot1", TicTacToeExample.class);
//		MCTSBot<TicTacToeExample> bot2 = new MCTSBot<TicTacToeExample>("bot2", TicTacToeExample.class);
//		try {
//			tttExample.addActivePlayer(bot1);
//			tttExample.addActivePlayer(bot2);
//		} catch (MaximumPlayerNumberExceededException e) {
//			fail(e.getMessage());
//		}
//		
//	}
	
}
