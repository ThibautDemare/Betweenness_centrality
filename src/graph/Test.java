package graph;

import java.io.IOException;
import java.sql.Savepoint;
import java.util.HashMap;
import java.util.prefs.NodeChangeEvent;

import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;

public class Test extends SingleGraph {

	public Test(String id) {
		super(id);
		HashMap<String, Double> hm1 = new HashMap<String, Double>();
		hm1.put("1", 1.0);
		hm1.put("2", 1.0);
		hm1.put("3", 1.0);
		hm1.put("4", 1.0);
		hm1.put("1", 2.0);
		
		HashMap<String, Double> hm2 = new HashMap<String, Double>();
		hm2.put("1", 1.0);
		hm2.put("2", 1.0);
		hm2.put("3", 1.0);
		hm2.put("4", 1.0);
		hm2.put("1", 2.0);
		
		setStrict(false);
		setAutoCreate(true);
		addNode("n");
		Node n = getNode("n");
		n.addAttribute("hm1", hm1);
		n.addAttribute("hm2", hm2);
		try {
			write("test.dgs");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String args[]){
		new Test("p");
	}
}
