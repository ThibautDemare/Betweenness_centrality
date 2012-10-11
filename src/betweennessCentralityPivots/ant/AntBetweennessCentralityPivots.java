package betweennessCentralityPivots.ant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

import org.graphstream.algorithm.Algorithm;
import org.graphstream.algorithm.ConnectedComponents;
import org.graphstream.algorithm.DynamicAlgorithm;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.Path;
import org.graphstream.stream.SinkAdapter;


public class AntBetweennessCentralityPivots extends SinkAdapter implements Algorithm, DynamicAlgorithm{
	/**
	 * Number of betweennessCentralityPivots.ant
	 */
	protected int antCount;

	/**
	 * List of betweennessCentralityPivots.ant
	 */
	protected ArrayList<Ant> listAnt;

	/**
	 * The name of the class of the entities
	 */
	protected String antClassName;
	
	/**
	 * The weight attribute on the edge.
	 */
	protected String weightAttribute;
	
	/**
	 * Object defining the random
	 */
	protected Random random;

	/**
	 * The graph
	 */
	protected Graph graph;
	
	/**
	 * Name of the attribute associated to the value of the betweenness centrality
	 */
	protected String betweennessCentralityAttribute;
	
	/**
	 * Maximum number of pair of node used in order to compute the centrality of all node
	 */
	protected int numberOfPairToUsed;
	
	/**
	 * List of all pair of nodes used by the algorithm.
	 */
	protected String[][] listPaire;
	
	/**
	 * List of shortest path computed by the ants and associated to each paire of nodes.
	 */
	protected Path[] listPath;
	
	/**
	 * The indice of the current pair.
	 */
	protected int currentPaire;
	
	/**
	 * End of the compute
	 */
	protected boolean end;
	
	/**
	 * Constructor of this class
	 * @param entityCount Number of betweennessCentralityPivots.ant used
	 */
	public AntBetweennessCentralityPivots(int antCount){
		//Number of entity used
		this.antCount = antCount;
		
		//Maximum number of entity used
		numberOfPairToUsed = 0;
		
		//The calculation is finished ?
		end = false;
		
		antClassName = Ant.class.getName();
		
		//Initialisation of the name of the attribute of the betweenness centrality
		betweennessCentralityAttribute = "AntBetweennessCentralityPivots";
		
		//The random object 
		random = new Random(System.currentTimeMillis());
	}

	/**
	 * Initialize the algorithm
	 */
	public void init(Graph arg) {
		setGraph(arg);
		
		//Add the attribute containing the betweenness centrality of the node 
		for(Node n : getGraph())
			n.addAttribute(getBetweennessCentralityAttribute(), 0);
		
		//and for the edges
		for(Edge e: getGraph().getEachEdge()){
			e.addAttribute(getBetweennessCentralityAttribute(), 0);
		}
		
		//Creation of the list which manage the entities
		setListAnt(new ArrayList<Ant>());
		for(int i=0; i<getAntCount(); i++){
			getListAnt().add(createAnt());
		}

		// Creation of the list of paire used
		currentPaire = 0;
		if(arg.hasAttribute("listPaire")){
			listPaire = (String[][]) arg.getAttribute("listPaire");
		}
		else{
			listPaire = new String[getNumberOfPairToUsed()][2];
			for(int i=0; i<listPaire.length; i++)
				listPaire[i] = selectPairNode();
			//Must be tested if the cast allow to pass String[][]
			arg.addAttribute("listPaire", listPaire);
		}
		
		listPath = new Path[getNumberOfPairToUsed()];
	}

	/**
	 * Terminate the algorithm
	 */
	public void terminate() {
		/*
		 * ...
		 */
	}
	
	//In order to see it is more fast.
	long t1=0, t2=0, d1=0, d2=0;
	
