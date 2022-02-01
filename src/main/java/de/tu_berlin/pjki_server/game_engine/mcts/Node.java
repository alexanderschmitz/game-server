package de.tu_berlin.pjki_server.game_engine.mcts;

import java.util.List;

import de.tu_berlin.pjki_server.game_engine.State;

public class Node {
	Node parent;
	List<Node> children;
	State state;
	int visits, wins, losses, draws = 0;
	
	public Node() {
	}
	
	public double getValue() {
		if (visits == 0) {
			return 0;
		} else {
			return (wins + 0.5*draws) / visits;
		}
	}

	public Node getParent() {
		return parent;
	}

	public void setParent(Node parent) {
		this.parent = parent;
	}

	public List<Node> getChildren() {
		return children;
	}

	public void setChildren(List<Node> children) {
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

	public int getWins() {
		return wins;
	}

	public void setWins(int wins) {
		this.wins = wins;
	}

	public int getLosses() {
		return losses;
	}

	public void setLosses(int losses) {
		this.losses = losses;
	}

	public int getDraws() {
		return draws;
	}

	public void setDraws(int draws) {
		this.draws = draws;
	}
	
	
	
}
