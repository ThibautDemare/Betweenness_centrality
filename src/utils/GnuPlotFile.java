package utils;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;

import org.graphstream.graph.Element;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

public class GnuPlotFile {
	
	/**
	 * Sauvegarde dans un fichier la quantité de noeud ayant une valeur pris dans chaque intervalle
	 */
	public static void distribution(Graph g, String filePath, String att, int nbInt){
		double max = Double.NEGATIVE_INFINITY;
		double min = Double.POSITIVE_INFINITY;
		for(Node n : g){
			max = Math.max(max, n.getNumber(att));
			min = Math.min(min, n.getNumber(att));
		}
		double debInt = min;
		double finInt = (max-min) / nbInt;
		String s = "";
		int i = 0;
		while(finInt<max){
			int qteInt = 0;
			for(Node n : g){
				double val = n.getNumber(att);
				if(val>=debInt && val<=finInt && val>0.){
					qteInt++;
				}
			}
			s+=(i++)+"\t"+qteInt+"\n";
			debInt += (max-min) / nbInt;
			finInt += (max-min) / nbInt;
		}
		
		try{
			PrintWriter out  = new PrintWriter(new FileWriter(filePath+"distribution"+File.separator+"distribution_"+att+"_"+g.getId()));
			out.println("'''Distribution selon "+att+"'");
			out.println(s);
			out.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Sauvegarde dans un fichier la quantité de noeud ayant une valeur pris dans chaque intervalle
	 */
	public static void simple(Graph g, String filePath, String att1, String att2){
		try{
			PrintWriter out_param  = new PrintWriter(new FileWriter(filePath+"simple"+File.separator+"simple_"+att1+"_"+att2+"_"+g.getId()+".param"));
			out_param.println(att1);
			out_param.println(att2);
			out_param.close();
			
			PrintWriter out  = new PrintWriter(new FileWriter(filePath+"simple"+File.separator+"simple_"+att1+"_"+att2+"_"+g.getId()));
			//out.println("''");
			for(Node n : g){
				out.println(n.getNumber(att1)+"\t"+n.getNumber(att2));
			}
			out.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param g
	 * @param att1
	 * @param att2
	 */
	public static void rank(Graph g, String filePath, String att1, String att2){
		try{
			PrintWriter out_param  = new PrintWriter(new FileWriter(filePath+"rank"+File.separator+"rank_"+att1+"_"+att2+"_"+g.getId()+".param"));
			out_param.println("Rang associé à "+att1);
			out_param.println("Rang associé à "+att2);
			out_param.close();
			
			PrintWriter out  = new PrintWriter(new FileWriter(filePath+"rank"+File.separator+"rank_"+att1+"_"+att2+"_"+g.getId()));
			//out.println("'Repartition des nœuds en fonction des rangs obtenus avec "+att1+" (en abscisses) et "+att2+" (en ordonnées)'");
			
			ArrayList<Element> listAtt1 = new ArrayList<Element>(g.getNodeSet());
			Collections.sort(listAtt1, new ComparatorElement(att1));
			
			ArrayList<Element> listAtt2 = new ArrayList<Element>(g.getNodeSet());
			Collections.sort(listAtt2, new ComparatorElement(att2));
			for(int i=0; i<listAtt1.size(); i++){
				Element e1 = listAtt1.get(i);
				Element e2;
				int j = 0;
				do{
					e2 = listAtt2.get(j++);
				}while(e1!=e2);
				out.println(i+"\t"+j);
			}
			out.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param g
	 * @param att1
	 * @param att2
	 */
	public static void repartition(Graph g, String filePath, String att1){
		try{
			PrintWriter out_param  = new PrintWriter(new FileWriter(filePath+"repartition1"+File.separator+"repartition1_"+att1+"_"+g.getId()+".param"));
			out_param.println("nœuds");
			out_param.println(att1);
			out_param.close();
			
			PrintWriter out  = new PrintWriter(new FileWriter(filePath+"repartition1"+File.separator+"repartition1_"+att1+"_"+g.getId()));
			//out.println("'nœuds'\t'"+att1+"'");
			
			ArrayList<Element> listAtt1 = new ArrayList<Element>(g.getNodeSet());
			Collections.sort(listAtt1, new ComparatorElement(att1));
			
			for(int i=0; i<listAtt1.size(); i++){
				out.println(i+"\t"+listAtt1.get(i).getNumber(att1));
			}
			out.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param g
	 * @param att1
	 * @param att2
	 */
	public static void repartition2(Graph g, String filePath, String att1, String att2){
		try{
			PrintWriter out_param  = new PrintWriter(new FileWriter(filePath+"repartition2"+File.separator+"repartition2_"+att1+"_"+att2+"_"+g.getId()+".param"));
			out_param.println("nœuds");
			out_param.println(att1);
			out_param.println(att2);
			out_param.close();
			
			PrintWriter out  = new PrintWriter(new FileWriter(filePath+"repartition2"+File.separator+"repartition2_"+att1+"_"+att2+"_"+g.getId()));
			out.println("'"+att1+"'\t'"+att2+"'");
			
			ArrayList<Element> listAtt1 = new ArrayList<Element>(g.getNodeSet());
			Collections.sort(listAtt1, new ComparatorElement(att1));
			
			for(int i=0; i<listAtt1.size(); i++){
				out.println(i+"\t"+listAtt1.get(i).getNumber(att1)+"\t"+listAtt1.get(i).getNumber(att2));
			}
			out.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param g
	 * @param att1
	 * @param att2
	 */
	public static void repartition3(Graph g, String filePath, String att1, String att2){
		try{
			PrintWriter out_param  = new PrintWriter(new FileWriter(filePath+"repartition3"+File.separator+"repartition3_"+att1+"_"+att2+"_"+g.getId()+".param"));
			out_param.println("nœuds");
			out_param.println(att2);
			out_param.println(att1);
			out_param.close();
			
			PrintWriter out  = new PrintWriter(new FileWriter(filePath+"repartition3"+File.separator+"repartition3_"+att1+"_"+att2+"_"+g.getId()));
			out.println("'"+att2+"'\t'"+att1+"'");
			
			ArrayList<Element> listAtt2 = new ArrayList<Element>(g.getNodeSet());
			Collections.sort(listAtt2, new ComparatorElement(att2));
			
			for(int i=0; i<listAtt2.size(); i++){
				out.println(i+"\t"+listAtt2.get(i).getNumber(att1)+"\t"+listAtt2.get(i).getNumber(att2));
			}
			out.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}
