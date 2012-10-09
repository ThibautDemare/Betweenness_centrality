package betweennessCentralityPivots;

import org.graphstream.graph.Path;

public abstract class Entity {
	/**
	 * The BetweennessCentralityPivots object
	 */
	protected BetweennessCentralityPivots rwbc;
	
	/**
	 * The pair of node 
	 */
	protected PairNode pair;
	
	/**
	 * Determine if there is still pair to compute or not
	 */
	protected boolean terminate;
	
	/**
	 * The path associated to pair of node and to this entity
	 */
	protected Path path;
	
	/**
	 * The path length
	 */
	protected int pathLength;
	
	public Entity(){
		//if we have terminate
		terminate = false;
	}

	/*
	 * Abstract methods
	 */
	
	public abstract void step();
	
	public abstract void init(BetweennessCentralityPivots rwbc);
	
	/*
	 * Not Abstract methods
	 */
	
	public boolean isTerminate() {
		return terminate;
	}

	public void setTerminate(boolean terminate) {
		this.terminate = terminate;
	}
	
	public PairNode getPair() {
		return pair;
	}

	public void setPair(PairNode pair) {
		this.pair = pair;
	}

	public BetweennessCentralityPivots getRwbc() {
		return rwbc;
	}

	public void setRwbc(BetweennessCentralityPivots rwbc) {
		this.rwbc = rwbc;
	}

	public Path getPath() {
		return path;
	}

	public void setPath(Path path) {
		this.path = path;
	}
}
