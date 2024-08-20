package motivation;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

import dataStructure.SSH2DCell;
//import estimation.histogram.SimpleSpatialHistogram;
import io.LoadHistogram;
import optHistogram.SimpleHistogramOpt;
import optHistogram.SimpleSpatialHistogram;
import utils.UtilsFunctionHistogram;

public class MotivationExp {

	public static void ReshapingSingle(String filePath) {

		LoadHistogram load = new LoadHistogram();

		SimpleHistogramOpt hisOpt = load.loadSimpleHistogramOpt(filePath, 100);

		double[] loadBoundary = hisOpt.getBoundary();

		double unit = 1;

		System.out.println("sd = " + hisOpt.computeStandardDeviation());
		System.out.println(Arrays.toString(loadBoundary));
		double[] startB = new double[11];
		double[] errors = new double[11];

		for (int i = 1; i < 11; i++) {
			double[] newBoundary = new double[loadBoundary.length + 1];
			newBoundary[0] = loadBoundary[0];
			for (int b = 0; b < loadBoundary.length; b++) {
				newBoundary[b + 1] = loadBoundary[b] + i * unit;
			}
//			System.out.println("i = " + i + ", boundary = " + Arrays.toString(newBoundary));
			SimpleHistogramOpt reshapeHist = new SimpleHistogramOpt(newBoundary);
			reshapeHist.aggregateHistogram(hisOpt);

//			System.out.println(Arrays.toString(reshapeHist.getData()));
//			System.out.println(Arrays.toString(reshapeHist.getBoundary()));

//			System.out.println("sd = " + reshapeHist.computeStandardDeviation());

			SimpleHistogramOpt reshapeBS = new SimpleHistogramOpt(newBoundary);
			reshapeBS = load.loadSimpleHistogramOptBysHist(filePath, reshapeBS);

			startB[i] = newBoundary[1];
//			System.out.println(newBoundary[1]);
			
			if (i == 1) {
				drawHistogram(reshapeHist);
				drawHistogram(reshapeBS);
			}
			
			if (i == 5) {
				drawHistogram(reshapeHist);
				drawHistogram(reshapeBS);
			}

			errors[i] = UtilsFunctionHistogram.SimpleHistogramOptEvaluation(reshapeHist, reshapeBS);
		}

		System.out.println(Arrays.toString(startB));
		System.out.println(Arrays.toString(errors));
	}

