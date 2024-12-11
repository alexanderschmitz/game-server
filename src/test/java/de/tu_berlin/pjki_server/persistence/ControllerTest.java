package de.tu_berlin.pjki_server.persistence;

import static org.junit.Assert.assertEquals;

import java.util.List;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.junit.Test;

import de.tu_berlin.pjki_server.TicTacToeExample;
import de.tu_berlin.pjki_server.examples.TicTacToeTest;
import de.tu_berlin.pjki_server.examples.TicTacToeTestUtility;
import de.tu_berlin.pjki_server.game_engine.AbstractGame;

public class ControllerTest {

	Controller controller = Controller.getController();
	
	@Test
	public void testGamePersitence() {
		Class<TicTacToeExample> game = TicTacToeExample.class;
		int n = 10;
		Query q1 = controller.getEntityManager()
				.createQuery("SELECT COUNT(p) FROM %s p".formatted(game.getSimpleName()));
		long initial = (long)q1.getSingleResult();
		for (int i = 0; i < n; i++) {
			TicTacToeExample tttExample = new TicTacToeExample();
			controller.persistGame(tttExample);
		}
		Query q2 = controller.getEntityManager()
				.createQuery("SELECT COUNT(p) FROM %s p".formatted(game.getSimpleName()));
		long newEntries = (long)q2.getSingleResult() - initial;
        assertEquals(newEntries, n);
		
//        TypedQuery<TicTacToeExample> query = controller.getEntityManager()
//        		.createQuery("SELECT p FROM %s p".formatted(game.getSimpleName()), TicTacToeExample.class);
//        List<TicTacToeExample> results = query.getResultList();
//        for (TicTacToeExample ttt: results) {
//        	ttt.getMoveHistory().forEach(v ->{
//        		System.out.print(v + ",");});
//        	System.out.printf("maxPlayer: %s, draw? %s \n", ttt.getMaxPlayerNumber(), ttt.isDraw());
//        }
	}
	
	
//	@Test
//	public void testDeleteTicTacToe() {
//		Class game = TicTacToeTest.class;
//		for (int i = 0; i < 10; i++) {
//			TicTacToeTestUtility.simulateRandomGame();
//		}
//		controller.deleteTable(game);
//		Query q1 = controller.getEntityManager()
//				.createQuery("SELECT COUNT(p) FROM %s p".formatted(game.getSimpleName()));
//		assertEquals((long)q1.getSingleResult(), 0);
//	}
	
	
	@Test
	public void testGetAllGames() {
		Class<TicTacToeTest> game = TicTacToeTest.class;
		Controller controller = Controller.getController();
		List<? extends AbstractGame> games = controller.query(game);
//		System.out.println("retrieved: " + games.size());
//		for (AbstractGame entry: games ) {
//			System.out.println(entry.toString());
//		}
		
	}

	
}
