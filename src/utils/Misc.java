package utils;

import java.awt.Color;
import java.io.File;

import org.graphstream.graph.Graph;

public class Misc {

	public static void showGraph(Graph g, boolean b){
		//System.setProperty("gs.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
		g.addAttribute("ui.stylesheet", g.getAttribute("stylesheet"));
		g.display(b);
	}

	/**
	 * Return a formated string representing a color. The color is determined by a function
	 * @param value
	 * @return
	 */
	public static String getColor(double value){
		//Color c = Color.getHSBColor((float)Math.pow(10000, -1*value-0.04), 0.8f, 0.8f);
		//Color c = Color.getHSBColor((float)(1./(70.*(value+0.027))), 0.8f, 0.8f);
		Color c = Color.getHSBColor((float)(-0.7*value+0.7), 0.8f, 0.8f);
		String s = "rgb("+c.getRed()+", "+c.getGreen()+", "+c.getBlue()+");";
		return s;
	}

	/**
	 * Save a graph in a DGS format in the specified file
	 * @param f
	 * @param g
	 */
	public static void saveGraph(String f, Graph g){
		if(f.equals(""))
			f=System.getProperty("user.dir" )+File.separator+g.getId()+".dgs";
		try {
			g.write(f);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
