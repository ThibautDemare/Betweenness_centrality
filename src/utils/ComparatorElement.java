package utils;

import java.util.Comparator;

import org.graphstream.graph.Element;

/**
 * A comparator of element
 */
public class ComparatorElement implements Comparator<Element>{
	String att;
	public ComparatorElement(String att){
		this.att = att;
	}
	
	public int compare(Element n1, Element n2) {
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