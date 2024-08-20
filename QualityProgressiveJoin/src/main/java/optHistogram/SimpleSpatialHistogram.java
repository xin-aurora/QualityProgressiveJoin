package optHistogram;

import java.util.ArrayList;

import dataStructure.CellCellOverlapInfo;
import dataStructure.SHCell;
import dataStructure.SSH2DCell;
//import dataStructure.metric.JMTargetCell;
import utils.UtilsFunction;

public class SimpleSpatialHistogram extends SpatialHistogram {

	// histogram cells
	private SSH2DCell[][] mCells;
	double standardDeviation = 0;
	public int totalN = 0;
	
	public ArrayList<ArrayList<Double>> points; // 0: lon, 1: lat
	public ArrayList<ArrayList<Double>> frequency; // 0: lon, 1: lat
//	public ArrayList<ArrayList<Double>> midPoints; // 0: lon, 1: lat

	public SimpleSpatialHistogram(double minLon, double minLat, double maxLon, double maxLat, int numLonBucket,
			int numLatBucket) {
		super(minLon, minLat, maxLon, maxLat, numLonBucket, numLatBucket);

		mCells = new SSH2DCell[numLonBucket][numLatBucket];
		ArrayList<Double> lon = new ArrayList<Double>();
		ArrayList<Double> lat = new ArrayList<Double>();
//		ArrayList<Double> lonMid = new ArrayList<Double>();
//		ArrayList<Double> latMid = new ArrayList<Double>();
		ArrayList<Double> freLon = new ArrayList<Double>();
		ArrayList<Double> freLat = new ArrayList<Double>();
		boolean fillLat = false;
//		lonMid.add(mMinLon);
		freLon.add(0.0);
		for (int i = 0; i < numLonBucket; i++) {
			lon.add(mMinLon + i * mLonUnitBucket);
//			if (i>0) {
//				double midLonBound = i * mLonUnitBucket + mLonUnitBucket / 2 + mMinLon;
//				lonMid.add(midLonBound);
//			}
			freLon.add(0.0);
			freLat.add(0.0);
//			latMid.add(mMinLat);
			for (int j = 0; j < numLatBucket; j++) {
				double cellMinLon = mMinLon + i * mLonUnitBucket;
				double cellMaxLon = cellMinLon + mLonUnitBucket;
				double cellMinLat = mMinLat + j * mLatUnitBucket;
				double cellMaxLat = cellMinLat + mLatUnitBucket;
				mCells[i][j] = new SSH2DCell(cellMinLon, cellMinLat, cellMaxLon, cellMaxLat);
				if (!fillLat) {
					lat.add(cellMinLat);
//					if (j>0) {
//						double midLatBound = j * mLatUnitBucket + mLatUnitBucket / 2 + mMinLat;
//						latMid.add(midLatBound);
//					}
					freLat.add(0.0);
				}
			}
			fillLat = true;
		}
		lon.add(maxLon);
		lat.add(maxLat);
//		lonMid.add(maxLon);
//		latMid.add(maxLat);
		points = new ArrayList<ArrayList<Double>>();
		points.add(lon);
		points.add(lat);
//		midPoints = new ArrayList<ArrayList<Double>>();
//		midPoints.add(lonMid);
//		midPoints.add(latMid);
		frequency = new ArrayList<ArrayList<Double>>();
		frequency.add(freLon);
		frequency.add(freLat);
	}
	
	public SimpleSpatialHistogram(double minLon, double minLat, double maxLon, double maxLat,int numLonBucket,
			int numLatBucket, double[] lons, double[] lats) {
		super(minLon, minLat, maxLon, maxLat, numLonBucket, numLatBucket);

		ArrayList<Double> freLon = new ArrayList<Double>();
		ArrayList<Double> freLat = new ArrayList<Double>();
		freLon.add(0.0);
		
		mCells = new SSH2DCell[numLonBucket][numLatBucket];
		double cellMinLon = lons[0];
		boolean fillLat = false;
		
		for (int i = 1; i < lons.length; i++) {
			double cellMaxLon = lons[i];
			double cellMinLat = lats[0];
			freLon.add(0.0);
			freLat.add(0.0);
			for (int j = 1; j < lats.length; j++) {
//				System.out.println("i=" + i + ", j=" + j);
				double cellMaxLat = lats[j];
				mCells[i-1][j-1] = new SSH2DCell(cellMinLon, cellMinLat, cellMaxLon, cellMaxLat);
				cellMinLat = cellMaxLat;
				if (!fillLat) {
					freLat.add(0.0);
				}
			}
			cellMinLon = cellMaxLon;
		}
		frequency = new ArrayList<ArrayList<Double>>();
		frequency.add(freLon);
		frequency.add(freLat);
		
	}


