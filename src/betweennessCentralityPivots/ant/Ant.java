package betweennessCentralityPivots.ant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import org.graphstream.algorithm.Dijkstra;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;
import org.graphstream.graph.Path;



public class Ant {
	
	protected AntBetweennessCentralityPivots abcp;
	
	protected Path p;
	
	protected Node current;
	protected Node dest;
	
	protected Heuristic heuristic;
	
	protected ArrayList<Edge> tabuList;
	protected int tabuMemorySize = 200;
	protected double tabuProba = 1.0E-15;
	
	protected static double Q = 10000;
	protected static double alpha = 5;
	protected static double beta = 2;
	protected static double rho = 0.8;
	protected static double initialPheromone = 0.001;
	protected static double minPheromone = 1.0E-8;
	
	public void init(AntBetweennessCentralityPivots abcp){
		this.abcp = abcp;
		p = new Path();
		tabuList = new ArrayList<Edge>();
		heuristic = new RandomHeuristic();
	}
	
	public Path makePath(){
		p.clear();
		tabuList.clear();
		
		String[] paire = abcp.getCurrentPaire();
		current = abcp.getGraph().getNode(paire[0]);
		dest = abcp.getGraph().getNode(paire[1]);
		p.setRoot(current);
		
		while(p.peekNode()!=dest){
			current = p.peekNode();
			
			if(nextAreTabu(current) || current.getLeavingEdgeSet().size()<=0){
				jumpBack();
			}
			else{
				double sum = 0;
				double h;
				double pheromone;
				
				for(Edge e : current.getLeavingEdgeSet()){
					double proba;
					if(tabuList.contains(e)){
						proba = tabuProba;
					}
					else{
						if(e.getOpposite(current)!=dest){
							double ed = heuristic.h(current, e.getOpposite(current), dest);
							h = 1 / ed;
							pheromone = getPheromone(e, p.getRoot().getId()+"_"+dest.getId());
							proba = Math.pow(h, alpha)*Math.pow(pheromone, beta);
						}
						else {
							proba = 0.99;
						}
					}
					e.addAttribute("proba", proba);
					sum += proba;
				}
				
				//Select the edge with probability dependant of heuristic and pheromones
				Edge selected = null;
				Object[] neighbors = current.getLeavingEdgeSet().toArray();
				double rand = abcp.getRandom().nextDouble();
				double proba = 0;
				for(int i=0; i<neighbors.length && selected == null; i++){
					Edge e = (Edge) neighbors[i];
					proba += e.getNumber("proba")/sum;
					if(rand<proba){
						selected = e;
					}
				}
				p.add(current, selected);
				
				//Add the current node to the tabu list
				tabuList.add(p.peekEdge());
				if(tabuList.size()>tabuMemorySize)
					tabuList.remove(0);
			}
		}
		p.removeLoops();
		return p;
	}
	
	/*
	 * Manage the pheromones
	 */
	
	public void depositPheromone(){
		double length = p.getPathWeight(abcp.getWeightAttribute());
		for(Edge e : p.getEdgePath()){
			double pheromones = getPheromone(e, p.getRoot().getId()+"_"+dest.getId());
			pheromones += Q/length;
			setPheromone(e, p.getRoot().getId()+"_"+dest.getId(), pheromones);
		}
	}
	
	private double getPheromone(Edge e, String type){
		HashMap<String, Double> pheromones;
		if(!e.hasAttribute("pheromones")){
			pheromones = new HashMap<String, Double>();
			e.addAttribute("pheromones", pheromones);
		}
		else
			pheromones = (HashMap<String, Double>)(e.getAttribute("pheromones"));
		
		if(!pheromones.containsKey(type)){
			pheromones.put(type, initialPheromone);
			e.addAttribute("pheromones", pheromones);
		}
		
		return pheromones.get(type);
	}
	
	private void setPheromone(Edge e, String type, double value){
		HashMap<String, Double> pheromones;
		if(!e.hasAttribute("pheromones"))
			pheromones = new HashMap<String, Double>();
		else
			pheromones = (HashMap<String, Double>)(e.getAttribute("pheromones"));
		pheromones.put(type, value);
		e.addAttribute("pheromones", pheromones);
	}
	
	/*
	 * Manage tabu problem
	 */
	
	public boolean nextAreTabu(Node n){
		int nbNeighbor = 0;
		int nbNeighborTabu = 0;
		for(Edge v : n.getLeavingEdgeSet()){
			if(tabuList.contains(v.getOpposite(n)))
				nbNeighborTabu++;
			nbNeighbor++;
		}
		return nbNeighbor == nbNeighborTabu;
	}
	
	public void jumpBack(){
		if(p.size() == 0)
			p.clear();
		else{
			int nbDeleted = abcp.getRandom().nextInt(p.size()-1)+1;
			for(int i=0; i<nbDeleted; i++)
				p.popNode();
		}
	}
	
	/*
	 * Heuristic(s)
	 */
	
	public abstract class Heuristic {
		public abstract double h(Node current, Node next, Node dest);
	}
	
	public class RandomHeuristic extends Heuristic{
		/**
		 * Heuristics of euclidean distance
		 * return the euclidean distance between the two nodes in parameters 
		 */
		public double h(Node current, Node next, Node dest){
			return Math.random()+1;
		}
	}
}