package graph;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;

public class Network extends SingleGraph{
	protected String color1 = "rgb(255, 255, 0)";
	protected String color2 = "rgb(0, 100, 255)";
	protected String color3 = "rgb(255, 170, 0)";
	protected String color4 = "rgb(255, 0, 0)";
	
	protected String stylesheet = 
		"graph {  fill-mode : none; padding : 20px, 20px;" +
				 "fill-color : rgb(200, 200, 200);}" +//white
		"node  {  text-mode : normal; size : 1px; " +
				 "fill-color: "+color1+", "
				 +color2+", "
				 //+color3+", "
				 +color4+";" +
				 "fill-mode : dyn-plain;}"+
		"edge  {  text-mode : normal; size : 1px; shape : line; arrow-shape : none; " +
				 "fill-color: "+color1+", "
				 +color2+", "
				 //+color3+", "
				 +color4+";" +
				 "fill-mode : dyn-plain;}";
	protected String nameMap;

	public Network(String id) {
		super(id);
		nameMap=id;
		try {
			read(System.getProperty("user.dir" )+File.separator+"DGS_and_results"+File.separator+"Villes"+File.separator+nameMap+".dgs");
		} catch (Exception e) {
			e.printStackTrace();
		}
		addAttribute("stylesheet", stylesheet);
	}
	
	public Network(){
		super("mini graph");
		setStrict(false);
		setAutoCreate(true);
		createAndPositionNode();
		createEdge();
		createEuclideanEdgeValue();
		modifyAttributeXYZ();
	}
	
	private void createAndPositionNode(){
		//creation des noeuds
		for(int i=1; i<20; i++)
			addNode(""+i);
		
		//Positionnement
		getNode("1").addAttribute("x", 0);
		getNode("1").addAttribute("y", 0);
		
		getNode("2").addAttribute("x", 0);
		getNode("2").addAttribute("y", 1);

		getNode("3").addAttribute("x", 2);
		getNode("3").addAttribute("y", 1);
		
		getNode("4").addAttribute("x", 1);
		getNode("4").addAttribute("y", -1);
		
		getNode("5").addAttribute("x", 0);
		getNode("5").addAttribute("y", -1);
		
		getNode("6").addAttribute("x", -1);
		getNode("6").addAttribute("y", -1);
		
		getNode("7").addAttribute("x", -1);
		getNode("7").addAttribute("y", 0);
		
		getNode("8").addAttribute("x", -0.5);
		getNode("8").addAttribute("y", 3);
		
		getNode("9").addAttribute("x", 2);
		getNode("9").addAttribute("y", 3);
		
		getNode("10").addAttribute("x", 3);
		getNode("10").addAttribute("y", 1);
		
		getNode("11").addAttribute("x", 3);
		getNode("11").addAttribute("y", 0);
		
		getNode("12").addAttribute("x", 1);
		getNode("12").addAttribute("y", -2);
		
		getNode("13").addAttribute("x", 0.5);
		getNode("13").addAttribute("y", -2);
		
		getNode("14").addAttribute("x", -0.5);
		getNode("14").addAttribute("y", -2);
		
		getNode("15").addAttribute("x", -0.5);
		getNode("15").addAttribute("y", -3);
		
		getNode("16").addAttribute("x", -2);
		getNode("16").addAttribute("y", -2);
		
		getNode("17").addAttribute("x", 1);
		getNode("17").addAttribute("y", -2);
		
		getNode("18").addAttribute("x", -2);
		getNode("18").addAttribute("y", 3);
		
		getNode("19").addAttribute("x", -2);
		getNode("19").addAttribute("y", 0);
		
	}
	
	private void createEdge(){
		//Creation des arretes
		addEdge("1-2", "1", "2");
		addEdge("1-3", "1", "3");
		addEdge("1-4", "1", "4");
		addEdge("1-5", "1", "5");
		addEdge("1-6", "1", "6");
		addEdge("1-7", "1", "7");
		
		addEdge("2-8", "2", "8");
		addEdge("2-9", "2", "9");
		addEdge("2-3", "2", "3");
		
		addEdge("3-9", "3", "9");
		addEdge("3-10", "3", "10");
		addEdge("3-11", "3", "11");
		addEdge("3-4", "3", "4");
		
		addEdge("4-5", "4", "5");
		addEdge("4-11", "4", "11");
		addEdge("4-12", "4", "12");
		addEdge("4-13", "4", "13");
		
		addEdge("5-6", "5", "6");
		addEdge("5-14", "5", "14");
		addEdge("5-13", "5", "13");
		
		addEdge("6-7", "6", "7");
		addEdge("6-16", "6", "16");
		addEdge("6-14", "6", "14");
		addEdge("6-19", "6", "19");
		
		addEdge("7-8", "7", "8");
		addEdge("7-19", "7", "19");
		
		addEdge("8-9", "8", "9");
		addEdge("8-18", "8", "18");
		
		addEdge("9-10", "9", "10");
		
		addEdge("10-11", "10", "11");
		
		addEdge("12-13", "12", "13");
		addEdge("12-17", "12", "17");
		
		addEdge("13-15", "13", "15");
		
		addEdge("14-15", "14", "15");
		addEdge("14-16", "14", "16");
		
		addEdge("18-19", "18", "19");
	}
	
