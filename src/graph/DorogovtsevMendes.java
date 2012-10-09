package graph;

import java.io.File;
import java.util.ArrayList;

import mains.Main;

import org.graphstream.algorithm.generator.DorogovtsevMendesGenerator;
import org.graphstream.algorithm.generator.Generator;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.graphicGraph.GraphicElement;
import org.graphstream.ui.swingViewer.View;

import utils.ChangeGraph;
import utils.Misc;

public class DorogovtsevMendes extends SingleGraph {
	protected int nbNode;
	
	protected String stylesheet = 
		"graph {  fill-mode : none; padding : 20px, 20px;" +
				 "fill-color : white;}" +//rgb(200, 200, 200);
		"node  {  text-mode : normal; size : 8px; " +
				 "fill-color: rgb(0, 90, 170);}"+
		"edge  {  text-mode : normal; size : 1px; shape : line; arrow-shape : none; " +
				 "fill-color: rgb(0, 0, 0);}";
	
	public DorogovtsevMendes(int nbNode, boolean newGen){
		super("Dorogovtsev_Mendes_"+nbNode);
		this.nbNode = nbNode;
		if(newGen){
			Generator gen = new DorogovtsevMendesGenerator();
			gen.addSink(this);
			gen.begin();
			for(int i=0; i<nbNode; i++) {
			    gen.nextEvents();
			}
			gen.end();
		}
		else{
			try {
				read(System.getProperty("user.dir" )+File.separator+"DGS_and_results"+File.separator+"DorogovtsevMendes"+File.separator
						+"Dorogovtsev_Mendes_"
						+nbNode+".dgs");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		addAttribute("stylesheet", stylesheet);
	}
	
	public int getNbNode() {
		return nbNode;
	}

	public void setNbNode(int nbNode) {
		this.nbNode = nbNode;
	}

	public View showGraph(boolean b){
		//System.setProperty("gs.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
		addAttribute("ui.stylesheet", stylesheet);
		return display(b).getDefaultView();
	}
	
	public void setAttributesXYZ(View v){
		ArrayList<GraphicElement> lge = v.allNodesOrSpritesIn(0, 0, v.getWidth(), v.getHeight());
		for(GraphicElement ge : lge){
			String id = ge.getId();
			Node n = getNode(id); 
			if(n!=null){
				n.addAttribute("x", ge.getX());
				n.addAttribute("y", ge.getY());
				n.addAttribute("z", ge.getZ());
			}
		}
	}
	
	public static void main(String args[]){
		DorogovtsevMendes dm = new DorogovtsevMendes(100, true);
		View v = dm.showGraph(true);
		Main.attente("");
		//Sauvegarde dans une version svg
		dm.addAttribute("ui.screenshot", 
				"screenshot_Dorogovtsev_Mendes.svg");
		
		//ChangeGraph.mixGraph("DorogovtsevMendes", dm);
		//ChangeGraph.createEuclideanEdgeValue(dm);
		//ChangeGraph.computeTravelTime(dm);
		
		/*View v = dm.showGraph(true);
		Main.attente("");
		
		dm.setAttributesXYZ(v);
		//ChangeGraph.addAverage(dm);
		*/
		/*Misc.saveGraph(
				System.getProperty("user.dir" )+File.separator+"DGS_and_results"+File.separator+"DorogovtsevMendes"+File.separator
				+"Dorogovtsev_Mendes_"
				+dm.getNbNode()+"_2.dgs",
				dm);
		System.out.println("Sauvegarde effectuée");/**/
	}
}
