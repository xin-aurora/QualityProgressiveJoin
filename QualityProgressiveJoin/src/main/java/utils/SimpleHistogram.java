package utils;

import java.util.ArrayList;

import dataStructure.SHCell;
import utils.UtilsFunction;

public class SimpleHistogram {

	double min;
	double max;
//	int numBucket;
	double bucketUnit;
	
	double standardDeviation = 0;
	int totalN = 0;

	SHCell[] cells;
	
	public ArrayList<Double> points;

	double[] dataArray;

	public SimpleHistogram(double min, double max, int numBucket) {
		this.min = min;
		this.max = max;
//		this.numBucket = numBucket;
		this.bucketUnit = (max - min) / numBucket;

		cells = new SHCell[numBucket];
		dataArray = new double[numBucket];
		points = new ArrayList<Double>();
		for (int i = 0; i < numBucket; i++) {
			double cellMin = i * bucketUnit + min;
			cells[i] = new SHCell(cellMin, cellMin + bucketUnit);
			points.add(cellMin);
		}
		points.add(max);
	}
	
	public SimpleHistogram(double min, double max,
			ArrayList<Double> update) {
		this.min = min;
		this.max = max;
		this.points = update;

		cells = new SHCell[update.size()-1];
		dataArray = new double[update.size()-1];
		points = new ArrayList<Double>();
		for (int i = 1; i < update.size(); i++) {
			double cellMin = update.get(i-1);
			double cellMax = update.get(i);
			cells[i-1] = new SHCell(cellMin, cellMax);
			points.add(cellMin);
		}
		points.add(max);
		
	}
	
	public SimpleHistogram(double min, double max,
			double[] update) {
		this.min = min;
		this.max = max;
		this.points = new ArrayList<Double>();

		cells = new SHCell[update.length-1];
		dataArray = new double[update.length-1];
		points = new ArrayList<Double>();
		for (int i = 1; i < update.length; i++) {
			double cellMin = update[i-1];
			double cellMax = update[i];
			cells[i-1] = new SHCell(cellMin, cellMax);
			points.add(cellMin);
		}
		points.add(max);
		
	}

	public void addRecord(double key) {
//		if (min <= key && key <= max) {
//			int idx = (int) ((key - min) / bucketUnit);
//			cells[idx].update(1);
//			dataArray[idx] += 1.0;
//			totalN += 1;
//		}
		for (int i=0; i<cells.length; i++) {
			if (cells[i].getMin() <= key && key < cells[i].getMax()) {
				cells[i].update(1);
				dataArray[i] += 1.0;
//				System.out.println(cells[i]);
				totalN += 1;
				break;
			}
		}
	}

	public void addRecord(int idx, int val) {
		cells[idx].update(val);
		dataArray[idx] += val;
		totalN += val;
	}
	
	public void addRecordNonUniform(double key) {
		for (int i=0; i<cells.length; i++) {
			if (cells[i].getMin() <= key && key < cells[i].getMax()) {
				cells[i].update(1);
				dataArray[i] += 1.0;
//				System.out.println(cells[i]);
				totalN += 1;
				break;
			}
		}
	}
	

	public SHCell[] getCells() {
		return cells;
	}

	public double[] getDataArray() {
		return dataArray;
	}

	public double getBucketUnit() {
		return bucketUnit;
	}
	
	public double getStandardDeviation() {
		return this.standardDeviation;
	}
	
	public double computeStandardDeviation() {
		
		if (totalN == 0) {
			for (int i=0; i<cells.length; i++) {
				totalN += cells[i].getVal();
			}
		}
		
		double mean = totalN / cells.length;
		
//		System.out.println("totalN = " + totalN + ", mean = " + mean);
		
		for (int i=0; i<cells.length; i++) {
			standardDeviation += Math.pow(cells[i].getVal() - mean, 2);
		}
		
		standardDeviation = standardDeviation / cells.length;
		
		standardDeviation = Math.sqrt(standardDeviation);
		
		return getStandardDeviation();
	}

	public void aggregateHistogram(SimpleHistogram src) {
		SHCell[] srcCells = src.getCells();

		int idxMerge = 0;
		int idxSrc = 0;
		int numBucketMerge = cells.length;
		int numBucketSrc = srcCells.length;
		while (idxMerge < numBucketMerge && idxSrc < numBucketSrc) {
			SHCell srcCell = srcCells[idxSrc];
			SHCell mergeCell = cells[idxMerge];

			if (mergeCell.isOverlap(srcCell)) {
				double overlapRegion = Math.min(srcCell.getMax(), mergeCell.getMax())
						- Math.max(srcCell.getMin(), mergeCell.getMin());
				// update = val * (common region / src area)
//				double update = overlapRegion / src.getBucketUnit() * srcCell.getVal();
				double update = srcCell.getVal() * overlapRegion / src.getBucketUnit();
//				System.out.println(
//						overlapRegion + ", " + src.getBucketUnit() + ", " + srcCell.getVal() + ", " + updateVal
//						+ ", " + update);
				cells[idxMerge].update(update);
				dataArray[idxMerge] += update;
			}

			if (srcCell.getMax() < mergeCell.getMax()) {
				idxSrc++;
			} else {
				idxMerge++;
			}
		}
	}

}
