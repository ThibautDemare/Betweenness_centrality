package mains;

import org.graphstream.graph.Graph;

import betweennessCentralityPivots.astar.AStarEntityEuclideanDistance;

import graph.MultiNetwork;

public class AllTestOnVilles  extends AllTestOnGraph {
	/**
	 * Nom du graphe
	 */
	protected String nameMap;
	
	public AllTestOnVilles(String nameMap, boolean visuMode, int nbIt){
		super(visuMode, false, false, nbIt, nameMap, "Villes");
		//valuedAttribute = "travelTime";
		//valuedAttribute = "length";
		valuedAttribute = "noweigth";
	}
	
	public Graph getGraph(){
		return new MultiNetwork(id);
	}
	
	public static void main(String args[]){
		String nameMap;
		
		//Le Havre simple
		//nameMap="LeHavre_Single_Modified";
		
		//Le Havre modifie
			//carte avec l'ajout des distance euclidiennes sur les arcs
		//nameMap="LeHavre_Medium_Modified";
			//Carte basee sur la precedente mais cette fois en version multigraphe
		//nameMap="LeHavre_Medium_Multi_Modified";
			//Carte basee sur la precedente mais avec en plus les attributs associes aux moyennes
		nameMap="LeHavre_Medium_SingleGraph_4";
		
		//Le Havre classique
		//nameMap="LeHavre_Medium";
		
		//Paris
		//nameMap="Paris_Medium";
		
		//Paris modifié
		//nameMap="Paris_Medium_Modified";
		
		//Simple graph
		//nameMap = "SimpleGraph";
		
		AllTestOnVilles atolh = new AllTestOnVilles(nameMap, true, 1);
		atolh.setNumberOfPairToUsed(20000);
		atolh.setEntityClassName(AStarEntityEuclideanDistance.class.getName());
		//atolh.setValuedAttribute("length");
		//atolh.computeStatisticalMeasure();
		atolh.createGnuPlotFile();
		//atolh.clearAndSave();
		
		/**
		 * Random Walk Betweenness Centrality
		 */
		//atolh.testBetweennessCentralityPivots();
		
		/**
		 * Random Walk classique
		 */
		//atolh.testRandomWalk();
		
		/**
		 * Classical betweenness centrality
		 */
		//atolh.testClassicalBetweennessCentrality();
		
		/**
		 * Test de la division des valeurs par le degré du noeud
		 */
		//atolh.testDivision();
		
		/**
		 * Test comparaison
		 */
		//atolh.testComparaison();
		//atolh.testDegreeCorrelation();
		/**
		 * Comparaison
		 */
		//atolh.comparaison();
	}
}
