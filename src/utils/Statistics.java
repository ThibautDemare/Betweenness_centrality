package utils;

import java.util.ArrayList;
import java.util.Collections;

import org.graphstream.graph.Element;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

public class Statistics {
	/**
	 * Empirical standard deviation
	 */
	public static double ecartTypeNode(Graph g, String att){
		double ecartType = 0.;
		double average = 0.;
		double max = Double.NEGATIVE_INFINITY;
		for(Node n : g){
			average += n.getNumber(att);
			max = Math.max(max, n.getNumber(att));
		}
		//double sum = average;
		average /= g.getNodeCount();
		for(Node n : g){
			ecartType += (n.getNumber(att)-average)*(n.getNumber(att)-average);
		}
		ecartType = Math.sqrt((1./g.getNodeCount())*ecartType);
		return ecartType/max;
	}
	
	/*
	 * Set of function which compute correlation coefficient
	 */
	
	/**
	 * Compute the Pearson's correlation coefficient directly on the values
	 * @param g
	 * @param att1
	 * @param att2
	 * @return
	 */
	public static double pearsonCorrelationCoefficient(Graph g, String att1, String att2){
		/*
		 *  \frac{\sum (x_i-\bar{x}).(y_i-\bar{y})}{\sqrt{\sum (x_i-\bar{x})^2 \sum (y_i-\bar{y})^2}}
		 *  X = x_i-\bar{x}
		 *  Y = y_i-\bar{y}
		 *  \frac{\sum X.Y}{\sqrt{\sum X^2 \sum Y^2}}
		 */
		double moyX = 0;
		double moyY = 0;
		for(Node n : g){
			if(att1.equals("random"))
				moyX += Math.random()*1000;
			else
				moyX += n.getNumber(att1);
			moyY += n.getNumber(att2);
		}
		moyX /= g.getNodeCount();
		moyY /= g.getNodeCount();
		
		double sumXY = 0;
		double sumX2 = 0;
		double sumY2 = 0;
		for(Node n : g){
			double X;
			if(att1.equals("random"))
				X = Math.random()*1000-moyX;
			else
				X = n.getNumber(att1)-moyX;
			double Y = n.getNumber(att2)-moyY;
			sumXY += X*Y;
			sumX2 += X*X;
			sumY2 += Y*Y;
		}
		double res = sumXY / (Math.sqrt(sumX2*sumY2));
		return res;
	}
	
	/**
	 * Compute the Spearman's correlation coefficient (with tied values)
	 * @param g
	 * @param att1
	 * @param att2
	 * @return
	 */
	public static double spearmanCorrelationCoefficient(Graph g, String att1, String att2){
		ArrayList<Element> listAtt1 = new ArrayList<Element>(g.getNodeSet());
		Collections.sort(listAtt1, new ComparatorElement(att1));
		
		ArrayList<Element> listAtt2 = new ArrayList<Element>(g.getNodeSet());
		Collections.sort(listAtt2, new ComparatorElement(att2));
				
		double moyX = g.getNodeCount()/2.;
		double moyY = g.getNodeCount()/2.;
		double sumXY = 0;
		double sumX2 = 0;
		double sumY2 = 0;
		
		for(int i=0; i<listAtt1.size(); i++){
			Element e1 = listAtt1.get(i);
			Element e2;
			int j = 0;
			do{
				e2 = listAtt2.get(j++);
			}while(e1!=e2);			
			double X = i - moyX;
			double Y = j - moyY;
			sumXY += X*Y;
			sumX2 += X*X;
			sumY2 += Y*Y;
		}
		return sumXY / (Math.sqrt(sumX2*sumY2));
	}
	
}
