package graph;

import java.io.File;
import java.util.ArrayList;

import org.graphstream.algorithm.generator.Generator;
import org.graphstream.algorithm.generator.GridGenerator;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.graphicGraph.GraphicElement;
import org.graphstream.ui.swingViewer.View;

import utils.ChangeGraph;
import utils.Misc;

public class Grid extends SingleGraph {
	protected int nbLine;
	protected boolean cross;
	protected boolean tore;
	
	protected String stylesheet = 
		"graph {  fill-mode : none; padding : 20px, 20px;" +
				 "fill-color : rgb(200, 200, 200);}" +
		"node  {  text-mode : normal; size : 2px; " +
				 "fill-color: rgb(0, 0, 255);}"+
		"edge  {  text-mode : normal; size : 1px; shape : line; arrow-shape : none; " +
				 "fill-color: rgb(255, 0, 0);}";
	
	public Grid(int nbLine, boolean cross, boolean tore, boolean newGen){
		super("Grid_"+nbLine+"_"+cross+"_"+tore);
		this.nbLine = nbLine;
		this.cross = cross;
		this.tore = tore;
		if(newGen){
			Generator gen = new GridGenerator(cross, tore, true, false);
			gen.addSink(this);
			gen.begin();
			for(int i=0; i<nbLine; i++) {
			    gen.nextEvents();
			}
			gen.end();
		}
		else{
			try {
				read(System.getProperty("user.dir" )+File.separator+"DGS_and_results"+File.separator+"Grid"+File.separator
						+"Grid_"
						+nbLine+"_"
						+cross+"_"
						+tore+".dgs");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		addAttribute("stylesheet", stylesheet);
	}
	
	public int getNbLine() {
		return nbLine;
	}

	public void setNbLine(int nbLine) {
		this.nbLine = nbLine;
	}

	public boolean isCross() {
		return cross;
	}

	public void setCross(boolean cross) {
		this.cross = cross;
	}

	public boolean isTore() {
		return tore;
	}

	public void setTore(boolean tore) {
		this.tore = tore;
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
		Grid g = new Grid(10, false, false, true);
		ChangeGraph.createEuclideanEdgeValue(g);
		ChangeGraph.computeTravelTime(g);
		
		
		
		//ChangeGraph.mixGraph("Grid", g);
		
		//g.showGraph(false);
	//	Main.attente("");
		Misc.saveGraph(
				System.getProperty("user.dir" )+File.separator+"DGS_and_results"+File.separator+"Grid"+File.separator
				+"Grid_"
				+g.getNbLine()+"_"
				+g.isCross()+"_"
				+g.isTore()+"_2.dgs",
				g);/**/
	}
}
