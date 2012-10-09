package graph;

import java.io.File;
import java.util.ArrayList;

import mains.Main;

import org.graphstream.algorithm.generator.Generator;
import org.graphstream.algorithm.generator.WattsStrogatzGenerator;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.graphicGraph.GraphicElement;
import org.graphstream.ui.swingViewer.View;

import utils.ChangeGraph;

public class SmallWorld extends SingleGraph{
	protected int nbNode;
	protected int k;
	protected double beta;
	
	protected String stylesheet = 
		"graph {  fill-mode : none; padding : 20px, 20px;" +
				 "fill-color : white;}" +//rgb(200, 200, 200);
		"node  {  text-mode : normal; size : 8px; " +
				 "fill-color: rgb(0, 90, 170);}"+
		"edge  {  text-mode : normal; size : 1px; shape : line; arrow-shape : none; " +
				 "fill-color: rgb(0, 0, 0);}";
	
	public SmallWorld(int nbNode, int k, double beta, boolean newGen){
		super("Small_World_"+nbNode+"_"+k+"_"+beta);
		
		this.nbNode = nbNode;
		this.k = k;
		this.beta = beta;
		
		if(newGen){
			Generator gen = new WattsStrogatzGenerator(this.nbNode, this.k, this.beta);
			 
			gen.addSink(this);
			gen.begin();
			while(gen.nextEvents()) {}
			gen.end();
		}
		else{
			try {
				read(System.getProperty("user.dir" )+File.separator+"DGS_and_results"+File.separator+"SmallWorld"+File.separator
						+"Small_World_"
						+nbNode+"_"
						+k+"_"
						+beta
						+".dgs");
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

	public int getK() {
		return k;
	}

	public void setK(int k) {
		this.k = k;
	}

	public double getBeta() {
		return beta;
	}

	public void setBeta(double beta) {
		this.beta = beta;
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
				if(n.hasAttribute("xyz"))
					n.removeAttribute("xyz");
				if(n.hasAttribute("xy"))
					n.removeAttribute("xy");
				n.addAttribute("x", ge.getX());
				n.addAttribute("y", ge.getY());
				n.addAttribute("z", ge.getZ());
			}
		}
	}
	
	public static void main(String args[]){
		/*ConnectedComponents cc;
		SmallWorld sw;
		do{
			cc = new ConnectedComponents();
			sw = new SmallWorld(10000, 6, 0.001, true);
			//sw = new SmallWorld(1000, 2, 0.5, true);
			cc.init(sw);
		}while(cc.getConnectedComponentsCount()>1);
		
		//View v = sw.showGraph(true);
		View v = sw.showGraph(false);
		Main.attente("");
		sw.setAttributesXYZ(v);
		Misc.saveGraph(
				System.getProperty("user.dir" )+File.separator+"DGS_and_results"+File.separator+"SmallWorld"+File.separator
				+"Small_World_"
				+sw.getNbNode()+"_"
				+sw.getK()+"_"
				+sw.getBeta()
				+".dgs",
				sw);/**/
		
		SmallWorld sw1=new SmallWorld(100, 2, 0.1, true);
		View v = sw1.showGraph(false);
		Main.attente("");
		//Sauvegarde dans une version svg
		sw1.addAttribute("ui.screenshot", 
				"screenshot_small_world.svg");
		
		/*SmallWorld sw2 = new SmallWorld(10000, 6, 0.001, false);
		View v = sw.showGraph(false);
		
		SmallWorld sw3 = new SmallWorld(1000, 2, 0.5, false);
		View v = sw.showGraph(false);*/
	}
}
