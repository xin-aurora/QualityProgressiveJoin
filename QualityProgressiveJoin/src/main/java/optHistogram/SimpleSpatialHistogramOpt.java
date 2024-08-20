package optHistogram;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import dataStructure.CellCellOverlapInfo;
import utils.UtilsFunction;
import utils.UtilsFunctionHistogram;

public class SimpleSpatialHistogramOpt {

	// boundary
	protected double minLon;
	protected double minLat;
	protected double maxLon;
	protected double maxLat;

	protected double lonBucketLenUnit;
	protected double latBucketLenUnit;

	// data and boundary
	double[][] data;
	double[] lonBoundary;
	double[] latBoundary;

	public int numLonBucket;
	public int numLatBucket;

	public ArrayList<double[]> frequency = new ArrayList<double[]>();

	double standardDeviation = 0;
	public int totalN = 0;

	public SimpleSpatialHistogramOpt(double minLon, double minLat, double maxLon, double maxLat, int numLonBucket,
			int numLatBucket) {
		this.minLon = minLon;
		this.maxLon = maxLon;
		this.minLat = minLat;
		this.maxLat = maxLat;

		this.data = new double[numLonBucket][numLatBucket];
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

		// frequency
		double[] lonFre = new double[numLonBucket + 1];
		double[] latFre = new double[numLatBucket + 1];
		frequency.add(lonFre);
		frequency.add(latFre);

		this.numLonBucket = numLonBucket;
		this.numLatBucket = numLatBucket;
	}

	public SimpleSpatialHistogramOpt(double minLon, double minLat, double maxLon, double maxLat, int numLonBucket,
			int numLatBucket, double[] lons, double[] lats) {

		this.minLon = minLon;
		this.maxLon = maxLon;
		this.minLat = minLat;
		this.maxLat = maxLat;

		this.data = new double[numLonBucket][numLatBucket];
		this.lonBoundary = lons;
		this.latBoundary = lats;

		// frequency
		double[] lonFre = new double[numLonBucket + 1];
		double[] latFre = new double[numLatBucket + 1];
		frequency.add(lonFre);
		frequency.add(latFre);

		this.numLonBucket = numLonBucket;
		this.numLatBucket = numLatBucket;
	}

	public void addRecord(double lon, double lat) {
		if ((minLon <= lon && lon <= maxLon) && (minLat <= lat && lat <= maxLat)) {
			int idxLon = (int) ((lon - minLon) / lonBucketLenUnit);
			int idxLat = (int) ((lat - minLat) / latBucketLenUnit);
			data[idxLon][idxLat] += 1;
			totalN += 1;
			frequency.get(0)[idxLon] += 1;
			frequency.get(1)[idxLat] += 1;
		}
	}

	public void addRecordNonUnitform(double lon, double lat) {
//		if ((minLon <= lon && lon <= maxLon) && (minLat <= lat && lat <= maxLat)) {
//			int idxLon = UtilsFunctionHistogram.FirstBiggerSearch(lon, lonBoundary);
//			int idxLat = UtilsFunctionHistogram.FirstBiggerSearch(lat, latBoundary);
//			data[idxLon][idxLat] += 1;
//			totalN += 1;
//			frequency.get(0)[idxLon] += 1;
//			frequency.get(1)[idxLat] += 1;
//		}
		if (UtilsFunction.isOverlapPointCell(lonBoundary[0], latBoundary[0], lonBoundary[numLonBucket],
				latBoundary[numLatBucket], lon, lat)) {

			int idxLon = 1;
			double cellLon = lonBoundary[idxLon];
			while (cellLon < lon) {
				idxLon++;
				cellLon = lonBoundary[idxLon];
			}
			int idxLat = 1;
			double cellLat = latBoundary[idxLat];
			while (cellLat < lat) {
				idxLat++;
				cellLat = latBoundary[idxLat];
			}
			data[idxLon - 1][idxLat - 1] += 1;
			frequency.get(0)[idxLon - 1] += 1;
			frequency.get(1)[idxLat - 1] += 1;
			totalN++;
		}
	}

