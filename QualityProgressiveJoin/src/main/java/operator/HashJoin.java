package operator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import dataStructure.StringTuple2;
import dataStructure.Tuple3;

/**
 * https://rosettacode.org/wiki/Hash_join
 * 
 * @author xin_aurora
 *
 */
public class HashJoin {

	/*
	 * Hash Phase: Create a multimap from one of the two tables, mapping from each
	 * join column value to all the rows that contain it. Join phase: Scan the other
	 * table, and find matching rows by looking in the multimap created before.
	 * Join phase: Scan the other table, and find matching rows by looking 
	 * in the multimap created before.
	 * 
	 * A = the first input table (the larger one) B = the second input table (the
	 * smaller one) jA = the join column ID of table A jB = the join column ID of
	 * table B MB = a multimap for mapping from single values to multiple rows of
	 * table B (starts out empty) C = the output table (starts out empty)
	 * 
	 * for each row b in table B: place b in multimap MB under key b(jB)
	 * 
	 * for each row a in table A: for each row b in multimap MB under key a(jA): let
	 * c = the concatenation of row a and row b place row c in table C
	 */

	// TODO: return join size in the refine step
	public static Queue<Tuple3> HashJoinTuple(List<StringTuple2> recordList1, int idx1, List<StringTuple2> recordList2,
			int idx2) {
		Queue<Tuple3> result = new LinkedList<Tuple3>();
		if (recordList1.size() == 0 || recordList2.size() == 0) {
			return result;
		}
		
		// extract attributes from the two list
		String[][] records1;
		String[][] records2;
		boolean swap = false;
		if (recordList1.size() < recordList2.size()) {
			records1 = extractAttributesToArray(recordList1);
			records2 = extractAttributesToArray(recordList2);
//			System.out.println("did not swap");
		} else {
			records1 = extractAttributesToArray(recordList2);
			records2 = extractAttributesToArray(recordList1);
			int tmp = idx1;
			idx1 = idx2;
			idx2 = tmp;
			swap = true;
//			System.out.println("swap");
		}

		Map<String, List<String[]>> map = new HashMap<>();
		
		for (String[] record : records1) {
            List<String[]> v = map.getOrDefault(record[idx1], new ArrayList<>());
            v.add(record);
            map.put(record[idx1], v);
        }
 
        for (String[] record : records2) {
            List<String[]> lst = map.get(record[idx2]);
            if (lst != null) {
            	for (String[] r : lst) {
            		if (swap) {
                		result.add(new Tuple3<String, String, String>(r[idx1], record[1-idx2], r[1-idx1]));
            		} else {
                		result.add(new Tuple3<String, String, String>(r[idx1], r[1-idx1], record[1-idx2]));
            		}
            	}  
            }
        }
		
		return result;
	}
	
	public static List<String[][]> HashJoinArray(ArrayList<StringTuple2> recordList1, int idx1, ArrayList<StringTuple2> recordList2,
			int idx2) {
		// extract attributes from the two list
		String[][] records1;
		String[][] records2;
		boolean swap = false;
		if (recordList1.size() < recordList2.size()) {
			records1 = extractAttributesToArray(recordList1);
			records2 = extractAttributesToArray(recordList2);
//			System.out.println("did not swap");
		} else {
			records1 = extractAttributesToArray(recordList2);
			records2 = extractAttributesToArray(recordList1);
			int tmp = idx1;
			idx1 = idx2;
			idx2 = tmp;
			swap = true;
//			System.out.println("swap");
		}

		List<String[][]> result = new ArrayList<>();
		Map<String, List<String[]>> map = new HashMap<>();
		
		for (String[] record : records1) {
            List<String[]> v = map.getOrDefault(record[idx1], new ArrayList<>());
            v.add(record);
            map.put(record[idx1], v);
        }
 
        for (String[] record : records2) {
            List<String[]> lst = map.get(record[idx2]);
            if (lst != null) {
            	if (swap) {
               	 lst.stream().forEach(r -> {
                     result.add(new String[][]{record, r});
                 });
            	} else {
               	 lst.stream().forEach(r -> {
                     result.add(new String[][]{r, record});
                 });
            	}
            }
        }
		
		return result;
	}

	
	public static String[][] extractAttributesToArray(List<StringTuple2> recordList) {
		int listSize = recordList.size();
		String[][] records = new String[listSize][recordList.get(0).getSize()];
		for (int i = 0; i < listSize; i++) {
//			String[] tem = new String[2];
//			tem[0] = (String) recordList.get(i).getValue0();
//			tem[1] = (String) recordList.get(i).getValue1();
			records[i] = recordList.get(i).toArray();
		}
		
		return records;
	}

}
