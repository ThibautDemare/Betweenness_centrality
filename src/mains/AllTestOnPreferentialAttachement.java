package mains;

import org.graphstream.graph.Graph;

import graph.PreferentialAttachement;

public class AllTestOnPreferentialAttachement extends AllTestOnGraph{
	protected int nbNode;
	protected int maxLinksPerStep;
	
	public AllTestOnPreferentialAttachement(int nbNode, int maxLinksPerStep, boolean visuMode, int nbIt){
		super(visuMode, false, false, nbIt, "Preferential_Attachement_"+maxLinksPerStep+"_"+nbNode, "PreferentialAttachement");
		this.nbNode = nbNode;
		this.maxLinksPerStep = maxLinksPerStep;
	}

	public Graph getGraph(){
		return new PreferentialAttachement(maxLinksPerStep, nbNode, false);
	}
	
	public static void main(String args[]){
		AllTestOnPreferentialAttachement atopa = new AllTestOnPreferentialAttachement(1000, 2, false, 100);
		/**
		 * Random Walk Betweenness Centrality
		 */
		atopa.testBetweennessCentralityPivots();
		
		/**
		 * Random Walk classique
		 */
		atopa.testRandomWalk();

		/**
		 * Classical betweenness centrality
		 */
		atopa.testClassicalBetweennessCentrality(false, "noweigth");
		
		/**
		 * Comparaison
		 */
		//atopa.comparaison();
		
		//atopa.testDivision();
		
		//atopa.testComparaison();
	}
}
