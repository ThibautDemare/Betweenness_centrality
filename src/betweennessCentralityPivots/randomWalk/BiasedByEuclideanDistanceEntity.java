package betweennessCentralityPivots.randomWalk;

import java.util.ArrayList;
import java.util.Iterator;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;

public class BiasedByEuclideanDistanceEntity extends RandomWalkEntity {
	public BiasedByEuclideanDistanceEntity(){
		super();
	}
	
	/**
	 * Select the next node where the entity goes
	 * @return the next node where the entity goes
	 */
	public Edge selectNextNode(){
		//We get the neighbors
		Iterator<Node> it = currentNode.getNeighborNodeIterator();
		ArrayList<Node> neighbors = new ArrayList<Node>();
		while(it.hasNext())
			neighbors.add(it.next());
		
		//Compute the heuristics
		neighborsEuclideanDistance(neighbors);
		
		//We return the selected node
		Node n = biasedWheelOfFortune(neighbors);
		if(currentNode.getEdgeBetween(n) == null)
			return n.getEdgeBetween(currentNode);
		else
			return currentNode.getEdgeBetween(n);
	}
	
	/**
	 * Return a node selected by a biased wheel of fortune
	 * @param ln a sorted list.
	 * @return The selected next node
	 */
	public Node biasedWheelOfFortune(ArrayList<Node> ln){
		double rand = Math.random();
		double i = 0;
		for(Node n : ln){
			i += n.getNumber("EuclideanDistance");
			if(rand < i)
				return n;
		}
		//For the compiler but it is (normaly) never done
		return null;
	}
	
	/**
	 * Normalized between 0 and 1 the euclidean distance from the nodes of the list to a target
	 * @param neighbors
	 */
	public void neighborsEuclideanDistance(ArrayList<Node> neighbors){
		double sum = 0;
		for(int i=0; i<neighbors.size(); i++){
			sum += euclideanDistance(neighbors.get(i), getPair().getNode2());
		}
		for(Node n : neighbors)
			n.addAttribute("EuclideanDistance", n.getNumber("EuclideanDistance")/sum);
	}
	
	/**
	 * Compute the euclidean distance between two node (and in 2 dimensions)
	 * @param n1
	 * @param n2
	 * @return the euclidean distance between two node
	 */
	public double euclideanDistance(Node n1, Node n2){
		double dist;
		Object[] xyz1=n1.getAttribute("xyz");
		Object[] xyz2=n2.getAttribute("xyz");
		dist = Math.sqrt(
				  ((Double)(xyz1[0])-(Double)(xyz2[0]))*((Double)(xyz1[0])-(Double)(xyz2[0]))
					 +((Double)(xyz1[1])-(Double)(xyz2[1]))*((Double)(xyz1[1])-(Double)(xyz2[1])));
		n1.addAttribute("EuclideanDistance", dist);
		n2.addAttribute("EuclideanDistance", dist);
		return dist;
	}
}
