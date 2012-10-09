package mains;

import graph.SmallWorld;
import org.graphstream.graph.Graph;

public class AllTestOnSmallWorld extends AllTestOnGraph{
	protected int nbNode;
	protected int k;
	protected double beta;
	
	public AllTestOnSmallWorld(int nbNode, int k, double beta, boolean visuMode, int nbIteration){
		super(visuMode, false, false, nbIteration, "Small_World_"+nbNode+"_"+k+"_"+beta, "SmallWorld");
		this.nbNode = nbNode;
		this.k = k;
		this.beta = beta;
	}
	
	public Graph getGraph(){
		return new SmallWorld(nbNode, k, beta, false);
	}
	
	public static void main(String args[]){
		//AllTestOnSmallWorld atosw = new AllTestOnSmallWorld(1000, 4, 0.01, false, 100);
		AllTestOnSmallWorld atosw = new AllTestOnSmallWorld(1000, 2, 0.5, false, 100);
		atosw.clearAndSave();
		/**
		 * Random Walk Betweenness Centrality
		 */
		//atosw.testRandomWalkBetweennessCentrality();
		
		/**
		 * Random Walk classique
		 */
		//atosw.testRandomWalk();
		
		/**
		 * Classical betweenness centrality
		 */
		//atosw.testClassicalBetweennessCentrality();
		
		/**
		 * Comparaison
		 */
		//atosw.comparaison();
		//atosw.testComparaison();
	}
}
