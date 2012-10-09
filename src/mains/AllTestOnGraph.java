package mains;

import graph.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Element;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

import betweennessCentralityPivots.BetweennessCentralityPivots;
import betweennessCentralityPivots.randomWalk.RandomWalkEntity;

import classicalBetweennessCentrality.BetweennessCentrality;

import randomWalk.RandomWalk;
import utils.ChangeGraph;
import utils.GnuPlotFile;
import utils.Misc;
import utils.Statistics;

public abstract class AllTestOnGraph {
	protected ArrayList<Element> listRandomWalk;
	protected ArrayList<Element> listRandomWalkBetweennessCentrality;
	protected ArrayList<Element> listClassicalBetweennessCentrality;

	protected boolean visuMode;
	protected boolean multigraph;
	protected boolean valued;
	protected int nbIteration;
	protected int numberOfPairToUsed;
	protected String entityClassName;
	protected String valuedAttribute;
	protected String dgs;
	protected String comparison;
	protected String id;
	protected String filePath;
	
	public AllTestOnGraph(boolean visuMode, boolean multi, boolean valued, int nbIt, String id, String dir){
		this.numberOfPairToUsed = 10000;
		this.entityClassName = RandomWalkEntity.class.getName();
		this.id = id;
		this.multigraph = multi;
		this.visuMode = visuMode;
		this.valued = valued;
		this.nbIteration = nbIt;
		this.filePath = System.getProperty("user.dir" )+File.separator+"DGS_and_results"+File.separator+dir+File.separator;
		this.dgs = filePath
					+this.id
					+".dgs";
		this.comparison = filePath
					+this.id
					+".dat";
	}
	
	public abstract Graph getGraph();
	
	public int getNumberOfPairToUsed() {
		return numberOfPairToUsed;
	}

	public void setNumberOfPairToUsed(int numberOfPairToUsed) {
		this.numberOfPairToUsed = numberOfPairToUsed;
	}

	public String getEntityClassName() {
		return entityClassName;
	}

	public void setEntityClassName(String entityClassName) {
		this.entityClassName = entityClassName;
	}

	public String getValuedAttribute() {
		return valuedAttribute;
	}

	public void setValuedAttribute(String valuedAttribute) {
		this.valuedAttribute = valuedAttribute;
	}

	public int getNbIteration() {
		return nbIteration;
	}

	public void setNbIteration(int nbIteration) {
		this.nbIteration = nbIteration;
	}

	public void clearAndSave(){
		Graph g = getGraph();
		ChangeGraph.clear(g, numberOfPairToUsed, entityClassName);
		//ChangeGraph.clearAll(g);
		ChangeGraph.addAverage(g, numberOfPairToUsed, entityClassName);
		Misc.saveGraph(dgs, g);
	}
	
	public void initAverageValuesRandomWalk(){
		Graph g = getGraph();
		RandomWalk rwalk2 = new RandomWalk();
		ChangeGraph.addAverage(g, rwalk2.getEntityClass());
		Misc.saveGraph(dgs, g);
	}
	
	public void suppAverageValuesRandomWalk(){
		Graph g = getGraph();
		for(Node n : g.getEachNode()){
			Iterator<String> it = n.getAttributeKeyIterator();
			ArrayList<String> ls = new ArrayList<String>();
			while(it.hasNext()){
				String s = it.next();
				if(s.contains("random_walk_passes"))
					ls.add(s);
			}
			for(String s : ls){
				n.removeAttribute(s);
			}
		}
		
		for(Edge e : g.getEachEdge()){
			Iterator<String> it = e.getAttributeKeyIterator();
			ArrayList<String> ls = new ArrayList<String>();
			while(it.hasNext()){
				String s = it.next();
				if(s.contains("random_walk_passes"))
					ls.add(s);
			}
			for(String s : ls)
				e.removeAttribute(s);
		}
		Misc.saveGraph(dgs, g);
	}
	
