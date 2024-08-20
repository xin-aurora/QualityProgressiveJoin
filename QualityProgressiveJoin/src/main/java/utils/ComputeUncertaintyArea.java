package utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;

public class ComputeUncertaintyArea {
	
	public ComputeUncertaintyArea(String filePath,  HashSet<String> totalClass,
			HashSet<String> unClass12Set, HashSet<String> unClass23Set,
			HashSet<String> unClass34Set, HashSet<String> unClass45Set) {
		BufferedReader reader;
		try {
			
			reader = new BufferedReader(new FileReader(filePath));
			String[] header = reader.readLine().split(";");
			String line = reader.readLine();
			double total = 0;
			double unC12 = 0.0;
			double unC23 = 0.0;
			double unC34 = 0.0;
			double unC45 = 0.0;
			while (line != null) {
				String[] tmp = line.split(",");
				String name = tmp[0].trim().toLowerCase();
				int area = Integer.parseInt(tmp[1].trim());
				total += area;
				if (unClass12Set.contains(name)) {
					unC12 += area;
				} else if (unClass23Set.contains(name)) {
					unC23 += area;
				} else if (unClass34Set.contains(name)) {
					unC34 += area;
				} else if (unClass45Set.contains(name)) {
					unC45 += area;
				} 
				line = reader.readLine();
			}
			reader.close();
			System.out.println("ratio of U12 = " + unC12 / total);
			System.out.println("ratio of U23 = " + unC23 / total);
			System.out.println("ratio of U34 = " + unC34 / total);
			System.out.println("ratio of U45 = " + unC45 / total);
			System.out.println("total ratio = " + (unC12 + unC23 + unC34 + unC45) / total);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void FirstBatch() {
		String[] unClass12 = { "georgia", "florida", "massachusetts",
				"michigan", "pennsylvania", "virginia"};
		HashSet<String> unClass12Set = new HashSet<String>();
		for (String s: unClass12) {
			unClass12Set.add(s);
		}
		String[] unClass23 = {"indiana", "washington", "missouri", "maryland",
                "mississippi", "connecticut", "alabama", "louisiana"};
		HashSet<String> unClass23Set = new HashSet<String>();
		for (String s: unClass23) {
			unClass23Set.add(s);
		}
		String[] unClass34 = { "new hampshire", "arizona", "wisconsin", "arkansas"};
		HashSet<String> unClass34Set = new HashSet<String>();
		for (String s: unClass34) {
			unClass34Set.add(s);
		}
		String[] unClass45 = {"oklahoma", "nebraska", "west virginia", "district of columbia", 
				"new mexico", "vermont", "maine", "idaho"};
		HashSet<String> unClass45Set = new HashSet<String>();
		for (String s: unClass45) {
			unClass45Set.add(s);
		}
	}
	
	public static void SecondBatchOur() {
		String[] unClass12 = {"michigan", "pennsylvania", "virginia"};
		HashSet<String> unClass12Set = new HashSet<String>();
		for (String s: unClass12) {
			unClass12Set.add(s);
		}
		String[] unClass23 = {"missouri", "maryland","mississippi"};
		HashSet<String> unClass23Set = new HashSet<String>();
		for (String s: unClass23) {
			unClass23Set.add(s);
		}
		String[] unClass34 = {"new hampshire", "arizona", "wisconsin"};
		HashSet<String> unClass34Set = new HashSet<String>();
		for (String s: unClass34) {
			unClass34Set.add(s);
		}
		String[] unClass45 = {"oklahoma", "nebraska", "west virginia", "district of columbia"};
		HashSet<String> unClass45Set = new HashSet<String>();
		for (String s: unClass45) {
			unClass45Set.add(s);
		}
	}
	
	public static void SecondBatchBS() {
		String[] unClass12 = {"florida", "massachusetts", "michigan",
                "pennsylvania", "virginia"};
		HashSet<String> unClass12Set = new HashSet<String>();
		for (String s: unClass12) {
			unClass12Set.add(s);
		}
		String[] unClass23 = {"washington", "missouri", "maryland",
                "mississippi", "connecticut", "alabama"};
		HashSet<String> unClass23Set = new HashSet<String>();
		for (String s: unClass23) {
			unClass23Set.add(s);
		}
		String[] unClass34 = {"new hampshire", "arizona", "wisconsin"};
		HashSet<String> unClass34Set = new HashSet<String>();
		for (String s: unClass34) {
			unClass34Set.add(s);
		}
		String[] unClass45 = {"oklahoma", "nebraska",
                "west virginia", "district of columbia", "new mexico", "vermont", "maine"};
		HashSet<String> unClass45Set = new HashSet<String>();
		for (String s: unClass45) {
			unClass45Set.add(s);
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String folder = "/Users/xin_aurora/vldb2024/workshop/data/map/";
		String path = folder + "state-areas.csv";
		String[] unClass12 = {"michigan", "pennsylvania", "virginia"};
		HashSet<String> unClass12Set = new HashSet<String>();
		for (String s: unClass12) {
			unClass12Set.add(s);
		}
		String[] unClass23 = {"missouri", "maryland","mississippi"};
		HashSet<String> unClass23Set = new HashSet<String>();
		for (String s: unClass23) {
			unClass23Set.add(s);
		}
		String[] unClass34 = {"new hampshire", "arizona", "wisconsin"};
		HashSet<String> unClass34Set = new HashSet<String>();
		for (String s: unClass34) {
			unClass34Set.add(s);
		}
		String[] unClass45 = {"oklahoma", "nebraska", "west virginia", "district of columbia"};
		HashSet<String> unClass45Set = new HashSet<String>();
		for (String s: unClass45) {
			unClass45Set.add(s);
		}
		String[] totalClass = {"california", "texas", "new york", "ohio", "illinois", "new jersey",
				"georgia", "florida", "massachusetts", "michigan", "pennsylvania", "virginia",
				"tennessee", "kansas", "kentucky", "north carolina",
				"indiana", "washington", "missouri", "maryland",
                "mississippi", "connecticut", "alabama", "louisiana",
                "south carolina", "iowa", "oregon", "minnesota", "colorado",
                "new hampshire", "arizona", "wisconsin", "arkansas",
                "utah", "delaware", "nevada", "rhode island",
                "oklahoma", "nebraska", "west virginia", "district of columbia", "new mexico", "vermont", "maine", "idaho",
                "south dakota", "montana", "wyoming", "north dakota"};
		HashSet<String> totalClassSet = new HashSet<String>();
		for (String s: totalClass) {
//			totalClassSet.add(s);
			if (totalClassSet.contains(s)) {
				System.out.println(s);
			} else {
				totalClassSet.add(s);
			}
		}
//		System.out.println("total class size = " + totalClass.length);
//		System.out.println("total class set size = " + totalClassSet.size());
		
		ComputeUncertaintyArea run = new ComputeUncertaintyArea(path, totalClassSet,
				unClass12Set, unClass23Set, unClass34Set, unClass45Set);
		
	}

}
