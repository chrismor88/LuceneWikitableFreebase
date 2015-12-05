package lucene;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.KeyStore.Entry;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import bean.EntryKB;
import bean.StatisticalKB_Bean;
import lucene.SearcherEntryKB;
import lucene.SearcherWikidToMid;
import lucene.SearcherRelationsBetweenTypes;
import lucene.SearcherRedirectToWikid;
import util.ReaderConfigFile;

public class SearcherRelations2 {

	private SearcherWikidToMid searcherEntryMapped;
	private SearcherRedirectToWikid searcherRedirect;
	private SearcherEntryKB searcherKB;
	private SearcherRelationsBetweenTypes searcherRT;


	public SearcherRelations2(ReaderConfigFile readerFileConfig) {
		try {
			this.searcherEntryMapped = new SearcherWikidToMid(readerFileConfig);
			this.searcherKB = new SearcherEntryKB(readerFileConfig);
			this.searcherRedirect = new SearcherRedirectToWikid(readerFileConfig);
			this.searcherRT = new SearcherRelationsBetweenTypes(readerFileConfig);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}




	//restituisce la lista di relazioni trovate per le coppie analizzate con il relativo numero di occorrenza
	public synchronized Map<String,Integer> findRealtionsFromCoupleOfColumns(List<String> firstColumn, List<String> secondColumn,int index_first_column,int index_second_column, int id_pair_of_columns, StatisticalKB_Bean statisticalObj){

		int mid1_found_counter = 0;
		int mid2_found_counter = 0;
		int not_found_counter = 0;
		int couple_of_entities_with_at_least_one_relation = 0;

		List<String> relationsFound = new LinkedList<String>();
		Map<String,Integer> relationsFoundWithFrequencies = new HashMap<String,Integer>();
		for(int i=0;i<firstColumn.size() && i<secondColumn.size();i++){

			String subject = firstColumn.get(i);
			String object = secondColumn.get(i);

			String mid1 = findMidForThisEntity(subject);
			if(mid1!=null)
				mid1_found_counter++;

			String mid2 = findMidForThisEntity(object);
			if(mid2!=null)
				mid2_found_counter++;

			try {
				if(mid1!=null && mid2!=null){
					mid1 = "m."+mid1;
					mid2 = "m."+mid2;

					List<String> relations = this.searcherKB.findRelationsBetween(mid1, mid2);

					if(relations.size() > 0){
						couple_of_entities_with_at_least_one_relation++;
						for(String rel : relations){
							if(relationsFoundWithFrequencies.containsKey(rel)){
								int counterRelation = relationsFoundWithFrequencies.get(rel);
								counterRelation++;
								relationsFoundWithFrequencies.put(rel,counterRelation);
							}
							else{
								relationsFoundWithFrequencies.put(rel,1);
							}
						}
					}
					else{
						//parte nuova con interrogazione dell'indice "types1_rel_types2"
						List<String> types1 = this.searcherKB.findTypes1For(mid1);
						List<String> types2 = this.searcherKB.findTypes2For(mid2);
						Set<String> relationsBetweenTypes = this.searcherRT.findRelationsBetweenTheseTypes(types1, types2);
						if(relationsBetweenTypes.size() > 0){
							couple_of_entities_with_at_least_one_relation++;
							for(String rel : relationsBetweenTypes){
								if(relationsFoundWithFrequencies.containsKey(rel)){
									int counterRelation = relationsFoundWithFrequencies.get(rel);
									counterRelation++;
									relationsFoundWithFrequencies.put(rel,counterRelation);
								}
								else{
									relationsFoundWithFrequencies.put(rel,1);
								}
							}
						}
						else{
							not_found_counter++;
						}
					}
				}
				else{
					not_found_counter++;
				}


			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}


		relationsFoundWithFrequencies.put("NF", not_found_counter);
		relationsFoundWithFrequencies = orderRelationsFound(relationsFoundWithFrequencies);

		statisticalObj.setMidFound("mid"+index_first_column,mid1_found_counter);
		statisticalObj.setMidFound("mid"+index_second_column, mid2_found_counter);
		statisticalObj.setNumberOfCouplesWithAtLeastOneRelationForTheseColumns(id_pair_of_columns, couple_of_entities_with_at_least_one_relation);

		return relationsFoundWithFrequencies;


	}



	private String findMidForThisEntity(String entity){
		String mid = null;
		try {
			mid = searcherEntryMapped.findMidFor(entity);
			if(mid==null){
				String wikid = searcherRedirect.findWikidFor(entity);
				if(wikid!=null)
					mid = searcherEntryMapped.findMidFor(wikid);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return mid;

	}


	//restituisce una mappa ordinata in cui compaiono le prima tre relazioni con più occorrenze
	//aggiungendo anche le relazioni N/A non trovate su Freebase
	private Map<String,Integer> orderRelationsFound(Map<String,Integer> map){
		ValueComparator valueComparator = new ValueComparator(map);
		TreeMap<String,Integer> sortedMap = new TreeMap<>(valueComparator); 
		sortedMap.putAll(map);
		
		return sortedMap;
	}
	

}



class ValueComparator implements Comparator{

	private Map<String,Integer> map;

	public ValueComparator(Map<String,Integer> map2) {
		this.map = map2;
	}

	public int compare(Object first, Object second) {
		Comparable valueA = (Comparable) map.get(first);
		Comparable valueB = (Comparable) map.get(second);
		return valueB.compareTo(valueA); 
	}

}


