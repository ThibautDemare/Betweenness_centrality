package betweennessCentralityPivots.astar;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;

import betweennessCentralityPivots.BetweennessCentralityPivots;
import betweennessCentralityPivots.Entity;
import betweennessCentralityPivots.PairNode;

public abstract class AStarEntity extends Entity{
	
	protected AStar astar;
	
	protected static AStar.Costs ec = null;
		
	public AStarEntity(){
		super();
	}
	
	/**
	 * Init the pair and the A* algorithm
	 */
	public void init(BetweennessCentralityPivots rwbc) {
		setRwbc(rwbc);
		pair = new PairNode();
		astar = new AStar();
		astar.setCosts(ec);
		astar.init(getRwbc().getGraph());
	}

	/**
	 * Execute one step : compute a path with A*, increment the counter of betwenness centrality 
	 */
	public void step() {
		if(!terminate){
			if(getRwbc().hasNextPair()){
				getRwbc().selectPairNode(getPair());
				astar.compute(pair.getNode1().getId(), pair.getNode2().getId());
				
				path = astar.getShortestPath();
				
				if(path != null){
					Edge e;
					Node n = path.popNode();//in order to delete the useless target node
					while(!path.peekNode().equals(path.getRoot())){
						e = path.popEdge();
						n = path.peekNode();// e.getOpposite(path.peekNode());
						
						if(!n.equals(path.getRoot())){
							if(n.hasAttribute(getRwbc().getBetweennessCentralityAttribute()))
								n.setAttribute(getRwbc().getBetweennessCentralityAttribute(), n.getNumber(getRwbc().getBetweennessCentralityAttribute())+1);
							else
								n.setAttribute(getRwbc().getBetweennessCentralityAttribute(), 0);
						}
						
						if(e.hasAttribute(getRwbc().getBetweennessCentralityAttribute()))
							e.setAttribute(getRwbc().getBetweennessCentralityAttribute(), e.getNumber(getRwbc().getBetweennessCentralityAttribute())+1);
						else
							e.setAttribute(getRwbc().getBetweennessCentralityAttribute(), 0);
					}
				}
			}
			else {
				terminate = true;
			}
		}
	}
}
