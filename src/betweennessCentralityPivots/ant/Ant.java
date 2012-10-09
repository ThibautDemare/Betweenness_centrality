package betweennessCentralityPivots.ant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;
import org.graphstream.graph.Path;

import mains.Main;

public class Ant {
	/**
	 * 
	 */
	protected AntBetweennessCentralityPivots abcp;
	
	/**
	 * The path constructed by the ant
	 */
	protected Path p;
	
	/**
	 * Node where the ant is
	 */
	protected Node current;
	
	/**
	 * Node of destination
	 */
	protected Node dest;
	
	/**
	 * Heuristic used in order to choose the next node
	 */
	protected Heuristic heuristic;
	
	/**
	 * Parameters for tabu node
	 */
	protected ArrayList<Edge> tabuList;
	protected int tabuMemorySize = 200;
	protected double tabuProba = 1.0E-12;
	
	/**
	 * Paramters for pheromones
	 */
	protected static double Q = 1;
	protected static double alpha = 2;
	protected static double beta = 2;
	protected static double rho = 0.95;
	protected static double initialPheromone = 0.1;
	
	public void init(AntBetweennessCentralityPivots abcp){
		this.abcp = abcp;
		p = new Path();
		tabuList = new ArrayList<Edge>();
		heuristic = new EuclideanHeuristic();
	}
	
