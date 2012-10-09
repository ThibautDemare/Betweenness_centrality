package graph;

import java.io.File;

import mains.Main;

import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.swingViewer.View;

import utils.ChangeGraph;
import utils.Misc;

public class MultiNetwork extends SingleGraph{
	
	protected String color1 = "rgb(255, 255, 0)";
	protected String color2 = "rgb(0, 100, 255)";
	protected String color3 = "rgb(255, 170, 0)";
	protected String color4 = "rgb(255, 0, 0)";
	
	/*protected String stylesheet = 
		"graph {  fill-mode : none; padding : 20px, 20px;" +
				 "fill-color : rgb(200, 200, 200);}" +
		"node  {  text-mode : normal; size : 2px; " +
				 "fill-color: "+color1+", "
				 +color2+", "
				 //+color3+", "
				 +color4+";" +
				 "fill-mode : dyn-plain;" +
				 "}"+
		"edge  {  text-mode : normal; size : 1px; shape : line; " +
				 "fill-color: "+color1+", "
				 +color2+", "
				 //+color3+", "
				 +color4+";"
				 +"fill-mode : dyn-plain;"
				 +"}";*/
	
	protected String stylesheet = 
		"graph {  fill-mode : none; padding : 20px, 20px;" +
				 "fill-color : white;}" +//rgb(200, 200, 200);
		"node  {  text-mode : normal; size : 2px; " +
				 "fill-color: rgb(0, 90, 170);}"+
		"edge  {  text-mode : normal; size : 1px; shape : line; arrow-shape : none; " +
				 "fill-color: rgb(0, 0, 0);}";
	
	protected String nameMap;

	public MultiNetwork(String id) {
		super(id);
		nameMap=id;
		try {
			read(System.getProperty("user.dir" )+File.separator+"DGS_and_results"+File.separator+"Villes"+File.separator+nameMap+".dgs");
		} catch (Exception e) {
			e.printStackTrace();
		}
		addAttribute("stylesheet", stylesheet);
	}
	
	public View showGraph(boolean b){
		//System.setProperty("gs.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
		addAttribute("ui.stylesheet", stylesheet);
		return display(b).getDefaultView();
	}
	
	public String getNameMap() {
		return nameMap;
	}

	public static void main(String args[]){
		//Nom du graphe - selon les cas c'est aussi le nom du fichier DGS
		String nameMap;
		//Le Havre simple
			//nameMap="LeHavre_Single_Modified";
		//Le Havre modifie
				//carte avec l'ajout des distance euclidiennes sur les arcs
			//nameMap="LeHavre_Medium_Modified";
				//Carte basee sur la precedente mais cette fois en version multigraphe
			//nameMap="LeHavre_Medium_Multi_Modified";
				//Carte basee sur la precedente mais avec en plus les attributs associes aux moyennes
			nameMap="LeHavre_Medium_SingleGraph_ant";
		//Rouen
			//nameMap="Rouen_Medium";
		//Paris
			//nameMap="Paris_Medium";
		//Paris modifié
			//nameMap="Paris_Medium_Modified";
		//Simple graph
			//nameMap = "SimpleGraph";
		
		//Creration du graphe
		MultiNetwork rv=new MultiNetwork(nameMap);
		System.out.println(rv.getNodeCount());
		/*View v = rv.showGraph(false);
		//ChangeGraph.updateGraph(rv, "BetweennessCentrality_noweigth");//noweigth
		Main.attente("");
		//Sauvegarde dans une version svg
		rv.addAttribute("ui.screenshot", 
				"screenshot_Rouen.svg");*/
		
		//ChangeGraph.clearAll(rv);
		
		//ChangeGraph.createEuclideanEdgeValue(rv);
		//ChangeGraph.computeTravelTime(rv);
		
		//Melange les attributs entre deux graphes
		//ChangeGraph.mixGraph("Villes", rv);
		
		//Transforme en multi ou en simple graphe
		//Utils.updateInMultiGraph(rv);
		//ChangeGraph.updateInSingleGraph(rv);
		
		//Affiche le graph
		//System.out.println("Affichage");
		//Misc.showGraph(rv, false);
		
		//Ajout des attributs associes a la moyenne
		//Utils.addAverage(rv);
		
		/*Misc.saveGraph(System.getProperty("user.dir" )+File.separator+"DGS_and_results"+File.separator+"Villes"+File.separator+rv.getNameMap()
				+"_ant"
				+".dgs", rv);
		System.out.println("Sauvegarde");/**/
	}
}