	public void recompute(){
		String att1 = "random_walk_betweenness_centrality";
		Graph g = getGraph();
		for(Node n : g.getNodeSet()){
			n.setAttribute(att1+"_average_"+numberOfPairToUsed, n.getNumber(att1+"_sum_"+numberOfPairToUsed)/n.getNumber(att1+"_nb_computed_"+numberOfPairToUsed));
		}
		
		for(Edge e : g.getEdgeSet()){
			e.setAttribute(att1+"_average_"+numberOfPairToUsed, e.getNumber(att1+"_sum_"+numberOfPairToUsed)/e.getNumber(att1+"_nb_computed_"+numberOfPairToUsed));
		}
		Misc.saveGraph(dgs, g);
	}
	
	public void testRandomWalk(){
		Runtime r = Runtime.getRuntime();
		Graph g = null;
		RandomWalk rwalk2 = null;
		suppAverageValuesRandomWalk();
		initAverageValuesRandomWalk();
		for(int j=0; j<nbIteration; j++){
			System.out.println("Itération n°"+j);
			
			g = getGraph();
			rwalk2 = new RandomWalk();
			// Populate the graph.
			System.out.println("init");
			rwalk2.setEntityCount(g.getNodeCount());
	        rwalk2.setEvaporation(0.97);
	        rwalk2.setEntityMemory(20);
	        if(valued)
	        	rwalk2.setWeightAttribute(valuedAttribute);
			rwalk2.init(g);
			
			//Computing
			System.out.println("Calcul..");
			int i = 0;
			while(!rwalk2.hasStableValues(rwalk2.getPassesAttribute())){
			//for(int i=0 ; i < 400; i++){
				rwalk2.compute();
				//if(i%100==0){
					//System.out.println("etape : "+i);
				//}
				i++;
			}

			//Mise a jour des valeurs sur les arcs lorsqu'il existe plusieurs arcs entre deux noeuds
			if(multigraph){
				System.out.println("MAJ des valeurs sur les arcs multiples");
				ChangeGraph.updateEdgeValues(rwalk2.getPassesAttribute(), g);
			}
			
			//Sauvegarde des valeurs dans les attributs associes a la moyenne
			rwalk2.saveCurrentValuesInAverageValues();
			
			rwalk2.terminate();
			
			//On nettoye les attributs non indispendasble stocker dans les noeuds
			ChangeGraph.clear(g);
			
			//Sauvegarde du graphe au format DGS
			Misc.saveGraph(dgs, g);
			
			if(i+1<nbIteration){
				rwalk2 = null;
				g = null;
				r.gc();
			}
		}
		
		if(visuMode){
			//Mise a jour des couleurs
			System.out.println("Mise à jour des couleurs");
			ChangeGraph.updateGraph(g, rwalk2.getPassesAttribute());
			
			//Affiche le graph
			System.out.println("Affichage du réseaux 2");
			Misc.showGraph(g, false);
			
			Main.attente("Attente avant sauvegarde");
			
			//Sauvegarde dans une version svg
			g.addAttribute("ui.screenshot", 
					id+"_"
					+rwalk2.getEntityClass()+"_"
					+rwalk2.getEntityCount()+"Entity"+"_"
					+rwalk2.getEvaporation()+"Evaporation"+"_"
					+rwalk2.getContext().getEntityMemory()+"EntityMemory"+"_"
					+"RandomWalk.svg");
			System.out.println("Screenshot effectué");
		}
	}
	
