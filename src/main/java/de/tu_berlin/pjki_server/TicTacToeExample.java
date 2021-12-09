package de.tu_berlin.pjki_server;


import com.google.gson.Gson;

import de.tu_berlin.pjki_server.game_engine.Game;
import de.tu_berlin.pjki_server.game_engine.exception.IllegalMoveException;

public class TicTacToeExample extends Game {

	static int winComb[][] = {{0,1,2},{3,4,5},{6,7,8},{0,3,6},{1,4,7},{2,5,8},{0,4,8},{2,4,6}};
	public static int[] state = {0,0,0,0,0,0,0,0,0}; //0 if empty, 1 for player 1, 2 for player 2

	public TicTacToeExample() {
		super();
		Gson g = new Gson();
		setValue("state", g.toJson(state));
	}
	
	
	public Game getNewInstance() {
		return new TicTacToeExample();
	}


	@Override
	public void setup(String[] args) {
		
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isOver(String[] args) {
		if (isDraw()) {
			setValue("draw", Boolean.toString(true));
			return true;
		}
		for(int[] combination: winComb){
			if (state[combination[0]] == state[combination[1]] 
				&& state[combination[1]] == state[combination[2]] 
				&& state[combination[1]] != 0){
					setValue("winner", Integer.toString(combination[0]));
					return true;
				}
		}
		return false;
	}
	
	private boolean isDraw() {
		for (int cell: state) {
			if (cell == 0) {
				return false;
			}
		}
		return true;
	}

	@Override
	public void move(String[] args) throws IllegalMoveException {
		int move = Integer.parseInt(args[0]);
		if (state[move] != 0) {
			throw new IllegalMoveException();
		}
		int currentPlayer = Integer.parseInt(getState().get("currentPlayer"));
		state[move] = currentPlayer + 1;
		endTurn();
	}

}