	public double RangeQuery(double qMinLon, double qMaxLon, double qMinLat, double qMaxLat) {
		double ans = 0;
//		System.out.println("Query = " + qMinLon + "-" + qMaxLon + "," + qMinLat + "-" + qMaxLat);
		if (maxLon < qMinLon || qMaxLon < minLon || maxLat < qMinLat || qMaxLat < minLat) {
//			System.out.println("not in range");
			return 0;
		} else {
			int minIdxLon = UtilsFunctionHistogram.FirstBiggerSearch(qMinLon, lonBoundary);
			int minIdxLat = UtilsFunctionHistogram.FirstBiggerSearch(qMinLat, latBoundary);

			minIdxLon = Math.max(0, minIdxLon);
			minIdxLat = Math.max(0, minIdxLat);
			
//			System.out.println("minIdxLon = " + minIdxLon + ", minIdxLat = " + minIdxLat);
			double cellMinLon = lonBoundary[minIdxLon];
			minIdxLon++;
			double cellMaxLon = lonBoundary[minIdxLon];
			while (cellMaxLon < qMaxLon) {
				cellMaxLon = lonBoundary[minIdxLon];

				double overMinLon = Math.max(qMinLon, cellMinLon);
				double overMaxLon = Math.min(qMaxLon, cellMaxLon);
				double mWo = overMaxLon - overMinLon;

				
				double cellMinLat = latBoundary[minIdxLat];
				int idxLatStart = minIdxLat + 1;
				
				double cellMaxLat = latBoundary[idxLatStart];
				while (cellMaxLat < qMaxLat) {
					cellMaxLat = latBoundary[idxLatStart];

					double overMinLat = Math.max(qMinLat, cellMinLat);
					double overMaxLat = Math.min(qMaxLat, cellMaxLat);
					double mHo = overMaxLat - overMinLat;

					// query ans
					double ratio = mWo * mHo / (cellMaxLon - cellMinLon) / (cellMaxLat - cellMinLat);
					
//					System.out.println("Query = " + qMinLon + "-" + qMaxLon + "," + qMinLat + "-" + qMaxLat);
//					System.out.println("cell = " + cellMinLon + "-" + cellMaxLon + "," + cellMinLat + "-" + cellMaxLat);
//					System.out.println("ratio = " + ratio);
					
					ans += ratio * data[minIdxLon-1][idxLatStart-1];

					idxLatStart++;
					cellMinLat = cellMaxLat;
				}
				minIdxLon++;
				cellMinLon = cellMaxLon;
			}
		}

		return ans;
	}

	public double[][] getData() {
		return data;
	}

	public double[] getLonBoundary() {
		return lonBoundary;
	}

	public double[] getLatBoundary() {
		return latBoundary;
	}

	public double getLonUnit() {
		return lonBucketLenUnit;
	}

	public double getLatUnit() {
		return latBucketLenUnit;
	}