	private void createEuclideanEdgeValue(){
		for(Edge e : getEdgeSet()){
			Node n1=e.getNode0();
			Node n2=e.getNode1();
			double dist=Math.sqrt((n1.getNumber("x")-n2.getNumber("x"))*(n1.getNumber("x")-n2.getNumber("x"))
					 +(n1.getNumber("y")-n2.getNumber("y"))*(n1.getNumber("y")-n2.getNumber("y")));
			e.addAttribute("length", dist);
			e.addAttribute("ui.label", dist);
		}
	}
	
	public void showGraph(boolean b){
		//System.setProperty("gs.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
		addAttribute("ui.stylesheet", stylesheet);
		display(b);
	}

	public String getNameMap() {
		return nameMap;
	}

	public void modifyAttributeXYZ(){
		for(Node n : this){
			if(!n.hasAttribute("xyz")){
				Object[] coord=new Object[3];
				coord[0]=n.getNumber("x");
				coord[1]=n.getNumber("y");
				if(n.hasAttribute("z"))
					coord[2]=n.getNumber("z");
				else
					coord[2]=0.;
				n.addAttribute("xyz", coord);
			}
		}
	}

	public void transformInDirectedGraph(){
		ArrayList<Edge> le = new ArrayList<Edge>();
		for(int i=0; i<getEdgeCount(); i++){
			le.add(getEdge(i));
			removeEdge(i);
		}

		for(Edge e : le){
			addEdge(e.getId()+"_"+e.getNode0()+"_"+e.getNode1(), e.getNode0(), e.getNode1(), true);
			addEdge(e.getId()+"_"+e.getNode1()+"_"+e.getNode0(), e.getNode1(), e.getNode0(), true);
		}
	}
	
	public int testIsolatedNode(){
		int i=0;
		for(Node n : this){
			Iterator<Edge> itL = n.getLeavingEdgeIterator();
			
			if(!itL.hasNext()){
				i++;
				removeIsolation();
				Iterator<Edge> ite = n.getEnteringEdgeIterator();
				Edge e = ite.next();
				addEdge(n.getId()+"_"+e.getOpposite(n), n.getIndex(), e.getOpposite(n).getIndex(), true);
				n.addAttribute("isolated", true);
			}
		}
		return i;
	}
	
	public void removeIsolation(){
		boolean newIsolated;
		for(Node n : this){
			n.addAttribute("isolated", false);
		}
		
		//On boucle tant qu'on decouvre encore de nouveu noeud isoler ou conduisant exclusivement a de tel noeud
		do{
			newIsolated=false;
			
			for(Node n : this){
				//Si le noeud n'est pas deja considerer isoler (sinon il ne sert a rien de recommencer)
				if(!(Boolean)n.getAttribute("isolated")){
					//Pour chaque noeud du graphe on test s'il sont isoler ou en contact avec des noeuds isoler
					Iterator<Edge> it = n.getLeavingEdgeIterator();
					boolean b=true;
					while(it.hasNext() && b){
						if(!(Boolean)it.next().getOpposite(n).getAttribute("isolated"))
							b=false;
					}
					if(b){//S'ils le sont
						//On regarde l'ensemble de leur arrete entrante
						Iterator<Edge> ite = n.getEnteringEdgeIterator();
						while(ite.hasNext()){
							Edge e = ite.next();
							//Et s'il n'existe pas d'arrete sortante on la cree
							if(n.getEdgeToward(e.getOpposite(n))==null)
								addEdge(n.getId()+"_"+e.getOpposite(n), n.getIndex(), e.getOpposite(n).getIndex(), true);
						}
						//et on dit que ce noeud fait partie d'une composante manant a l'isolement
						n.addAttribute("isolated", true);
						//On en a donc decouvert un nouveau il faut donc recommencer
						newIsolated=true;
					}
				}
			}
		}while(newIsolated);
	}
	
	public class ComparatorNode implements Comparator<Node>{
		String att;
		public ComparatorNode(String att){
			this.att = att;
		}
		
		public int compare(Node n1, Node n2) {
			double nbPassesN1 = n1.getNumber(att);
			double nbPassesN2 = n2.getNumber(att);
			if(nbPassesN1>nbPassesN2)
				return -1;
			else if(nbPassesN1 == nbPassesN2)
				return 0;
			else
				return 1;
		}
		
	}
	
	public static void main(String args[]){
		//Nom du graphe - selon les cas c'est aussi le nom du fichier DGS
		//String nameMap;
		//Le Havre simple
		//nameMap="LeHavre_Single_Modified";
		
		//Le Havre modifie
		//nameMap="LeHavre_Medium_Modified";
		
		//Le Havre classique
		//nameMap="LeHavre_Medium";
		
		//Paris
		//nameMap="Paris_Medium";
		
		//Paris modifié
		//nameMap="Paris_Medium_Modified";
		
		//Simple graph
		//nameMap = "SimpleGraph";
		
		//Creration du graphe
		//Network rv=new Network(nameMap);
		
		//Affiche le graph
		//System.out.println("Affichage");
		//rv.showGraph();
		
		//rv.saveGraph(System.getProperty("user.dir" )+File.separator+"DGS_and_results"+File.separator+"Villes"+File.separator
				//+rv.getNameMap()
				//+"_Modified"
			//	+".dgs");
		//System.out.println("Sauvegarde");
	}
}
