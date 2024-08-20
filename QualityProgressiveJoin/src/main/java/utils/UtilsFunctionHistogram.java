package utils;

import dataStructure.SHCell;
import dataStructure.SSH2DCell;
//import estimation.histogram.SimpleHistogram;
//import estimation.histogram.SimpleSpatialHistogram;
import optHistogram.SimpleHistogramOpt;
import optHistogram.SimpleSpatialHistogramOpt;

public class UtilsFunctionHistogram {

	/**
	 * Implemented based on https://www.geeksforgeeks.org/binary-search/
	 * 
	 * @param key:  search key
	 * @param data: data array
	 * @return index of the search key
	 */
	public static int BinarySearch(double key, double[] data) {
		int l = 0, r = data.length - 1;
		while (l <= r) {
			int m = l + (r - l) / 2;

			// Check if x is present at mid
			if (data[m] == key) {
				return m;
			}

			// If x greater, ignore left half
			if (data[m] < key) {
				l = m + 1;
			}

			// If x is smaller, ignore right half
			else {
				r = m - 1;
			}
		}

		// if we reach here, then element was
		// not present
		return -1;

	}

	public static int FirstBiggerSearch(double key, double[] data) {
		int l = 0, r = data.length - 1;
		while (l <= r) {
			int m = l + (r - l) / 2;

			// Check if x is present at mid
			if (data[m] == key) {
				return m;
			}

			// If x greater, ignore left half
			if (data[m] < key) {
				l = m + 1;
			}

			// If x is smaller, ignore right half
			else {
				r = m - 1;
			}
		}

//		if (l>= r) {
//			System.out.println(r + ", " + l);
//			System.out.println(data[r] + ", " + data[l] + ", " + key);
//		}
		
		if (l == r) {
			return l;
		} else {
			return r;
		}

//		return r;

	}

	public static void SimpleHistogramOptValidation(SimpleHistogramOpt hist, SimpleHistogram baseline) {

		double[] data = hist.getData();
		SHCell[] cells = baseline.getCells();

		boolean valid = true;
		for (int i = 0; i < data.length; i++) {
//			System.out.println(i + ": d=" + data[i] + ", bs=" + cells[i].getVal());
			if (data[i] != cells[i].getVal()) {
//				System.out.println("not valid! " + i + ": d=" + data[i] + ", bs=" + cells[i].getVal());
				valid = false;
			} 
//			else {
//				System.out.println("---valid" + i + ": d=" + data[i] + ", bs=" + cells[i].getVal());
//			}
		}
		
		System.out.println("valid = " + valid);

	}
	
	public static double SimpleHistogramOptEvaluation(SimpleHistogramOpt hist, SimpleHistogramOpt baseline) {

		double[] data = hist.getData();
		double[] gtData = baseline.getData();

		double relError = 0.0;

		for (int i = 0; i < data.length; i++) {

			if (gtData[i] == 0 && Math.abs(data[i]) > 1.1102230246251565E-10) {
				relError += Math.abs(data[i]);
			} else {
				if (Math.abs((gtData[i] - data[i])) > 1.1102230246251565E-10){
					relError += Math.abs((gtData[i] - data[i]) / gtData[i]);
				}
				
			}
//			System.out.println(gtData[i] + ", " + data[i]);

		}
		
		int cellNum = gtData.length;

//		System.out.println("----SimpleHistogramOpt Evaluation----");
//		System.out.println("cellNum = " + cellNum);

//		String evaluation = "total relative error = " + relError + ", avg relative error = " + (relError / cellNum);

//		System.out.println(evaluation);

//		System.out.println("--------");

		return (relError / cellNum);
	}

//	public static void SimpleSpatialHistogramOptValidation(SimpleSpatialHistogramOpt hist,
//			SimpleSpatialHistogram baseline) {
//
//		double[][] data = hist.getData();
//		SSH2DCell[][] cells = baseline.getCells();
//
//		boolean valid = true;
//		for (int i = 0; i < data.length; i++) {
//			for (int j = 0; j < data[0].length; j++) {
//				if (Math.abs(data[i][j] - cells[i][j].getVal()) > 1.1102230246251565E-10 ) {
//					System.out.println("not valid! " + i + ": d=" + data[i][j] + ", bs=" + cells[i][j].getVal());
//					valid = false;
//				} 
////				else {
////					System.out.println("--- " + i + ": d=" + data[i][j] + ", bs=" + cells[i][j].getVal());
////				} 
//			}
//		}
//		
//		System.out.println("valid = " + valid);
//
//	}
	
	public static void SimpleSpatialHistogramOptEvaluation(
			SimpleSpatialHistogramOpt gt, SimpleSpatialHistogramOpt hist) {

		double relError = 0.0;
		
		double[][] data = hist.getData();
		double[][] gtData = gt.getData();

		double totalGT = 0;
		double totalData = 0;
		
		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < data[0].length; j++) {
				System.out.println("data[i][j] = " + data[i][j] + ", gtData[i][j] = " 
						+ gtData[i][j] + ", relError = " + relError);
				totalGT += gtData[i][j];
				totalData += data[i][j];
				if (gtData[i][j] == 0 && Math.abs(data[i][j]) > 1.1102230246251565E-10) {
					relError += Math.abs(data[i][j]);
				} else if (gtData[i][j] != 0){
					if (Math.abs((gtData[i][j] - data[i][j])) > 1.1102230246251565E-10){
						relError += Math.abs((gtData[i][j] - data[i][j]) / gtData[i][j]);
					}
					
				}
			}
		}
		
		int cellNum = gt.numLonBucket * gt.numLatBucket;

		System.out.println("----SimpleSpatialHistogramOpt Evaluation----");
		System.out.println("cellNum = " + cellNum);

		System.out.println("totalGT = " + totalGT + ", totalData = " + totalData);
		String evaluation = "total relative error = " + relError + ", avg relative error = " + (relError / cellNum);

		System.out.println(evaluation);

		System.out.println("--------");

	}
	
	public static void SSHEvaluationByCells(
			SSH2DCell[][] gtCells, SimpleSpatialHistogramOpt hist) {

		double relError = 0.0;
		
		double[][] data = hist.getData();
//		double[][] gtData = gt.getData();

		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < data[0].length; j++) {
				SSH2DCell gtCell = gtCells[i][j];
				double geCellVal = gtCell.getVal();
				if (geCellVal == 0 && Math.abs(data[i][j]) > 1.1102230246251565E-10) {
					relError += Math.abs(data[i][j]);
				} else {
					if (Math.abs((geCellVal - data[i][j])) > 1.1102230246251565E-10){
						relError += Math.abs((geCellVal - data[i][j]) / geCellVal);
					}
					
				}
			}
		}
		
		int cellNum = hist.numLonBucket * hist.numLatBucket;

		System.out.println("----SimpleSpatialHistogramOpt Evaluation----");
//		System.out.println("cellNum = " + cellNum);

		String evaluation = "total relative error = " + relError + ", avg relative error = " + (relError / cellNum);

		System.out.println(evaluation);

		System.out.println("--------");

	}

}
