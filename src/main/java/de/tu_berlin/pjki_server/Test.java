package de.tu_berlin.pjki_server;

import de.tu_berlin.pjki_server.game_engine.AbstractGame;
import de.tu_berlin.pjki_server.game_engine.exception.IllegalMoveException;

public class Test extends AbstractGame{
	
	public Test() {
		
	}

	@Override
	public void move(String move) throws IllegalMoveException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isOver() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isDraw() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public AbstractGame getNewInstance() {
		// TODO Auto-generated method stub
		return null;
	}
	
}