package utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.graph.implementations.SingleGraph;

public class ChangeGraph {

	public static void createEuclideanEdgeValue(Graph g){
		for(Edge e : g.getEdgeSet()){
			Node n1=e.getNode0();
			Node n2=e.getNode1();
			
			Object[] xyz1;
			Object[] xyz2;
			
			if(n1.hasAttribute("xyz")){
				xyz1=n1.getAttribute("xyz");
				xyz2=n2.getAttribute("xyz");
			}
			else if(n1.hasAttribute("xy")){
				xyz1=n1.getAttribute("xy");
				xyz2=n2.getAttribute("xy");
			}
			else{
				xyz1=new Double[2];
				xyz1[0]=n1.getNumber("x");
				xyz1[1]=n1.getNumber("y");
				
				xyz2=new Double[2];
				xyz2[0]=n2.getNumber("x");
				xyz2[1]=n2.getNumber("y");
			}
			
			double dist = Math.sqrt(
					  ((Double)(xyz1[0])-(Double)(xyz2[0]))*((Double)(xyz1[0])-(Double)(xyz2[0]))
					 +((Double)(xyz1[1])-(Double)(xyz2[1]))*((Double)(xyz1[1])-(Double)(xyz2[1])));
			
			e.addAttribute("length", dist);
		}
	}

	public static void computeTravelTime(Graph g){
		for(Edge e : g.getEdgeSet()){
			int speed;
			if(e.hasAttribute("SPEED_CAT")){
				int val = (int)e.getNumber("SPEED_CAT");
				switch(val){
				case 1 :
					speed = 130;
				case 2 :
					speed = 115;
				case 3 :
					speed = 95;
				case 4 :
					speed = 80;
				case 5 :
					speed = 60;
				case 6 :
					speed = 40;
				case 7 :
					speed = 21;
				default : //8
					speed = 11;
				}
			}
			else
				speed = 1;
			e.addAttribute("travelTime", e.getNumber("length") / speed );
		}
	}
	
	public static void updateInSingleGraph(Graph g){
		ArrayList<Edge> le = new ArrayList<Edge>(g.getEdgeSet());
		for(int i = 0; i<le.size(); i++){
			Node n11 = le.get(i).getNode0();
			Node n12 = le.get(i).getNode1();
			int j = 0;
			while(j<le.size()){
				Node n21 = le.get(j).getNode0();
				Node n22 = le.get(j).getNode1();
				if(n21==n11 && n12==n22)
					le.remove(j);
				else
					j++;
			}
		}
	}
	
	public static void updateInMultiGraph(Graph g){
		ArrayList<Edge> le = new ArrayList<Edge>();
		
		//Identification of edges 
		for(Edge e : g.getEdgeSet()){
			e.addAttribute("hasClone", false);
			String cat = "LANE_CAT";
			double nbLane = e.getNumber(cat);
			if(nbLane > 1)
				le.add(e);
		}
		
		for(Edge e : le){
			String cat = "LANE_CAT";
			int nbLane = (int)e.getNumber(cat);
			g.removeEdge(e);
			ArrayList<String> tmp=new ArrayList<String>();
			switch(nbLane){
			case 2 :
				tmp.add(g.addEdge(e.getId()+"_1", e.getNode0(), e.getNode1(), e.isDirected()).getId());
				tmp.add(g.addEdge(e.getId()+"_2", e.getNode0(), e.getNode1(), e.isDirected()).getId());
				g.getEdge((String)tmp.get(0)).addAttribute("clone", tmp.toArray());
				g.getEdge((String)tmp.get(0)).addAttribute("hasClone", true);
				g.getEdge((String)tmp.get(1)).addAttribute("clone", tmp.toArray());
				g.getEdge((String)tmp.get(1)).addAttribute("hasClone", true);
				break;
			default :
				tmp.add(g.addEdge(e.getId()+"_1", e.getNode0(), e.getNode1(), e.isDirected()).getId());
				tmp.add(g.addEdge(e.getId()+"_2", e.getNode0(), e.getNode1(), e.isDirected()).getId());
				tmp.add(g.addEdge(e.getId()+"_3", e.getNode0(), e.getNode1(), e.isDirected()).getId());
				tmp.add(g.addEdge(e.getId()+"_4", e.getNode0(), e.getNode1(), e.isDirected()).getId());
				g.getEdge((String)tmp.get(0)).addAttribute("clone", tmp.toArray());
				g.getEdge((String)tmp.get(0)).addAttribute("hasClone", true);
				g.getEdge((String)tmp.get(1)).addAttribute("clone", tmp.toArray());
				g.getEdge((String)tmp.get(1)).addAttribute("hasClone", true);
				g.getEdge((String)tmp.get(2)).addAttribute("clone", tmp.toArray());
				g.getEdge((String)tmp.get(2)).addAttribute("hasClone", true);
				g.getEdge((String)tmp.get(3)).addAttribute("clone", tmp.toArray());
				g.getEdge((String)tmp.get(3)).addAttribute("hasClone", true);
				break;
			}
		}
	}
	
