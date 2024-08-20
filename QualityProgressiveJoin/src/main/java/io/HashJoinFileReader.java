package io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;

import dataStructure.StringTuple2;

public class HashJoinFileReader {

	private String filePath = "";
	private String regex;
	private int numPartition;
	private int keyIdx;
	private BufferedReader reader;

	public HashJoinFileReader(String filePath, String regex, int numPartition, int keyIdx) {
		this.filePath = filePath;
		this.regex = regex;
		this.numPartition = numPartition;
		this.keyIdx = keyIdx;
	}

	public void openFile() {
		File file = new File(filePath);
		try {
			this.reader = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void closeFile() {
		try {
			this.reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public ArrayList<ArrayList<StringTuple2>> readFile(int start, int end) {
		ArrayList<ArrayList<StringTuple2>> partitionedDatas = new ArrayList<ArrayList<StringTuple2>>();

		for (int j = 0; j < numPartition; j++) {
			ArrayList<StringTuple2> partitionedData = new ArrayList<StringTuple2>();
			partitionedDatas.add(partitionedData);
		}

		try {
			for (int i = start; i < end; i++) {
				String line = reader.readLine();
//				System.out.println(i + ": " + line);
				// file
				String[] tem = line.split(regex);
				StringTuple2 tuple = new StringTuple2(tem[2], tem[3]);
				int parId = Math.abs(tuple.computeHashkey(keyIdx)) % numPartition;
				partitionedDatas.get(parId).add(tuple);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return partitionedDatas;

	}
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String[][] paths = new String[1][2];
		String pathTmp0 = "/Users/xin_aurora/Downloads/Work/2019/UCR/Research/Spatial/Progressive_Join"
				+ "/newExp/sparkStructure/equalJoin/1p/20partitions/20p/0p_";
		String pathTmp1 = "/Users/xin_aurora/Downloads/Work/2019/UCR/Research/Spatial/Progressive_Join"
				+ "/newExp/sparkStructure/equalJoin/1p/20partitions/20p/1p_";

		for (int i = 0; i < 1; i++) {
			String[] path = new String[2];
			path[0] = pathTmp0 + i + ".csv";
			path[1] = pathTmp1 + i + ".csv";
			paths[i] = path;
//			System.out.println(Arrays.toString(path));
		}

		int[] keyIdxs = { 0, 0 };
		HashJoinFileReader reader = new HashJoinFileReader(paths[0][0], ";", 5, keyIdxs[0]);
		reader.openFile();
		int[] lineNum = { 0, 10, 20, 30, 40, 762 };
		for (int i = 1; i < lineNum.length; i++) {
			ArrayList<ArrayList<StringTuple2>> data = reader.readFile(lineNum[i - 1], lineNum[i]);
//			System.out.println(data.size());
		}
	}

}
