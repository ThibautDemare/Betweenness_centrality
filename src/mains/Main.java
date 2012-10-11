package mains;

import java.io.IOException;

import betweennessCentralityPivots.astar.AStarEntity;
import betweennessCentralityPivots.astar.AStarEntityEuclideanDistance;
import betweennessCentralityPivots.astar.AStarEntityTravelTime;
import betweennessCentralityPivots.randomWalk.BiasedByEuclideanDistanceEntity;
import betweennessCentralityPivots.randomWalk.BiasedByTravelTimeEntity;
import betweennessCentralityPivots.randomWalk.RandomWalkEntity;
import betweennessCentralityPivots.randomWalk.UnbiasedEntity;


public class Main {
	protected static int[] nbIter = {
	//	1,
		1,
		100,
	//	100,
		100,
		100
	};
	protected static String[] entities = {
	//	AStarEntityTravelTime.class.getName(),
	//	AStarEntityEuclideanDistance.class.getName(),
	//	BiasedByEuclideanDistanceEntity.class.getName(),
	//	BiasedByTravelTimeEntity.class.getName(),
		RandomWalkEntity.class.getName(),
	//	UnbiasedEntity.class.getName()
	};
	protected static int[] numberOfPairToUsed = {
		100,
		1000,
		10000,
		20000
	};
	
	public static void afficher(String s){
		synchronized (System.out) {
			System.out.println(s);
		}
	}
	
	/**
	 * méthode permettant d'interagir avec l'utilisateur
	 * tant que l'utilisateur n'a pas tapez sur la touche entrée,
	 * l'application reste figée
	 */
	public static void attente(String message) {
		System.out.println("============================");
		System.out.println(message);
		System.out.println("Tapez sur ENTREE pour continuer");
		try {
			System.in.read();
		} catch (IOException ioe) {}
	}
	
