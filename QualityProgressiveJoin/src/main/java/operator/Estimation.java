package operator;

import java.util.ArrayList;
import java.util.Comparator;

import dataStructure.Tuple3;

public class Estimation {

	// E_i = E_join
	public static EstimationResult EstimationWithoutSelectivity(int numPar, int[] curResultSize,
			double[][] processRatio, double errorBound) {
		EstimationResult estimationResult = new EstimationResult(numPar);

		// E_join = curResultSize * ratio
		// E_join / totalDataSize = curResultSize / totalDataSize
		ArrayList<Tuple3<Integer, Integer, Double>> outputRatioList = new ArrayList<Tuple3<Integer, Integer, Double>>();
		int totalEstimation = 0;
		for (int i = 0; i < numPar; i++) {
			int estimation = 0;
			if (processRatio[0][i] != 0) {
				estimation = (int) (curResultSize[i] * processRatio[0][i] * processRatio[1][i]);
				// output ratio
				double outputRatio = curResultSize[i] / (double) estimation;
				estimationResult.addEstimationAndnRatio(i, estimation, outputRatio);
				totalEstimation += estimation;
				outputRatioList.add(new Tuple3<Integer, Integer, Double>(i, estimation, outputRatio));
			}
		}
		outputRatioList.sort(new Comparator<Tuple3<Integer, Integer, Double>>() {

			@Override
			public int compare(Tuple3<Integer, Integer, Double> o1, Tuple3<Integer, Integer, Double> o2) {
				return Double.compare(o1.getValue2(), o2.getValue2());
			}
		});

		double minOutputRatio = outputRatioList.get(0).getValue2();
		double tmpOutputRatio;
		double error = 0;

		// get the optimal progressive progress
		double optimalProgress = 0;
		for (int i = 1; i < outputRatioList.size(); i++) {
			// Attempt to update the minOutputRatio
			tmpOutputRatio = outputRatioList.get(i).getValue2();
			// compute error for the partitions which cannot output enough results
			for (int j = 0; j < i; j++) {
				// error = (hat{d__o_ri} - d_o_ri) / hat{d__o_ri}
				error += (outputRatioList.get(j).getValue1() * tmpOutputRatio
						- outputRatioList.get(j).getValue1() * outputRatioList.get(j).getValue2())
						/ (totalEstimation * tmpOutputRatio);
			}
			error = error / numPar;
			if (error < errorBound) {
				minOutputRatio = tmpOutputRatio;
//				System.out.println("error = " + error + " smaller than error bound " + errorBound);
			} else {
//				System.out.println("error = " + error + " larger than error bound " + errorBound);
				// get the optimal progressive progress
				// check the sum of error
				error -= (outputRatioList.get(i - 1).getValue1() * tmpOutputRatio
						- outputRatioList.get(i - 1).getValue1() * outputRatioList.get(i - 1).getValue2())
						/ (totalEstimation * tmpOutputRatio);
				if (error == 0) {
					optimalProgress = minOutputRatio;
				} else {

					for (int j = 0; j < (i - 1); j++) {
						optimalProgress += outputRatioList.get(j).getValue2();
					}

					optimalProgress = optimalProgress / (double) (i - 1 - numPar * errorBound);
				}
				break;
			}
		}

		if (optimalProgress == 0) {
			for (int j = 0; j < numPar; j++) {
				optimalProgress += outputRatioList.get(j).getValue2();
			}

			optimalProgress = optimalProgress / (double) (numPar - numPar * errorBound);
		}

//		System.out.println("optimal output progressive progress = " + optimalProgress);
		estimationResult.setMinOutputRatio(minOutputRatio);

		return estimationResult;

	}

	// E_i = i/r * E_join + (r-i)/r * E_selecvitity
	public static EstimationResult EstimationWithSelectivity(int numPar, int[] curResultSize, double[][] processRatio,
			double errorBound, int curBId, int totalBatch,  int[] selectiviey) {
		EstimationResult estimationResult = new EstimationResult(numPar);

		// joinEst = E_join = curResultSize * ratio
		// E_join / totalDataSize = curResultSize / totalDataSize
		ArrayList<Tuple3<Integer, Integer, Double>> outputRatioList = new ArrayList<Tuple3<Integer, Integer, Double>>();
		int totalEstimation = 0;
		for (int i = 0; i < numPar; i++) {
			double joinEst = curResultSize[i] * processRatio[0][i] * processRatio[1][i];
			int estimation = (int) (curBId / (double) totalBatch * joinEst
					+ (totalBatch - curBId) / (double) totalBatch * selectiviey[i]);
			// output ratio
			double outputRatio = curResultSize[i] / (double) estimation;
			estimationResult.addEstimationAndnRatio(i, estimation, outputRatio);
			totalEstimation += estimation;
			outputRatioList.add(new Tuple3<Integer, Integer, Double>(i, estimation, outputRatio));
			
			totalEstimation += estimation;
			outputRatioList.add(new Tuple3<Integer, Integer, Double>(i, estimation, outputRatio));
		}
		
		outputRatioList.sort(new Comparator<Tuple3<Integer, Integer, Double>>() {

			@Override
			public int compare(Tuple3<Integer, Integer, Double> o1, Tuple3<Integer, Integer, Double> o2) {
				return Double.compare(o1.getValue2(), o2.getValue2());
			}
		});

		double minOutputRatio = outputRatioList.get(0).getValue2();
		double tmpOutputRatio;
		double error = 0;

		// get the optimal progressive progress
		double optimalProgress = 0;
		for (int i = 1; i < outputRatioList.size(); i++) {
			// Attempt to update the minOutputRatio
			tmpOutputRatio = outputRatioList.get(i).getValue2();
			// compute error for the partitions which cannot output enough results
			for (int j = 0; j < i; j++) {
				// error = (hat{d__o_ri} - d_o_ri) / hat{d__o_ri}
				error += (outputRatioList.get(j).getValue1() * tmpOutputRatio
						- outputRatioList.get(j).getValue1() * outputRatioList.get(j).getValue2())
						/ (totalEstimation * tmpOutputRatio);
			}
			error = error / numPar;
			if (error < errorBound) {
				minOutputRatio = tmpOutputRatio;
//				System.out.println("error = " + error + " smaller than error bound " + errorBound);
			} else {
//				System.out.println("error = " + error + " larger than error bound " + errorBound);
				// get the optimal progressive progress
				// check the sum of error
				error -= (outputRatioList.get(i - 1).getValue1() * tmpOutputRatio
						- outputRatioList.get(i - 1).getValue1() * outputRatioList.get(i - 1).getValue2())
						/ (totalEstimation * tmpOutputRatio);
				if (error == 0) {
					optimalProgress = minOutputRatio;
				} else {

					for (int j = 0; j < (i - 1); j++) {
						optimalProgress += outputRatioList.get(j).getValue2();
					}

					optimalProgress = optimalProgress / (double) (i - 1 - numPar * errorBound);
				}
				break;
			}
		}

		if (optimalProgress == 0) {
			for (int j = 0; j < numPar; j++) {
				optimalProgress += outputRatioList.get(j).getValue2();
			}

			optimalProgress = optimalProgress / (double) (numPar - numPar * errorBound);
		}

//		System.out.println("optimal output progressive progress = " + optimalProgress);
		estimationResult.setMinOutputRatio(minOutputRatio);

		return estimationResult;
	}
}
