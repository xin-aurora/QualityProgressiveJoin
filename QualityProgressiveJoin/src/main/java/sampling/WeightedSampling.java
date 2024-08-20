package sampling;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

public class WeightedSampling {
	int[][] numOfOutputBigPartitionTotal;
	int[] numOfOutputBigPartition;
	int[][] numOfFound;
	int[] sampleBound;

	public WeightedSampling(int[] numOfOutputBigPartition, int[][] numOfFound, int[] foundBefore,
			int[][] numOfOutputBigPartitionTotal) {
		this.numOfOutputBigPartitionTotal = numOfOutputBigPartitionTotal;
		this.numOfOutputBigPartition = numOfOutputBigPartition;
//		this.foundStr = foundStr;
		this.numOfFound = numOfFound;

		sampleBound = new int[numOfOutputBigPartition.length];
		for (int bigPId = 0; bigPId < numOfOutputBigPartition.length; bigPId++) {
			sampleBound[bigPId] = numOfOutputBigPartition[bigPId] - foundBefore[bigPId];
		}

//		System.out.println("sample size = " + Arrays.toString(sampleBound));
	}

	public int[][] sampling(int[][] baseRecord) {

		// build sampling model
		// build weighted sampling model

		for (int bigPId = 0; bigPId < numOfFound.length; bigPId++) {
			ArrayList<Integer> weightedSamplingModel = buildReverseSamplingModel(bigPId,
					numOfOutputBigPartitionTotal[bigPId]);
//			int max = weightedSamplingModel.size();
			Random random = new Random();
			int totalNum = sampleBound[bigPId];
//			System.out.println("total of totalNum = " + totalNum);
			int current = 0;
			HashSet<Integer> emptySet = new HashSet<Integer>();
			while (current < totalNum) {
				int pick = random.nextInt(weightedSamplingModel.size());
				int pickId = weightedSamplingModel.get(pick);
				weightedSamplingModel.remove(pick);
				if (numOfFound[bigPId][pickId] > 0) {
					baseRecord[bigPId][pickId] += 1;
					numOfFound[bigPId][pickId] -= 1;
					current++;
					
				} else {
					emptySet.add(pickId);
				}
				if (emptySet.size() == numOfFound[0].length) {
					break;
				}
			}
			if (emptySet.size() == numOfFound[0].length) {
				break;
			}
		}

		return baseRecord;
	}

	private ArrayList<Integer> buildReverseSamplingModel(int bigPId, int[] numOfFoundInBigPartition) {
		int numOfSmallPartition = numOfFoundInBigPartition.length;
		// build reverse weight
		ArrayList<Integer> weightedSamplingModel = new ArrayList<Integer>();

		for (int smallPId = 0; smallPId < numOfSmallPartition; smallPId++) {
			int count = numOfFoundInBigPartition[smallPId];
			int cnt = 0;
			while (cnt < count) {
				weightedSamplingModel.add(smallPId);
				cnt++;
			}
		}

		return weightedSamplingModel;
	}
}