	public static void compute(){
		Runtime r = Runtime.getRuntime();
		
		/*
		 * Dorogovtsev Mendes
		 */
		/*System.out.println("Lancement des tests sur Dorogovtsev Mendes");
		AllTestOnDorogovtsevMendes atodm = new AllTestOnDorogovtsevMendes(1000, false, 0);
		/*for(int i=2; i<entities.length; i++){
			for(int j=0; j<2; j++){
				//Random Walk Betweenness Centrality
				atodm.setNumberOfPairToUsed(numberOfPairToUsed[j]);
				atodm.setEntityClassName(entities[i]);
				atodm.setNbIteration(nbIter[i]);
				atodm.clearAndSave();
				atodm.testBetweennessCentralityPivots();
			}
		}/**/
		//Random Walk classique
		/*atodm.setNbIteration(100);
		atodm.testRandomWalk();
		//Classical betweenness centrality
		//atodm.testClassicalBetweennessCentrality(false, "noweight");
		r.gc();/**/
				
		/*
		 * Simple Grid
		 */
		/*System.out.println("Lancement des tests sur simple grille");
		AllTestOnGrids atog = new AllTestOnGrids(100, false, false, false, 0);
		/*for(int i=2; i<entities.length; i++){
			for(int j=0; j<2; j++){
				//Random Walk Betweenness Centrality
				atog.setNumberOfPairToUsed(numberOfPairToUsed[j]);
				atog.setEntityClassName(entities[i]);
				atog.setNbIteration(nbIter[i]);
				atog.clearAndSave();
				atog.testBetweennessCentralityPivots();
			}
		}/**/
		//Random Walk classique
		/*atog.setNbIteration(100);
		atog.testRandomWalk();
		//Classical betweenness centrality
		//atog.testClassicalBetweennessCentrality(false, "noweight");
		r.gc();/**/
				
		/*
		 * Preferential Attachement
		 */
		/*System.out.println("Lancement des tests sur preferential attachement 1 (1000 et 1");
		AllTestOnPreferentialAttachement atopa = new AllTestOnPreferentialAttachement(1000, 1, false, 0);
		/*for(int i=2; i<entities.length; i++){
			for(int j=0; j<2; j++){
				//Random Walk Betweenness Centrality
				atopa.setNumberOfPairToUsed(numberOfPairToUsed[j]);
				atopa.setEntityClassName(entities[i]);
				atopa.setNbIteration(nbIter[i]);
				atopa.clearAndSave();
				atopa.testBetweennessCentralityPivots();
			}
		}/**/
		//Random Walk classique
		/*atopa.setNbIteration(100);
		atopa.testRandomWalk();
		//Classical betweenness centrality
		//atopa.testClassicalBetweennessCentrality(false, "noweight");
		r.gc();/**/
				
		/*System.out.println("Lancement des tests sur preferential attachement 2 (1000 et 2)");
		AllTestOnPreferentialAttachement atopa2 = new AllTestOnPreferentialAttachement(1000, 2, false, 0);
		/*for(int i=2; i<entities.length; i++){
			for(int j=0; j<2; j++){
				//Random Walk Betweenness Centrality
				atopa2.setNumberOfPairToUsed(numberOfPairToUsed[j]);
				atopa2.setEntityClassName(entities[i]);
				atopa2.setNbIteration(nbIter[i]);
				atopa2.clearAndSave();
				atopa2.testBetweennessCentralityPivots();
			}
		}/**/
		//Random Walk classique
		/*atopa2.setNbIteration(100);
		atopa2.testRandomWalk();
		//Classical betweenness centrality
		//atopa2.testClassicalBetweennessCentrality(false, "noweight");
		r.gc();/**/
				
		/*
		 * Small world
		 */
		/*System.out.println("Lancement des tests sur small world 1 (1000, 4 et 0.01)");
		AllTestOnSmallWorld atosw1 = new AllTestOnSmallWorld(1000, 4, 0.01, false, 0);//(1000, 2, 0.5)
		/*for(int i=2; i<entities.length; i++){
			for(int j=0; j<2; j++){
				//Random Walk Betweenness Centrality
				atosw1.setNumberOfPairToUsed(numberOfPairToUsed[j]);
				atosw1.setEntityClassName(entities[i]);
				atosw1.setNbIteration(nbIter[i]);
				atosw1.clearAndSave();
				atosw1.testBetweennessCentralityPivots();
			}
		}/**/
		//Random Walk classique
		/*atosw1.setNbIteration(100);
		atosw1.testRandomWalk();
		//Classical betweenness centrality
		//atosw1.testClassicalBetweennessCentrality(false, "noweight");
		r.gc();/**/
				
		/*System.out.println("Lancement des tests sur small world 2 (1000; 2 et 0.5)");
		AllTestOnSmallWorld atosw2 = new AllTestOnSmallWorld(1000, 2, 0.5, false, 0);
		/*for(int i=2; i<entities.length; i++){
			for(int j=0; j<2; j++){
				//Random Walk Betweenness Centrality
				atosw2.setNumberOfPairToUsed(numberOfPairToUsed[j]);
				atosw2.setEntityClassName(entities[i]);
				atosw2.setNbIteration(nbIter[i]);
				atosw2.clearAndSave();
				atosw2.testBetweennessCentralityPivots();
			}
		}/**/
		//Random Walk classique
		/*atosw2.setNbIteration(100);
		atosw2.testRandomWalk();
		//Classical betweenness centrality
		//atosw2.testClassicalBetweennessCentrality(false, "noweight");
		r.gc();/**/
		
		/*System.out.println("Lancement des tests sur small world (10 000, 6 et 0.001)");
		AllTestOnSmallWorld atosw3 = new AllTestOnSmallWorld(10000, 6, 0.001, false, 0);
		/*for(int i=2; i<entities.length; i++){
			for(int j=0; j<2; j++){
				//Random Walk Betweenness Centrality
				atosw3.setNumberOfPairToUsed(numberOfPairToUsed[j]);
				atosw3.setEntityClassName(entities[i]);
				atosw3.setNbIteration(nbIter[i]);
				atosw3.clearAndSave();
				atosw3.testBetweennessCentralityPivots();
			}
		}/**/
		//Random Walk classique
		/*atosw3.setNbIteration(100);
		atosw3.testRandomWalk();
		//Classical betweenness centrality
		//atosw3.testClassicalBetweennessCentrality(false, "noweight");
		r.gc();/**/
				
		/*
		 * Le Havre
		 */
		System.out.println("Lancement des tests sur Le Havre");
		String nameMapLH = "LeHavre_Medium";
		AllTestOnVilles atolh = new AllTestOnVilles(nameMapLH, false, 0);
		/*for(int i=0; i<entities.length; i++){
			int nbPair;
			//if(i<2)
				//nbPair = numberOfPairToUsed.length;
			//else
				nbPair = 2;
			for(int j=0; j<nbPair; j++){
				//Random Walk Betweenness Centrality
				atolh.setNumberOfPairToUsed(numberOfPairToUsed[j]);
				atolh.setEntityClassName(entities[i]);
				atolh.setNbIteration(nbIter[i]);
				atolh.clearAndSave();
				atolh.testBetweennessCentralityPivots();
			}
		}
		//Random Walk classique
		atolh.setNbIteration(100);
		atolh.testRandomWalk();/**/
		//Classical betweenness centrality
		atolh.testClassicalBetweennessCentrality(true, "length");
		//atolh.testClassicalBetweennessCentrality(true, "travelTime");
		//atolh.testClassicalBetweennessCentrality(false, "noweight");
		r.gc();/**/
		
		/*
		 * Rouen
		 */
		/*System.out.println("Lancement des tests sur Rouen");
		String nameMapRouen = "Rouen_Medium";
		AllTestOnVilles ator = new AllTestOnVilles(nameMapRouen, false, 0);
		for(int i=0; i<entities.length; i++){
			int nbPair;
			//if(i<2)
				//nbPair = numberOfPairToUsed.length;
			//else
				nbPair = 2;
			for(int j=0; j<nbPair; j++){
				//Random Walk Betweenness Centrality
				ator.setNumberOfPairToUsed(numberOfPairToUsed[j]);
				ator.setEntityClassName(entities[i]);
				ator.setNbIteration(nbIter[i]);
				ator.clearAndSave();
				ator.testBetweennessCentralityPivots();
			}
		}
		//Random Walk classique
		ator.setNbIteration(100);
		ator.testRandomWalk();
		//Classical betweenness centrality
		ator.testClassicalBetweennessCentrality(true, "length");
		ator.testClassicalBetweennessCentrality(true, "travelTime");
		ator.testClassicalBetweennessCentrality(false, "noweight");
		r.gc();/**/
		
		/*
		 * Paris
		 */
		/*System.out.println("Lancement des tests sur Paris");
		String nameMapParis = "Paris_Medium";
		AllTestOnVilles atop = new AllTestOnVilles(nameMapParis, false, 0);
		for(int i=0; i<entities.length; i++){
			int nbPair;
			if(i<2)
				nbPair = numberOfPairToUsed.length;
			else
				nbPair = 2;
			for(int j=0; j<nbPair; j++){
				//Random Walk Betweenness Centrality
				atop.setNumberOfPairToUsed(numberOfPairToUsed[j]);
				atop.setEntityClassName(entities[i]);
				atop.setNbIteration(nbIter[i]);
				atop.clearAndSave();
				atop.testBetweennessCentralityPivots();
			}
		}
		//Random Walk classique
		atop.setNbIteration(100);
		atop.testRandomWalk();
		//Classical betweenness centrality
		//atop.testClassicalBetweennessCentrality(true, "length");
		//atop.testClassicalBetweennessCentrality(true, "travelTime");
		//atop.testClassicalBetweennessCentrality(false, "noweight");
		r.gc();/**/
	}
	
