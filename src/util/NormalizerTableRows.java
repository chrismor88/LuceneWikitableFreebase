package util;

import java.util.LinkedList;
import java.util.List;

public class NormalizerTableRows {


	public static List<String> normalizesTableRows(String singleTableRow){
		List<String> newTablesRows = new LinkedList<String>();
		String[] columns = singleTableRow.split("\t");
		recursiveContentRow("",columns,0,newTablesRows);

		return newTablesRows;
	}


	private static void recursiveContentRow(String currentRow,String[] columns,int currentColumn,List<String> rows) {
		String[] contentCell = columns[currentColumn].split("<>");
		for(int i=0;i<contentCell.length;i++){
			if(currentColumn==columns.length-1){
				rows.add(currentRow.concat("\t"+contentCell[i]));
			}
			else{
				recursiveContentRow(currentRow.concat("\t"+contentCell[i]),columns,currentColumn+1,rows);
			}
		}
	}


}
