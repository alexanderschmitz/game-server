package de.tu_berlin.pjki_server;

import org.junit.Test;

import de.tu_berlin.pjki_server.examples.RandomBot;
import de.tu_berlin.pjki_server.examples.TicTacToeTest;

public class RandomBotTest {

	
	@Test
	public void testMCTSBot() {
		TicTacToeTest tttExample = new TicTacToeTest();
		RandomBot bot1 = new RandomBot("bot1");
		RandomBot bot2 = new RandomBot("bot2");
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