	public Path makePath(){
		//System.out.println("Make path");
		
		//p.clear();
		p = new Path();
		tabuList.clear();
		
		String[] paire = abcp.getCurrentPaire();
		current = abcp.getGraph().getNode(paire[0]);
		dest = abcp.getGraph().getNode(paire[1]);
		p.setRoot(current);
		
		while(!p.peekNode().equals(dest)){
			
			/*if(current == p.getRoot())
				current.addAttribute("ui.style", "fill-color : black; size : 10px;");
			else
				current.addAttribute("ui.style", "fill-color : blue; size : 1px;");/**/
			//Main.attente("");
			current = p.peekNode();
			
			/*current.addAttribute("ui.style", "fill-color : grey; size : 10px;");
			try {
				Thread.sleep(100);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}/**/
			
			if(nextAreTabu(current) || current.getLeavingEdgeSet().toArray().length == 0){
				if(!current.equals(p.getRoot()))
					jumpBack();
				else
					tabuList.clear();
			}
			else{
				if(current.getEdgeSet().size()>0){
					double sum = 0;
					double h;
					double pheromone;
					
					double minProba = Double.POSITIVE_INFINITY;
					double nbNoTabu = 0;
					for(Edge e : current.getLeavingEdgeSet()){
						double proba;
						if(!tabuList.contains(e)){
							if(e.getOpposite(current)!=dest){
								double ed = heuristic.h(current, e.getOpposite(current), dest);//e.getNumber(abcp.getWeightAttribute());//euclideanDistance(e.getOpposite(current), dest);//
								h = 1 / ed;
								pheromone = getPheromone(e, p.getRoot().getId()+"_"+dest.getId());
								//System.out.println("h = "+h+" et h^alpha = "+Math.pow(h, alpha)+" et pheromone^beta = "+Math.pow(pheromone, beta));
								proba = Math.pow(h, alpha)*Math.pow(pheromone, beta);
							}
							else {
								proba = 1.;
							}
							minProba = Math.min(minProba, proba);
							e.addAttribute("proba", proba);
							sum += proba;
							nbNoTabu++;
						}
					}
					
					if(minProba == Double.POSITIVE_INFINITY || nbNoTabu <=1)
						minProba = tabuProba;
						
					for(Edge e : current.getLeavingEdgeSet()){
						if(tabuList.contains(e)){
							e.addAttribute("proba", minProba);
							sum += minProba;
						}
					}
					
					//Test que la proba est égale à 1
					double testProba = 0.;
					for(Edge e : current.getLeavingEdgeSet()){
						testProba += e.getNumber("proba")/sum;
						if(e.getNumber("proba") == Double.NaN)
							System.out.println("e.getNumber('proba')/sum = "+e.getNumber("proba")/sum +" et e.getNumber('proba') = " + e.getNumber("proba") + " et sum = "+sum);
					}
					//System.out.println(testProba);
					
					//Select the edge with probability dependant of heuristic and pheromone
					Edge selected = null;
					Object[] neighbors = current.getLeavingEdgeSet().toArray();
					double rand = abcp.getRandom().nextDouble();
					double proba = 0;
					for(int i=0; i<neighbors.length && selected == null; i++){
						Edge e = (Edge) neighbors[i];
						proba += e.getNumber("proba")/sum;
						//System.out.println("proba e = "+e.getNumber("proba") + " et resultat = "+ e.getNumber("proba")/sum);
						if(rand<=proba){
							//System.out.println("proba e = "+e.getNumber("proba")+" et i = "+i);
							selected = e;
						}
					}
					
					p.add(current, selected);
					//Add the current node to the tabu list
					tabuList.add(p.peekEdge());
					if(tabuList.size()>tabuMemorySize)
						tabuList.remove(0);
				}
					
				/*
				 * Revoir la manière d'utiliser la tabu list : 
				 * les éléments qui la constitue peuvent être pris mais avec une proba très très faible
				 * Plutot que de contenir des noeuds la tabu list devrait contenir des aretes.
				 * 
				 * Revoir également la sélection des aretes avec une roue de la fortune un peu mieux fichu!
				 * placer les arcs sur tableau, tirer un dé, parcourir le tableau en sommant leur proba, et quand la somme dépasse la valeur tiré aléatoirement alors on a le bon arc
				 */
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
		int nbDeleted = abcp.getRandom().nextInt(p.size()-1)+1;
		for(int i=0; i<nbDeleted; i++)
			p.popNode();
	}
	
	/*
	 * Heuristic(s)
	 */
	
	public abstract class Heuristic {
		public abstract double h(Node current, Node next, Node dest);
	}
	
	public class EuclideanHeuristic extends Heuristic{
		/**
		 * Heuristics of euclidean distance
		 * return the euclidean distance between the two nodes in parameters 
		 */
		public double h(Node current, Node next, Node dest){
			Object[] xyz1;
			Object[] xyz2;
			
			if(next.hasAttribute("xyz")){
				xyz1=next.getAttribute("xyz");
				xyz2=dest.getAttribute("xyz");
			}
			else if(next.hasAttribute("xy")){
				xyz1=next.getAttribute("xy");
				xyz2=dest.getAttribute("xy");
			}
			else{
				xyz1=new Double[2];
				xyz1[0]=next.getNumber("x");
				xyz1[1]=next.getNumber("y");
				
				xyz2=new Double[2];
				xyz2[0]=dest.getNumber("x");
				xyz2[1]=dest.getNumber("y");
			}
			
			return Math.sqrt(
					  ((Double)(xyz1[0])-(Double)(xyz2[0]))*((Double)(xyz1[0])-(Double)(xyz2[0]))
					 +((Double)(xyz1[1])-(Double)(xyz2[1]))*((Double)(xyz1[1])-(Double)(xyz2[1])));
		}
	}
	
	public class RandomHeuristic extends Heuristic{
		/**
		 * Heuristics of euclidean distance
		 * @return the euclidean distance between the two nodes in parameters 
		 */
		public double h(Node current, Node next, Node dest){
			return Math.random()+1;
		}
	}
	
	public class SmallestEdgeHeuristic extends Heuristic{
		/**
		 * Heuristics of euclidean distance
		 * @return the euclidean distance between the two nodes in parameters 
		 */
		public double h(Node current, Node next, Node dest){
			return current.getEdgeToward(next).getNumber(abcp.getWeightAttribute());
		}
	}
}