	public static void addAverage(Graph g, int numb, String entityClassName){
		for(Node n : g.getNodeSet()){
			String att = "random_walk_betweenness_centrality";
			if(!n.hasAttribute(att+"_sum_"+numb+"_"+entityClassName)){
				n.addAttribute(att+"_sum_"+numb+"_"+entityClassName, 0);
				n.addAttribute(att+"_nb_computed_"+numb+"_"+entityClassName, 0);
				n.addAttribute(att+"_average_"+numb+"_"+entityClassName, 0);
			}
		}
		
		for(Edge e : g.getEdgeSet()){
			String att = "random_walk_betweenness_centrality";
			if(!e.hasAttribute(att+"_sum_"+numb+"_"+entityClassName)){
				e.addAttribute(att+"_sum_"+numb+"_"+entityClassName, 0);
				e.addAttribute(att+"_nb_computed_"+numb+"_"+entityClassName, 0);
				e.addAttribute(att+"_average_"+numb+"_"+entityClassName, 0);
			}
		}
	}
	
	public static void addAverage(Graph g, String entityClassName){
		for(Node n : g.getNodeSet()){
			String att = "random_walk_passes";
			if(!n.hasAttribute(att+"_sum_"+entityClassName)){
				n.addAttribute(att+"_sum_"+entityClassName, 0);
				n.addAttribute(att+"_nb_computed_"+entityClassName, 0);
				n.addAttribute(att+"_average_"+entityClassName, 0);
			}
		}
		
		for(Edge e : g.getEdgeSet()){
			String att = "random_walk_passes";
			if(!e.hasAttribute(att+"_sum_"+entityClassName)){
				e.addAttribute(att+"_sum_"+entityClassName, 0);
				e.addAttribute(att+"_nb_computed_"+entityClassName, 0);
				e.addAttribute(att+"_average_"+entityClassName, 0);
			}
		}
	}
	
	/**
	 * Clean a graph deleting some useless attributes
	 * @param g
	 */
	public static void clear(Graph g){
		for(Node n : g.getEachNode()){
			n.removeAttribute("BiasedRandomWalkBetweennessCentrality_tmp");
			Iterator<String> it = n.getAttributeKeyIterator();
			ArrayList<String> ls = new ArrayList<String>();
			while(it.hasNext()){
				String s = it.next();
				if(s.contains("NumberOfPassesFrom") || s.contains("computedFrom") || s.contains("brandes."))
					ls.add(s);
			}
			for(String s : ls)
				n.removeAttribute(s);
		}
		
		for(Edge e : g.getEachEdge()){
			e.removeAttribute("BiasedRandomWalkBetweennessCentrality_tmp");
			Iterator<String> it = e.getAttributeKeyIterator();
			ArrayList<String> ls = new ArrayList<String>();
			while(it.hasNext()){
				String s = it.next();
				if(s.contains("NumberOfPassesFrom") || s.contains("computedFrom") || s.contains("brandes."))
					ls.add(s);
			}
			for(String s : ls)
				e.removeAttribute(s);
		}
	}
	
	/**
	 * Clean a graph deleting all attributes
	 * @param g
	 */
	public static void clear(Graph g, int num, String entityClassName){
		
		for(Node n : g.getEachNode()){
			Iterator<String> it = n.getAttributeKeyIterator();
			ArrayList<String> ls = new ArrayList<String>();
			while(it!=null && it.hasNext()){
				String s = it.next();
				if(s.contains("_sum_"+num+"_"+entityClassName) || s.contains("_average_"+num+"_"+entityClassName) || s.contains("_nb_computed_"+num+"_"+entityClassName))
					ls.add(s);
			}
			for(String s : ls)
				n.removeAttribute(s);
		}
		
		for(Edge e : g.getEachEdge()){
			Iterator<String> it = e.getAttributeKeyIterator();
			ArrayList<String> ls = new ArrayList<String>();
			while(it!=null && it.hasNext()){
				String s = it.next();
				if(s.contains("_sum_"+num+"_"+entityClassName) || s.contains("_average_"+num+"_"+entityClassName) || s.contains("_nb_computed_"+num+"_"+entityClassName))
					ls.add(s);
			}
			for(String s : ls)
				e.removeAttribute(s);
		}
	}
	
