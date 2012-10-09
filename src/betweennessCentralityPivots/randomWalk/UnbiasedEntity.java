package betweennessCentralityPivots.randomWalk;

import java.util.ArrayList;
import java.util.Iterator;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;

public class UnbiasedEntity extends RandomWalkEntity {
	/**
	 * Select the next node where the entity goes. The algorithm used here is from 
	 * "On unbiased sampling for unstructured peer-to-peer networks" proposed by 
	 * "Stutzbach, Daniel and Rejaie, Reza and Duffield, Nick and Sen, Subhabrata and Willinger, Walter"
	 * @return the next node where the entity goes
	 */
	public Node selectNextNode(){
		//We get the neighbors
		Iterator<Edge> it = getCurrentNode().getLeavingEdgeIterator();
		ArrayList<Node> neighbors = new ArrayList<Node>();
		while(it.hasNext())
			neighbors.add(it.next().getOpposite(getCurrentNode()));
		
		/* 1 : choose a neighbor j from Voisin uniformly at random
		 * 2 : Query j for d_j (its current degree)
		 * 3 : Generate a random number p \in \[0, 1\] uniformly
		 * 4 : if p \leq d_i/d_j then
		 * 5 : 		make the step to j
		 * 6 : else
		 * 7 : 		remain at i
		 */
		
		//We choose the next using an unbiased method
		Node v;
		double p;
		do{
			p=getRwbc().getRandom().nextDouble();
			v=neighbors.get(getRwbc().getRandom().nextInt(neighbors.size()));
		}while(p > (1.*getCurrentNode().getDegree()/v.getDegree()));
		
		//We return the selected node
		return v;
	}
}
