package de.tu_berlin.pjki_server.game_engine;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.List;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import de.tu_berlin.pjki_server.game_engine.entities.Player;
import de.tu_berlin.pjki_server.game_engine.exception.IllegalMoveException;

public class GameTest {
	
	AbstractGame test;
	
	@Before
	public void initialiseTestVariables() {
		test = new SampleGame();
	}
	
	@Test
	public void gameConstructor() {	
		assertEquals(test.getMaxPlayerNumber(), 2);
		assertEquals(test.getCurrentPlayer(), null);
		assertEquals(test.getWinner(), null);
	}
	
	@Test
	public void endTurn_shouldRotateTurns() {
		Player player1 = new Player("player1", null, UUID.randomUUID(), null);
		Player player2 = new Player("player2", null, UUID.randomUUID(), null);
		try {
			test.addActivePlayer(player1);
			test.addActivePlayer(player2);
		} catch (Exception e) {
			fail();
		}
		
		assertEquals(test.getCurrentPlayer(), player1);
		test.endTurn();
		assertEquals(test.getCurrentPlayer(), player2);
		test.endTurn();
		assertEquals(test.getCurrentPlayer(), player1);
	}
	
	@Test 
	public void observerRegistrationAndUnregistration() {
		Observer observer = new GameObserver();
		test.registerObserver(observer);
		try {
			test.unregisterObserver(observer);
		} catch (Exception e) {
			fail("");
		}
	}
	
	@Test
	public void unregisterForeignObserver() {
		try {
			test.unregisterObserver(new GameObserver());
		} catch (Exception e) {
			return;   //success
		}
	}
	
	@Test
	public void testNotifyObservers() {
		GameObserver observer = new GameObserver();
		test.registerObserver(observer);
		test.notifyAllObservers();
		assertEquals(test, observer.getGame());
	}
	
	@Test
	public void testAddingTooManyPlayers() {
		SampleGame testGame = new SampleGame();
		int maxPlayers = testGame.getMaxPlayerNumber();
		// completely fill the game with players
		for (int i = 0; i < maxPlayers; i++) {
			try {
				testGame.addActivePlayer(new Player("Player" + i, null, UUID.randomUUID(), null));
			} catch (Exception e) {
				fail();
			}
		}
		// try to add one more player
		try {
			testGame.addActivePlayer(new Player("OneTooMany", null, UUID.randomUUID(), null));
			fail();
		} catch (Exception e) {
			return;
		}
	}

	
	private class SampleGame extends AbstractGame{

		@Override
		public void move(String move) throws IllegalMoveException {
		}

		@Override
		public boolean isOver() {
			return false;
		}

		@Override
		public AbstractGame getNewInstance() {
			return null;
		}

		@Override
		public boolean isDraw() {
			return false;
		}

		
	}
	
	private class GameObserver implements Observer {

		private AbstractGame game;
		
		@Override
		public void update(AbstractGame game) {
			this.game = game;
		}
		
		protected AbstractGame getGame(){
			return game;
		}		
	}

}
