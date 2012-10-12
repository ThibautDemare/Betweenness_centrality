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
	
	protected Edge[] tabuList;
	protected int tabuMemorySize = 20;
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
		tabuList = new Edge[tabuMemorySize];
		heuristic = new RandomHeuristic();
	}
	
	protected Path makePath(){
		//Variable for compute proba
		double sum = 0;
		double h;
		double pheromone;
		double proba;
		double rand;
		
		//initiate path
		p.clear();
		String[] paire = abcp.getCurrentPaire();
		current = abcp.getGraph().getNode(paire[0]);
		dest = abcp.getGraph().getNode(paire[1]);
		p.setRoot(current);
		
		//initiate tabu list
		clearTabuList();
		int iTabuList = 0;
		
		while(p.peekNode()!=dest){
			current = p.peekNode();
			
			if(current.getLeavingEdgeSet().size()<=0){
				jumpBack();
			}
			else{
				sum = 0.;
				for(Edge e : current.getLeavingEdgeSet()){
					if(tabuListContains(e)){
						proba = tabuProba;
					}
					else{
						if(e.getOpposite(current)!=dest){
							h = 1 / heuristic.h(current, e.getOpposite(current), dest);;
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
				rand = abcp.getRandom().nextDouble();
				proba = 0;
				for(int i=0; i<current.getLeavingEdgeSet().size() && selected == null; i++){
					Edge e = current.getLeavingEdge(i);
					proba += e.getNumber("proba")/sum;
					if(rand<proba){
						selected = e;
					}
				}
				p.add(current, selected);
				
				//Add the current node to the tabu list
				tabuList[iTabuList++] = p.peekEdge();
				if(iTabuList>=tabuMemorySize)
					iTabuList = 0;
			}
		}
		p.removeLoops();
		return p;
	}
	
	/*
	 * Manage the pheromones
	 */
	
	protected void depositPheromone(){
		double length = p.getPathWeight(abcp.getWeightAttribute());
		double pheromones;
		for(Edge e : p.getEdgePath()){
			pheromones = getPheromone(e, p.getRoot().getId()+"_"+dest.getId());
			pheromones += Q/length;
			setPheromone(e, p.getRoot().getId()+"_"+dest.getId(), pheromones);
		}
	}
	
	private double getPheromone(Edge e, String type){
		double[] pheromones;
		if(!e.hasAttribute("pheromones")){
			pheromones = new double[abcp.getNumberOfPairToUsed()];
			//System.out.println("here");
		}
		else{
			pheromones = e.getAttribute("pheromones");
			//System.out.println("or here?");
		}
		//System.out.println("pheromones[abcp.getIndexCurrentPaire()] = "+pheromones[abcp.getIndexCurrentPaire()]);
		if(!( (Double)(pheromones[abcp.getIndexCurrentPaire()]) == null) || (pheromones[abcp.getIndexCurrentPaire()] == 0.) )
			pheromones[abcp.getIndexCurrentPaire()] = initialPheromone;
		
		e.addAttribute("pheromones", pheromones);
		
		return pheromones[abcp.getIndexCurrentPaire()];
	}
	
	private void setPheromone(Edge e, String type, double value){
		double[] pheromones;
		if(!e.hasAttribute("pheromones"))
			pheromones = new double[abcp.getNumberOfPairToUsed()];
		else
			pheromones = e.getAttribute("pheromones");
		pheromones[abcp.getIndexCurrentPaire()] = value;
		e.addAttribute("pheromones", pheromones);
	}
	
	/*
	 * Manage tabu problem
	 */
	
	public boolean tabuListContains(Edge e){
		for(int i = 0; i < tabuList.length; i++)
			if(e == tabuList[i])
				return true;
		return false;
	}
	
	public void clearTabuList(){
		for(int i = 0; i<tabuList.length; i++)
			tabuList[i] = null;
	}
	
	public boolean nextAreTabu(Node n){
		int nbNeighbor = 0;
		int nbNeighborTabu = 0;
		for(Edge v : n.getLeavingEdgeSet()){
			if(tabuListContains(v))
				nbNeighborTabu++;
			nbNeighbor++;
		}
		return nbNeighbor == nbNeighborTabu;
	}
	
	public void jumpBack(){
		if(p.size() <= 1){
			Node r = p.getRoot();
			p.clear();
			p.setRoot(r);
		}
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