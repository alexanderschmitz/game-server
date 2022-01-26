package de.tu_berlin.pjki_server;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import de.tu_berlin.pjki_server.game_engine.exception.IllegalMoveException;


public class TicTacToeExampleTest {

	TicTacToeExample game;
	
	@Before
	public void initialiseTestVariables() {
		game = new TicTacToeExample();
	}
	
	@Test
	public void testGameOver() {
		game.state = new int[]{1,1,1,0,0,0,0,0,0};
		assertEquals(game.isOver(), true);
	}
	
	@Test
	public void testMove() {
		game.state = new int[]{1,1,0,0,0,0,0,0,0};
		if (game.isOver()) {
			fail();
		}
		try {
			game.move(new String[] {"2"});
		} catch (IllegalMoveException e) {
			fail();
		}
		assertEquals(game.isOver(), true);
	}
	
	@Test
	public void testMultipleMoves() {
		game.state = new int[]{2,2,0,0,0,0,0,0,0};
		if (game.isOver()) {
			fail();
		}
		try {
			game.move(new String[] {"3"});
		} catch (IllegalMoveException e) {
			fail();
		}
		try {
			game.move(new String[] {"2"});
		} catch (IllegalMoveException e) {
			fail();
		}
		for (int i: game.state) {
			System.out.print(i + ",");
		}
		assertEquals(game.isOver(), true);
	}
	
	
}
