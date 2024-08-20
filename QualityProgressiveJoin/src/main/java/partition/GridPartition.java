package partition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import dataStructure.StringTuple2;
import operator.SpatialJoinSelectivity;

public class GridPartition {

	private double mMinLon;
	private double mMinLat;
	private double mMaxLon;
	private double mMaxLat;

	private int mNumLonPar;
	private int mNumLatPar;

	private double mLonUnitPar;
	private double mLatUnitPar;

	private int[][] partitionSize;
	private int[][] fileTotalLines;

	// spatial join selectivity estimation
	private SpatialJoinSelectivity mSelectivityEstimation;
	boolean mDoSelectivityEstimation = false;

	public GridPartition(double minLon, double minLat, double maxLon, double maxLat, int numLonPar, int numLatPar) {

		mMinLon = minLon;
		mMinLat = minLat;
		mMaxLon = maxLon;
		mMaxLat = maxLat;

//		mNumLonBucket = (int) Math.pow(2, level);
//		mNumLatBucket = (int) Math.pow(2, level);
		mNumLonPar = numLonPar;
		mNumLatPar = numLatPar;

		mLonUnitPar = (maxLon - minLon) / numLonPar;
		mLatUnitPar = (maxLat - minLat) / numLatPar;

		partitionSize = new int[2][mNumLonPar * mNumLatPar];

	}

	public void addRecord(int dsID, double minLon, double minLat, double maxLon, double maxLat) {
		HashSet<Integer> overlapPIds = overlapBIdsPIds(minLon, minLat, maxLon, maxLat);
		for (int pId : overlapPIds) {
			partitionSize[dsID][pId] += 1;
		}

		if (mDoSelectivityEstimation) {
			mSelectivityEstimation.addRecord(minLon, minLat, maxLon, maxLat, dsID);
		}
	}

	public void addRecordMulti(double minLon, double minLat, double maxLon, double maxLat) {
		HashSet<Integer> overlapPIds = overlapBIdsPIds(minLon, minLat, maxLon, maxLat);
		for (int pId : overlapPIds) {
			partitionSize[0][pId] += 1;
		}
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

	public void enableSelectivityEstimation() {
		mDoSelectivityEstimation = true;
		// double minLon, double minLat, double maxLon,
		// double maxLat, int numLonBucket, int numLatBucket
		mSelectivityEstimation = new SpatialJoinSelectivity(mMinLon, mMinLat, mMaxLon, mMaxLat, mNumLonPar * mNumLonPar,
				mNumLatPar * mNumLatPar, mNumLonPar, mNumLatPar);
	}

	public int[] getSelectivityEstimaiton() {
		return mSelectivityEstimation.selectivityEstimation();
	}

	public void setFileTotalLines(int[][] fileTotalLines) {
		this.fileTotalLines = fileTotalLines;
	}

	public int[][] getFileTotalLines() {
		return fileTotalLines;
	}

	public int[][] getPartitionFrequency() {
		return this.partitionSize;
	}

}