	public static void compare(){
		Runtime r = Runtime.getRuntime();
		
		/*
		 * Dorogovtsev Mendes
		 */
		/*System.out.println("Lancement des tests sur Dorogovtsev Mendes");
		AllTestOnDorogovtsevMendes atodm = new AllTestOnDorogovtsevMendes(1000, false, 0);
		for(int i=0; i<entities.length; i++){
			for(int j=0; j<2; j++){
				//Random Walk Betweenness Centrality
				atodm.setNumberOfPairToUsed(numberOfPairToUsed[j]);
				atodm.setEntityClassName(entities[i]);
				atodm.createGnuPlotFile();
				//atodm.computeStatisticalMeasure();
				//atodm.computePersonalComparisonMeasure();
			}
		}
		r.gc();/**/
				
		/*
		 * Simple Grid
		 */
		/*System.out.println("Lancement des tests sur simple grille");
		AllTestOnGrids atog = new AllTestOnGrids(100, false, false, false, 0);
		for(int i=2; i<entities.length; i++){
			for(int j=0; j<2; j++){
				//Random Walk Betweenness Centrality
				atog.setNumberOfPairToUsed(numberOfPairToUsed[j]);
				atog.setEntityClassName(entities[i]);
				//atog.createGnuPlotFile();
				atog.computeStatisticalMeasure();
				//atog.computePersonalComparisonMeasure();
			}
		}
		r.gc();/**/
				
		/*
		 * Preferential Attachement
		 */
		/*System.out.println("Lancement des tests sur preferential attachement 1 (1000 et 1)");
		AllTestOnPreferentialAttachement atopa = new AllTestOnPreferentialAttachement(1000, 1, false, 0);
		for(int i=2; i<entities.length; i++){
			for(int j=0; j<2; j++){
				//Random Walk Betweenness Centrality
				atopa.setNumberOfPairToUsed(numberOfPairToUsed[j]);
				atopa.setEntityClassName(entities[i]);
				//atopa.createGnuPlotFile();
				atopa.computeStatisticalMeasure();
				//atopa.computePersonalComparisonMeasure();
			}
		}
		r.gc();/**/
				
		/*System.out.println("Lancement des tests sur preferential attachement 2 (1000 et 2)");
		AllTestOnPreferentialAttachement atopa2 = new AllTestOnPreferentialAttachement(1000, 2, false, 0);
		for(int i=2; i<entities.length; i++){
			for(int j=0; j<2; j++){
				//Random Walk Betweenness Centrality
				atopa2.setNumberOfPairToUsed(numberOfPairToUsed[j]);
				atopa2.setEntityClassName(entities[i]);
				//atopa2.createGnuPlotFile();
				atopa2.computeStatisticalMeasure();
				//atopa2.computePersonalComparisonMeasure();
			}
		}
		r.gc();/**/
				
		/*
		 * Small world
		 */
		/*System.out.println("Lancement des tests sur small world 1 (1000, 4 et 0.01)");
		AllTestOnSmallWorld atosw1 = new AllTestOnSmallWorld(1000, 4, 0.01, false, 0);//(1000, 2, 0.5)
		for(int i=2; i<entities.length; i++){
			for(int j=0; j<2; j++){
				//Random Walk Betweenness Centrality
				atosw1.setNumberOfPairToUsed(numberOfPairToUsed[j]);
				atosw1.setEntityClassName(entities[i]);
				atosw1.createGnuPlotFile();
				//atosw1.computeStatisticalMeasure();
				//atosw1.computePersonalComparisonMeasure();
			}
		}
		r.gc();/**/
				
		/*System.out.println("Lancement des tests sur small world 2 (1000; 2 et 0.5)");
		AllTestOnSmallWorld atosw2 = new AllTestOnSmallWorld(1000, 2, 0.5, false, 0);
		for(int i=2; i<entities.length; i++){
			for(int j=0; j<2; j++){
				//Random Walk Betweenness Centrality
				atosw2.setNumberOfPairToUsed(numberOfPairToUsed[j]);
				atosw2.setEntityClassName(entities[i]);
				atosw2.createGnuPlotFile();
				//atosw2.computeStatisticalMeasure();
				//atosw2.computePersonalComparisonMeasure();
			}
		}
		r.gc();/**/
		
		/*System.out.println("Lancement des tests sur small world (10 000, 6 et 0.001)");
		AllTestOnSmallWorld atosw3 = new AllTestOnSmallWorld(10000, 6, 0.001, false, 0);
		for(int i=2; i<entities.length; i++){
			for(int j=0; j<2; j++){
				//Random Walk Betweenness Centrality
				atosw3.setNumberOfPairToUsed(numberOfPairToUsed[j]);
				atosw3.setEntityClassName(entities[i]);
				atosw3.createGnuPlotFile();
				//atosw3.computeStatisticalMeasure();
				//atosw3.computePersonalComparisonMeasure();
			}
		}
		r.gc();/**/
				
		/*
		 * Le Havre
		 */
		/*System.out.println("Lancement des tests sur Le Havre");
		String nameMapLH = "LeHavre_Medium";
		AllTestOnVilles atolh = new AllTestOnVilles(nameMapLH, false, 0);
		for(int i=0; i<entities.length; i++){
			int nbPair;
			//if(i<2)
				nbPair = numberOfPairToUsed.length;
			//else
				//nbPair = 2;
			for(int j=nbPair-1; j<nbPair; j++){
				//Random Walk Betweenness Centrality
				atolh.setNumberOfPairToUsed(numberOfPairToUsed[j]);
				atolh.setEntityClassName(entities[i]);
				atolh.createGnuPlotFile();
				//atolh.computeStatisticalMeasure();
				//atolh.computePersonalComparisonMeasure();
			}
		}
		r.gc();/**/
		
		/*
		 * Rouen
		 */
		System.out.println("Lancement des tests sur Rouen");
		String nameMapRouen = "Rouen_Medium";
		AllTestOnVilles ator = new AllTestOnVilles(nameMapRouen, false, 0);
		for(int i=0; i<entities.length; i++){
			int nbPair;
			//if(i<1)
				//nbPair = numberOfPairToUsed.length;
			//else
				nbPair = 2;
			for(int j=nbPair-1; j<nbPair; j++){
				//Random Walk Betweenness Centrality
				ator.setNumberOfPairToUsed(numberOfPairToUsed[j]);
				ator.setEntityClassName(entities[i]);
				ator.createGnuPlotFile();
				//ator.computeStatisticalMeasure();
				//ator.computePersonalComparisonMeasure();
			}
		}
		r.gc();/**/
		
		/*
		 * Paris
		 */
		/*System.out.println("Lancement des tests sur Paris");
		String nameMapParis = "Paris_Medium";
		AllTestOnVilles atop = new AllTestOnVilles(nameMapParis, false, nbIter);
		for(int i=0; i<entities.length; i++){
			int nbPair;
			if(i<2)
				nbPair = numberOfPairToUsed.length;
			else
				nbPair = 2;
			for(int j=0; j<nbPair; j++){
				//Random Walk Betweenness Centrality
				atop.setNumberOfPairToUsed(numberOfPairToUsed[i]);
				atop.setEntityClassName(entities[j]);
				atop.createGnuPlotFile();
				//atop.computeStatisticalMeasure();
				//atop.computePersonalComparisonMeasure();
			}
		}
		r.gc();/**/
	}
	
	public static void main(String[] args) {
		Main.compute();
	//	Main.compare();
	}

}