	public static void AggreExp(String bit03Path, String bit045Path) {

		LoadHistogram load = new LoadHistogram();

		SimpleHistogramOpt his03 = load.loadSimpleHistogramOpt(bit03Path, 10);
		
		drawHistogram(his03);

		SimpleHistogramOpt hist045 = load.loadSimpleHistogramOpt(bit045Path, 10);
		
		drawHistogram(hist045);

		double min = 1000.0;
		double max = 2049;
		double unit = (max - min) / 10;

		double[] error = new double[6];
		// different start point
		// 1
		double[] b03 = new double[11];
		for (int i = 0; i < 11; i++) {
			b03[i] = min + unit * i;
		}

		SimpleHistogramOpt hist03Agg = new SimpleHistogramOpt(b03);
		hist03Agg.aggregateHistogram(his03);
		hist03Agg.aggregateHistogram(hist045);
		
//		drawHistogram(hist03Agg);

		SimpleHistogramOpt hist03BS = new SimpleHistogramOpt(b03);
		hist03BS = load.loadSimpleHistogramOptBysHist(bit03Path, hist03BS);
		hist03BS = load.loadSimpleHistogramOptBysHist(bit045Path, hist03BS);
//		error[0] = UtilsFunctionHistogram.SimpleHistogramOptEvaluation(hist03Agg, hist03BS);

//		drawHistogram(hist03BS);
		
		// 2
		double[] b03H = new double[11];
		min = 1025.0;
		for (int i = 0; i < 11; i++) {
			b03H[i] = min + unit * i;
		}
		SimpleHistogramOpt hist03HAgg = new SimpleHistogramOpt(b03H);
		hist03HAgg.aggregateHistogram(his03);
		hist03HAgg.aggregateHistogram(hist045);
		
//		drawHistogram(hist03HAgg);

		SimpleHistogramOpt hist03HBS = new SimpleHistogramOpt(b03H);
		hist03HBS = load.loadSimpleHistogramOptBysHist(bit03Path, hist03HBS);
		hist03HBS = load.loadSimpleHistogramOptBysHist(bit045Path, hist03HBS);
//		drawHistogram(hist03HBS);
//		error[1] = UtilsFunctionHistogram.SimpleHistogramOptEvaluation(hist03HAgg, hist03HBS);
//
//		// 3
//		double[] b045 = new double[11];
//		min = 1050.0;
//		for (int i = 0; i < 11; i++) {
//			b045[i] = min + unit * i;
//		}
//		SimpleHistogramOpt hist045Agg = new SimpleHistogramOpt(b045);
//		hist045Agg.aggregateHistogram(his03);
//		hist045Agg.aggregateHistogram(hist045);
//
//		SimpleHistogramOpt hist045BS = new SimpleHistogramOpt(b045);
//		hist045BS = load.loadSimpleHistogramOptBysHist(bit03Path, hist045BS);
//		hist045BS = load.loadSimpleHistogramOptBysHist(bit045Path, hist045BS);
//		error[2] = UtilsFunctionHistogram.SimpleHistogramOptEvaluation(hist045Agg, hist045BS);
//		// 4
//		double[] b045H = new double[11];
//		min = 1075.0;
//		for (int i = 0; i < 11; i++) {
//			b045H[i] = min + unit * i;
//		}
//		SimpleHistogramOpt hist045HAgg = new SimpleHistogramOpt(b045H);
//		hist045HAgg.aggregateHistogram(his03);
//		hist045HAgg.aggregateHistogram(hist045);
//
//		SimpleHistogramOpt hist045HBS = new SimpleHistogramOpt(b045H);
//		hist045HBS = load.loadSimpleHistogramOptBysHist(bit03Path, hist045HBS);
//		hist045HBS = load.loadSimpleHistogramOptBysHist(bit045Path, hist045HBS);
//		error[3] = UtilsFunctionHistogram.SimpleHistogramOptEvaluation(hist045HAgg, hist045HBS);
//		// 5
		double[] b031 = new double[11];
		min = 1100.0;
		for (int i = 0; i < 11; i++) {
			b031[i] = min + unit * i;
		}
		SimpleHistogramOpt hist031Agg = new SimpleHistogramOpt(b031);
		hist031Agg.aggregateHistogram(his03);
		hist031Agg.aggregateHistogram(hist045);
//		drawHistogram(hist031Agg);
		SimpleHistogramOpt hist031BS = new SimpleHistogramOpt(b031);
		hist031BS = load.loadSimpleHistogramOptBysHist(bit03Path, hist031BS);
		hist031BS = load.loadSimpleHistogramOptBysHist(bit045Path, hist031BS);
//		drawHistogram(hist031BS);
//		error[4] = UtilsFunctionHistogram.SimpleHistogramOptEvaluation(hist031Agg, hist031BS);
//		// 6
//		double[] b0451 = new double[11];
//		min = 1150.0;
//		for (int i = 0; i < 11; i++) {
//			b0451[i] = min + unit * i;
//		}
//		SimpleHistogramOpt hist0451Agg = new SimpleHistogramOpt(b0451);
//		hist0451Agg.aggregateHistogram(his03);
//		hist0451Agg.aggregateHistogram(hist045);
//
//		SimpleHistogramOpt hist0451BS = new SimpleHistogramOpt(b0451);
//		hist0451BS = load.loadSimpleHistogramOptBysHist(bit03Path, hist0451BS);
//		hist0451BS = load.loadSimpleHistogramOptBysHist(bit045Path, hist0451BS);
//		error[5] = UtilsFunctionHistogram.SimpleHistogramOptEvaluation(hist0451Agg, hist0451BS);
//
//		System.out.println(Arrays.toString(error));
	}

	public static void drawHistogram(SimpleHistogramOpt hist) {
		double[] boundary = hist.getBoundary();
		double[] data = hist.getData();

//		System.out.println(Arrays.toString(boundary));
		for (int i=0; i<3; i++) {
			System.out.println((int)boundary[i] + "-" + (int)boundary[i+1]);
		}
		System.out.println(Arrays.toString(data));
		System.out.println();
	}
	
	public static void drawSpatialHistogram(SimpleSpatialHistogram hist) {
		SSH2DCell[][] cells = hist.getCells();
		
		for (int i=0; i<3; i++) {
			System.out.println(String.format("%.2f",cells[4][i].minLat)
					 + "-" + String.format("%.2f",cells[4][i].maxLat));
		}
		for (int i=0; i<3; i++) {
			System.out.println(String.format("%.2f",cells[4][i].minLon)
					 + "-" + String.format("%.2f",cells[4][i].maxLon));
		}
		for (int i=0; i<3; i++) {
			System.out.println(cells[4][i].getVal());
		}
	}

	public static void main(String[] args) {

		String folder = "/Users/xin_aurora/Downloads/Work/2019/UCR/Research/Spatial/sketches/data/medium/point/";
		String bit04Path = folder + "1d/bit04mo.csv";
		String bit02Path = folder + "1d/bit02mo.csv";

//		ReshapingSingle(bit04Path);

		AggreExp(bit02Path, bit04Path);
	}

}