	/**
	 * The algorithm : compute each paths and define the centrality.
	 */
	public void compute() {
		int i = 1;
		t1 = System.currentTimeMillis();
		while(!end){
			System.out.println(i);
			step(i);
			i++;
			if(i>20000)
				end = true;
		}
		
		/*
		 * This section defines the betweeness centrality on all node and edge which are in shortest paths
		 */
		currentPaire = 0;
		for(int j=0; j<listPath.length; j++){
			Path p = listPath[j];
			System.out.println("j="+j+" p="+p);
			p.popNode();
			while(p.peekNode() != p.getRoot()){
				Node n = p.peekNode();
				Edge e = p.popEdge();
				
				if(e.hasAttribute(getBetweennessCentralityAttribute()))
					e.setAttribute(getBetweennessCentralityAttribute(), e.getNumber(getBetweennessCentralityAttribute())+1);
				else
					e.setAttribute(getBetweennessCentralityAttribute(), 0);
				
				if(n.hasAttribute(getBetweennessCentralityAttribute()))
					n.setAttribute(getBetweennessCentralityAttribute(), n.getNumber(getBetweennessCentralityAttribute())+1);
				else
					n.setAttribute(getBetweennessCentralityAttribute(), 0);
			}
		}
	}
	
	/**
	 * Get the pheromone on an edge and of a given type.
	 * @param e the edge
	 * @param type the type of pheromone
	 * @return the value of pheromone on the edge
	 */
	private double getPheromone(Edge e, String type){
		if(!e.hasAttribute("pheromones"))
			return 0.;
		HashMap<String, Double> pheromones = (HashMap<String, Double>)(e.getAttribute("pheromones"));
		
		if(pheromones.containsKey(type))
			pheromones.put(type, random.nextDouble());
		
		return pheromones.get(type);
	}
	
	/**
	 * Execute one step of the algorithm.
	 */
	public void step(int etape){
		for(Ant en : listAnt){
			Path p = en.makePath();
			if(listPath[currentPaire]== null || listPath[currentPaire].getPathWeight(weightAttribute)>p.getPathWeight(weightAttribute)){
				listPath[currentPaire] = p;
			}
		}
		//It is not done before, in the for, in order to avoid the creation of many useless copy of path which obstruct the memory space.
		listPath[currentPaire] = listPath[currentPaire].getACopy();
		
		for(Ant en : listAnt){
			en.depositPheromone();
		}
		
		currentPaire++;
		if(currentPaire >= numberOfPairToUsed)
			currentPaire = 0;
		
		if(etape%numberOfPairToUsed == 0){
			//In order to see it is more fast.
			t2 = System.currentTimeMillis();
			d2 = t2 - t1;
			System.out.println("Différence de temps = "+(d2-d1));
			d1 = d2;
			t1 = t2;
			
			evaporatePheromone();
		}
	}

	/**
	 * Return the current pair of node computed
	 * @return
	 */
	public String[] getCurrentPaire(){
		return listPaire[currentPaire];
	}
	
	/**
	 * Return a new pair of node.
	 * @return a pair of node
	 */
	public String[] selectPairNode(){
		Node n1, n2;
		do{
			n1 = getGraph().getNode(getRandom().nextInt(getGraph().getNodeCount()));
			n2 = getGraph().getNode(getRandom().nextInt(getGraph().getNodeCount()));
			ArrayList<Node> possibilities = isTherePath(n1, n2);
			
			//If there is no path, we must change
			if(possibilities != null){
				//If the origin node is not isolated
				if(possibilities.size()>1){
					n2 = possibilities.get(random.nextInt(possibilities.size()-1)+1);
				}
				else{
					n1 = null;
				}
			}
		}while(n1 == null);
		return new String[]{n1.getId(), n2.getId()};
	}
	
