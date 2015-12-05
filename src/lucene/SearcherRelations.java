package lucene;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import bean.StatisticalKB_Bean;
import util.ReaderConfigFile;

public class SearcherRelations {
	
	private SearcherWikidToMid searcherEntryMapped;
	private SearcherRedirectToWikid searcherRedirect;
	private SearcherEntryKB searcherKB;
	private SearcherRelationsBetweenTypes searcherRT;
	

	public SearcherRelations(ReaderConfigFile readerConfigFile) {
		try {
			this.searcherEntryMapped = new SearcherWikidToMid(readerConfigFile);
			this.searcherKB = new SearcherEntryKB(readerConfigFile);
			this.searcherRedirect = new SearcherRedirectToWikid(readerConfigFile);
			this.searcherRT = new SearcherRelationsBetweenTypes(readerConfigFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
public synchronized Map<String,Integer> findRealtionsFromCoupleOfColumns(List<String> firstColumn, List<String> secondColumn,int index_first_column,int index_second_column, StatisticalKB_Bean statisticalObj){
		
		int mid1_found_counter = 0;
		int mid2_found_counter = 0;
		int not_found_counter = 0;
		
		List<String> relationsFound = new LinkedList<String>();
		Map<String,Integer> relationsFoundWithOccurrences = new HashMap<String,Integer>();
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
						for(String rel : relations){
							if(relationsFoundWithOccurrences.containsKey(rel)){
								int counterRelation = relationsFoundWithOccurrences.get(rel);
								counterRelation++;
								relationsFoundWithOccurrences.put(rel,counterRelation);
							}
							else{
								relationsFoundWithOccurrences.put(rel,1);
							}
						}
					}
					else{
						not_found_counter++;
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


		relationsFoundWithOccurrences.put("NF", not_found_counter);

		statisticalObj.setMidFound("mid"+index_first_column,mid1_found_counter);
		statisticalObj.setMidFound("mid"+index_second_column, mid2_found_counter);
		
		return relationsFoundWithOccurrences;
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
	
}