	/**
	 * Clean a graph deleting all attributes
	 * @param g
	 */
	public static void clearAll(Graph g){
		
		for(Node n : g.getEachNode()){
			if(n.hasAttribute("BiasedRandomWalkBetweennessCentrality_tmp"))
				n.removeAttribute("BiasedRandomWalkBetweennessCentrality_tmp");
			if(n.hasAttribute("passes"))
				n.removeAttribute("passes");
			if(n.hasAttribute("BetweennessCentrality"))
				n.removeAttribute("BetweennessCentrality");
			if(n.hasAttribute("ui.color"))
				n.removeAttribute("ui.color");
			if(n.hasAttribute("BiasedRandomWalkBetweennessCentrality"))
				n.removeAttribute("BiasedRandomWalkBetweennessCentrality");
				
			Iterator<String> it = n.getAttributeKeyIterator();
			ArrayList<String> ls = new ArrayList<String>();
			while(it.hasNext()){
				String s = it.next();
				if(s.contains("NumberOfPassesFrom") || s.contains("computedFrom") || s.contains("brandes.") ||
				   s.contains("_sum") || s.contains("_average") || s.contains("_nb_computed"))
					ls.add(s);
			}
			for(String s : ls)
				n.removeAttribute(s);
		}
		
		for(Edge e : g.getEachEdge()){
			if(e.hasAttribute("random_walk_passes_sum"))
				e.removeAttribute("random_walk_passes_sum");
			if(e.hasAttribute("passes_tmp"))
				e.removeAttribute("passes_tmp");
			if(e.hasAttribute("passes"))
				e.removeAttribute("passes");
			if(e.hasAttribute("BetweennessCentrality"))
				e.removeAttribute("BetweennessCentrality");
			if(e.hasAttribute("ui.color"))
				e.removeAttribute("ui.color");
			if(e.hasAttribute("BiasedRandomWalkBetweennessCentrality"))
				e.removeAttribute("BiasedRandomWalkBetweennessCentrality");
			if(e.hasAttribute("BiasedRandomWalkBetweennessCentrality_tmp"))
				e.removeAttribute("BiasedRandomWalkBetweennessCentrality_tmp");
			if(e.hasAttribute("travelTime"))
				e.removeAttribute("travelTime");
			
			Iterator<String> it = e.getAttributeKeyIterator();
			ArrayList<String> ls = new ArrayList<String>();
			while(it.hasNext()){
				String s = it.next();
				if(s.contains("NumberOfPassesFrom") || s.contains("computedFrom") || s.contains("brandes.") ||
				   s.contains("_sum") || s.contains("_average") || s.contains("_nb_computed"))
					ls.add(s);
			}
			for(String s : ls)
				e.removeAttribute(s);
		}
	}
	

	/**
	 * Clean a graph deleting all attributes
	 * @param g
	 */
	public static void clearAll(Graph g, int num, String entityClassName){
		
		for(Node n : g.getEachNode()){
			if(n.hasAttribute("BiasedRandomWalkBetweennessCentrality_tmp"))
				n.removeAttribute("BiasedRandomWalkBetweennessCentrality_tmp");
			if(n.hasAttribute("passes"))
				n.removeAttribute("passes");
			if(n.hasAttribute("BetweennessCentrality"))
				n.removeAttribute("BetweennessCentrality");
			if(n.hasAttribute("ui.color"))
				n.removeAttribute("ui.color");
			if(n.hasAttribute("BiasedRandomWalkBetweennessCentrality"))
				n.removeAttribute("BiasedRandomWalkBetweennessCentrality");
				
			Iterator<String> it = n.getAttributeKeyIterator();
			ArrayList<String> ls = new ArrayList<String>();
			while(it!=null && it.hasNext()){
				String s = it.next();
				if(s.contains("NumberOfPassesFrom") || s.contains("computedFrom") || s.contains("brandes.") ||
				   s.contains("_sum_"+num+"_"+entityClassName) || s.contains("_average_"+num+"_"+entityClassName) || s.contains("_nb_computed_"+num+"_"+entityClassName))
					ls.add(s);
			}
			for(String s : ls)
				n.removeAttribute(s);
		}
		
		for(Edge e : g.getEachEdge()){
			if(e.hasAttribute("random_walk_passes_sum"))
				e.removeAttribute("random_walk_passes_sum");
			if(e.hasAttribute("passes_tmp"))
				e.removeAttribute("passes_tmp");
			if(e.hasAttribute("passes"))
				e.removeAttribute("passes");
			if(e.hasAttribute("BetweennessCentrality"))
				e.removeAttribute("BetweennessCentrality");
			if(e.hasAttribute("ui.color"))
				e.removeAttribute("ui.color");
			if(e.hasAttribute("BiasedRandomWalkBetweennessCentrality"))
				e.removeAttribute("BiasedRandomWalkBetweennessCentrality");
			if(e.hasAttribute("BiasedRandomWalkBetweennessCentrality_tmp"))
				e.removeAttribute("BiasedRandomWalkBetweennessCentrality_tmp");
			
			Iterator<String> it = e.getAttributeKeyIterator();
			ArrayList<String> ls = new ArrayList<String>();
			while(it!=null && it.hasNext()){
				String s = it.next();
				if(s.contains("NumberOfPassesFrom") || s.contains("computedFrom") || s.contains("brandes.") ||
				   s.contains("_sum_"+num+"_"+entityClassName) || s.contains("_average_"+num+"_"+entityClassName) || s.contains("_nb_computed_"+num+"_"+entityClassName))
					ls.add(s);
			}
			for(String s : ls)
				e.removeAttribute(s);
		}
	}
	
