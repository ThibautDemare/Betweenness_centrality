package betweennessCentralityPivots.ant;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import org.graphstream.algorithm.Algorithm;
import org.graphstream.algorithm.Dijkstra;
import org.graphstream.algorithm.DynamicAlgorithm;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.Path;
import org.graphstream.stream.SinkAdapter;

import classicalBetweennessCentrality.BetweennessCentrality;


public class AntBetweennessCentralityPivots extends SinkAdapter implements Algorithm, DynamicAlgorithm{
	/**
	 * Number of betweennessCentralityPivots.ant
	 */
	protected int antCount;

	/**
	 * List of betweennessCentralityPivots.ant
	 */
	protected Ant[] listAnt;

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
	protected Node[][] listPaire;
	
	/**
	 * List of shortest path computed by the ants and associated to each paire of nodes.
	 */
	protected Path[] listPath;
	
	/**
	 * The index of the current pair.
	 */
	protected int indexCurrentPaire;
	
	/**
	 * End of the compute
	 */
	protected boolean end;
	
	/**
	 * Number of the step
	 */
	protected int step;
	
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
		
		//The class name of the ant. The user could change the ant.
		antClassName = Ant.class.getName();
		
		//Step number
		step = 0;
		
		//Initialisation of the name of the attribute of the betweenness centrality
		betweennessCentralityAttribute = "AntBetweennessCentralityPivots";
		
