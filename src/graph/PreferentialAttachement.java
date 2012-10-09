package graph;

import java.io.File;
import java.util.ArrayList;

import mains.Main;

import org.graphstream.algorithm.generator.BarabasiAlbertGenerator;
import org.graphstream.algorithm.generator.Generator;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.graphicGraph.GraphicElement;
import org.graphstream.ui.swingViewer.View;

import utils.ChangeGraph;

public class PreferentialAttachement extends SingleGraph {
	protected int maxLinksPerStep;
	protected int nbNode;
	
	protected String stylesheet = 
		"graph {  fill-mode : none; padding : 20px, 20px;" +
				 "fill-color : white;}" +//rgb(200, 200, 200);
		"node  {  text-mode : normal; size : 8px; " +
				 "fill-color: rgb(0, 90, 170);}"+
		"edge  {  text-mode : normal; size : 1px; shape : line; arrow-shape : none; " +
				 "fill-color: rgb(0, 0, 0);}";
	
	public PreferentialAttachement(int maxLinksPerStep, int nbNode, boolean newGen){
		super("Preferential_Attachement_"+maxLinksPerStep+"_"+nbNode);
		
		this.maxLinksPerStep = maxLinksPerStep;
		this.nbNode = nbNode;
		
		if(newGen){
			Generator gen = new BarabasiAlbertGenerator(maxLinksPerStep);
			gen.addSink(this);
			gen.begin();
			for(int i=0; i<nbNode; i++) {
			    gen.nextEvents();
			}
			gen.end();
		}
		else{
			try {
				read(System.getProperty("user.dir" )+File.separator+"DGS_and_results"+File.separator+"PreferentialAttachement"+File.separator
						+"Preferential_Attachement_"
						+maxLinksPerStep+"_"
						+nbNode+".dgs");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		addAttribute("stylesheet", stylesheet);
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
	
	public int getMaxLinksPerStep() {
		return maxLinksPerStep;
	}

	public void setMaxLinksPerStep(int maxLinksPerStep) {
		this.maxLinksPerStep = maxLinksPerStep;
	}

	public int getNbNode() {
		return nbNode;
	}

	public void setNbNode(int nbNode) {
		this.nbNode = nbNode;
	}

	public static void main(String args[]){
		PreferentialAttachement pa1 = new PreferentialAttachement(2, 50, true);
		//ChangeGraph.mixGraph("PreferentialAttachement", pa1);
		
		View v = pa1.showGraph(true);
		Main.attente("");
		//Sauvegarde dans une version svg
		pa1.addAttribute("ui.screenshot", 
				"sceenshot_preferential_attachement.svg");
		
		//PreferentialAttachement pa2 = new PreferentialAttachement(1, 1000, false);
		//ChangeGraph.mixGraph("PreferentialAttachement", pa2);
		
		/*View v = pa.showGraph(true);
		Main.attente("Appuyer sur Enter lorsque la vue est correcte");
		pa.setAttributesXYZ(v);
		Misc.saveGraph(
				System.getProperty("user.dir" )+File.separator+"DGS_and_results"+File.separator+"PreferentialAttachement"+File.separator
				+"Preferential_Attachement_"
				+pa.getMaxLinksPerStep()+"_"
				+pa.getNbNode()+".dgs",
				pa);/**/
	}
}
