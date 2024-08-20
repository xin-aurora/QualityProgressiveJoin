package io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

import dataStructure.DoubleTuple4;

public class SpatialJoinFileReader {
	
	private String filePath = "";
	private String regex;
	private int numPartition;
	private BufferedReader reader;
	
	// partition
	private double mMinLon;
	private double mMinLat;
	private double mMaxLon;
	private double mMaxLat;

	private int mNumLonPar;
	private int mNumLatPar;

	private double mLonUnitPar;
	private double mLatUnitPar;
	
	public SpatialJoinFileReader(String filePath, String regex, 
			double minLon, double minLat, double maxLon, double maxLat, int numLonPar, int numLatPar) {
		this.filePath = filePath;
		this.regex = regex;
		this.numPartition = numLonPar * numLatPar;
		
		mMinLon = minLon;
		mMinLat = minLat;
		mMaxLon = maxLon;
		mMaxLat = maxLat;
		mNumLonPar = numLonPar;
		mNumLatPar = numLatPar;
		mLonUnitPar = (maxLon - minLon) / numLonPar;
		mLatUnitPar = (maxLat - minLat) / numLatPar;
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
	
	public ArrayList<ArrayList<DoubleTuple4>> readFile(int start, int end) {
		ArrayList<ArrayList<DoubleTuple4>> partitionedDatas = new ArrayList<ArrayList<DoubleTuple4>>();

		for (int j = 0; j < numPartition; j++) {
			ArrayList<DoubleTuple4> partitionedData = new ArrayList<DoubleTuple4>();
			partitionedDatas.add(partitionedData);
		}

		try {
			for (int i = start; i < end; i++) {
				String line = reader.readLine();
//				System.out.println(i + ": " + line);
				// file
				String[] tem = line.split(regex);
				double minLon = Double.parseDouble(tem[0]);
				double minLat = Double.parseDouble(tem[1]);
				double maxLon = Double.parseDouble(tem[2]);
				double maxLat = Double.parseDouble(tem[3]);
				// double minLon, double minLat, double maxLon, double maxLat
				DoubleTuple4 tuple = new DoubleTuple4(minLon, minLat,
						maxLon,  maxLat);
				HashSet<Integer> overlapPIds = overlapBIdsPIds(minLon, minLat, maxLon, maxLat);
				for (int pId : overlapPIds) {
					partitionedDatas.get(pId).add(tuple);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return partitionedDatas;

	}
	
	public HashSet<Integer> overlapBIdsPIds(double minLon, double minLat, double maxLon, double maxLat) {

		HashSet<Integer> overlapIds = new HashSet<Integer>();

		int p1 = getBIdParId(minLat, minLon);
		int p2 = getBIdParId(minLat, maxLon);
		int p3 = getBIdParId(maxLat, minLon);

		int numLat = (p3 - p1) / mNumLonPar + 1;

		int numLon = p2 - p1 + 1;
		if (p3 == p1) {
			numLat = 1;
		}
		if (p2 == p1) {
			numLon = 1;
		}
		for (int i = 0; i < numLat; i++) {
			for (int j = p1; j < (p1 + numLon); j++) {
				overlapIds.add(j);
			}

			p1 += mNumLonPar;

		}

		return overlapIds;
	}

	private int getBIdParId(double latitude, double longitude) {

		int xP = -1;
		int yP = -1;

		if (latitude <= mMinLat) {
			xP = 0;
		} else if (latitude >= mMaxLat) {
			xP = mNumLatPar - 1;
		} else {
			xP = (int) ((latitude - mMinLat) / mLatUnitPar);
		}
		if (longitude <= mMinLon) {
			yP = 0;
		} else if (longitude >= mMaxLon) {
			yP = mNumLonPar - 1;
		} else {
			yP = (int) ((longitude - mMinLon) / mLonUnitPar);
		}

		int bIdPId = yP + xP * mNumLonPar;

		return bIdPId;
	}

	public void closeFile() {
		try {
			this.reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