	/**
	 * Search a path between an origin node and a destination node. If a path is found, the returned list is null (because the reachable node list is not necessary), else, the returned list contains all reachable node. 
	 * @param origin The origin node
	 * @param dest The destination node
	 * @return	A list containing all reachable node if no path is found, or null if a path is found.
	 */
	private ArrayList<Node> isTherePath(Node origin, Node dest){
		ArrayList<Node> open = new ArrayList<Node>();
		ArrayList<Node> close = new ArrayList<Node>();
		open.add(origin);
		while(!open.isEmpty()){
			Node current = open.remove(0);
			close.add(current);
			for(Edge e : current.getLeavingEdgeSet()){
				Node o = e.getOpposite(current);
				if(o == dest)
					return null;
				if(!close.contains(o) && !open.contains(o))
					open.add(o);
			}
		}
		return close;
	}
	
	private void evaporatePheromone(){
		/*
		 * Voir pour supprimer les pheromones qui sont a zero afin de limiter la memoire utilisee.
		 */
		int i = 0;
		double sum = 0;
		double max = Double.NEGATIVE_INFINITY;
		double min = Double.POSITIVE_INFINITY;
		int nbPetits = 0;
		for(Edge e : graph.getEdgeSet()){
			if(e.hasAttribute("pheromones")){
				HashMap<String, Double> pheromones = (HashMap<String, Double>)(e.getAttribute("pheromones"));
				for(String s : pheromones.keySet()){
					double d = pheromones.get(s);
					d = d*Ant.rho;
					if(d<Ant.minPheromone)
						d = Ant.minPheromone;
					sum += d;
					max = Math.max(d, max);
					min = Math.min(d, min);
					if(d<10)
						nbPetits ++ ;
					i++;
					pheromones.put(s, d);
				}
			}
		}
		System.out.println("moyenne pheromone = "+sum/i);
		System.out.println("max pheromone = "+max);
		System.out.println("min pheromone = "+min);
		System.out.println("nb small = "+nbPetits);
	}
	
	/**
	 * Create a new entity object 
	 * @return a new entity
	 */
	public Ant createAnt() {
		try {
			Object o = Class.forName(getAntClassName()).newInstance();
			if(o instanceof Ant) {
				Ant e = (Ant) o;
				e.init(this);
				
				return e;
			} else {
				System.err.printf("Object %s pointed at by class name '%s' does not implement Ant.%n", o.getClass().getName(), getAntClassName());
			}
		} catch (Exception e) {
			System.err.printf("Error: %s%n", e.getMessage());
			e.printStackTrace();
		}

		return null;
	}
	
	/*
	 * Functions whose the goal is to manage the average attributes.
	 */
	
	public void clear(){
		String att = "random_walk_betweenness_centrality";
		String param = getNumberOfPairToUsed()+"_"+antClassName;
		
		for(Node n : graph){
			Iterator<String> it = n.getAttributeKeyIterator();
			while(it!=null && it.hasNext()){
				String s = it.next();
				if(s.contains(att+"_sum_"+param)){
					n.setAttribute(s, 0);
				}else if(s.contains(att+"_average_"+param)){
					n.setAttribute(s, 0);
				}else if(s.contains(att+"_nb_computed_"+param)){
					n.setAttribute(s, 0);
				}
			}
		}
		
		for(Edge e : graph.getEachEdge()){
			Iterator<String> it = e.getAttributeKeyIterator();
			while(it!=null && it.hasNext()){
				String s = it.next();
				if(s.contains(att+"_sum_"+param)){
					e.setAttribute(s, 0);
				}else if(s.contains(att+"_average_"+param)){
					e.setAttribute(s, 0);
				}else if(s.contains(att+"_nb_computed_"+param)){
					e.setAttribute(s, 0);
				}
			}
		}
	}
	
	public void addAverage(){
		String att = "random_walk_betweenness_centrality";
		String param = getNumberOfPairToUsed()+"_"+antClassName;
		
		for(Node n : graph){
			n.addAttribute(att+"_sum_"+param, 0);
			n.addAttribute(att+"_nb_computed_"+param, 0);
			n.addAttribute(att+"_average_"+param, 0);
		}
		
		for(Edge e : graph.getEdgeSet()){
			e.addAttribute(att+"_sum_"+param, 0);
			e.addAttribute(att+"_nb_computed_"+param, 0);
			e.addAttribute(att+"_average_"+param, 0);
		}
	}
	
