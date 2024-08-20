package operator;

import java.util.ArrayList;

public class OutputScore {

	// compute output score for single partition
	public static double OutputScoreSinglePartition(double[] inputWeights, double[] numOfInputs, double outputWeight,
			double numOfOutput) {
		double score = outputWeight * numOfOutput;

		for (int i = 0; i < inputWeights.length; i++) {
			score += inputWeights[i] * numOfInputs[i];
		}

		return score;
	}

	// Given # of output of every partitions,
	// compute weights for them
	public static double[] OutputWeightsComputation(double totalNumResult, int numOfPar, double[] numOfItems) {
		double[] weights = new double[numOfPar];

		for (int i = 0; i < numOfPar; i++) {
			weights[i] = numOfItems[i] / totalNumResult;
		}

		return weights;
	}

	public static ArrayList<Double> OutputWeightsListComputation(double totalNumResult, int numOfPar,
			double[] numOfItems) {
		ArrayList<Double> weights = new ArrayList<Double>();

		for (int i = 0; i < numOfPar; i++) {
			weights.add(numOfItems[i] / totalNumResult);
		}

		return weights;
	}

	public static ArrayList<Double> OutputWeightsListComputation(double totalNumResult, int numOfPar,
			ArrayList<Integer> numOfItems) {
		ArrayList<Double> weights = new ArrayList<Double>();

		for (int i = 0; i < numOfPar; i++) {
			weights.add(numOfItems.get(i) / totalNumResult);
		}

		return weights;
	}

	// Given # of output of every partitions,
	// compute weights for them
	public static double[] OutputWeightsComputation(double[] numOfItems) {
		int numOfPar = numOfItems.length;
		double totalNumResult = 0.0;
		for (int i = 0; i < numOfPar; i++) {
			totalNumResult += numOfItems[i];
		}
		return OutputWeightsComputation(totalNumResult, numOfPar, numOfItems);
	}

	// Given # of output of every partitions,
	// compute weights for them
	public static ArrayList<Double> OutputWeightsComputation(ArrayList<Integer> numOfItems) {
		int numOfPar = numOfItems.size();
		double totalNumResult = 0.0;
		for (int i = 0; i < numOfPar; i++) {
			totalNumResult += numOfItems.get(i);
		}
		return OutputWeightsListComputation(totalNumResult, numOfPar, numOfItems);
	}

	// Input weights computation
	public static ArrayList<ArrayList<Double>> OutputWeightsComputation(double[] totalNumResult, int[] numOfPar,
			double[][] numOfItems) {

		ArrayList<ArrayList<Double>> weights = new ArrayList<ArrayList<Double>>();

		for (int bigParId = 0; bigParId < totalNumResult.length; bigParId++) {
			ArrayList<Double> weightsOfBigPartition = OutputWeightsListComputation(totalNumResult[bigParId],
					numOfPar[bigParId], numOfItems[bigParId]);

			weights.add(weightsOfBigPartition);
		}

		return weights;

	}

}
