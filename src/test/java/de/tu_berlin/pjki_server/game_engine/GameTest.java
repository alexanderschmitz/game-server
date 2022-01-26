package de.tu_berlin.pjki_server.game_engine;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.theories.suppliers.TestedOn;

import com.google.gson.Gson;

import de.tu_berlin.pjki_server.game_engine.exception.IllegalMoveException;

public class GameTest {
	
	AbstractGame test;
	
	@Before
	public void initialiseTestVariables() {
		test = new SampleGame();
	}
	
	@Test
	public void gameConstructor() {	
		assertEquals(test.getValue("maxPlayerNumber"), Integer.toString(2));
		assertEquals(test.getValue("currentPlayer"), "0");
		assertEquals(test.getValue("draw"), Boolean.toString(false));
		assertEquals(test.getValue("winner"), null);
	}
	
	@Test
	public void endTurn_shouldRotateTurns() {
		test.setValue("currentPlayer", "0");
		assertEquals(test.getValue("currentPlayer"), "0");
		test.endTurn();
		assertEquals(test.getValue("currentPlayer"), "1");
		test.endTurn();
		assertEquals(test.getValue("currentPlayer"), "0");
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
		test.notifyObservers();
		assertEquals(test.getState(), observer.getState());
	}
	
	@Test
	public void setState_gameStateShouldEqualNewState() {
		Map<String, String> state;
		state = new HashMap<>();
		state.put("maxPlayerNumber", "4");
		state.put("currentPlayer", "1");
		state.put("draw", Boolean.toString(true));
		state.put("winner", null);
		test.setState(state);
		assertEquals(test.getState(), state);
	}
	
	@Test
	public void stateToJson() {
		Gson g = new Gson();  
		System.out.println(g.toJson(test.getState()));
	}
	
	
	private class SampleGame extends AbstractGame{

		@Override
		public void setup(String[] args) {
		}

		@Override
		public void move(String[] args) throws IllegalMoveException {
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
		public String toJson() {
			// TODO Auto-generated method stub
			return null;
		}	
		
	}
	
	private class GameObserver implements Observer {

		private Map<String, String> state;
		
		@Override
		public void update(AbstractGame game) {
			this.state = game.getState();
			
		}
		
		protected Map<String, String> getState(){
			return state;
		}

		@Override
		public void update(AbstractGame game, List<UUID> players) {
			// TODO Auto-generated method stub
			
		}
		
	}

}
