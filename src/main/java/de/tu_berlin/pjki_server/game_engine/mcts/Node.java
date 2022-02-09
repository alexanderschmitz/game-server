package de.tu_berlin.pjki_server.game_engine.mcts;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.tu_berlin.pjki_server.game_engine.AbstractGame;
import de.tu_berlin.pjki_server.game_engine.State;
import de.tu_berlin.pjki_server.game_engine.entities.AbstractPlayer;
import de.tu_berlin.pjki_server.game_engine.exception.IllegalMoveException;

public class Node<T extends AbstractGame & MCTS> {
	private Node<T> parent = null;
	private List<Node<T>> children;
	private String move;
	private State state;
	private T game;
	private int visits = 0;
	private double wins = 0;
	
	
	public Node(String move, Node<T> parent, T game) {
		this.move = move;
		this.parent = parent;
		this.game = game;
		this.children = new ArrayList<>();
	}
	
	public void expandNode(State state, AbstractPlayer player) {
		game.setState(state);
		if (!game.checkIfOver()) {
			for (String move: game.listMoves()) {
				game.setState(state);
				Node<T> child = new Node<T>(move, this, game);
				try {
					game.move(player, move);
				} catch (IllegalMoveException e) {
					e.printStackTrace();
				}
				child.setState(game.getState());
				children.add(child);
			}
		}
	}
	
	public void update(double result) {
		visits++;
		wins +=result;
	}
	
	public double calculateScore() {
		return (double) wins / (double) visits;
	}
	
	public boolean isLeaf() {
		return children.isEmpty();
	}
	
	public boolean hasParent() {
		return parent == null;
	}

	public Node<T> getParent() {
		return parent;
	}

	public void setParent(Node<T> parent) {
		this.parent = parent;
	}

	public List<Node<T>> getChildren() {
		return children;
	}

	public void setChildren(List<Node<T>> children) {
		this.children = children;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public int getVisits() {
		return visits;
	}

	public void setVisits(int visits) {
		this.visits = visits;
	}

	public double getWins() {
		return wins;
	}

	public void setWins(double wins) {
		this.wins = wins;
	}

	public String getMove() {
		return move;
	}

	public void setMove(String move) {
		this.move = move;
	}

	public T getGame() {
		game.setState(state);
		return game;
	}

	public void setGame(T game) {
		this.game = game;
	}
	
	
	
}
