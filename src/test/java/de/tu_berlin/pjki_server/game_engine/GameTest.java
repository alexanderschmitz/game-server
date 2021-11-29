package de.tu_berlin.pjki_server.game_engine;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.tu_berlin.pjki_server.game_engine.exception.IllegalMoveException;

public class GameTest {
	
	@Test
	public void gameConstructor() {
		
		Game test = new SampleGame();
		assertEquals(test.getValue("maxPlayerNumber"), Integer.toString(2));
		assertEquals(test.getValue("currentPlayer"), "0");
		assertEquals(test.getValue("draw"), Boolean.toString(false));
		assertEquals(test.getValue("winner"), null);
		test.endTurn();
		assertEquals(test.getValue("currentPlayer"), "1");
		
	}
	
	private class SampleGame extends Game{

		@Override
		public void setup(String[] args) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void move(String[] args) throws IllegalMoveException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public boolean isOver(String[] args) {
			// TODO Auto-generated method stub
			return false;
		}
		
	}

}
