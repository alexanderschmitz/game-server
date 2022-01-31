package de.tu_berlin.pjki_server;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import com.google.gson.Gson;
import de.tu_berlin.pjki_server.game_engine.AbstractGame;
import de.tu_berlin.pjki_server.game_engine.MCTS;
import de.tu_berlin.pjki_server.game_engine.State;
import de.tu_berlin.pjki_server.game_engine.exception.IllegalMoveException;

public class TicTacToeExample extends AbstractGame implements MCTS {
	
	Logger log = Logger.getLogger(this.getClass().getName());

	static int winComb[][] = {{0,1,2},{3,4,5},{6,7,8},{0,3,6},{1,4,7},{2,5,8},{0,4,8},{2,4,6}};
	Gson g = new Gson();

	public TicTacToeExample() {
		super();
		state = new State(new HashMap<>());
		state.put("board", new int[]{0,0,0,0,0,0,0,0,0});
		state.put("moveNumber", 1);
	}
	
	
	public AbstractGame getNewInstance() {
		return new TicTacToeExample();
	}


	@Override
	public boolean isOver() {
		if (isDraw()) {
			return true;
		}
		int[] board = (int[]) state.get("board");
		for(int[] combination: winComb){
			if (board[combination[0]] == board[combination[1]] 
				&& board[combination[1]] == board[combination[2]] 
				&& board[combination[1]] != 0){
					setWinner(getActivePlayerList().get(board[combination[0]]-1));
					return true;
				}
		}
		return false;
	}
	
	@Override
	public boolean isDraw() {
		int[] board = (int[]) state.get("board");
		for (int cell: board) {
			if (cell == 0) {
				return false;
			}
		}
		return true;
	}

	public void move(String move) throws IllegalMoveException {
		int cell = Integer.parseInt(move);
		int[] board = (int[]) state.get("board");
		if (board[cell] != 0) {
			throw new IllegalMoveException();
		}
		int currentPlayer = getActivePlayerList().indexOf(getCurrentPlayer());
		board[cell] = currentPlayer + 1;
		state.put("board", board);
		state.put("moveNumber", (int) state.get("moveNumber")+1);
		log.info("%s, id: %s, board:  %s on move %d".formatted(this.getClass().getCanonicalName(), this.getID().toString(), this.toString(), state.get("moveNumber")));
		if (!isOver()) {
			notifyObservers();
			endTurn();
		}	
	}


//	@Override
//	public String toJson() {
//		JsonObject jsonObject = new JsonObject();
//		Gson g = new Gson();
//		jsonObject.add("gameID", g.toJsonTree(super.ID));
//		jsonObject.add("state", g.toJsonTree(super.getState()));
//		
//		return g.toJson(jsonObject);
//	}


	@Override
	public List<String> listMoves() {
		List<String> legalMoves = new ArrayList<>();
		int[] board = (int[]) state.get("board");
		for (int i = 0; i < 9; i++) {
			if (board[i] == 0) {
				legalMoves.add(String.valueOf(i));
			}
		}
		return legalMoves;
	}
	
	@Override
	public String toString() {
		int[] board = (int[]) state.get("board");
		return "TicTacToe - %s".formatted(Arrays.toString(board));
	}


}