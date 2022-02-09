package de.tu_berlin.pjki_server.examples;

import java.util.Random;

public class TicTacToeTestUtility {

	public static void simulateRandomGame() {
//		TicTacToe ttt = new TicTacToe();
//		int[] board = new int[] {0,0,0,0,0,0,0,0,0};
//		Random random = new Random();
//		for (int i = 0; i < 9; i++) {
//			int j = random.nextInt(9);
//			while (board[j] != 0) {
//				j = random.nextInt(9);
//			}
//			board[j] = (i % 2) + 1;
//		}
//		return ttt;
		
		TicTacToe tttExample = new TicTacToe();
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
