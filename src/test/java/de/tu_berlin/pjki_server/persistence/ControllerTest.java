package de.tu_berlin.pjki_server.persistence;

import java.util.List;

import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.junit.Test;

import de.tu_berlin.pjki_server.examples.TicTacToe;
import de.tu_berlin.pjki_server.examples.TicTacToeTestUtility;

public class ControllerTest {

	Controller controller = Controller.getController();
	
	@Test
	public void testGamePersitence() {
		controller.deleteTable(TicTacToe.class);
		
		for (int i = 0; i < 1; i++) {
			TicTacToeTestUtility.simulateRandomGame();
		}
		Query q1 = controller.getEntityManager()
				.createQuery("SELECT COUNT(p) FROM TicTacToe p");
        System.out.println("Total Points: " + q1.getSingleResult());
		
        TypedQuery<TicTacToe> query = controller.getEntityManager()
        		.createQuery("SELECT p FROM TicTacToe p", TicTacToe.class);
        List<TicTacToe> results = query.getResultList();
        for (TicTacToe ttt: results) {
        	ttt.getMoveHistory().forEach(v ->{
        		System.out.print(v + ",");});
        	System.out.printf("maxPlayer: %s, draw? %s \n", ttt.getMaxPlayerNumber(), ttt.isDraw());
        }
	}
	
	
	
	
}