	public void testBetweennessCentralityPivots(){
		Runtime r = Runtime.getRuntime();
		Graph g = null;
		BetweennessCentralityPivots rwalk1 = null;
		for(int i=0; i<nbIteration; i++){
			System.out.println("Itération n°"+i);
			
			//Creation du graphe
			System.out.println("Création du graphe depuis le DGS");
			g = getGraph();
			
			//Random Walk
			System.out.println("Instanciation de l'objet BetweennessCentralityPivots");
			rwalk1 = new BetweennessCentralityPivots(10);
			
			//Initialisation des parametre
			System.out.println("init");
			
			//Modifie le nom de la class entity utilise
			rwalk1.setEntityClassName(entityClassName);
			
			rwalk1.setNumberOfPairToUsed(numberOfPairToUsed);
			
			rwalk1.init(g);

			if(i==0){
				rwalk1.initAverage();
			}
			
			//Calcul
			System.out.println("Calcul...");
			rwalk1.compute();
			
			//Calcul termine
			System.out.println("Terminé");
			rwalk1.terminate();
			
			if(multigraph){
				//Mise a jour des valeurs sur les arcs lorsqu'il existe plusieurs arcs entre deux noeuds
				System.out.println("MAJ des valeurs sur les arcs multiples");
				ChangeGraph.updateEdgeValues(rwalk1.getBetweennessCentralityAttribute(), g);
			}
			
			//Sauvegarde des valeurs dans les attributs associes a la moyenne
			System.out.println("Sauvegarde des valeurs moyennes");
			rwalk1.saveCurrentValuesInAverageValues();/**/

			//Suppression des attributs inutiles
			ChangeGraph.clear(g);
			
			//Sauvegarde du graphe au format DGS
			System.out.println("Sauvegarde du graphe au format DGS");
			Misc.saveGraph(dgs, g);/**/
			
			if(i+1<nbIteration){
				rwalk1 = null;
				g = null;
				r.gc();
			}
		}
		
		if(visuMode){
			//Mise a jour des couleurs
			System.out.println("Mise à jour des couleurs");
			ChangeGraph.updateGraph(g, rwalk1.getBetweennessCentralityAttribute());
			
			//Affiche le graph
			System.out.println("Affichage");
			Misc.showGraph(g, false);
			
			Main.attente("Attente avant screenshot");
			
			//Sauvegarde dans une version SVG
			g.addAttribute("ui.screenshot", 
					id+"_"
					+rwalk1.getEntityClassName()+"_"
					+rwalk1.getEntityCount()+"Entity"+"_"
					+rwalk1.getNumberOfPairUsed()+"NumberOfPairUsed"+"_"
					+"BetweennessCentralityPivots.svg");
		}
	}
	
	public void testClassicalBetweennessCentrality(boolean valued, String valuedAttribute){
		System.out.println("Creation du graphe");
		Graph g = getGraph();
		System.out.println("Instanciation de l'objet BetweennessCentrality");
		BetweennessCentrality bcb = new BetweennessCentrality();
		
		//Initialisation
		System.out.println("Initialisation");
		
		if(valued)
			bcb.setWeightAttributeName(valuedAttribute);
		else
			bcb.setUnweighted();
		
		bcb.setCentralityAttributeName("BetweennessCentrality_"+valuedAttribute);
		
		bcb.init(g);
		
		//Calcul
		System.out.println("Calcul...");
		bcb.compute();
		
		//Suppression des attributs inutiles
		ChangeGraph.clear(g);
		
		//Sauvegarde du graphe au format DGS
		System.out.println("Sauvegarde du graphe au format DGS");
		Misc.saveGraph(dgs, g);/**/
		
		if(visuMode){
			//Mise a jour des couleurs
			System.out.println("Mise à jour des couleurs");
			ChangeGraph.updateGraph(g, bcb.getCentralityAttributeName());
			
			//Affiche le graph
			System.out.println("Affichage");
			Misc.showGraph(g, false);
			
			Main.attente("Attente avant screenshot");
			
			//Sauvegarde dans une version SVG
			g.addAttribute("ui.screenshot", 
					id+"_"
					+"ClassicalBetweennessCentrality.svg");
		}
	}

	public void computeStatisticalMeasure(){
		Graph g = getGraph();
		double s;
		
		for(Node n : g){
			n.addAttribute("degree", n.getDegree());
		}
		
		String mesures[]={
			//"random_walk_betweenness_centrality_average_"+numberOfPairToUsed+"_"+entityClassName,
			//"BetweennessCentrality_travelTime",
			//"BetweennessCentrality_length",
			"BetweennessCentrality_noweight",
			//"degree",
			"random_walk_passes_average"
			//"BetweennessCentralityPivots",
			//"passes",
			
		};
	
		System.out.println("Calcul du coefficient de corrélation linéaire de Pearson");
		for(int i=0; i<mesures.length; i++){
			for(int j=0; j<mesures.length; j++){
				if(i!=j){
					s = Statistics.pearsonCorrelationCoefficient(g, mesures[i], mesures[j]);
					System.out.println("\tPearson coef entre "+mesures[i]+" et "+mesures[j]+" : "+s);
				}
			}
		}
			
		System.out.println("Calcul du coefficient de corrélation non linéaire de Spearman");
		for(int i=0; i<mesures.length; i++){
			for(int j=0; j<mesures.length; j++){
				if(i!=j){
					s = Statistics.spearmanCorrelationCoefficient(g, mesures[i], mesures[j]);
					System.out.println("\tSpearman coef entre "+mesures[i]+" et "+mesures[j]+" : "+s);
				}
			}
		}
		
		/*System.out.println("Ecart type :");
		for(int i=0; i<mesures.length; i++){
			System.out.println("\tEcart type CBC (average) :"+Statistics.ecartTypeNode(g, mesures[i]));
		}/**/
		
		for(Node n : g){
			n.removeAttribute("degree");
		}
	}
	
