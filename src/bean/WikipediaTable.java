package bean;

import java.util.LinkedList;
import java.util.List;

public class WikipediaTable {

	private String idTable;
	private String wikid;
	private int number_original_rows;
	private int number_original_columns;
	private int number_significant_columns;
	private int nrows_with_multiple_mentions;
	List<String> rows;
	private int index_columns_key;
	
	
	public WikipediaTable() {
		this.rows = new LinkedList<String>();
	}


	public String getIdTable() {
		return idTable;
	}


	public String getWikid() {
		return wikid;
	}


	public int getNumberOriginalRows() {
		return number_original_rows;
	}


	public int getNumberOriginalColumns() {
		return number_original_columns;
	}


	public List<String> getRows() {
		return rows;
	}


	public void setIdTable(String idTable) {
		this.idTable = idTable;
	}


	public void setWikid(String wikid) {
		this.wikid = wikid;
	}


	public void setNumberOriginalRows(int nrows) {
		this.number_original_rows = nrows;
	}


	public void setNumberOriginalColumns(int ncols) {
		this.number_original_columns = ncols;
	}


	public void setRows(List<String> rows) {
		this.rows = rows;
	}

	public void setNumberSignificantColumns(int n) {
		this.number_significant_columns = n;
	}
	
	public int getSignificantColumns() {
		return number_significant_columns;
	}
	
	
	public void addRow(String row){
		this.rows.add(row);
	}
	
	public List<String> firstFourRows(){
		int maxShowRows = 4;
		if(rows.size() < maxShowRows)
			maxShowRows = rows.size();
		
		return rows.subList(0, maxShowRows-1);
		
	}
	
	public void setRowsWithMultipleMentions(int k) {
		this.nrows_with_multiple_mentions = k;
	}
	
	public int getNrows_with_multiple_mentions() {
		return nrows_with_multiple_mentions;
	}
	
	public void setKeyColumn(int index_column_key) {
		this.index_columns_key = index_column_key;
	}
	
	public int getIndexColumnKey(){
		return this.index_columns_key;
	}
	
	@Override
	public String toString() {
		return this.idTable+"\t"+this.wikid+"\t"+this.number_original_columns+"\t"+this.number_original_rows+"\t"+this.getRows();
	}



	
}
