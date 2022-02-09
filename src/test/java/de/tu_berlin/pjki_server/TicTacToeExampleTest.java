package de.tu_berlin.pjki_server;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import de.tu_berlin.pjki_server.examples.TicTacToe;
import de.tu_berlin.pjki_server.game_engine.entities.Player;
import de.tu_berlin.pjki_server.game_engine.exception.IllegalMoveException;


public class TicTacToeExampleTest {

	TicTacToe game;
	Player player1 = new Player("player1", null, UUID.randomUUID(), null);
	Player player2 = new Player("player2", null, UUID.randomUUID(), null);
	
	@Before
	public void initialiseTestVariables() {
		game = new TicTacToe();
		try {
			game.addActivePlayer(player1);
			game.addActivePlayer(player2);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGameOverPlayer1Wins() {
		game.getState().put("board", new int[]{1,1,1,0,0,0,0,0,0});
		assertEquals(game.checkIfOver(), true);
		assertEquals(player1, game.getWinner());
	}
	
	@Test
	public void testGameOverPlayer2Wins() {
		game.getState().put("board", new int[]{2,2,2,0,0,0,0,0,0});
		assertEquals(game.checkIfOver(), true);
		assertEquals(player2, game.getWinner());
	}
	
	@Test
	public void testMove() {
		game.getState().put("board", new int[]{1,1,0,0,0,0,0,0,0});
		if (game.checkIfOver()) {
			fail();
		}
		try {
			game.executeMove(player1, "2");
		} catch (IllegalMoveException e) {
			fail();
		}
		assertEquals(game.checkIfOver(), true);
	}
	
	@Test
	public void testMultipleMoves() {
		game.getState().put("board", new int[]{2,2,0,0,0,0,0,0,0});
		if (game.checkIfOver()) {
			fail();
		}
		try {
			game.executeMove(player1, "3");
		} catch (IllegalMoveException e) {
			fail();
		}
		try {
			game.executeMove(player2, "2");
		} catch (IllegalMoveException e) {
			fail();
		}
		int[] board = (int[]) game.getState().get("board");
		for (int i: board) {
			System.out.print(i + ",");
		}
		assertEquals(game.checkIfOver(), true);
	}
}