	public void addRecord(double lon, double lat) {

		if (UtilsFunction.isOverlapPointCell(mMinLon, mMinLat, mMaxLon, mMaxLat, lon, lat)) {
			int[] p = getBId(lat, lon);

			// p[0] lat, p[1]: lon
			mCells[p[1]][p[0]].update(1);
			totalN++;
			double freLon = frequency.get(0).get(p[0]) + 1;
			frequency.get(0).set(p[0], freLon);
			double freLat = frequency.get(1).get(p[1]) + 1;
			frequency.get(1).set(p[1], freLat);
		}

	}
	
	public void addRecord(double lon, double lat, boolean bl) {

		if (UtilsFunction.isOverlapPointCell(mMinLon, mMinLat, mMaxLon, mMaxLat, lon, lat)) {
			
			int idxLon = 0;
			SSH2DCell cellLon = mCells[idxLon][0];
			while (cellLon.maxLon < lon) {
				idxLon++;
				cellLon = mCells[idxLon][0];
			}
			int idxLat =0;
			double cellLat = mCells[idxLon][idxLat].maxLat;
			while (cellLat < lat) {
				idxLat++;
				cellLat = mCells[idxLon][idxLat].maxLat;
			}
			mCells[idxLon][idxLat].update(1);
			double freLon = frequency.get(0).get(idxLon) + 1;
			frequency.get(0).set(idxLon, freLon);
			double freLat = frequency.get(1).get(idxLat) + 1;
			frequency.get(1).set(idxLat, freLat);
			totalN++;
		}

	}
	
	public void mergeHistogramCells(SSH2DCell[][] src) {
		int numLonBucketMerged = this.mNumLonBucket;
		int numLatBucketMerged = this.mNumLatBucket;

		int numLonBucketSrc = src.length;
		int numLatBucketSrc = src[0].length;

		int mDimension = 0;

		while (mDimension < numLonBucketMerged) {

			int srcDimension = 0;
			int mId = 0;
			int srcId = 0;
			while (srcDimension < numLonBucketSrc) {
				// getOverlap
				SSH2DCell mergedCell = mCells[mDimension][mId];
				SSH2DCell srcCell = src[srcDimension][srcId];
				CellCellOverlapInfo overlapInfo = getCellCellOverlapInfo(mergedCell.minLon, mergedCell.minLat,
						mergedCell.maxLon, mergedCell.maxLat, (mergedCell.maxLon - mergedCell.minLon),
						(mergedCell.maxLat - mergedCell.minLat), srcCell.minLon, srcCell.minLat, srcCell.maxLon,
						srcCell.maxLat, (srcCell.maxLon - srcCell.minLon), (srcCell.maxLat - srcCell.minLat));

				if (overlapInfo.overlap) {
					double c = src[srcDimension][srcId].getVal() * overlapInfo.mComAreaSource;
//					System.out.println("map " + src[srcDimension][srcId].getVal() + " to "  + srcDimension + "-" + srcId + " + " + c);
//					System.out.println("merge cell = " + mergedCell.minLon + "-" + mergedCell.maxLon + ", " +
//							 mergedCell.minLat + "-" + mergedCell.maxLat + ", " +
//								", src cell = " + srcCell.minLon + "-" + srcCell.maxLon + ", " +
//								srcCell.minLat + "-" + srcCell.maxLat);
//					System.out.println(srcDimension + "-" + srcId + " + " + overlapInfo);
					mCells[mDimension][mId].update(c);
				}
				if (srcCell.maxLat < mergedCell.maxLat) {
					srcId++;
				} else {
					mId++;
				}

				if (mId >= numLatBucketMerged || srcId >= numLatBucketSrc) {
					mId = 0;
					srcId = 0;
					srcDimension++;
				}
			}

			mDimension++;
		}
	}