	/**
	 * Update the coloration of the elements of the graph
	 */
	public static void updateGraph(Graph g, String att) {
		double mine = Double.POSITIVE_INFINITY;
		double maxe = Double.NEGATIVE_INFINITY;

		// Obtain the maximum and minimum values.
		for(Node n: g.getEachNode()) {
			double passes = n.getNumber(att);
			maxe = Math.max(maxe, passes);
			mine = Math.min(mine, passes);
		}

		// Set the colors.
		for(Node n: g.getEachNode()) {
			if(n.hasAttribute(att)){
				double passes = n.getNumber(att);
				double color;
				if(maxe==mine)
					color = 0.;
				else
					color = ((passes-mine)/(maxe-mine));
				//when we use the fill-mode:dyn-plain in the stylesheet.
				//n.setAttribute("ui.color", color);
				n.changeAttribute("ui.style", "fill-color: "+Misc.getColor(color)+" size: 4px;");//
			}
			else
				n.changeAttribute("ui.style", "fill-color: blue; size: 1px;");
		}

		mine = Double.POSITIVE_INFINITY;
		maxe = Double.NEGATIVE_INFINITY;
		// Obtain the maximum and minimum values.
		for(Edge e: g.getEachEdge()) {
			double passes = e.getNumber(att);
			maxe = Math.max(maxe, passes);
			mine = Math.min(mine, passes);
		}

		// Set the colors.
		for(Edge e: g.getEachEdge()) {
			if(e.hasAttribute(att)){
				double passes = e.getNumber(att);
				double color;
				if(maxe==mine)
					color = 0.;
				else
					color = ((passes-mine)/(maxe-mine));
				//when we use the fill-mode:dyn-plain in the stylesheet.
				//e.setAttribute("ui.color", color);
				e.changeAttribute("ui.style", "fill-color: "+Misc.getColor(color)+" size: 1"+""+"px;");//(int)(1+Math.sqrt(color)*2)
			}
			else
				e.changeAttribute("ui.style", "fill-color: blue; size: 1px;");
		}

	}
	
	/*
	 * Set of function which update the value on the edge if there is multiple edge between the same paire of node
	 */
	
	public static void updateEdgeValues(String att, Graph g){
		for(Edge e: g.getEachEdge()) {
			//For the multi-edge, we sum their value of passes
			if((Boolean)e.getAttribute("hasClone")){
				Object[] clone = (e.getAttribute("clone"));
				double p=0;
				for(Object o : clone){
					Edge c = g.getEdge((String)o);
					p+=c.getNumber(att);
				}
				e.setAttribute(att+"_tmp", p);
			}
		}
		//And now we update the value in the right attribute
		for(Edge e: g.getEachEdge()) {
			if((Boolean)e.getAttribute("hasClone")){
				e.setAttribute(att, e.getNumber(att+"_tmp"));
			}
		}
	}
	

	public static void mixGraph(String dir, Graph g1){
		//Graph g2=new MultiGraph("");
		Graph g2=new SingleGraph("");
		try {
			g2.read(System.getProperty("user.dir" )+File.separator+".."+File.separator+dir+File.separator
					+g1.getId()
					+"_v2.dgs");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		for(int i=0; i<g1.getNodeCount(); i++){
			Node nc = g1.getNode(i);
			Node ng = g2.getNode(nc.getId());
			
			Iterator<String> it = ng.getAttributeKeyIterator();
			while(it!=null && it.hasNext()){
				String s = it.next();
				if(!nc.hasAttribute(s))
					nc.addAttribute(s, ng.getAttribute(s));
			}
		}
		
		String f = System.getProperty("user.dir" )+File.separator+".."+File.separator+g1.getId()+"_v2.dgs";
		Misc.saveGraph(f, g1);
	}
	
}