		//The random object 
		random = new Random(System.currentTimeMillis());//987654321//123456789//
	}

	/**
	 * Initialize the algorithm
	 */
	public void init(Graph arg) {
		setGraph(arg);
		
		//Add the attribute containing the betweenness centrality of the node 
		for(Node n : getGraph())
			n.addAttribute(getBetweennessCentralityAttribute(), 0.);
		
		//and for the edges
		for(Edge e: getGraph().getEachEdge()){
			e.addAttribute(getBetweennessCentralityAttribute(), 0.);
		}
		
		//Creation of the list which manage the entities
		setListAnt(new Ant[getAntCount()]);
		for(int i=0; i<getAntCount(); i++){
			getListAnt()[i] = createAnt();
		}

		// Creation of the list of paire used
		indexCurrentPaire = 0;
		if(arg.hasAttribute("listPaire")){
			listPaire = (Node[][]) arg.getAttribute("listPaire");
		}
		else{
			listPaire = new Node[getNumberOfPairToUsed()][2];
			for(int i=0; i<listPaire.length; i++)
				listPaire[i] = selectPairNode();
			//Must be tested if the cast allow to pass String[][]
			//arg.addAttribute("listPaire", listPaire);
		}
		
		listPath = new Path[getNumberOfPairToUsed()];
		
		System.out.println("Compute with Brandes' algorithm");
		BetweennessCentrality bcb = new BetweennessCentrality();
		bcb.setWeightAttributeName("length");
		bcb.setWeighted();
		bcb.setCentralityAttributeName("InitialBetweennessCentrality");
		bcb.setPutPheromones(true);
		bcb.setListNode(listPaire);
		bcb.init(graph);
		bcb.compute();
	}

	/**
	 * Terminate the algorithm
	 */
	public void terminate() {
		/*
		 * ...
		 */
	}
	
	/**
	 * The algorithm : compute each paths and define the centrality.
	 */
	public void compute() {
		System.out.println("Compute with ant co");
		while(!end){
			System.out.println(step);
			step();
			step++;
			if(step>6000)
				end = true;
		}
		
		/*
		 * This section defines the betweeness centrality on all node and edge which are in shortest paths
		 */
		indexCurrentPaire = 0;
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
	
	public void compareResultat(){
		for(int j=0; j<listPath.length; j++){
			Path p = listPath[j];
			Dijkstra dijkstra = new Dijkstra(Dijkstra.Element.EDGE, null, "length");
			dijkstra.init(graph);
			dijkstra.setSource(p.getRoot());
			dijkstra.compute();
			System.out.println("différence = "+(p.getPathWeight("length")-dijkstra.getPathLength(p.peekNode())));
			System.out.println("Longueur Dijsktra = "+dijkstra.getPathLength(p.peekNode()));
			System.out.println("Longueur fourmis = "+p.getPathWeight("length"));
			int nbNodeCommun = 0;
			for(Node n : p.getNodeSet()){
				if(dijkstra.getPath(p.peekNode()).contains(n))
					nbNodeCommun++;
			}
			System.out.println("Nb node en commun = "+nbNodeCommun);
			System.out.println("Nb node dijsktra = "+dijkstra.getPath(p.peekNode()).getNodeCount());
		}
	}
	
	/**
	 * Execute one step of the algorithm.
	 */
	public void step(){
		for(Ant en : listAnt){
			Path p = en.makePath();
			if(listPath[indexCurrentPaire]== null || listPath[indexCurrentPaire].getPathWeight(weightAttribute)>p.getPathWeight(weightAttribute)){
				listPath[indexCurrentPaire] = p;
			}
		}
		//It is not done before, in the for, in order to avoid the creation of many useless copy of path which obstruct the memory space.
		listPath[indexCurrentPaire] = listPath[indexCurrentPaire].getACopy();
		
		for(Ant en : listAnt){
			en.depositPheromone();
		}
		
		indexCurrentPaire++;
		if(indexCurrentPaire >= numberOfPairToUsed)
			indexCurrentPaire = 0;
		
		if((step+1)%numberOfPairToUsed == 0){
			//In order to see if the algorithm compute "good" solution.
			//compareResultat();
			evaporatePheromone();
		}
	}

	/**
	 * Return the current pair of node computed
	 * @return
	 */
	public Node[] getCurrentPaire(){
		return listPaire[indexCurrentPaire];
	}
	
	/**
	 * Return the index of the current pair of node computed
	 * @return
	 */
	public int getIndexCurrentPaire(){
		return indexCurrentPaire;
	}
	
	/**
	 * Return a new pair of node.
	 * @return a pair of node
	 */
	public Node[] selectPairNode(){
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
		return new Node[]{n1, n2};
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
		//Some attributes in order to analyse evolution of pheromone quantity => not in final version
		int cpt = 0;
		double sum = 0;
		double max = Double.NEGATIVE_INFINITY;
		double min = Double.POSITIVE_INFINITY;
		double nbMax = 0;
		double nbMin = 0;
		for(Edge e : graph.getEdgeSet()){
			if(e.hasAttribute("pheromones")){
				double[] pheromones = e.getAttribute("pheromones");
				for(int i = 0; i<pheromones.length; i++){
					double d = pheromones[i];
					d = d*Ant.rho;
					
					if(d<Ant.minPheromone){
						d = Ant.minPheromone;
						nbMin++;
					}
					if(d > Ant.maxPheromones){
						d = Ant.maxPheromones;
						nbMax++;
					}
					pheromones[i] = d;
					//piece of code in order to analyse the evolution of pheromone => it will be removed in final version
					sum += d;
					max = Math.max(d, max);
					min = Math.min(d, min);
					cpt++;
					
				}
				e.setAttribute("pheromones", pheromones);
			}
		}
		Ant.maxPathLength = Double.NEGATIVE_INFINITY;
		Ant.minPathLength = Double.POSITIVE_INFINITY;
		
		//piece of code in order to analyse the evolution of pheromone => it will be removed in final version
		System.out.println("moyenne pheromone = "+sum/cpt);
		System.out.println("max pheromone = "+max);
		System.out.println("min pheromone = "+min);
		System.out.println("nbMax = "+nbMax);
		System.out.println("nbMin = "+nbMin);
		System.out.println("nbMinMax = "+(nbMax+nbMin));
		System.out.println("nb = "+cpt);
		System.out.println("nb arrete dans PCCC = "+listPath[indexCurrentPaire].getEdgeCount());/**/
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
	
	public int getStep() {
		return step;
	}

	public void setStep(int step) {
		this.step = step;
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

	public synchronized Ant[] getListAnt() {
		return listAnt;
	}

	public synchronized void setListAnt(Ant[] listAnt) {
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
