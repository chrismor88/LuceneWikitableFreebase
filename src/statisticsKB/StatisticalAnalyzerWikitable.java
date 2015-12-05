package statisticsKB;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import bean.Pair;
import bean.StatisticalKB_Bean;
import bean.WikipediaTable;
import lucene.SearcherRelations2;
import util.FinderColumnKey;
import util.ReaderConfigFile;

public class StatisticalAnalyzerWikitable {

	private SearcherRelations2 searcher;


	public StatisticalAnalyzerWikitable(ReaderConfigFile readerConfigFile) {
		searcher = new SearcherRelations2(readerConfigFile);
	}




	//restituisce la lista di relazioni trovate per le coppie analizzate con il relativo numero di occorrenza
	public synchronized StatisticalKB_Bean analyzeThisWikitable(WikipediaTable wikitable){
		List<String> tableRows = wikitable.getRows();
		int num_columns = (tableRows.get(0).split("\t")).length;
		StatisticalKB_Bean statisticalResult = new StatisticalKB_Bean(num_columns);
		Map<Integer,List<String>> indexColumnsToContent = convertTableRowsIntoTableColumns(tableRows,num_columns);
		
		int index_column_key = FinderColumnKey.findColumnKey(indexColumnsToContent);
		wikitable.setKeyColumn(index_column_key);
		
		int id_pair_of_columns = 0;

		if(index_column_key==0){
			for(int i=1;i<num_columns;i++){
				id_pair_of_columns++;
				Map<String,Integer> relationsFound = this.searcher.findRealtionsFromCoupleOfColumns(indexColumnsToContent.get(0), indexColumnsToContent.get(i), 0, i, id_pair_of_columns, statisticalResult);
				List<Pair> pairs = extractFirstPairsForTheseColumns(indexColumnsToContent.get(0),indexColumnsToContent.get(i));
				statisticalResult.setRelationsForThisCouple(id_pair_of_columns, relationsFound);
				statisticalResult.setListOfPairForThisCoupleOfColumns(id_pair_of_columns, pairs);
			}
		}
		else{
			for(int i=0; i<index_column_key; i++){
				id_pair_of_columns++;
				Map<String,Integer> relationsFound = this.searcher.findRealtionsFromCoupleOfColumns(indexColumnsToContent.get(i), indexColumnsToContent.get(index_column_key), i, index_column_key,id_pair_of_columns, statisticalResult);
				List<Pair> pairs = extractFirstPairsForTheseColumns(indexColumnsToContent.get(i),indexColumnsToContent.get(index_column_key));
				statisticalResult.setRelationsForThisCouple(id_pair_of_columns, relationsFound);
				statisticalResult.setListOfPairForThisCoupleOfColumns(id_pair_of_columns, pairs);
			}
			
			for(int j=index_column_key+1; j<num_columns; j++){
				id_pair_of_columns++;
				Map<String,Integer> relationsFound = this.searcher.findRealtionsFromCoupleOfColumns(indexColumnsToContent.get(index_column_key), indexColumnsToContent.get(j), index_column_key, j, id_pair_of_columns, statisticalResult);
				List<Pair> pairs = extractFirstPairsForTheseColumns(indexColumnsToContent.get(index_column_key),indexColumnsToContent.get(j));
				statisticalResult.setRelationsForThisCouple(id_pair_of_columns, relationsFound);
				statisticalResult.setListOfPairForThisCoupleOfColumns(id_pair_of_columns, pairs);
			}

		}

		return statisticalResult;
	}





	private List<Pair> extractFirstPairsForTheseColumns(List<String> col1, List<String> col2) {
		List<Pair> pairs = new LinkedList<Pair>();
		int MAX_NUMBER_OF_PAIRS = 4;
		for(int i=0;i<MAX_NUMBER_OF_PAIRS && i<col1.size() && i<col2.size();i++){
			Pair<String,String> currentPair = new Pair<String, String>(col1.get(i),col2.get(i));
			pairs.add(currentPair);
		}
		
		return pairs;
	
	}




	private Map<Integer, List<String>> convertTableRowsIntoTableColumns(List<String> tableRows,int num_columns) {

		//creazione mappa indice colonna -> contenuto colonna
		Map<Integer,List<String>> tableColumns = new HashMap<>();

		//inizializzazione
		for(int k=0;k<num_columns;k++){
			List<String> currentColumn = new LinkedList<>();
			tableColumns.put(k,currentColumn);
		}


		//popolamento della mappa
		for(int i=0;i<tableRows.size();i++){
			String[] fields = tableRows.get(i).split("\t");
			for(int j=0;j<num_columns;j++)
				tableColumns.get(j).add(fields[j]);
		}

		return tableColumns;
	}

}




