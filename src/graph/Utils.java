package graph;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.graphstream.graph.Element;
import org.graphstream.graph.Graph;
import utils.ComparatorElement;

public class Utils {

	/*
	 * Set of functions which save the ordered list of node (or eventually of edge) and the comparison values
	 */
	
	/**
	 * Allows to save an ordered list of node in a file when the values are computed by a random walk betweenness centrality
	 * @param rwalk
	 * @return
	 */
	public static List<Element> saveOrderedListNode(String att, Graph g){
		String nameFile = g.getId()+"_"
			+att+"_"
			+"listNode.dat";
		return Utils.saveOrderedListElement(new ArrayList<Element>(g.getNodeSet()), att, nameFile, g);
	}

	/**
	 * Save an ordered list of element sorted by the attribute att
	 * @param le List of element
	 * @param att The name of the attribute to compare
	 * @param nameFile The name of file wherein we save the list
	 * @param g The associated graph
	 * @return An ordered list of element sorted by the attribute att
	 */
	public static List<Element> saveOrderedListElement(List<Element> le, String att, String nameFile, Graph g){
		Collections.sort(le, new ComparatorElement(att));
		//Delete all element of the list which have a value equal to zero
		/*while(le.get(le.size()-1).getNumber(att)<0.25){
			le.remove(le.size()-1);
		}*/
		
		//Write the id's element in the file
		try{
			PrintWriter out  = new PrintWriter(new FileWriter(nameFile));
			for(Element e : le)
				out.println(e.getId()+"\t"+e.getNumber(att));
			out.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return le;
	}
	
	public static void saveComparisonValue(String file, double[] values, String[] atts){
		//Write the id's element in the file
		try{
			PrintWriter out  = new PrintWriter(new FileWriter(file));
			for(int i=0; i<values.length; i++)
				out.println("'"+atts[i]+"'\t"+values[i]);
			out.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}
