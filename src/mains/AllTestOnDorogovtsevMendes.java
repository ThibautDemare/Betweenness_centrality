package mains;

import graph.DorogovtsevMendes;
import org.graphstream.graph.Graph;

public class AllTestOnDorogovtsevMendes extends AllTestOnGraph{
	protected int nbNode;
	
	public AllTestOnDorogovtsevMendes(int nbNode, boolean visuMode, int nbIt){
		super(visuMode, false, false, nbIt, "Dorogovtsev_Mendes_"+nbNode, "DorogovtsevMendes");
		this.nbNode = nbNode;
	}
	
	public Graph getGraph(){
		return new DorogovtsevMendes(nbNode, false);
	}
	
	public static void main(String args[]){
		AllTestOnDorogovtsevMendes atodm = new AllTestOnDorogovtsevMendes(10, false, 100);
		//atodm.clearAndSave();
		
		/**
		 * Random Walk Betweenness Centrality
		 */
		atodm.testBetweennessCentralityPivots();
		
		/**
		 * Random Walk classique
		 */
		atodm.testRandomWalk();

		/**
		 * Classical betweenness centrality
		 */
		atodm.testClassicalBetweennessCentrality(false, "noweigth");
		
		/**
		 * Comparaison
		 */
		//atodm.comparaison();
		
		//atodm.testDivision();
		
		//atodm.testComparaison();
	}
}
