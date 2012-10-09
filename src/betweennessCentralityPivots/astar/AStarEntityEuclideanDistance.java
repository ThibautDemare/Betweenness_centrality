package betweennessCentralityPivots.astar;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;

public class AStarEntityEuclideanDistance extends AStarEntity {
	
	public AStarEntityEuclideanDistance(){
		super();
		if(ec == null)
			ec = new EuclideanCosts();
	}
	
	public class EuclideanCosts implements AStar.Costs {
		public double cost(Node arg0, Edge arg1, Node arg2) {
			if(!arg2.hasAttribute("length"))
				arg1.addAttribute("length", heuristic(arg0, arg2));
			return arg1.getNumber("length");
		}

		public double heuristic(Node n1, Node n2) {
			Object[] xyz1=n1.getAttribute("xyz");
			Object[] xyz2=n2.getAttribute("xyz");
			
			double dist = Math.sqrt(
					  ((Double)(xyz1[0])-(Double)(xyz2[0]))*((Double)(xyz1[0])-(Double)(xyz2[0]))
						 +((Double)(xyz1[1])-(Double)(xyz2[1]))*((Double)(xyz1[1])-(Double)(xyz2[1])));
			return dist;
		}
	}
	
}
