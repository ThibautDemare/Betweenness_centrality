package betweennessCentralityPivots;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import org.graphstream.algorithm.Algorithm;
import org.graphstream.algorithm.ConnectedComponents;
import org.graphstream.algorithm.DynamicAlgorithm;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.stream.SinkAdapter;

import betweennessCentralityPivots.randomWalk.RandomWalkEntity;

public class BetweennessCentralityPivots extends SinkAdapter implements Algorithm, DynamicAlgorithm{
	/**
	 * Number of entities
	 */
	protected int entityCount;

	/**
	 * List of distributed entity managers
	 */
	protected ArrayList<Entity> listEntity;

	/**
	 * Object defining the random
	 */
	protected Random random;

	/**
	 * The graph
	 */
	protected Graph graph;
	
	/**
	 * The name of the class of the entities
	 */
	protected String entityClassName;
	
	/**
	 * Number of computed path between each pair of node
	 */
	protected int pathCount;
	
	/**
	 * Name of the attribute associated to the value of the betweenness centrality
	 */
	protected String betweennessCentralityAttribute;
	
	/**
	 * Number of pair of node used in order to compute the centrality of all node
	 */
	protected int numberOfPairUsed;
	
	/**
	 * Maximum number of pair of node used in order to compute the centrality of all node
	 */
	protected int numberOfPairToUsed;
	
	/**
	 * Current pair
	 */
	protected PairNode currentPair;
	
	/**
	 * Boolean in order to call the garbage collector
	 */
	private boolean gc = false;
	
	/**
	 * End of the compute
	 */
	protected boolean end;
	
	/**
	 * Constructor of this class
	 * @param entityCount Number of entity used
	 */
	public BetweennessCentralityPivots(int entityCount){
		//Number of entity used
		this.entityCount = entityCount;
		
		//Name of the class of the walker entity 
		entityClassName = RandomWalkEntity.class.getName();
		
		//Number of computed path 
		pathCount = 1;
		
		//Number of entity used
		numberOfPairUsed = 0;
		
		//Maximum number of entity used
		numberOfPairToUsed = 0;
		
		//The calculation is finished ?
		end = false;
		
		//Initialisation of the name of the attribute of the betweenness centrality
		betweennessCentralityAttribute = "BetweennessCentralityPivots";
		
		//The object random
		random = new Random(System.currentTimeMillis());
	}

	/**
	 * Compute one step of the algorithm
	 */
	public void compute() {
		Runtime r = Runtime.getRuntime();
		while(!end){
			step();
			if(getNumberOfPairUsed()%500==0 && !gc){
				r.gc();
				gc = false;
			}
		}
	}
	
	/**
	 * Order to all entities to exucte one step  
	 */
	public void step(){
		for(Entity en : listEntity){
			en.step();
		}
	}
	
	/**
	 * Initialize the distributed random walk
	 */
	public void init(Graph arg) {
		setGraph(arg);
		
		ConnectedComponents cc = new ConnectedComponents(arg);
		cc.setCountAttribute("ConnectedComponentId");
		cc.compute();
		
		//Add or initialize the attribute containing the betweenness centrality of the node 
		for(Node n : getGraph())
			n.addAttribute(getBetweennessCentralityAttribute(), 0);
		
		//and for the edges
		for(Edge e: getGraph().getEachEdge()){
			e.addAttribute(getBetweennessCentralityAttribute(), 0);
		}
		
		//Creation of the list of entities
		setListEntity(new ArrayList<Entity>());
		for(int i=0; i<getEntityCount(); i++){
			getListEntity().add(createEntity());
		}
		
	}
	
	/**
	 * Create a new entity object 
	 * @return a new entity
	 */
	public Entity createEntity() {
		try {
			Object o = Class.forName(getEntityClassName()).newInstance();
			if(o instanceof Entity) {
				Entity e = (Entity) o;
				e.init(this);
				return e;
			} else {
				System.err.printf("Object %s pointed at by class name '%s' does not implement Entity.%n", o.getClass().getName(), getEntityClassName());
			}
		} catch (Exception e) {
			System.err.printf("Error: %s%n", e.getMessage());
			e.printStackTrace();
		}

		return null;
	}
	
	/**
	 * Inform all of the Entity that the compute is finished
	 */
	public void terminate() {
		for(Entity en : getListEntity())
			en.setTerminate(true);
	}

	/**
	 * Return the next pair of node. If the current pair have been computed, it return a new pair
	 * @return a pair of node
	 */
	public void selectPairNode(PairNode pn){
		if(pn==null || pn.getNode1()==null || pn.getNode2()==null || pn.getTakenCount()>=getPathCount()){
			Node n1, n2;
			do{
				n1 = getGraph().getNode(getRandom().nextInt(getGraph().getNodeCount()));
				n2 = getGraph().getNode(getRandom().nextInt(getGraph().getNodeCount()));
			}while(n2==null || n1==n2 || n1.getNumber("ConnectedComponentId")!=n2.getNumber("ConnectedComponentId"));// || alreadyCompute(n1, n2));
			
			
			setNumberOfPairUsed(getNumberOfPairUsed()+1);
			
			gc = true;
			
			pn.setNode1(n1);
			pn.setNode2(n2);
			pn.setTakenCount(0);
		}
		pn.setTakenCount(pn.getTakenCount()+1);
	}
	
	public boolean hasNextPair(){
		//If we have finished the calculation
		if(getNumberOfPairUsed()>getNumberOfPairToUsed()){
			end = true;
			return false;
		}
		else
			return true;
	}
	
	/**
	 * Clear the attribute containing the average values on each node and on each edge
	 */
	public void clear(){
		String att = "random_walk_betweenness_centrality";
		String param = getNumberOfPairToUsed()+"_"+entityClassName;
		
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
	
	/**
	 * Add and init an attribute of average value on each node and each edge
	 */
	public void initAverage(){
		String att = "random_walk_betweenness_centrality";
		String param = getNumberOfPairToUsed()+"_"+entityClassName;
		
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
		String param = "_"+getNumberOfPairToUsed()+"_"+entityClassName;
		
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
	
	public synchronized String getEntityClassName(){
		return entityClassName;
	}
	
	public synchronized void setEntityClassName(String s){
		entityClassName = s;
	}
	
	public synchronized Graph getGraph() {
		return graph;
	}

	public synchronized void setGraph(Graph graph) {
		this.graph = graph;
	}

	public synchronized int getPathCount() {
		return pathCount;
	}

	public synchronized void setPathCount(int pathCount) {
		this.pathCount = pathCount;
	}

	public synchronized String getBetweennessCentralityAttribute() {
		return betweennessCentralityAttribute;
	}

	public synchronized void setBetweennessCentralityAttribute(
			String betweennessCentralityAttribute) {
		this.betweennessCentralityAttribute = betweennessCentralityAttribute;
	}

	public synchronized int getEntityCount() {
		return entityCount;
	}

	public synchronized void setEntityCount(int entityCount) {
		this.entityCount = entityCount;
	}

	public synchronized ArrayList<Entity> getListEntity() {
		return listEntity;
	}

	public synchronized void setListEntity(ArrayList<Entity> listEntity) {
		this.listEntity = listEntity;
	}

	public synchronized int getNumberOfPairUsed(){
		return numberOfPairUsed;
	}
	
	public synchronized void setNumberOfPairUsed(int numberOfPairUsed) {
		this.numberOfPairUsed = numberOfPairUsed;
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
