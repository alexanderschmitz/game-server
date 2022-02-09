package de.tu_berlin.pjki_server.game_engine.entities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import javax.persistence.Entity;
import javax.persistence.Transient;

import de.tu_berlin.pjki_server.game_engine.AbstractGame;
import de.tu_berlin.pjki_server.game_engine.State;
import de.tu_berlin.pjki_server.game_engine.exception.IllegalMoveException;
import de.tu_berlin.pjki_server.game_engine.mcts.MCTS;
import de.tu_berlin.pjki_server.game_engine.mcts.Node;

@Entity
public class MCTSBot<T extends AbstractGame & MCTS> extends AbstractPlayer{
	
	
	@Transient
	private Logger log = Logger.getLogger(this.getClass().getName());
	@Transient
	Random random = new Random();
	@Transient
	private final Class<T> gameClass;
	
	public MCTSBot(String name, Class<T> gameClass) {
		super(name);
		this.gameClass = gameClass;
	}	
	
	@Override
	public void update(AbstractGame game) {
		if (game.getCurrentPlayer().equals(this) && !game.isOver()) {
			try {
				String move = calculateNextMove(game);
				log.info("%s - Move: %s".formatted(getPlayerName(), move));
				game.executeMove(this, move);				
			} catch (IllegalMoveException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	
	private String calculateNextMove(AbstractGame game) {
		if (!(gameClass.isInstance(game))) {
			System.out.println("Game does not implement MCTS interface");
			return null;
		} else {
			
			T gameCopy;
			try {
				gameCopy = gameClass.cast(game.clone());
				Node<T> rootNode = new Node<T>(null, null, gameCopy);
				rootNode.setState(game.getState());
				
				long end = System.currentTimeMillis() + 1000;
				while (System.currentTimeMillis() < end) {
					Node<T> node = getPromisingChild(rootNode);		//selection
					node.expandNode(node.getState(), this);			//expansion
					Node<T> nodeToExplore = node;
					if (!nodeToExplore.isLeaf()) {
						nodeToExplore = getRandomChild(node);
					}
					double result = simulateRandom(nodeToExplore);		//simulation
					backPropagation(nodeToExplore, result);			//backpropagation
					
				}
				Node<T> bestNode = getChildWithBestScore(rootNode);
				return bestNode.getMove();
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
				return null;
			}
			
		}
	}
		
	public List<State> createLegalStates(State initialState, T game){
		List<State> legalStates = new ArrayList<State>();
		for (String move: game.listMoves()) {
			game.setState(initialState);
			try {
				game.move(this, move);
			} catch (IllegalMoveException e) {
				continue;
			}
			legalStates.add(game.getState());
		}
		return legalStates;
	}
	
	public State getRandomState(State initialState, T game) {
		List<State> legalStates = createLegalStates(initialState, game);
		return legalStates.get(random.nextInt(legalStates.size()));
	}
	
	private Node<T> getRandomChild(Node<T> parent){
		List<Node<T>> children = parent.getChildren();
		Node<T> child = children.get(random.nextInt(children.size()));
		return child;
	}
	
	private Node<T> getPromisingChild(Node<T> root) {
		Node<T> node = root;
		while(!node.isLeaf()) {
			node = findBestNodeWithUCT(node);
		}
		return node;
	}
	
	private double uctValue(int totalVisits, double winScore, int nodeVisits) {
		if (nodeVisits == 0) {
			return Double.MAX_VALUE;
		} else {
			return ((double) winScore / (double) nodeVisits) 
					+ 1.41 * Math.sqrt(Math.log(totalVisits) / (double) nodeVisits);
		}
	}
	
	private Node<T> findBestNodeWithUCT(Node<T> parent) {
		return Collections.max(
				parent.getChildren(),
				Comparator.comparing(child -> uctValue(
						parent.getVisits(), child.getWins(), child.getVisits())));
	}

	private void backPropagation(Node<T> node, double result) {
		Node<T> tempNode = node;
		while (tempNode != null) {
			tempNode.update(result);
			tempNode = tempNode.getParent();
		}
	}
	
	private double simulateRandom(Node<T> node) {
		Node<T> tempNode = node;
		while (!tempNode.getGame().isOver()) {
			tempNode.setState(getRandomState(tempNode.getState(), tempNode.getGame()));
		}
		if (tempNode.getGame().isDraw()) {
			return 0.5;
		} else if (tempNode.getGame().getWinner().equals(this)) {
			return 1;
		} else {
			return 0;
		}
	}
	
	private Node<T> getChildWithBestScore(Node<T> parent){
		Node<T> bestChild = parent.getChildren().get(0);
		for (Node<T> child: parent.getChildren()) {
			if (child.calculateScore() > bestChild.calculateScore()) {
				bestChild = child;
			}
		}
		return bestChild;
	}
	
}
