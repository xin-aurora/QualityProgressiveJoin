package sampling;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;

public class UniformSampling {

	int[] numOfOutputBigPartition;
	int[][] numOfFound;
	int[] sampleBound;

	public UniformSampling(int[] numOfOutputBigPartition, int[][] numOfFound, int[] foundBefore) {
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
		Random random = new Random();
		for (int bigPId = 0; bigPId < numOfFound.length; bigPId++) {
			int totalNum = sampleBound[bigPId];
//			System.out.println("total of totalNum = " + totalNum);
			int current = 0;
			HashSet<Integer> emptySet = new HashSet<Integer>();
			while (current < totalNum) {

				for (int smallPId = 0; smallPId < numOfFound[0].length; smallPId++) {
					if (!emptySet.contains(smallPId)) {
						int range = totalNum - current + 1;
						int sample = random.nextInt(range);
//						System.out.println("range = " + range);
						if ((numOfFound[bigPId][smallPId] - sample) > 0) {
							numOfFound[bigPId][smallPId] -= sample;
							current += sample;
							baseRecord[bigPId][smallPId] += sample;
						} else {
							int sampleNew = numOfFound[bigPId][smallPId];
							numOfFound[bigPId][smallPId] = 0;
							emptySet.add(smallPId);
							current += sampleNew;
							baseRecord[bigPId][smallPId] += sampleNew;
						}
					}
					if (emptySet.size() == numOfFound[0].length) {
						break;
					}

				}
//				System.out.println(Arrays.toString(baseRecord[bigPId]));
//				System.out.println("current drop = " + current);
				if (emptySet.size() == numOfFound[0].length) {
					break;
				}
			}
		}
		
//		for (int bigPId = 0; bigPId < numOfFound.length; bigPId++) {
//			for (int smallPId = 0; smallPId < numOfFound[0].length; smallPId++) {
//				numOfFound[bigPId][smallPId] += baseRecord[bigPId][smallPId];
//			}
//		}

		return baseRecord;

	}
	
}
