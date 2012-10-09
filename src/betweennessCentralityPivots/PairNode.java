package betweennessCentralityPivots;

import org.graphstream.graph.Node;

/**
 * Class of pair of node
 */
public class PairNode{
	protected Node node1;
	protected Node node2;
	protected int takenCount;
	
	public PairNode(Node n1, Node n2){
		node1 = n1;
		node2 = n2;
		takenCount = 0;
	}
	
	public PairNode(){
		node1 = null;
		node2 = null;
		takenCount = 0;
	}
	
	public synchronized int getTakenCount(){
		return takenCount;
	}

	public synchronized void setTakenCount(int t){
		takenCount=t;
	}

	public synchronized Node getNode1() {
		return node1;
	}

	public synchronized void setNode1(Node node1) {
		this.node1 = node1;
	}

	public synchronized Node getNode2() {
		return node2;
	}

	public synchronized void setNode2(Node node2) {
		this.node2 = node2;
	}
}