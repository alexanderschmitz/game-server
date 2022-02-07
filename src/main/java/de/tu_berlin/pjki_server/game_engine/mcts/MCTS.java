package de.tu_berlin.pjki_server.game_engine.mcts;

import java.util.List;


/**
 * This interface is meant to be implemented by your game class, if you want AI capabilities.
 * It allows MCTS Bots to play your Game.
 *
 */
public interface MCTS {
	
	
	/**
	 * List all legal moves from the current game state.
	 * @return List of moves
	 */
	public List<String> listMoves();

}
