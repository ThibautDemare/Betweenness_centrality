package randomWalk;

import java.util.ArrayList;
import java.util.Iterator;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;

public class UnbiasedEntity2 extends Entity{
	void step() {
		//We get the neighbors
		Iterator<Edge> it = current.getLeavingEdgeIterator();
		ArrayList<Node> neighbors = new ArrayList<Node>();
		while(it.hasNext())
			neighbors.add(it.next().getOpposite(current));
		
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
			p=Math.random();
			v=neighbors.get((int)(Math.random()*neighbors.size()));
		}while(p > (1.*current.getDegree()/v.getDegree()));
		
		current.getEdgeBetween(v).setAttribute(context.passesAttribute, current.getEdgeBetween(v).getNumber(context.passesAttribute) + 1);
		v.setAttribute(context.passesAttribute, v.getNumber(context.passesAttribute) + 1);
		current = v;
	}
}
