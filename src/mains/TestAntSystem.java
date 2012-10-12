package mains;

import java.io.File;

import betweennessCentralityPivots.ant.AntBetweennessCentralityPivots;

import classicalBetweennessCentrality.BetweennessCentrality;
import utils.ChangeGraph;
import utils.GnuPlotFile;
import utils.Misc;
import utils.Statistics;
import graph.DorogovtsevMendes;
import graph.Grid;
import graph.MultiNetwork;

public class TestAntSystem {
	public void test(){
		//Creation du graphe
		System.out.println("Création du graphe depuis le DGS");
		MultiNetwork g = new MultiNetwork("LeHavre_Medium");
		//Grid g = new Grid(10, false, false, false);
		//DorogovtsevMendes g = new DorogovtsevMendes(1000, false);
		
		
		//Centralite intermediaire
		/*System.out.println("Centralite intermediaire...");
		BetweennessCentrality bcb = new BetweennessCentrality();
		bcb.setUnweighted();
		bcb.setCentralityAttributeName("BetweennessCentrality_noweight");
		bcb.init(g);
		bcb.compute();
		ChangeGraph.clear(g);
		Misc.saveGraph(System.getProperty("user.dir" )+File.separator+".."+File.separator+"Grid"+File.separator
				+"Grid_"
				+g.getNbLine()+"_"
				+g.isCross()+"_"
				+g.isTore()+".dgs", g);/**/
		
		//g.showGraph(false);
		
		//AntBetweennessCentralityPivots
		System.out.println("AntBetweennessCentralityPivots");
		AntBetweennessCentralityPivots antsys = new AntBetweennessCentralityPivots(100);
		antsys.setNumberOfPairToUsed(2000);
		antsys.init(g);
		antsys.setWeightAttribute("length");
		antsys.compute();
		antsys.terminate();
		
		/*Misc.saveGraph(System.getProperty("user.dir" )+File.separator+".."+File.separator+"Grid"+File.separator
				+"Grid_"
				+g.getNbLine()+"_"
				+g.isCross()+"_"
				+g.isTore()+".dgs", g);/**/
		
		//ChangeGraph.updateGraph(g, "AntBetweennessCentralityPivots");
		//g.showGraph(false);
		
		Misc.saveGraph(System.getProperty("user.dir" )+File.separator+"DGS_and_results"+File.separator+"Villes"+File.separator
						+"LeHavre_Medium_ant_res_2000paires.dgs", g);/**/
		
		/*String mesures[]={
				"AntBetweennessCentralityPivots",
				"BetweennessCentrality_noweight",
			};
		double s;
		// Mesure statistique 
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
		//Fichiers gnuplot
		String filePath = System.getProperty("user.dir" )+File.separator+".."+File.separator+"Grid"+File.separator;
		System.out.println("Creation des fichiers pour gnuplot (repartition2)");
		for(int i=0; i<mesures.length; i++){
			for(int j=0; j<mesures.length; j++){
				if(i!=j)
					GnuPlotFile.repartition2(g, filePath, mesures[i], mesures[j]);
			}
		}/**/
	}
	
	public static void main(String args[]){
		TestAntSystem tas = new TestAntSystem();
		tas.test();
	}
}
