package statisticsKB;


import java.io.BufferedWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import bean.Pair;
import bean.StatisticalKB_Bean;
import bean.WikipediaTable;




public class WriterStatisticalTable{

	private final String PREFIX_URL = "en.wikipedia.org/wiki/";

	private final int MAX_NUMBER_SIGNIFICANT_COLUMNS = 4;

	private final int MAX_NUMBER_PAIRS_OF_COLUMNS = 3;

	private BufferedWriter writerResultsFile;

	public WriterStatisticalTable(BufferedWriter writerResultsFile) {
		this.writerResultsFile = writerResultsFile;

	}


	public synchronized void writeStatisticsAbout(WikipediaTable wikiTable, StatisticalKB_Bean statisticalResult) throws IOException {

		//scrittura id, wikid, url e varie info per ciascuna tabella
		String record = wikiTable.getIdTable()+"\t"+wikiTable.getWikid()+"\t"+PREFIX_URL+wikiTable.getWikid()+"\t";

		record = record.concat(wikiTable.getNumberOriginalRows()+"\t"+wikiTable.getNumberOriginalColumns()+"\t"
				+wikiTable.getRows().size()+"\t"+wikiTable.getSignificantColumns()+"\t"+(wikiTable.getIndexColumnKey()+1)+"\t");


		//scrittura dei mid trovati per ciascuna colonna significativa
		int counterMids = 0;
		for(String currentMid : statisticalResult.getMidNamesWithOccurence().keySet()){
			record = record.concat(statisticalResult.getMidNamesWithOccurence().get(currentMid)+"\t");
			counterMids ++;
		}

		int deltaCounterMids = MAX_NUMBER_SIGNIFICANT_COLUMNS - counterMids ;


		for(int i=0; i<deltaCounterMids;i++){
			record = record.concat("-\t");
		}


		//scrittura risultati per ciascuna coppia di colonne
		int counterPairsOfColumns = 0;

		for(int id_couple_column : statisticalResult.getCouplesColumnsNumberToRelationsFound().keySet()){
			counterPairsOfColumns++;

			List<Pair> pairList = statisticalResult.getCoupleOfColumnsToPairs().get(id_couple_column);
			for(Pair pair : pairList)
				record = record.concat("<"+pair.getLeft()+","+pair.getRight()+">  ");
			record = record.concat("\t");

			int adder_of_relations_found = 0;
			Map<String,Integer> relationsFound = statisticalResult.getCouplesColumnsNumberToRelationsFound().get(id_couple_column);
			for(String rel : relationsFound.keySet()){
				record = record.concat("["+rel+" : "+relationsFound.get(rel)+"]  ");
				if(!rel.equals("NF"))
					adder_of_relations_found += relationsFound.get(rel);

			}

			record = record.concat("\t");

			record = record + statisticalResult.getPairsOfColumnsToNumberOfCouplesWithAtLeastOneRelation().get(id_couple_column)+"\t";

			double average_number_of_relations_found = (double) adder_of_relations_found / (double) relationsFound.size();
			record = record + round(average_number_of_relations_found,2)+"\t";

		}


		int deltaCounterPairOfColumns = MAX_NUMBER_PAIRS_OF_COLUMNS - counterPairsOfColumns;

		for(int i=0; i<deltaCounterPairOfColumns;i++){
			record = record.concat("-\t-\t-\t-\t");
		}

		record = record.concat(wikiTable.getNrows_with_multiple_mentions()+"\n");

		writerResultsFile.write(record);

	}


	private double round(double value, int places) {
		if (places < 0) throw new IllegalArgumentException();

		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}

}
