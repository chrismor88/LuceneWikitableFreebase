package util;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FinderColumnKey {
	
	
	public static int findColumnKey(Map<Integer,List<String>> tableColumns){
		
		Map<Integer,Set<String>> tableColumnsWithoutDuplicate = convertTheseTableColumns(tableColumns);		
		int index_key_column = 0;
		int maximum_cardinality = tableColumnsWithoutDuplicate.get(0).size();
		for(Integer i : tableColumnsWithoutDuplicate.keySet()){
			if(tableColumnsWithoutDuplicate.get(i).size() > maximum_cardinality){
				maximum_cardinality = tableColumnsWithoutDuplicate.get(i).size();
				index_key_column = i;
			}
		}
		
		return index_key_column;
	}
	

	
	private static Map<Integer, Set<String>> convertTheseTableColumns(Map<Integer, List<String>> tableColumns) {
		Map<Integer,Set<String>> newTableColumns = new HashMap<>();
		for(Integer i : tableColumns.keySet()){
			Set<String> currentColumns = new LinkedHashSet<>(tableColumns.get(i));
			newTableColumns.put(i, currentColumns);
			
		}
		return newTableColumns;
	}



	public static void main(String[] args) {
		List<String> firstSet = new LinkedList<String>();
		List<String> secondSet = new LinkedList<String>();
		List<String> thirdSet = new LinkedList<String>();
		firstSet.add("A");
		firstSet.add("B");
		
		secondSet.add("0");
		secondSet.add("1");
//		secondSet.add("2");
	
		thirdSet.add("fdsf");
		thirdSet.add("fdsfdd");
		thirdSet.add("fdskjlf");
		
		Map<Integer,List<String>> table = new HashMap<>();
		table.put(0, firstSet);
		table.put(1, secondSet);
		table.put(2, thirdSet);
		
		FinderColumnKey finder = new FinderColumnKey();
		
		
		System.out.println("La colonna chiave e':"+finder.findColumnKey(table));
		
	}
	
}
