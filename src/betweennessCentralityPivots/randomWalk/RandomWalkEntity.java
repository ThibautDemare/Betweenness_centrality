package betweennessCentralityPivots.randomWalk;

import java.util.Iterator;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;
import org.graphstream.graph.Path;

import betweennessCentralityPivots.BetweennessCentralityPivots;
import betweennessCentralityPivots.Entity;
import betweennessCentralityPivots.PairNode;

public class RandomWalkEntity extends Entity {
	/**
	 * The current node where the entity is located
	 */
	protected Node currentNode;
	
	/**
	 * Determine what type of step we do
	 */
	protected boolean construction_path;
	
	/**
	 * Help to define a unique ID
	 */
	protected static int incID=0;
	
	/**
	 * ID of the entity
	 */
	protected int id;
	
	
	/**
	 * Constructor
	 */
	public RandomWalkEntity(){
		super();

		//We start finding a path
		construction_path = true;
		
		//Define the ID of the entity
		id=incID++;
	}
	
	public void init(BetweennessCentralityPivots rwbc){
		setRwbc(rwbc);
		
		//Init the pair
		pair = new PairNode();
		rwbc.selectPairNode(pair);
		setCurrentNode(getPair().getNode1());
		//String attributeName="computedTo"+getPair().getNode2().getId();
		//getCurrentNode().addAttribute(attributeName, true);
		
		//creation of the path
		path = new Path();
		path.setRoot(getCurrentNode());
	}
	
	/**
	 * One step of the entity
	 */
	public void step(){
		//If we have not finished
		if(!terminate){
			if(construction_path){
				/*
				 * Construction of the path and wait other entity on the target node
				 */
				constructPath();
			}
			else{
				/*
				 * Accumulation of the centrality with retro-propagation
				 */
				accumulateCentrality();
			}
		}
	}
	
	/**
	 * One step where the entity construct the path
	 */
	private void constructPath(){
		/*
		 * Construction of the path
		 */
		//Choice of the next node
		Edge eNext = selectNextEdge();
		Node next = eNext.getOpposite(currentNode);
		if(!detectAndDeleteExistingLoop(next)){
			//We save the node in the path
			path.add(eNext);
		}
		
		//Update of the current node
		setCurrentNode(next);
		
		//If the entity have reached the target
		if(getCurrentNode()==getPair().getNode2()){
			//We stop to construct the path in order to start the accumulation of the centrality
			construction_path = false;
			pathLength = path.size();
		}
	}
	
	/**
	 * One step where the entity accumulate the centrality on the node of his path.
	 */
	private void accumulateCentrality(){
		//If we have reached the source node, we have finished to accumulate the centrality on the path.
		if(getCurrentNode().getId().equals(getPair().getNode1().getId()) || path.size()==1){
			/*
			 * Choice of a next pair if there is at least one
			 */
			if(getRwbc().hasNextPair()){
				getRwbc().selectPairNode(getPair());
				setCurrentNode(getPair().getNode1());
				//String attributeName="computedTo"+getPair().getNode2().getId();
				//getCurrentNode().addAttribute(attributeName, true);
				
				path.clear();
				path.setRoot(getCurrentNode());
				construction_path = true;
			}
			else
				terminate = true;
		}
		else{
			/*
			 * Edit centrality of the node and the edge
			 */
			Node w=path.popNode();
			updateCentrality(w);
			setCurrentNode(w);
		}
	}
	
	private void updateCentrality(Node w){
		
		//int add = (path.size()-1)*(pathLength-2 - (path.size()-2));
		int add = 1;
		if(w.hasAttribute(getRwbc().getBetweennessCentralityAttribute()))
			w.setAttribute(getRwbc().getBetweennessCentralityAttribute(), w.getNumber(getRwbc().getBetweennessCentralityAttribute())+add);
		else
			w.setAttribute(getRwbc().getBetweennessCentralityAttribute(), 0);
	
		Edge e;
		if(w.getEdgeFrom(path.peekNode())!=null)
			e=w.getEdgeFrom(path.peekNode());
		else
			e=w.getEdgeBetween(path.peekNode());
		
		if(e.hasAttribute(getRwbc().getBetweennessCentralityAttribute()))
			e.setAttribute(getRwbc().getBetweennessCentralityAttribute(), e.getNumber(getRwbc().getBetweennessCentralityAttribute())+add);
		else
			e.setAttribute(getRwbc().getBetweennessCentralityAttribute(), 0);
	}
	
	/**
	 * Delete the existing loop if it exists
	 * @param next
	 */
	private boolean  detectAndDeleteExistingLoop(Node next){
		//If the next is already in the path, we have done a loop. So we must remove all previous node untill the next node
		if(path.contains(next)){
			Node n;
			//We delete all node in the loop except the root node
			do{
				n = path.popNode();
			}while(!n.getId().equals(next.getId()) && path.size()>1);
			
			//We re-add the next node except if it is the root node
			if(!path.getRoot().getId().equals(next.getId())){
				if(n.getEdgeFrom(path.peekNode())!=null)
					path.add(n.getEdgeFrom(path.peekNode()));
				else
					path.add(n.getEdgeBetween(path.peekNode()));
			}
			
			return true;
		}
		return false;
	}
	
	/**
	 * Select the next node where the entity goes
	 * @return the next node where the entity goes
	 */
	public Edge selectNextEdge(){
		//Count number of neighbor
		Iterator<Edge> it = currentNode.getEdgeIterator();
		int nCount = 0;
		while(it.hasNext()){
			nCount++;
			it.next();
		}
		//Return the selected node
		it = currentNode.getEdgeIterator();
		int iReturned = (int)(Math.random()*nCount);
		int j = 0;
		while(it.hasNext())
			if(iReturned>j){
				j++;
				it.next();
			}
			else{
				return it.next();
			}
		//There is a problem...
		return null;
	}
	
	public Node getCurrentNode() {
		return currentNode;
	}

	public void setCurrentNode(Node currentNode) {
		this.currentNode = currentNode;
	}

	public boolean isConstruction_path() {
		return construction_path;
	}

	public void setConstruction_path(boolean constructionPath) {
		construction_path = constructionPath;
	}
}