	public void createGnuPlotFile(){
		Graph g = getGraph();
		
		/*
		 * Gnuplot file
		 */
		for(Node n : g){
			n.addAttribute("degree", n.getDegree());
		}
		
		String mesures[]={
			"random_walk_betweenness_centrality_average_"+numberOfPairToUsed+"_"+entityClassName,
			//"BetweennessCentrality_travelTime",
			//"BetweennessCentrality_length",
			"BetweennessCentrality_noweight",
			//"random_walk_passes_average",
			//"BetweennessCentralityPivots",
			//"passes",
			//"degree"
			};
	
		/*System.out.println("Creation des fichiers pour gnuplot (rank)");
		for(int i=0; i<mesures.length; i++){
			for(int j=0; j<mesures.length; j++){
				if(i!=j)
					GnuPlotFile.rank(g, filePath, mesures[i], mesures[j]);
			}
		}/**/
		
		/*System.out.println("Creation des fichiers pour gnuplot (simple)");
		for(int i=0; i<mesures.length; i++){
			for(int j=0; j<mesures.length; j++){
				if(i!=j)
					GnuPlotFile.simple(g, filePath, mesures[i], mesures[j]);
			}
		}/**/
		
		/*System.out.println("Creation des fichiers pour gnuplot (repartition)");
		for(int i=0; i<mesures.length; i++){
			GnuPlotFile.repartition(g, filePath, mesures[i]);
		}/**/
		
		System.out.println("Creation des fichiers pour gnuplot (repartition2)");
		for(int i=0; i<mesures.length; i++){
			for(int j=0; j<mesures.length; j++){
				if(i!=j)
					GnuPlotFile.repartition2(g, filePath, mesures[i], mesures[j]);
			}
		}/**/
		
		/*System.out.println("Creation des fichiers pour gnuplot (repartition3)");
		for(int i=0; i<mesures.length; i++){
			for(int j=0; j<mesures.length; j++){
				if(i!=j)
					GnuPlotFile.repartition3(g, filePath, mesures[i], mesures[j]);
			}
		}/**/
		
		/*System.out.println("Creation des fichiers pour gnuplot (distribution)");
		int nbInt = 1000;
		for(int i=0; i<mesures.length; i++){
			GnuPlotFile.distribution(g, filePath, mesures[i], nbInt);
		}/**/
	}
	
