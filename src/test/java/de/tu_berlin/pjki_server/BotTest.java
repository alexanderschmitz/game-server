package de.tu_berlin.pjki_server;

import org.junit.Test;

import de.tu_berlin.pjki_server.game_engine.entities.Bot;

public class BotTest {

	
	@Test
	public void testMCTSBot() {
		TicTacToeExample tttExample = new TicTacToeExample();
		Bot bot1 = new Bot("bot1");
		Bot bot2 = new Bot("bot2");
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