	public double computeStandardDeviation() {

		if (totalN == 0) {
			for (int i = 0; i < data.length; i++) {
				for (int j = 0; j < data[0].length; j++) {
					totalN += data[i][j];
				}

			}
		}

		double mean = totalN / data.length / data[0].length;
		standardDeviation = 0;

		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < data[0].length; j++) {
				standardDeviation += Math.pow(data[i][j] - mean, 2);
			}

		}

		standardDeviation = standardDeviation / data.length / data[0].length;

		standardDeviation = Math.sqrt(standardDeviation);

		return standardDeviation;
	}

	public void aggregateHistogram(SimpleSpatialHistogramOpt src) {
		double[][] srcData = src.getData();
		double[] srcLonBoundary = src.getLonBoundary();
		double[] srcLatBoundary = src.getLatBoundary();

		int dimensionT = lonBoundary.length;
		int numBucketT = latBoundary.length;

		int dimensionS = srcLonBoundary.length;
		int numBucketSrc = srcLatBoundary.length;

		// optimize the start dimension and start index for src
		int sdStart = UtilsFunctionHistogram.FirstBiggerSearch(minLon, srcLonBoundary);
		int idxSStart = UtilsFunctionHistogram.FirstBiggerSearch(minLat, srcLatBoundary);
		sdStart = Math.max(0, sdStart);
		idxSStart = Math.max(0, idxSStart);
//		System.out.println("sdStart = " + sdStart + ", idxSStart = " + idxSStart);

		int tdStart = UtilsFunctionHistogram.FirstBiggerSearch(src.minLon, lonBoundary);
		int idxTStart = UtilsFunctionHistogram.FirstBiggerSearch(src.minLat, latBoundary);
		tdStart = Math.max(0, tdStart);
		idxTStart = Math.max(0, idxTStart);

		// start from target lon-dimension = 0
		double tMinLon = lonBoundary[tdStart];
		int md = tdStart + 1;
//		double tMinLon = minLon;
//		int md = 1;
		while (md < dimensionT) {
			double tMaxLon = lonBoundary[md];

			// start from src lon-dimension = 0
			double sMinLon = srcLonBoundary[sdStart];
			int sd = sdStart + 1;
			int idxT = idxTStart + 1;
			int idxS = idxSStart + 1;

			double tMinLat = latBoundary[idxTStart];
			double tMaxLat = latBoundary[idxT];

			double sMinLat = srcLatBoundary[idxSStart];
			double sMaxLat = srcLatBoundary[idxS];

//			System.out.println("md = " + md + ", sdStart = " + sdStart);
			while (sd < dimensionS) {
				double sMaxLon = srcLonBoundary[sd];

				CellCellOverlapInfo overlapInfo = new CellCellOverlapInfo(tMinLon, tMinLat, tMaxLon, tMaxLat,
						(tMaxLon - tMinLon), (tMaxLat - tMinLat), sMinLon, sMinLat, sMaxLon, sMaxLat,
						(sMaxLon - sMinLon), (sMaxLat - sMinLat));

				if (overlapInfo.overlap) {
					data[md - 1][idxT - 1] += srcData[sd - 1][idxS - 1] * overlapInfo.mComAreaSource;
				}

				// update lat index
				if (sMaxLat < tMaxLat) {
					sMinLat = sMaxLat;
					idxS++;
					// update src lon-dimension index
					if (idxS == numBucketSrc) {
						// optimize
						if (overlapInfo.backSrcDimension) {
							sdStart = sd - 1;
							sdStart = Math.max(0, sdStart);
						}

						tMinLat = latBoundary[idxTStart];
						sMinLat = srcLatBoundary[idxSStart];
						idxT = idxTStart + 1;
						idxS = idxSStart + 1;
						tMaxLat = latBoundary[idxT];
						sMaxLat = srcLatBoundary[idxS];

						sMinLon = sMaxLon;
						sd++;
					} else {
						sMaxLat = srcLatBoundary[idxS];
					}

				} else {
					tMinLat = tMaxLat;
					idxT++;
					// update src lon-dimension index
					if (idxT == numBucketT) {
						// optimize
						if (overlapInfo.backSrcDimension) {
							sdStart = sd - 1;
							sdStart = Math.max(0, sdStart);
						}
						tMinLat = latBoundary[idxTStart];
						sMinLat = srcLatBoundary[idxSStart];
						idxT = idxTStart + 1;
						idxS = idxSStart + 1;
						tMaxLat = latBoundary[idxT];
						sMaxLat = srcLatBoundary[idxS];

						sMinLon = sMaxLon;
						sd++;
					} else {
						tMaxLat = latBoundary[idxT];
					}
				}

			}

			tMinLon = tMaxLon;
			md++;
		}
	}

	public void aggregateHistogramSep(SimpleSpatialHistogramOpt src) {
		double[][] srcData = src.getData();
		double[] srcLonBoundary = src.getLonBoundary();
		double[] srcLatBoundary = src.getLatBoundary();

		int dimensionT = lonBoundary.length;
		int numBucketT = latBoundary.length;

		int dimensionS = srcLonBoundary.length;
		int numBucketSrc = srcLatBoundary.length;

		// optimize the start dimension and start index for src
		int sdStart = UtilsFunctionHistogram.FirstBiggerSearch(minLon, srcLonBoundary);
		int idxSStart = UtilsFunctionHistogram.FirstBiggerSearch(minLat, srcLatBoundary);
		sdStart = Math.max(0, sdStart);
		idxSStart = Math.max(0, idxSStart);
//		System.out.println("sdStart = " + sdStart + ", idxSStart = " + idxSStart);

		int tdStart = UtilsFunctionHistogram.FirstBiggerSearch(src.minLon, lonBoundary);
		int idxTStart = UtilsFunctionHistogram.FirstBiggerSearch(src.minLat, latBoundary);
		tdStart = Math.max(0, tdStart);
		idxTStart = Math.max(0, idxTStart);

		// start from target lon-dimension = 0
		double tMinLon = lonBoundary[tdStart];
		int md = tdStart + 1;
		// start from src lon-dimension = 0
		double sMinLon = srcLonBoundary[sdStart];
		int sd = sdStart + 1;

		while (md < dimensionT && sd < dimensionS) {
			// first while loop-lon intersection
			double tMaxLon = lonBoundary[md];
			double sMaxLon = srcLonBoundary[sd];

			if (UtilsFunction.isOverlapInterval(tMinLon, tMaxLon, sMinLon, sMaxLon)) {
				// for overlap computation
				double overMinLon = Math.max(tMinLon, sMinLon);
				double overMaxLon = Math.min(tMaxLon, sMaxLon);
				double mWo = overMaxLon - overMinLon;
				if (mWo > 1.1102230246251565E-10) {

					// lat initialize
					int idxT = idxTStart + 1;
					int idxS = idxSStart + 1;
					double tMinLat = latBoundary[idxTStart];
//					double tMaxLat = latBoundary[idxT];

					double sMinLat = srcLatBoundary[idxSStart];
//					double sMaxLat = srcLatBoundary[idxS];

					while (idxT < numBucketT && idxS < numBucketSrc) {
						double tMaxLat = latBoundary[idxT];
						double sMaxLat = srcLatBoundary[idxS];
						if (UtilsFunction.isOverlapInterval(tMinLat, tMaxLat, sMinLat, sMaxLat)) {
							double overMinLat = Math.max(tMinLat, sMinLat);
							double overMaxLat = Math.min(tMaxLat, sMaxLat);

							double mHo = overMaxLat - overMinLat;
							if (mHo > 1.1102230246251565E-10) {
								// overlap
								// compute values
								double comAreaSource = mWo * mHo / (sMaxLon - sMinLon) / (sMaxLat - sMinLat);
								data[md - 1][idxT - 1] += srcData[sd - 1][idxS - 1] * comAreaSource;
							}
						}

						// update lat idx
						if (sMaxLat < tMaxLat) {
							sMinLat = sMaxLat;
							idxS++;
						} else {
							tMinLat = tMaxLat;
							idxT++;
						}
					}
				}
			}
			// update lon idx
			if (sMaxLon < tMaxLon) {
				sMinLon = sMaxLon;
				sd++;
			} else {
				tMinLon = tMaxLon;
				md++;
			}

		}
	}

	public void aggregateHistogramParallel(SimpleSpatialHistogramOpt src) {
		double[][] srcData = src.getData();
		double[] srcLonBoundary = src.getLonBoundary();
		double[] srcLatBoundary = src.getLatBoundary();
		int numBucketT = latBoundary.length;

		int dimensionS = srcLonBoundary.length;
		int numBucketSrc = srcLatBoundary.length;

		// optimize the start dimension and start index for src
		int sdStart = UtilsFunctionHistogram.FirstBiggerSearch(minLon, srcLonBoundary);
		int idxSStart = UtilsFunctionHistogram.FirstBiggerSearch(minLat, srcLatBoundary);
		sdStart = Math.max(0, sdStart);
		idxSStart = Math.max(0, idxSStart);
//		System.out.println("sdStart = " + sdStart + ", idxSStart = " + idxSStart);

		int tdStart = UtilsFunctionHistogram.FirstBiggerSearch(src.minLon, lonBoundary);
		int idxTStart = UtilsFunctionHistogram.FirstBiggerSearch(src.minLat, latBoundary);
		tdStart = Math.max(0, tdStart);
		idxTStart = Math.max(0, idxTStart);

		double tMinLon = lonBoundary[tdStart];
		double tMaxLon = lonBoundary[tdStart + 1];

		// start from src lon-dimension = 0
		double sMinLon = srcLonBoundary[sdStart];
		int sd = sdStart + 1;
		int idxT = idxTStart + 1;
		int idxS = idxSStart + 1;

		double tMinLat = latBoundary[idxTStart];
		double tMaxLat = latBoundary[idxT];

		double sMinLat = srcLatBoundary[idxSStart];
		double sMaxLat = srcLatBoundary[idxS];

//			System.out.println("md = " + md + ", sdStart = " + sdStart);
		while (sd < dimensionS) {
			double sMaxLon = srcLonBoundary[sd];

			CellCellOverlapInfo overlapInfo = new CellCellOverlapInfo(tMinLon, tMinLat, tMaxLon, tMaxLat,
					(tMaxLon - tMinLon), (tMaxLat - tMinLat), sMinLon, sMinLat, sMaxLon, sMaxLat, (sMaxLon - sMinLon),
					(sMaxLat - sMinLat));

			if (overlapInfo.overlap) {
				data[tdStart][idxT - 1] += srcData[sd - 1][idxS - 1] * overlapInfo.mComAreaSource;
			}

			// update lat index
			if (sMaxLat < tMaxLat) {
				sMinLat = sMaxLat;
				idxS++;
				// update src lon-dimension index
				if (idxS == numBucketSrc) {
					// optimize
					if (overlapInfo.backSrcDimension) {
						sdStart = sd - 1;
						sdStart = Math.max(0, sdStart);
					}

					tMinLat = latBoundary[idxTStart];
					sMinLat = srcLatBoundary[idxSStart];
					idxT = idxTStart + 1;
					idxS = idxSStart + 1;
					tMaxLat = latBoundary[idxT];
					sMaxLat = srcLatBoundary[idxS];

					sMinLon = sMaxLon;
					sd++;
				} else {
					sMaxLat = srcLatBoundary[idxS];
				}

			} else {
				tMinLat = tMaxLat;
				idxT++;
				// update src lon-dimension index
				if (idxT == numBucketT) {
					// optimize
					if (overlapInfo.backSrcDimension) {
						sdStart = sd - 1;
						sdStart = Math.max(0, sdStart);
					}
					tMinLat = latBoundary[idxTStart];
					sMinLat = srcLatBoundary[idxSStart];
					idxT = idxTStart + 1;
					idxS = idxSStart + 1;
					tMaxLat = latBoundary[idxT];
					sMaxLat = srcLatBoundary[idxS];

					sMinLon = sMaxLon;
					sd++;
				} else {
					tMaxLat = latBoundary[idxT];
				}
			}

		}

	}
	
	public static SimpleSpatialHistogramOpt loadSimpleSpatialHistogramOpt(String path, int lon, int lat) {

		SimpleSpatialHistogramOpt histogram = null;
		File file = new File(path);

		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));

			String[] header = reader.readLine().split(",");
			double minLon = 0;
			double maxLon = 0;
			double minLat = 0;
			double maxLat = 0;
			if (header.length > 2) {
				minLon = Double.parseDouble(header[0].split(":")[1].trim());
				maxLon = Double.parseDouble(header[1].split(":")[1].trim());
				minLat = Double.parseDouble(header[2].split(":")[1].trim());
				maxLat = Double.parseDouble(header[3].split(":")[1].trim());
			} else {
				minLon = Double.parseDouble(header[0]);
				maxLon = Double.parseDouble(header[1]);
				minLat = Double.parseDouble(header[0]);
				maxLat = Double.parseDouble(header[1]);
			}
			histogram = new SimpleSpatialHistogramOpt(minLon, minLat, maxLon, maxLat, lon, lat);
			String line = reader.readLine();
			while (line != null) {
				String[] tem = line.split(",");

//				histogram.addRecordNonUnitform(Double.parseDouble(tem[0]), Double.parseDouble(tem[1]));
				histogram.addRecord(Double.parseDouble(tem[0]), Double.parseDouble(tem[1]));

				line = reader.readLine();
			}
			reader.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return histogram;
	}

}