	public void computePersonalComparisonMeasure(){
		/*Graph g = getGraph();
		double s ;
		/*
		 * Methodes de comparaison personnelles
		 */
			/*System.out.println("Sauvegarde de la liste des noeuds pour random_walk_betweenness_centrality_average");
			listRandomWalkBetweennessCentrality = (ArrayList<Element>) Utils.saveOrderedListNode("random_walk_betweenness_centrality_average", g);
			System.out.println("Sauvegarde de la liste des noeuds pour BiasedRandomWalkBetweennessCentrality");
			ArrayList<Element> listRandomWalkBetweennessCentrality2 = (ArrayList<Element>) Utils.saveOrderedListNode("BiasedRandomWalkBetweennessCentrality", g);
			System.out.println("Sauvegarde de la liste des noeuds pour BetweennessCentrality");
			listClassicalBetweennessCentrality = (ArrayList<Element>) Utils.saveOrderedListNode("BetweennessCentrality", g);
			System.out.println("Sauvegarde de la liste des noeuds pour random_walk_passes_average");
			listRandomWalk = (ArrayList<Element>) Utils.saveOrderedListNode("random_walk_passes_average", g);
			System.out.println("Sauvegarde de la liste des noeuds pour passes");
			ArrayList<Element> listRandomWalk2 = (ArrayList<Element>) Utils.saveOrderedListNode("passes", g);
			System.out.println("Creation de la liste inversé de BetweennessCentrality");
			ArrayList<Element> listClassicalBetweennessCentralityReverse = new ArrayList<Element>(listClassicalBetweennessCentrality);
			Collections.reverse(listClassicalBetweennessCentralityReverse);/**/
			
			/*System.out.println("Comparaison 1 : (plus c'est proche de 0 mieux c'est)");
				s = ComparatorOfList.compare(listRandomWalkBetweennessCentrality, listRandomWalk);
				System.out.println("\tComparaison 1 RWBC (average) et RW (average) : "+s);
				s = ComparatorOfList.compare(listRandomWalkBetweennessCentrality, listClassicalBetweennessCentralityReverse);
				System.out.println("\tComparaison 1 RWBC (average) et CBCR : "+s);
				s = ComparatorOfList.compare(listClassicalBetweennessCentralityReverse, listRandomWalk);
				System.out.println("\tComparaison 1 CBCR et RW (average) : "+s);
				s = ComparatorOfList.compare(listRandomWalkBetweennessCentrality, listClassicalBetweennessCentrality);
				System.out.println("\tComparaison 1 RWBC (average) et CBC : "+s);
				s = ComparatorOfList.compare(listRandomWalkBetweennessCentrality2, listClassicalBetweennessCentrality);
				System.out.println("\tComparaison 1 RWBC (une exec) et CBC : "+s);
				s = ComparatorOfList.compare(listRandomWalk, listClassicalBetweennessCentrality);
				System.out.println("\tComparaison 1 RW (average) et CBC : "+s);
				s = ComparatorOfList.compare(listRandomWalk2, listClassicalBetweennessCentrality);
				System.out.println("\tComparaison 1 RW (une exec) et CBC : "+s);
				s = ComparatorOfList.compare(listClassicalBetweennessCentralityReverse, listClassicalBetweennessCentrality);
				System.out.println("\tComparaison 1 CBCR et CBC : "+s);/**/
			
			/*double percent = 0.3;
			System.out.println("\nComparaison 2 (avec pourcentage de "+percent+")  (plus c'est proche de 1 mieux c'est)");
				s = ComparatorOfList.compare_new(listRandomWalkBetweennessCentrality, listRandomWalk, percent);
				System.out.println("\tComparaison 2 RWBC (average) et RW (average) : "+s);
				s = ComparatorOfList.compare_new(listRandomWalkBetweennessCentrality, listClassicalBetweennessCentrality, percent);
				System.out.println("\tComparaison 2 RWBC (average) et CBC : "+s);
				s = ComparatorOfList.compare_new(listRandomWalkBetweennessCentrality2, listClassicalBetweennessCentrality, percent);
				System.out.println("\tComparaison 2 RWBC (une exec) et CBC : "+s);
				s = ComparatorOfList.compare_new(listRandomWalk, listClassicalBetweennessCentrality, percent);
				System.out.println("\tComparaison 2 RW (average) et CBC : "+s);
				s = ComparatorOfList.compare_new(listRandomWalk2, listClassicalBetweennessCentrality, percent);
				System.out.println("\tComparaison 2 RW (une exec) et CBC : "+s);
				s = ComparatorOfList.compare_new(listRandomWalkBetweennessCentrality, listClassicalBetweennessCentralityReverse, percent);
				System.out.println("\tComparaison 2 RWBC (average) et CBCR : "+s);
				s = ComparatorOfList.compare_new(listClassicalBetweennessCentralityReverse, listRandomWalk, percent);
				System.out.println("\tComparaison 2 CBCR et RW (average) : "+s);
				s = ComparatorOfList.compare_new(listClassicalBetweennessCentralityReverse, listClassicalBetweennessCentrality, percent);
				System.out.println("\tComparaison 2 CBCR et CBC : "+s);/**/
	}
	
	
}
