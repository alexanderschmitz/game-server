package de.tu_berlin.pjki_server;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import de.tu_berlin.pjki_server.game_engine.Bot;
import de.tu_berlin.pjki_server.game_engine.Player;
import de.tu_berlin.pjki_server.game_engine.exception.IllegalMoveException;


public class TicTacToeExampleTest {

	TicTacToeExample game;
	Player player1 = new Player("player1", null, UUID.randomUUID());
	Player player2 = new Player("player2", null, UUID.randomUUID());
	
	@Before
	public void initialiseTestVariables() {
		game = new TicTacToeExample();
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
		game.state.put("board", new int[]{1,1,1,0,0,0,0,0,0});
		assertEquals(game.isOver(), true);
		assertEquals(player1, game.getWinner());
	}
	
	@Test
	public void testGameOverPlayer2Wins() {
		game.state.put("board", new int[]{2,2,2,0,0,0,0,0,0});
		assertEquals(game.isOver(), true);
		assertEquals(player2, game.getWinner());
	}
	
	@Test
	public void testMove() {
		game.state.put("board", new int[]{1,1,0,0,0,0,0,0,0});
		if (game.isOver()) {
			fail();
		}
		try {
			game.move("2");
		} catch (IllegalMoveException e) {
			fail();
		}
		assertEquals(game.isOver(), true);
	}
	
	@Test
	public void testMultipleMoves() {
		game.state.put("board", new int[]{2,2,0,0,0,0,0,0,0});
		if (game.isOver()) {
			fail();
		}
		try {
			game.move("3");
		} catch (IllegalMoveException e) {
			fail();
		}
		try {
			game.move("2");
		} catch (IllegalMoveException e) {
			fail();
		}
		int[] board = (int[]) game.state.get("board");
		for (int i: board) {
			System.out.print(i + ",");
		}
		assertEquals(game.isOver(), true);
	}
	
	@Test
	public void testMCTSBot() {
		TicTacToeExample tttExample = new TicTacToeExample();
		Bot bot1 = new Bot("bot1", UUID.randomUUID());
		Bot bot2 = new Bot("bot2", UUID.randomUUID());
		tttExample.registerObserver(bot1);
		tttExample.registerObserver(bot2);
		try {
			tttExample.addActivePlayer(bot1);
			tttExample.addActivePlayer(bot2);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
}
