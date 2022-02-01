package de.tu_berlin.pjki_server.game_engine.mcts;

import java.util.Collections;
import java.util.Comparator;

public class UCT {
	
	
	public static double uctValue(int totalVisit, double nodeWinScore, int nodeVisit) {
        if (nodeVisit == 0) {
            return Integer.MAX_VALUE;
        }
        return ((double) nodeWinScore / (double) nodeVisit) + 1.41 * Math.sqrt(Math.log(totalVisit) / (double) nodeVisit);
    }

    public static Node findBestNodeWithUCT(Node node) {
    	
        int parentVisit = node.getVisits();
        return Collections.max(
          node.getChildren(), Comparator.comparing(c -> uctValue(parentVisit, 
            c.getValue(), c.getVisits())));
    }
}