	public void mergeHistogram(SimpleSpatialHistogram src) {
		int numLonBucketMerged = this.mNumLonBucket;
		int numLatBucketMerged = this.mNumLatBucket;

		int numLonBucketSrc = src.getNumLonBucket();
		int numLatBucketSrc = src.getNumLatBucket();

		int mDimension = 0;

		while (mDimension < numLonBucketMerged) {

			int srcDimension = 0;
			int mId = 0;
			int srcId = 0;
			while (srcDimension < numLonBucketSrc) {
				// getOverlap
				SSH2DCell mergedCell = mCells[mDimension][mId];
				SSH2DCell srcCell = src.getCellById(srcDimension, srcId);
				CellCellOverlapInfo overlapInfo = getCellCellOverlapInfo(mergedCell.minLon, mergedCell.minLat,
						mergedCell.maxLon, mergedCell.maxLat, (mergedCell.maxLon - mergedCell.minLon),
						(mergedCell.maxLat - mergedCell.minLat), srcCell.minLon, srcCell.minLat, srcCell.maxLon,
						srcCell.maxLat, (srcCell.maxLon - srcCell.minLon), (srcCell.maxLat - srcCell.minLat));

				if (overlapInfo.overlap) {
//					System.out.println(overlapInfo);
//					System.out.println(mergedCell);
//					System.out.println(srcCell);
//					System.out.println("tLon = " + (mergedCell.maxLon - mergedCell.minLon) + ", tLat = " + (mergedCell.maxLat - mergedCell.minLat)
//							+ ", sLon = " + (srcCell.maxLon - srcCell.minLon) + ", sLat = " + (srcCell.maxLat - srcCell.minLat));
					double c = src.getCellById(srcDimension, srcId).getVal() * overlapInfo.mComAreaSource;
//					System.out.println(srcDimension + "-" + srcId + " + " + c);
					mCells[mDimension][mId].update(c);
				}
				if (srcCell.maxLat < mergedCell.maxLat) {
					srcId++;
				} else {
					mId++;
				}

				if (mId >= numLatBucketMerged || srcId >= numLatBucketSrc) {
					mId = 0;
					srcId = 0;
					srcDimension++;
				}
			}

			mDimension++;
		}
	}

	public SSH2DCell[][] getCells() {
		return mCells;
	}
	
	public SSH2DCell[] getCellsOneArray() {
		SSH2DCell[] cells = new SSH2DCell[mCells.length * mCells[0].length];
		int idx = 0;
//		System.out.println("cell boundary = " + mCells.length + ", " + mCells[0].length);
		for (int i=0; i<mCells.length; i++) {
			for (int j=0; j<mCells[0].length; j++) {
//				System.out.println("i=" + i + ", j=" + j);
				cells[idx] = mCells[i][j];
				idx++;
			}
		}
		return cells;
	}

	public SSH2DCell getCellById(int lon, int lat) {
		return mCells[lon][lat];
	}

	public int[] getArrayByIdx(int dimension) {
		int[] array = null;
		if (dimension == 0) {
			array = new int[mNumLonBucket];
		} else {
			array = new int[mNumLatBucket];
		}
		return array;
	}

	public double[] getBoundary(int cellIdx, boolean lon) {
		double[] boundary = new double[2];

		if (lon) {
			boundary[0] = mMinLon + cellIdx * mLonUnitBucket;
			boundary[1] = boundary[0] + mLonUnitBucket;
		} else {
			boundary[0] = mMinLat + cellIdx * mLatUnitBucket;
			boundary[1] = boundary[2] + mLatUnitBucket;
		}

		return boundary;
	}

	private CellCellOverlapInfo getCellCellOverlapInfo(double tMinLon, double tMinLat, double tMaxLon, double tMaxLat,
			double tLonUnit, double tLatUnit, double sMinLon, double sMinLat, double sMaxLon, double sMaxLat,
			double sLonUnit, double sLatUnit) {

		return new CellCellOverlapInfo(tMinLon, tMinLat, tMaxLon, tMaxLat, tLonUnit, tLatUnit, sMinLon, sMinLat,
				sMaxLon, sMaxLat, sLonUnit, sLatUnit);
	}
	
	public double computeStandardDeviation() {
		
		if (totalN == 0) {
			for (int i=0; i<mCells.length; i++) {
				for (int j=0; j<mCells[0].length; j++) {
					totalN += mCells[i][j].getVal();
				}

			}
		}
		
		double mean = totalN / mNumLonBucket / mNumLatBucket;
		standardDeviation = 0;
		
//		System.out.println("totalN = " + totalN + ", mean = " + mean);
		
		for (int i=0; i<mCells.length; i++) {
			for (int j=0; j<mCells[0].length; j++) {
				standardDeviation += Math.pow(mCells[i][j].getVal() - mean, 2);
			}

		}
		
//		System.out.println("tmp = " + standardDeviation);
		standardDeviation = standardDeviation / mNumLonBucket / mNumLatBucket;
		
		standardDeviation = Math.sqrt(standardDeviation);
		
		return standardDeviation;
	}

}
