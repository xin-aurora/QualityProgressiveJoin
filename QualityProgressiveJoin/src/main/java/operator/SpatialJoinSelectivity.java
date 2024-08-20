package operator;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import dataStructure.GHCell;
import utils.UtilsFunction;

public class SpatialJoinSelectivity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8324026773263274273L;

	// boundary
	double minLon;
	double minLat;
	double maxLon;
	double maxLat;

	double lonBucketLenUnit;
	double latBucketLenUnit;

	// data and boundary
	double[][] count1;
	double[][] mSumRatioArea1;
	double[][] mSumRatioH1;
	double[][] mSumRatioV1;
	
	double[][] count2;
	double[][] mSumRatioArea2;
	double[][] mSumRatioH2;
	double[][] mSumRatioV2;
	
	double[] lonBoundary;
	double[] latBoundary;

	public int numLonBucket;
	public int numLatBucket;

	public int totalN1 = 0;
	public int totalN2 = 0;
	
	int numLonPar = 0;
	int numLatPar = 0;

	public SpatialJoinSelectivity(double minLon, double minLat, double maxLon, double maxLat, int numLonBucket,
			int numLatBucket, int numLonPar, int numLatPar) {
		this.minLon = minLon;
		this.maxLon = maxLon;
		this.minLat = minLat;
		this.maxLat = maxLat;

		this.count1 = new double[numLonBucket][numLatBucket];
		this.mSumRatioArea1 = new double[numLonBucket][numLatBucket];
		this.mSumRatioH1 = new double[numLonBucket][numLatBucket];
		this.mSumRatioV1 = new double[numLonBucket][numLatBucket];
		this.count2 = new double[numLonBucket][numLatBucket];
		this.mSumRatioArea2 = new double[numLonBucket][numLatBucket];
		this.mSumRatioH2 = new double[numLonBucket][numLatBucket];
		this.mSumRatioV2 = new double[numLonBucket][numLatBucket];
		this.lonBoundary = new double[numLonBucket + 1];
		this.latBoundary = new double[numLatBucket + 1];
		this.lonBucketLenUnit = (maxLon - minLon) / numLonBucket;
		this.latBucketLenUnit = (maxLat - minLat) / numLatBucket;

		for (int i = 0; i < numLonBucket; i++) {
			lonBoundary[i] = minLon + i * lonBucketLenUnit;
		}
		lonBoundary[numLonBucket] = maxLon;
		for (int i = 0; i < numLatBucket; i++) {
			latBoundary[i] = minLat + i * latBucketLenUnit;
		}
		latBoundary[numLatBucket] = maxLat;


		this.numLonBucket = numLonBucket;
		this.numLatBucket = numLatBucket;
		
		// selectivity estimation
		this.numLonPar = numLonPar;
		this.numLatPar = numLatPar;
	}
	
	public void addRecord(double minLon, double minLat, double maxLon, double maxLat, int dsID) {

		// --> lon
		// P1 - P2
		// P3 - P4
		int[] p1 = getBId(minLat, minLon);
		int[] p2 = getBId(minLat, maxLon);
		int[] p3 = getBId(maxLat, minLon);
		
//		System.out.println("p1 = " + Arrays.toString(p1));
//		System.out.println("p2 = " + Arrays.toString(p2));
//		System.out.println("p3 = " + Arrays.toString(p3));

		for (int lat = p1[0]; lat <= p3[0]; lat++) {
			for (int lon = p1[1]; lon <= p2[1]; lon++) {
//				int idx = lon + lat * mNumLonBucket;
//				System.out.println("idx = " + idx);
//				System.out.println("lon = " + lon + ", lat = " + lat);
				double cellMinLon = lonBoundary[lon];
				double cellMaxLon = lonBoundary[lon + 1];
				double cellMinLat = latBoundary[lat];
				double cellMaxLat = latBoundary[lat + 1];

//				System.out.println("Cell: " + cellMinLon + "-" + cellMaxLon + ", " + cellMinLat + "-" + cellMaxLat);

				GHCell tmpCell = fillCell(minLon, minLat, maxLon, maxLat, cellMinLon, cellMinLat, cellMaxLon,
						cellMaxLat);
//				mCells[lat][lon].accumulate(tmpCell);
				if (tmpCell.getmSumRatioArea() > 0) {
					if (dsID == 1) {
						totalN1 += 1;
					} else {
						totalN2 += 1;
					}
				}
				fillCell(tmpCell, lon, lat, dsID);
			}
		}
	}

	private void fillCell(GHCell cell, int lonIdx, int latIdx, int dsID) {
		if (dsID == 1) {
			this.count1[lonIdx][latIdx] += cell.getmNumC();
			this.mSumRatioArea1[lonIdx][latIdx]  += cell.getmSumRatioArea();
			this.mSumRatioH1[lonIdx][latIdx] += cell.getmSumRatioH();
			this.mSumRatioV1[lonIdx][latIdx] += cell.getmSumRatioV();
		} else {
			this.count2[lonIdx][latIdx] += cell.getmNumC();
			this.mSumRatioArea2[lonIdx][latIdx]  += cell.getmSumRatioArea();
			this.mSumRatioH2[lonIdx][latIdx] += cell.getmSumRatioH();
			this.mSumRatioV2[lonIdx][latIdx] += cell.getmSumRatioV();
		}
	}

	private GHCell fillCell(double minLon, double minLat, double maxLon, double maxLat, double minLonCell,
			double minLatCell, double maxLonCell, double maxLatCell) {
		GHCell cell = new GHCell(minLonCell, minLatCell, maxLonCell, maxLatCell);

		double minLonInt = 0.0;
		double maxLonInt = 0.0;
		double minLatInt = 0.0;
		double maxLatInt = 0.0;

		if (minLon < minLonCell) {
			minLonInt = minLonCell;

			if (maxLonCell < maxLon) {
				maxLonInt = maxLonCell;
				if (minLat < minLatCell) {
					minLatInt = minLatCell;

					if (maxLatCell < maxLat) {
						// cell in object
						maxLatInt = maxLatCell;
					} else {
						// maxLat <= maxLatCell;
						maxLatInt = maxLat;
						cell.addRatioH((maxLonInt - minLonInt) / lonBucketLenUnit);
					}

				} else {
					// minLatCell <= minLat
					minLatInt = minLat;

					if (maxLatCell < maxLat) {
						maxLatInt = maxLatCell;
						cell.addRatioH((maxLonInt - minLonInt) / lonBucketLenUnit);
					} else {
						// maxLat <= maxLatCell;
						maxLatInt = maxLat;
						cell.addRatioH((maxLonInt - minLonInt) / lonBucketLenUnit * 2);
					}
				}

			} else {
				// !!! might intersection vertices
				// maxLon <= maxLonCell
				maxLonInt = maxLon;
				if (minLat < minLatCell) {
					minLatInt = minLatCell;
					if (maxLatCell < maxLat) {
						// No intersection vertices
						maxLatInt = maxLatCell;
					} else {
						// maxLat <= maxLatCell;
						maxLatInt = maxLat;
						cell.addCornerPoint(1);
						cell.addRatioH((maxLonInt - minLonInt) / lonBucketLenUnit);
					}
					cell.addRatioV((maxLatInt - minLatInt) / latBucketLenUnit);
				} else {
					// minLatCell <= minLat
					minLatInt = minLat;
					if (maxLatCell < maxLat) {
						maxLatInt = maxLatCell;
						cell.addCornerPoint(1);
						cell.addRatioH((maxLonInt - minLonInt) / lonBucketLenUnit);
					} else {
						// maxLat <= maxLatCell;
						maxLatInt = maxLat;
						cell.addCornerPoint(2);
						cell.addRatioH((maxLonInt - minLonInt) / lonBucketLenUnit * 2);
					}
					cell.addRatioV((maxLatInt - minLatInt) / latBucketLenUnit);
				}

			}

		} else {
			// minLonCell <= minLon
			// no HL
			minLonInt = minLon;
			if (maxLonCell < maxLon) {
				maxLonInt = maxLonCell;
				if (minLat < minLatCell) {
					minLatInt = minLatCell;
					if (maxLatCell < maxLat) {
						// No intersection vertices
						maxLatInt = maxLatCell;
					} else {
						// maxLat <= maxLatCell;
						maxLatInt = maxLat;
						cell.addCornerPoint(1);
						cell.addRatioH((maxLonInt - minLonInt) / lonBucketLenUnit);
					}
					cell.addRatioV((maxLatInt - minLatInt) / latBucketLenUnit);
				} else {
					// minLatCell <= minLat
					minLatInt = minLat;
					if (maxLatCell < maxLat) {
						maxLatInt = maxLatCell;
						cell.addCornerPoint(1);
						cell.addRatioH((maxLonInt - minLonInt) / lonBucketLenUnit);
					} else {
						// maxLat <= maxLatCell;
						maxLatInt = maxLat;
						cell.addCornerPoint(2);
						cell.addRatioH((maxLonInt - minLonInt) / lonBucketLenUnit * 2);
					}
					cell.addRatioV((maxLatInt - minLatInt) / latBucketLenUnit);
				}

			} else {

				// maxLon <= maxLonCell
				maxLonInt = maxLon;
				if (minLat < minLatCell) {
					minLatInt = minLatCell;
					if (maxLatCell < maxLat) {
						maxLatInt = maxLatCell;
					} else {
						// maxLat <= maxLatCell;
						maxLatInt = maxLat;
						cell.addCornerPoint(2);
						cell.addRatioH((maxLonInt - minLonInt) / lonBucketLenUnit);
					}
					cell.addRatioV((maxLatInt - minLatInt) / latBucketLenUnit * 2);
				} else {
					// minLatCell <= minLat
					minLatInt = minLat;
					if (maxLatCell < maxLat) {
						maxLatInt = maxLatCell;
						cell.addCornerPoint(2);
						cell.addRatioH((maxLonInt - minLonInt) / lonBucketLenUnit);
					} else {
						// maxLat <= maxLatCell;
						maxLatInt = maxLat;
						cell.addCornerPoint(4);
						cell.addRatioH((maxLonInt - minLonInt) / lonBucketLenUnit * 2);
					}
					cell.addRatioV((maxLatInt - minLatInt) / latBucketLenUnit * 2);
				}

			}

		}

		double area = (maxLonInt - minLonInt) * (maxLatInt - minLatInt);

		cell.addRatioArea(area / lonBucketLenUnit / latBucketLenUnit);

		return cell;
	}

	public int[] getBId(double latitude, double longitude) {

		int[] point = new int[2];

		int xP = -1;
		int yP = -1;

		if (latitude <= minLat) {
			xP = 0;
		} else if (latitude >= maxLat) {
			xP = numLatBucket - 1;
		} else {
			xP = (int) ((latitude - minLat) / latBucketLenUnit);
		}
		if (longitude <= minLon) {
			yP = 0;
		} else if (longitude >= maxLon) {
			yP = numLonBucket - 1;
		} else {
			yP = (int) ((longitude - minLon) / lonBucketLenUnit);
		}

		point[0] = xP;
		point[1] = yP;

		return point;
	}
	
	
	public double[] getLonBoundary() {
		return lonBoundary;
	}

	public double[] getLatBoundary() {
		return latBoundary;
	}
	
	public int[] selectivityEstimation() {
		int[] estimations = new int[numLonPar*numLatPar];
		
		for (int lonId = 0; lonId < numLonPar; lonId++) {
			for (int latId = 0; latId < numLatPar; latId++) {
				// TODO: refine lat, lonId with numLonBucket and numLonBucket
				double intersectionPoints = 0;
				
				for (int i = 0; i < numLonBucket; i++) {
					for (int j = 0; j < numLonBucket; j++) {
						intersectionPoints += count1[i][j] * mSumRatioArea2[i][j] +
								mSumRatioArea1[i][j] * count2[i][j] +
								mSumRatioH1[i][j] * mSumRatioV2[i][j] +
								mSumRatioV1[i][j] * mSumRatioH2[i][j];
					}
				}
				
				System.out.println("intersectionPoints = " + intersectionPoints + 
						", " + intersectionPoints / 4);
				System.out.println("num of cells = " + numLatBucket * numLonBucket);
				
				estimations[numLonPar + latId*numLatPar] = (int) Math.ceil(intersectionPoints / 4);
			}
		}

		
		return estimations;
	}
	

}
