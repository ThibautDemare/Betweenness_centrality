package betweennessCentralityPivots.randomWalk;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import org.graphstream.graph.Node;

public class BiasedByTravelTimeEntity extends RandomWalkEntity{

	/**
	 * Select the next node where the entity goes
	 * @return the next node where the entity goes
	 */
	public Node selectNextNode(){
		//We get the neighbors
		Iterator<Node> it = currentNode.getNeighborNodeIterator();
		ArrayList<Node> neighbors = new ArrayList<Node>();
		while(it.hasNext())
			neighbors.add(it.next());
		
		//System.out.println(neighbors.size()+" et degré : "+getCurrentNode().getDegree());
		//Compute the heuristics
		normalizedEuclideanDistance(neighbors);
		
		//Shuffle the list in order to select the node 
		Collections.shuffle(neighbors, getRwbc().getRandom());
		
		//We return the selected node
		return wheelOfFortune(neighbors);
	}
	
	/**
	 * Return a node selected by a biased wheel of fortune
	 * @param ln
	 * @return The selected next node
	 */
	public Node wheelOfFortune(ArrayList<Node> ln){
		double p = getRwbc().getRandom().nextDouble();
		Node n;
		for(int i=0; i<ln.size(); i++){
			n=ln.get(i);
			double dist = n.getNumber("NormalizedTravelTimeTo"+getPair().getNode2().getId());
			double proba = Math.pow(Math.E, (-2*dist));
			if(proba > p){
				return n;
			}
		}
		return ln.get(0);
	}
	
	/**
	 * Normalized between 0 and 1 the euclidean distance from the nodes of the list to a target
	 * @param neighbors
	 */
	public synchronized void normalizedEuclideanDistance(ArrayList<Node> neighbors){
		double max = Double.NEGATIVE_INFINITY;
		for(int i=0; i<neighbors.size(); i++){
			max = Math.max(max, travelTime(neighbors.get(i), getPair().getNode2()));
		}
		
		for(int i=0; i<neighbors.size(); i++){
			Node n = neighbors.get(i);
			n.setAttribute("NormalizedTravelTimeTo"+getPair().getNode2(), n.getNumber("TravelTimeTo"+getPair().getNode2())/max);
		}
	}
	
	/**
	 * Compute the travel time between to node (and in 2 dimensions)
	 * @param n1
	 * @param n2
	 * @return the travel time distance between to node
	 */
	public synchronized double travelTime(Node n1, Node n2){
		double dist;
		double speed;
		double travelTime;
		if(!n1.hasAttribute("TravelTimeTo"+n2.getId())){
			Object[] xyz1=n1.getAttribute("xyz");
			Object[] xyz2=n2.getAttribute("xyz");
			dist = Math.sqrt(
					  ((Double)(xyz1[0])-(Double)(xyz2[0]))*((Double)(xyz1[0])-(Double)(xyz2[0]))
						 +((Double)(xyz1[1])-(Double)(xyz2[1]))*((Double)(xyz1[1])-(Double)(xyz2[1])));
			speed = associatedSpeed((int)getCurrentNode().getEdgeBetween(n1).getNumber("SPEED_CAT"));
			travelTime=dist / speed;
			n1.addAttribute("TravelTimeTo"+n2.getId(), travelTime);
		}
		else{
			travelTime = n1.getNumber("TravelTimeTo"+n2.getId());
		}
		return travelTime;
	}
	
	private double associatedSpeed(int val){
		switch(val){
		case 1 :
			return 130;
		case 2 :
			return 115;
		case 3 :
			return 95;
		case 4 :
			return 80;
		case 5 :
			return 60;
		case 6 :
			return 40;
		case 7 :
			return 21;
		default : //8
			return 11;
		}
	}
}
