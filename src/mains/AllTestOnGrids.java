package mains;

import graph.Grid;

import org.graphstream.graph.Graph;

import betweennessCentralityPivots.randomWalk.BiasedByTravelTimeEntity;
import betweennessCentralityPivots.randomWalk.RandomWalkEntity;
import betweennessCentralityPivots.randomWalk.UnbiasedEntity;


public class AllTestOnGrids extends AllTestOnGraph{
	protected int nbLine;
	protected boolean cross;
	protected boolean tore;

	public AllTestOnGrids(int nbLine, boolean cross, boolean tore, boolean visuMode, int nbIt){
		super(visuMode, false, false, nbIt, "Grid_"+nbLine+"_"+cross+"_"+tore, "Grid");
		this.nbLine = nbLine;
		this.cross = cross;
		this.tore = tore;
	}

	public Graph getGraph(){
		return new Grid(nbLine, cross, tore, false);
	}

	public static void main(String args[]){
		AllTestOnGrids atog = new AllTestOnGrids(100, false, false, false, 100);
		//atog.recompute();
		//atog.setNumberOfPairToUsed(500);
		//atog.setEntityClassName(Entity.class.getName());
		//atog.setEntityClassName(UnbiasedEntity.class.getName());
		//atog.clearAndSave();
		
		/**
		 * Random walk btweenness centrality
		 */
		//

		/**
		 * Random Walk classique
		 */
		//atog.testBetweennessCentralityPivots();
		//atog.testRandomWalk();
		//atog.testClassicalBetweennessCentrality(false, "noweight");
		//atog.createGnuPlotFile();
		atog.computeStatisticalMeasure();
		/**
		 * Classical betweenness centrality
		 */
		//atog.testClassicalBetweennessCentrality();

		/**
		 * Test comparaison
		 */
		//atog.testComparaison();

		/**
		 * Comparaison
		 */
		//atog.comparaison();
	}
}
