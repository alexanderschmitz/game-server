package de.tu_berlin.pjki_server.examples;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.persistence.Entity;

import de.tu_berlin.pjki_server.game_engine.AbstractGame;
import de.tu_berlin.pjki_server.game_engine.State;
import de.tu_berlin.pjki_server.game_engine.entities.AbstractPlayer;
import de.tu_berlin.pjki_server.game_engine.exception.IllegalMoveException;
import de.tu_berlin.pjki_server.game_engine.mcts.MCTS;

@Entity
public class TicTacToe extends AbstractGame implements MCTS {
	private static final long serialVersionUID = 1L;
	
	
	static int winComb[][] = {{0,1,2},{3,4,5},{6,7,8},{0,3,6},{1,4,7},{2,5,8},{0,4,8},{2,4,6}};
	
	public TicTacToe() {
		super(new State(new HashMap<>()));
		getState().put("board", new int[]{0,0,0,0,0,0,0,0,0});
		getState().put("ply", 1);
	}
	
	
	public TicTacToe getNewInstance() {
		return new TicTacToe();
	}


	@Override
	public boolean checkIfOver() {
		int[] board = (int[]) getState().get("board");
		for(int[] combination: winComb){
			if (board[combination[0]] == board[combination[1]] 
				&& board[combination[1]] == board[combination[2]] 
				&& board[combination[1]] != 0){
					setWinner(getActivePlayerList().get(board[combination[0]]-1));
					log.info("Final Board: %s. Winner is: %s.".formatted(this.toString(), getWinner().getPlayerName()));
					return true;
				}
		}
		if (checkIfDraw()) {
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public boolean checkIfDraw() {
		int[] board = (int[]) getState().get("board");
		for (int cell: board) {
			if (cell == 0) {
				return false;
			}
		}
		log.info("Final Board: %s. Draw.".formatted(this.toString()));
		return true;
	}

	public void move(AbstractPlayer player, String move) throws IllegalMoveException {
		int cell = Integer.parseInt(move);
		int[] board = (int[]) getState().get("board");
		if (board[cell] != 0 || !player.equals(getCurrentPlayer())) {
			throw new IllegalMoveException();
		}
		int currentPlayer = getActivePlayerList().indexOf(getCurrentPlayer());
		board[cell] = currentPlayer + 1;
		getState().put("board", board);
		getState().put("ply", (int) getState().get("ply")+1);
	}

	@Override
	public List<String> listMoves() {
		List<String> legalMoves = new ArrayList<>();
		int[] board = (int[]) getState().get("board");
		for (int i = 0; i < 9; i++) {
			if (board[i] == 0) {
				legalMoves.add(String.valueOf(i));
			}
		}
		return legalMoves;
	}
	
	@Override
	public String toString() {
		int[] board = (int[]) getState().get("board");
		return "TicTacToe - %s".formatted(Arrays.toString(board));
	}


}