package io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;

import dataStructure.DoubleTuple4;
import dataStructure.StringTuple2;
import partition.GridPartition;
import partition.HashPartition;
import partition.MultiHashPartition;
import utils.Configuration;

public class CSVReader {

	public static HashPartition readHashPartitionKey(String[][] filePaths, int numPar,
			int[] keyIdxs, String regex) {
		HashPartition hashPartition = new HashPartition(numPar);
		hashPartition.enableSelectivityEstimation(new HashSet<String>());
		
		int[][] fileTotalLines = new int[filePaths.length][2];
		
		for (int i = 0; i < filePaths.length; i++) {
			String[] filePath = filePaths[i];
			File file = new File(filePath[0]);
			BufferedReader reader;
			try {
				reader = new BufferedReader(new FileReader(file));
				String line = reader.readLine();
				int numOfLines = 0;
				while (line != null) {
					String[] tem = new String[2];
					tem = line.split(regex);
					StringTuple2 tuple = new StringTuple2(tem[2], tem[3]);
					hashPartition.addKey(0, tuple, keyIdxs[0]);
//					hashPartition.addKeyWithFId(0, i, tuple, keyIdxs[0]);
					line = reader.readLine();
					numOfLines++;
				}
				reader.close();
				fileTotalLines[i][0] = numOfLines;
				// read the second dataset
				file = new File(filePath[1]);
				reader = new BufferedReader(new FileReader(file));
				
				line = reader.readLine();
				numOfLines = 0;
				while (line != null) {
					String[] tem = new String[2];
					tem = line.split(regex);
					StringTuple2 tuple = new StringTuple2(tem[2], tem[3]);
					hashPartition.addKey(1, tuple, keyIdxs[1]);
//					hashPartition.addKeyWithFId(1, i, tuple, keyIdxs[1]);
					
					line = reader.readLine();
					numOfLines++;
				}
				reader.close();
				
				fileTotalLines[i][1] = numOfLines;
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		hashPartition.setFileTotalLines(fileTotalLines);
				
		return hashPartition;
	}
	
	public static GridPartition readGridPartitionKey(String[][] filePaths, String regex, Configuration conf) {
//		double minLon, double minLat, double maxLon, double maxLat, int numLonBucket, int numLatBucket,
//		int numLonPar, int numLatPar
		
		GridPartition gridPartition = new GridPartition(conf.getMinLon(), conf.getMinLat(), 
				conf.getMaxLon(), conf.getMaxLat(), conf.getNumLonPar(), conf.getNumLatPar());
		
//		gridPartition.enableSelectivityEstimation();
		int[][] fileTotalLines = new int[filePaths.length][2];
		
		
		for (int i = 0; i < filePaths.length; i++) {
			String[] filePath = filePaths[i];
			File file = new File(filePath[0]);
			BufferedReader reader;
			try {
				reader = new BufferedReader(new FileReader(file));
				String line = reader.readLine();
				int numOfLines = 0;
				while (line != null) {
					String[] tem = line.split(regex);
					double minLon = Double.parseDouble(tem[0]);
					double minLat = Double.parseDouble(tem[1]);
					double maxLon = Double.parseDouble(tem[2]);
					double maxLat = Double.parseDouble(tem[3]);
					// double minLon, double minLat, double maxLon, double maxLat
//					DoubleTuple4 tuple = new DoubleTuple4(minLon, minLat,
//							maxLon,  maxLat);
					gridPartition.addRecord(0, minLon, minLat, maxLon, maxLat);
					line = reader.readLine();
					numOfLines++;
				}
				reader.close();
				fileTotalLines[i][0] = numOfLines;
				// read the second dataset
				file = new File(filePath[1]);
				reader = new BufferedReader(new FileReader(file));
				
				line = reader.readLine();
				numOfLines = 0;
				while (line != null) {
					String[] tem = line.split(regex);
					double minLon = Double.parseDouble(tem[0]);
					double minLat = Double.parseDouble(tem[1]);
					double maxLon = Double.parseDouble(tem[2]);
					double maxLat = Double.parseDouble(tem[3]);
					// double minLon, double minLat, double maxLon, double maxLat
//					DoubleTuple4 tuple = new DoubleTuple4(minLon, minLat,
//							maxLon,  maxLat);
					gridPartition.addRecord(1, minLon, minLat, maxLon, maxLat);
					line = reader.readLine();
					numOfLines++;
				}
				reader.close();
				
				fileTotalLines[i][1] = numOfLines;
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		gridPartition.setFileTotalLines(fileTotalLines);
		
		return gridPartition;
		
	}
	
	public static MultiHashPartition readMultiHashPartitionKey(String[][] filePaths, int numPar, int lowNumPar,
			int[] keyIdxs, String regex) {
		MultiHashPartition hashPartition = new MultiHashPartition(numPar, lowNumPar);
		hashPartition.enableSelectivityEstimation(new HashSet<String>());
		
		int[][] fileTotalLines = new int[filePaths.length][2];
		
		for (int i = 0; i < filePaths.length; i++) {
			String[] filePath = filePaths[i];
			File file = new File(filePath[0]);
			BufferedReader reader;
			try {
				reader = new BufferedReader(new FileReader(file));
				String line = reader.readLine();
				int numOfLines = 0;
				while (line != null) {
					String[] tem = new String[2];
					tem = line.split(regex);
					StringTuple2 tuple = new StringTuple2(tem[2], tem[3]);
					hashPartition.addKey(0, tuple, keyIdxs[0]);
//					hashPartition.addKeyWithFId(0, i, tuple, keyIdxs[0]);
					line = reader.readLine();
					numOfLines++;
				}
				reader.close();
				fileTotalLines[i][0] = numOfLines;
				// read the second dataset
				file = new File(filePath[1]);
				reader = new BufferedReader(new FileReader(file));
				
				line = reader.readLine();
				numOfLines = 0;
				while (line != null) {
					String[] tem = new String[2];
					tem = line.split(regex);
					StringTuple2 tuple = new StringTuple2(tem[2], tem[3]);
					hashPartition.addKey(1, tuple, keyIdxs[1]);
//					hashPartition.addKeyWithFId(1, i, tuple, keyIdxs[1]);
					
					line = reader.readLine();
					numOfLines++;
				}
				reader.close();
				
				fileTotalLines[i][1] = numOfLines;
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		hashPartition.setFileTotalLines(fileTotalLines);
				
		return hashPartition;
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
		
		int[] keyIdxs = {0, 0};
		HashPartition partition = CSVReader.readHashPartitionKey(paths, 10, keyIdxs, ";");
		int[][] fre = partition.getPartitionFrequency();
		System.out.println(Arrays.toString(fre[0]));
		System.out.println(Arrays.toString(fre[1]));
		System.out.println(Arrays.toString(partition.getFileTotalLines()[0]));
//		System.out.println(Arrays.toString(partition.getFileTotalLines()[1]));
	}

}
