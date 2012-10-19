package mains;

import betweennessCentralityPivots.astar.AStarEntity;
import betweennessCentralityPivots.randomWalk.BiasedByTravelTimeEntity;
import betweennessCentralityPivots.randomWalk.RandomWalkEntity;
import utils.ChangeGraph;
import graph.DorogovtsevMendes;
import graph.Grid;
import graph.MultiNetwork;
import graph.PreferentialAttachement;
import graph.SmallWorld;
import graph.Utils;

public class AfficheGraphe {
	public static void affiche(){
		//Grid g = new Grid(100, false, false, false);
		//DorogovtsevMendes g = new DorogovtsevMendes(1000, false);
		//PreferentialAttachement g = new PreferentialAttachement(2, 1000, false);
		//SmallWorld g = new SmallWorld(1000, 2, 0.5, false);
		//SmallWorld g = new SmallWorld(1000, 4, 0.01, false);
		
		//String nameMap = "LeHavre_Medium_ant_res";
		//String nameMap = "LeHavre_Medium";
		String nameMap = "LeHavre_Medium_ant_res_500paires";
		//String nameMap = "Rouen_Medium_2";
		//String nameMap = "Paris_Medium";
		MultiNetwork g = new MultiNetwork(nameMap);/**/
		
		//Mise a jour des couleurs
		System.out.println("Mise à jour des couleurs");
		
		//String className = "betweennessCentralityPivots.AStarEntityTravelTime";
		//String className = "betweennessCentralityPivots.AStarEntityEuclideanDistance";
		//String className = "betweennessCentralityPivots.BiasedByTravelTimeEntity";
		//String className = "betweennessCentralityPivots.RandomWalkEntity";
		
		int nbPair = 20000;
		//String att = "random_walk_betweenness_centrality_average_"+nbPair+"_"+className;
		//String att = "BiasedRandomWalkBetweennessCentrality";
		//String att = "random_walk_passes_average";
		//String att = "BetweennessCentrality_travelTime";
		//String att = "BetweennessCentrality_length";
		//String att = "BetweennessCentrality_noweigth";
		String att = "AntBetweennessCentralityPivots";
		
		ChangeGraph.updateGraph(g, att);
		
		System.out.println("Nb node : "+g.getNodeCount()+" et nb edge : "+g.getEdgeCount());
		
		//Affiche le graph
		System.out.println("Affichage");
		g.showGraph(false);
		
		Main.attente("Attente avant screenshot");
		
		//Sauvegarde dans une version SVG
		g.addAttribute("ui.screenshot", 
				g.getId()+"_"
				+att+".svg");/**/
	}
	
	public static void main(String args[]){
		AfficheGraphe.affiche();
	}
}
