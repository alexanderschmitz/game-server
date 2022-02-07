package de.tu_berlin.pjki_server;

import static org.junit.Assert.*;

import org.junit.Test;

import de.tu_berlin.pjki_server.game_engine.entities.MCTSBot;
import de.tu_berlin.pjki_server.game_engine.exception.MaximumPlayerNumberExceededException;

public class MCTSBotTest {

	@Test
	public void testNodeGetLegalMoves() {
		TicTacToeExample tttExample = new TicTacToeExample();
		MCTSBot<TicTacToeExample> bot1 = new MCTSBot<TicTacToeExample>("bot1", TicTacToeExample.class);
		MCTSBot<TicTacToeExample> bot2 = new MCTSBot<TicTacToeExample>("bot2", TicTacToeExample.class);
		tttExample.registerObserver(bot1);
		tttExample.registerObserver(bot2);
		try {
			tttExample.addActivePlayer(bot1);
			tttExample.addActivePlayer(bot2);
		} catch (MaximumPlayerNumberExceededException e) {
			fail(e.getMessage());
		}
		
		
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
