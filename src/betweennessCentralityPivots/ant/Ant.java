package betweennessCentralityPivots.ant;

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
	protected int tabuMemorySize = 50;
	protected double tabuProba = 1.0E-15;

	public static final double alpha = 2;//h
	public static final double beta = 6;//p
	public static final double rho = 0.8;
	public static final double initialPheromone = 500.;
	protected static double minPheromone = 10.;
	protected static double maxPheromones = Double.POSITIVE_INFINITY;//75.;
	protected static double maxPathLength = Double.NEGATIVE_INFINITY;
	protected static double minPathLength = Double.POSITIVE_INFINITY;

	public void init(AntBetweennessCentralityPivots abcp){
		this.abcp = abcp;
		p = new Path();
		tabuList = new Edge[tabuMemorySize];
		heuristic = new RandomHeuristic();
	}

	protected Path makePath(){
		//Variable in order to compute proba
		double sum = 0.;
		double h;
		double pheromone;
		double proba;
		double rand;

		//initiate path
		p.clear();
		Node[] paire = abcp.getCurrentPaire();
		current = paire[0];
		dest = paire[1];
		p.setRoot(current);

		//initiate tabu list
		clearTabuList();
		int iTabuList = 0;

		while(p.peekNode()!=dest){
			current = p.peekNode();
			
			if(current.getLeavingEdgeSet().size()<=0){
				//There is no escape ! The ant must go back.
				jumpBack();
			}
			else{
				sum = 0.;
				for(Edge e : current.getLeavingEdgeSet()){
					if(tabuListContains(e)){
						proba = tabuProba;
					}
					else{
						h = 2. / heuristic.h(current, e.getOpposite(current), dest);
						pheromone = 1. + getPheromone(e, abcp.getIndexCurrentPaire(), abcp.getNumberOfPairToUsed());
						proba = Math.pow(h, alpha)*Math.pow(pheromone, beta);
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
		maxPathLength = Math.max(maxPathLength, p.getPathWeight("length"));
		minPathLength = Math.min(minPathLength, p.getPathWeight("length"));
		return p;
	}
	/*
	 * Manage the pheromones
	 */

	protected void depositPheromone(){
		double length = 1. + (1+p.getPathWeight(abcp.getWeightAttribute())-minPathLength) / (1+maxPathLength-minPathLength);
		double pheromones;
		for(Edge e : p.getEdgePath()){
			pheromones = getPheromone(e, abcp.getIndexCurrentPaire(), abcp.getNumberOfPairToUsed());
			pheromones += 1. / length;
			setPheromone(e, abcp.getIndexCurrentPaire(), abcp.getNumberOfPairToUsed(), pheromones);
		}
	}

	public static double getPheromone(Edge e, int type, int numberType){
		double[] pheromones;
		if(!e.hasAttribute("pheromones"))
			pheromones = new double[numberType];
		else
			pheromones = e.getAttribute("pheromones");
		
		if((pheromones[type] == 0.) )
			pheromones[type] = minPheromone;

		e.addAttribute("pheromones", pheromones);
		return pheromones[type];
	}

	public static void setPheromone(Edge e, int type, int numberType, double value){
		double[] pheromones;
		if(!e.hasAttribute("pheromones"))
			pheromones = new double[numberType];
		else
			pheromones = e.getAttribute("pheromones");
		pheromones[type] = value;
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
		 * Not really a heuristic
		 * return a random value between [1.; 2.[
		 */
		public double h(Node current, Node next, Node dest){
			return Ant.this.abcp.getRandom().nextDouble()+1;
		}
	}
}