package graph;

import java.io.File;
import java.io.IOException;

import mains.Main;

import org.graphstream.algorithm.Dijkstra;
import org.graphstream.graph.Edge;
import org.graphstream.graph.ElementNotFoundException;
import org.graphstream.graph.Node;
import org.graphstream.graph.Path;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.stream.GraphParseException;
import org.graphstream.ui.swingViewer.View;

import utils.ChangeGraph;

import classicalBetweennessCentrality.BetweennessCentrality;

public class Simple extends SingleGraph{
	protected String stylesheet = 
		"graph {  fill-mode : none; padding : 20px, 20px;" +
				 "fill-color : rgb(255, 255, 255);}" +
		"node  {  text-mode : normal; size : 2px; " +
				 "fill-color: rgb(0, 0, 255);}"+
		"edge  {  text-mode : normal; size : 1px; shape : line; arrow-shape : none; " +
				 "fill-color: rgb(255, 0, 0);}";

	public Simple(){
		super("Simple");
		
		try {
			read(System.getProperty("user.dir" )+File.separator+"DGS_and_results"+File.separator+"Villes"+File.separator+"LeHavre_Single.dgs");
		} catch (ElementNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (GraphParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public View showGraph(boolean b){
		//System.setProperty("gs.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
		addAttribute("ui.stylesheet", stylesheet);
		return display(b).getDefaultView();
	}
	
	public static void main(String args[]){
		Simple s = new Simple();
		
		/*BetweennessCentrality bcb = new BetweennessCentrality();
		//Initialisation
		bcb.setCentralityAttributeName("BetweennessCentrality");
		bcb.init(s);
		bcb.setWeightAttributeName("length");
		//Calcul
		bcb.compute();*/
		
		s.computeClosenessCentrality();
		
		/*for(Node n : s.getNodeSet())
			n.addAttribute("degree_centrality", n.getDegree());/**/
		
		//Mise a jour des couleurs
		ChangeGraph.updateGraph(s, "MaxFarnessCentrality");//BetweennessCentrality
		
		s.showGraph(false);
		
		//Sauvegarde dans une version SVG
		Main.attente("Attente avant sauvegarde");
		s.addAttribute("ui.screenshot", "max_farness_centrality.svg");
		
	}
	
	/**
	 * Calcul pour chaque noeud la centralité de proximité et retourne la plus haute centralité des noeuds du graphe
	 * @return La plus haute centralité des noeuds du graphe.
	 */
	public void computeClosenessCentrality(){
		//C_{closeness}(x_i)=\frac{1}{\sum_{j=1}^{n}\delta (x_i, x_j)}
		for(Node n1 : getNodeSet()){
			double closeness=0;
			double maxFarness = 0;
			for(Node n2 : getNodeSet()){
				if(n1!=n2){
					closeness+=getPathLength(n1, n2);//getDijkstra(n1).getPathLength(n2);
					maxFarness = Math.max(maxFarness, getPathLength(n1, n2));
				}
			}
			n1.addAttribute("FarnessCentrality", closeness);
			n1.addAttribute("MaxFarnessCentrality", maxFarness);
			n1.addAttribute("ClosenessCentrality", 1./closeness);
		}
	}
	

	/*
	 * Calcul de plus court chemin
	 */
	
	/**
	 * Returns the Dijkstra algorithm instance associated to a node. If no
	 * instance is associated, creates it. Used to compute the shortest path
	 * between to clients or between the warehouse and a client.
	 * 
	 * @param node
	 *            A node
	 * @return The Dijkstra instance associated to the node.
	 */
	public Dijkstra getDijkstra(Node node) {
		Dijkstra dijkstra = node.getAttribute("dijkstra");
		if (dijkstra == null) {
			dijkstra = new Dijkstra(Dijkstra.Element.EDGE, "dijkstra_"
					+ node.getId(), "length");
			dijkstra.init(this);
			dijkstra.setSource(node);
			dijkstra.compute();
			node.addAttribute("dijkstra", dijkstra);
		}
		return dijkstra;
	}
	
	public Path getPath(Node origin, Node dest){
		return getDijkstra(origin).getPath(dest);
	}
	
	public double getPathLength(Node origin, Node dest){
		return getDijkstra(origin).getPathLength(dest);
	}
}
