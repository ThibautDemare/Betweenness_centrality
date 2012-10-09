package betweennessCentralityPivots.astar;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;

public class AStarEntityTravelTime extends AStarEntity {
	
	public AStarEntityTravelTime(){
		super();
		if(ec == null)
			ec = new TravelTimeCosts();
	}
	
	public class TravelTimeCosts implements AStar.Costs {
		double speed;
		public double cost(Node n1, Edge e, Node n2) {
			double travelTime = e.getNumber("travelTime");
			speed = associatedSpeed(e);
			return travelTime;
		}
		
		public double heuristic(Node n1, Node n2) {
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
			
			return dist / 140;
		}

		private double associatedSpeed(Edge e){
			if(e.hasAttribute("SPEED_CAT")){
				int val = (int)e.getNumber("SPEED_CAT");
				switch(val){
				case 1 :
					return 130;
				case 2 :
					return 115;
				case 3 :
					return 95;
				case 4 :
					return 80;
				case 5 :
					return 60;
				case 6 :
					return 40;
				case 7 :
					return 21;
				default : //8
					return 11;
				}
			}
			else
				return 1;
		}
	}
}