	/**
	 * Save the values of each nodes and edges in some attributes in order to save the average values of each instances
	 */
	public void saveCurrentValuesInAverageValues(){
		String att1 = "random_walk_betweenness_centrality";
		String att2 = getBetweennessCentralityAttribute();
		String param = "_"+getNumberOfPairToUsed()+"_"+antClassName;
		
		for(Node n : graph.getNodeSet()){
			n.setAttribute(att1+"_sum"+param, n.getNumber(att1+"_sum"+param)+n.getNumber(att2));
			n.setAttribute(att1+"_nb_computed"+param, n.getNumber(att1+"_nb_computed"+param)+1);
			n.setAttribute(att1+"_average"+param, n.getNumber(att1+"_sum"+param)/n.getNumber(att1+"_nb_computed"+param));
		}
		
		for(Edge e : graph.getEdgeSet()){
			e.setAttribute(att1+"_sum"+param, e.getNumber(att1+"_sum"+param)+e.getNumber(att2));
			e.setAttribute(att1+"_nb_computed"+param, e.getNumber(att1+"_nb_computed"+param)+1);
			e.setAttribute(att1+"_average"+param, e.getNumber(att1+"_sum"+param)/e.getNumber(att1+"_nb_computed"+param));
		}
	}

	/*
	 * Getters and setters
	 */
	
	public double getBetweennessCentrality(Edge e){
		return e.getNumber(betweennessCentralityAttribute);
	}

	public double getBetweennessCentrality(Node n){
		return n.getNumber(betweennessCentralityAttribute);
	}
	
	public synchronized Random getRandom() {
		return random;
	}

	public synchronized void setRandom(Random random) {
		this.random = random;
	}
	
	public synchronized String getAntClassName(){
		return antClassName;
	}
	
	public synchronized void setAntClassName(String s){
		antClassName = s;
	}
	
	public synchronized Graph getGraph() {
		return graph;
	}

	public synchronized void setGraph(Graph graph) {
		this.graph = graph;
	}

	public synchronized String getBetweennessCentralityAttribute() {
		return betweennessCentralityAttribute;
	}

	public synchronized void setBetweennessCentralityAttribute(
			String betweennessCentralityAttribute) {
		this.betweennessCentralityAttribute = betweennessCentralityAttribute;
	}

	public String getWeightAttribute() {
		return weightAttribute;
	}

	public void setWeightAttribute(String weightAttribute) {
		this.weightAttribute = weightAttribute;
	}

	public synchronized int getAntCount() {
		return antCount;
	}

	public synchronized void setAntCount(int antCount) {
		this.antCount = antCount;
	}

	public synchronized ArrayList<Ant> getListAnt() {
		return listAnt;
	}

	public synchronized void setListAnt(ArrayList<Ant> listAnt) {
		this.listAnt = listAnt;
	}

	/*
	 * Listener
	 */
	
	public int getNumberOfPairToUsed() {
		return numberOfPairToUsed;
	}

	public void setNumberOfPairToUsed(int numberOfPairToUsed) {
		this.numberOfPairToUsed = numberOfPairToUsed;
	}

	public void edgeAdded(String graphId, long timeId, String edgeId,
			String fromNodeId, String toNodeId, boolean directed) {
		Edge edge = getGraph().getEdge(edgeId);

		if (edge != null)
			edge.addAttribute(getBetweennessCentralityAttribute(), 0.0);
	}

	public void nodeAdded(String graphId, long timeId, String nodeId) {
		Node node = getGraph().getNode(nodeId);

		node.addAttribute(getBetweennessCentralityAttribute(), 0.0);
	}
}
