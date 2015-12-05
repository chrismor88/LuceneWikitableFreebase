package bean;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class StatisticalKB_Bean {

	//la chiave di questa mappa rappresenta una coppia di colonne coinvolta nella ricerca di relazioni in Freebase
	//il dominio della chiave va da 1 a n-1 dove n è il numero totale di colonne della tabella
	private Map<Integer,Map<String,Integer>> id_couple_of_columns_to_relations;

	//id coppia di colonne e loro contenuto
	private Map<Integer,List<Pair>> id_couple_of_columns_to_pairs;
	
	///mid relativo a ciascuna colonna con il numero di quelli trovati in Freebase
	private Map<String,Integer> mid_found;

	//id coppia di colonne con numero di coppie di entità con almeno una relazione
	private Map<Integer,Integer> id_couple_of_columns_to_pairs_of_entities_with_at_least_one_relation;
	

	public StatisticalKB_Bean(int ncol){
		this.mid_found = new HashMap<>();
		this.id_couple_of_columns_to_relations = new HashMap<Integer,Map<String,Integer>>();
		this.id_couple_of_columns_to_pairs = new HashMap<Integer,List<Pair>>();
		this.id_couple_of_columns_to_pairs_of_entities_with_at_least_one_relation = new HashMap<Integer,Integer>();

		for(int i=0;i<ncol;i++)
			this.mid_found.put("mid"+i,0);
	
	}

	public Map<String, Integer> getMidNamesWithOccurence() {
		return mid_found;
	}


	public void setRelationsForThisCouple(int id_couple, Map<String,Integer> relations){
		id_couple_of_columns_to_relations.put(id_couple, relations);
	}

	public Map<Integer,Map<String,Integer>> getCouplesColumnsNumberToRelationsFound(){
		return this.id_couple_of_columns_to_relations;
	}
	
	
	public void setMidFound(String mid_column_reference, int counter) {
		if(mid_found.get(mid_column_reference)==0){
			mid_found.put(mid_column_reference, counter);
		}
	}
	
	public void setListOfPairForThisCoupleOfColumns(int id_couple,List<Pair> pairs){
		this.id_couple_of_columns_to_pairs.put(id_couple,pairs);
	}
	
	
	public Map<Integer, List<Pair>> getCoupleOfColumnsToPairs() {
		return id_couple_of_columns_to_pairs;
	}
	
	public Map<Integer,Integer> getPairsOfColumnsToNumberOfCouplesWithAtLeastOneRelation(){
		return this.id_couple_of_columns_to_pairs_of_entities_with_at_least_one_relation;
	}
	
	
	public void setNumberOfCouplesWithAtLeastOneRelationForTheseColumns(int id_couple_columns, int counter_pairs_of_entites){
		this.id_couple_of_columns_to_pairs_of_entities_with_at_least_one_relation.put(id_couple_columns,counter_pairs_of_entites);
	}
	
} 
