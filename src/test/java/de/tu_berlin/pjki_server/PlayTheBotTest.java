package de.tu_berlin.pjki_server;

import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import org.junit.Test;

import de.tu_berlin.pjki_server.examples.TicTacToeTest;
import de.tu_berlin.pjki_server.game_engine.AbstractGame;
import de.tu_berlin.pjki_server.game_engine.Observer;
import de.tu_berlin.pjki_server.game_engine.entities.MCTSBot;
import de.tu_berlin.pjki_server.game_engine.entities.Player;
import de.tu_berlin.pjki_server.game_engine.exception.IllegalMoveException;
import de.tu_berlin.pjki_server.game_engine.exception.MaximumPlayerNumberExceededException;

public class PlayTheBotTest implements Observer{


	@Test
	public void testNodeGetLegalMoves() {
		TicTacToeTest tttExample = new TicTacToeTest();
		MCTSBot<TicTacToeTest> bot1 = new MCTSBot<TicTacToeTest>("bot1", TicTacToeTest.class);
		TestPlayer you = new TestPlayer("tester");
		tttExample.registerObserver(bot1);
		tttExample.registerObserver(you);
		tttExample.registerObserver(this);
		try {
			tttExample.addActivePlayer(bot1);
			tttExample.addActivePlayer(you);
		} catch (MaximumPlayerNumberExceededException e) {
			fail(e.getMessage());
		}		
	}

	@Override
	public void update(AbstractGame game) {
		System.out.println("Update received");
		System.out.println(game.toString());
		
	}
	
	private class TestPlayer extends Player{

		public TestPlayer(String userName) {
			super(userName, null, null);
		}

		@Override
		public void update(AbstractGame game) {
			BufferedReader reader = new BufferedReader(
		            new InputStreamReader(System.in));
			boolean moved = false;
			while (!moved) {
				try {
					System.out.println("Your move: game %s".formatted(game.toString()));
					String move = reader.readLine();
					game.executeMove(this, move);
					moved = true;
				} catch (IOException | IllegalMoveException e) {
					System.out.println("Can't move there");
				}
			}
		}
	}

}
