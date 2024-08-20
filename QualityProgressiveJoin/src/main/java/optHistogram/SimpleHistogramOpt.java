package optHistogram;

import java.util.ArrayList;
import java.util.Arrays;

import io.CSVTupleReader;
import io.LoadHistogram;
import utils.SimpleHistogram;
import utils.UtilsFunction;
import utils.UtilsFunctionHistogram;

public class SimpleHistogramOpt {

	protected double min;
	protected double max;
	protected double[] data;
	protected double[] boundary;
	protected double bucketLenUnit = 0;

	protected int totalN = 0;
	double standardDeviation = 0;

	public SimpleHistogramOpt(double min, double max, int numBucket) {
		this.min = min;
		this.max = max;
		this.data = new double[numBucket];
		this.boundary = new double[numBucket + 1];
		this.bucketLenUnit = (max - min) / numBucket;
		for (int i = 0; i < numBucket; i++) {
			double b = min + bucketLenUnit * i;
			boundary[i] = b;
		}
		boundary[numBucket] = max;
	}

	public SimpleHistogramOpt(ArrayList<Double> update) {
		int numBucket = update.size() - 1;
		this.min = update.get(0);
		this.max = update.get(numBucket);
		this.data = new double[numBucket];
		this.boundary = new double[numBucket];
		for (int i = 0; i < numBucket; i++) {
			boundary[i] = update.get(i);
		}
	}

	public SimpleHistogramOpt(double[] update) {
		int numBucket = update.length - 1;
		this.min = update[0];
		this.max = update[numBucket];
//		System.out.println("min = " + min + ", max = " + max);
		this.data = new double[numBucket];
		this.boundary = update;
	}

	public void addRecord(double key) {
		if (min <= key && key <= max) {
			int idx = (int) ((key - min) / bucketLenUnit);
			data[idx] += 1;
			totalN += 1;
		}
	}

	public void addRecordNonUniform(double key) {
		if (min <= key && key <= max) {
//			int idx = UtilsFunctionHistogram.BinarySearch(key, boundary);
			int idx = UtilsFunctionHistogram.FirstBiggerSearch(key, boundary);
//			System.out.print(idx + ", ");
			if (idx == data.length) {
				idx = idx - 1;
			}
			data[idx] += 1;
			totalN += 1;
		}
	}

	public void addRecordByIdx(int idx, double val) {
		data[idx] += val;
		totalN += val;
	}

	public double[] getData() {
		return this.data;
	}

	public double[] getBoundary() {
		return this.boundary;
	}

	public double getBucketUnit() {
		return bucketLenUnit;
	}

	public void aggregateHistogram(SimpleHistogramOpt src) {
		double[] srcData = src.getData();
		double[] srcBoundary = src.getBoundary();

		int idxT = 1;
		int idxS = 1;
		int numBucketT = boundary.length;
		int numBucketSrc = srcBoundary.length;

		double tMin = boundary[0];
		double sMin = srcBoundary[0];

		while (idxT < numBucketT && idxS < numBucketSrc) {
			double tMax = boundary[idxT];
			double sMax = srcBoundary[idxS];
			
//			System.out.println("idxT = " + idxT + ", idxS = " + idxS);

			if (UtilsFunction.isOverlapInterval(tMin, tMax, sMin, sMax)) {
				double overlapRegion = Math.min(sMax, tMax) - Math.max(sMin, tMin);
				// update = val * (common region / src area)
				double update = srcData[idxS - 1] * overlapRegion / src.getBucketUnit();
				addRecordByIdx(idxT - 1, update);
			}

			if (sMax < tMax) {
				sMin = sMax;
				idxS++;
			} else {
				tMin = tMax;
				idxT++;
			}
		}
	}

	public double computeStandardDeviation() {

		if (totalN == 0) {
			for (int i = 0; i < data.length; i++) {
				totalN += data[i];
			}
		}

		double mean = totalN / data.length;

//		System.out.println("totalN = " + totalN + ", mean = " + mean);

		for (int i = 0; i < data.length; i++) {
			standardDeviation += Math.pow(data[i] - mean, 2);
		}
		
		System.out.println(standardDeviation);

		standardDeviation = standardDeviation / data.length;
		System.out.println(standardDeviation);

		standardDeviation = Math.sqrt(standardDeviation);

		return standardDeviation;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		CSVTupleReader reader = new CSVTupleReader(",", true, "");
		int sourceSize = 1024;
		String folder = "/Users/xin_aurora/Downloads/Work/2019/UCR/Research/Spatial/sketches/data/medium/point/";
		String path031 = folder + "1d/bit03-" + 0 + ".csv";
		SimpleHistogram hisB031 = reader.getSummaryDouble(path031, sourceSize);
		LoadHistogram load = new LoadHistogram();
		SimpleHistogramOpt hisB031Opt = load.loadSimpleHistogramOpt(path031, sourceSize);

//		UtilsFunctionHistogram.SimpleHistogramOptValidation(hisB031Opt, hisB031);

//		System.out.println(Arrays.toString(hisB031Opt.getBoundary()));

//		double[] updateReso = { 18.148038297753466, 120.14803829775346, 322.1480382977535, 524.1480382977535,
//				726.1480382977535, 928.1480382977534, 1130.1480382977534, 
//				1332.1480382977534, 1534.1480382977534,
//				1736.1480382977534 };

		double[] boundary = hisB031Opt.getBoundary();
		double[] updateReso = new double[sourceSize + 1];
		updateReso[0] = boundary[0];
		updateReso[sourceSize] = boundary[sourceSize];
		for (int i = 1; i < sourceSize - 1; i++) {
			updateReso[i] = boundary[i] + 10;
		}

		SimpleHistogram update = new SimpleHistogram(updateReso[0], updateReso[updateReso.length - 1], updateReso);

		SimpleHistogramOpt updateOpt = new SimpleHistogramOpt(updateReso);

		long startTime = System.nanoTime();
		update.aggregateHistogram(hisB031);
		long endTime = System.nanoTime();
		double mergeTime = (endTime - startTime) * 1E-9;
		System.out.println("mergeTime time = " + mergeTime + " s.");

		startTime = System.nanoTime();
		updateOpt.aggregateHistogram(hisB031Opt);
		endTime = System.nanoTime();
		mergeTime = (endTime - startTime) * 1E-9;
		System.out.println("update mergeTime time = " + mergeTime + " s.");

		UtilsFunctionHistogram.SimpleHistogramOptValidation(updateOpt, update);
	}

}
