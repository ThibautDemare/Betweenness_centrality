package mains;

import java.util.ArrayList;

import org.graphstream.graph.Element;

public class ComparatorOfList {
	public static double compare_old(ArrayList<? extends Element> l1, ArrayList<? extends Element> l2){
		double sum=0.;
		double normal=0.;
		for(int i=0; i<l1.size(); i++){
			if(i<l1.size()/2){
				normal += 2* (l1.size()-2*i);
			}
			Element e1 = l1.get(i);
			String id1 = e1.getId();
			for(int j=0; j<l2.size(); j++){
				Element e2 = l2.get(j);
				String id2 = e2.getId();
				if(id1.equals(id2)){
					sum+=Math.abs(i-j);
				}
			}
		}
		//System.out.println("Compare 1 => normal : "+normal+" et sum : "+sum+" et valeur final : "+sum/normal);
		return sum/normal;
	}
	
	/*
	 * Proposition : Si un noeud n'est pas à la meme place dans la deuxième liste, alors,
	 * on ne considere pas ses voisins de droite dans la première liste qui sont egalement ses voisins de droite dans la deuxième liste
	 */
	public static double compare(ArrayList<? extends Element> l1, ArrayList<? extends Element> l2){
		
		double sum=0.;
		double normal=0.;
		//The two list have not necessarily the same size
		int listSize = Math.min(l1.size(), l2.size());
		for(int i=0; i<listSize; i++){
			if(i<l1.size()/2){
				normal += 2* (l1.size()-2*i);
			}
			String id1 = l1.get(i).getId();
			boolean found = true;
			for(int j=0; j<listSize && found; j++){
				String id2 = l2.get(j).getId();
				if(id1.equals(id2)){
					//On a repere l'indice j du noeud dans la deuxieme liste correspondant au noeud i de la premiere 
					sum+=Math.abs(i-j);
					while((i+1)<listSize && (j+1)<listSize && id1.equals(id2)){
						i++;
						j++;
						id1 = l1.get(i).getId();
						id2 = l2.get(j).getId();
						
						if(i<l1.size()/2){
							normal += 2* (l1.size()-2*i);
						}
					}
					found = false;
				}
			}
		}
		//System.out.println("Compare 2 => normal : "+normal+" et sum : "+sum+" et valeur final : "+sum/normal);
		return sum/normal;
	}
	
	/*
	 * Proposition : regarde si un noeud est approximativement au meme endroit dans la seconde liste
	 * L'approximation se fait grace à un pourcentage d'erreur
	 */
	public static double compare_new(ArrayList<? extends Element> l1, ArrayList<? extends Element> l2, double acceptability){
		double sum = 0.;
		int listSize = l1.size();
		for(int i=0; i<listSize; i++){
			String id1 = l1.get(i).getId();
			String id2;
			int j=0;
			do{
				id2 = l2.get(j).getId();
				j++;
			}while(!id1.equals(id2) && j<listSize);
			
			//If we have found the element in the second list
			//if(j<listSize){
			if(id1.equals(id2)){
				/*int diff = Math.abs(i-j);
				if(diff<=acceptability*listSize)
					sum++;*/
				if(j>i*(1-acceptability) && j<i*(1+acceptability))
					sum++;
			}
		}
		return sum/listSize;
	}
}
