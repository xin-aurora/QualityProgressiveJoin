package sampling;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;

public class ScoreExp {
	
	public static void UniformTest(String folderPath) throws Exception {
		int numOfBigPartition = 10;
		int numOfSmallPartition = 10;
		
		int[][] baseResult = new int[numOfBigPartition][numOfSmallPartition];
		int[] numOfOutputBigPartitionPre = new int[numOfBigPartition];
		
		int[][] groundTruth = new int[numOfBigPartition][numOfSmallPartition];
		

		BufferedReader[] readers = new BufferedReader[numOfBigPartition];
		String outputFile = folderPath + "outputSize.txt";
		File fileOutput = new File(outputFile);
		BufferedReader readerOutput = new BufferedReader(new FileReader(fileOutput));
		
		String gtFilePath = folderPath + "gt.txt";
		BufferedReader gtFile = new BufferedReader(new FileReader(gtFilePath));
		String gtLine = gtFile.readLine();
		int parId = 0;
		int[] totals = new int[numOfBigPartition];
		while (gtLine != null) {
			String[] tmp = gtLine.split(",");
			int total = 0;
			for (int i=0; i<tmp.length; i++) {
				int frequency = Integer.parseInt(tmp[i].trim());
				groundTruth[parId][i] = frequency;
				total += frequency;
			}
//			System.out.println("parId = " + parId + ", total = " + total);
			totals[parId] = total;
			parId++;
			gtLine = gtFile.readLine();
		}
		gtFile.close();
//		System.out.println();
		double[][] GTDist = ComputeDist(totals, groundTruth);

		for (int i = 0; i < numOfBigPartition; i++) {
			String freFile = folderPath + "freP" + i + ".txt";

			File file = new File(freFile);

			BufferedReader reader = new BufferedReader(new FileReader(file));
			readers[i] = reader;

		}

		for (int batch = 14; batch < 30; batch++) {
			int[] numOfOutputBigPartition = new int[numOfBigPartition];
			readerOutput.readLine();
			String outputSizeLine = readerOutput.readLine();
			String[] sizeTmp = outputSizeLine.replace("[", "").replace("]", "").split(",");
			for (int bigPId = 0; bigPId < sizeTmp.length; bigPId++) {
				numOfOutputBigPartition[bigPId] = Integer.parseInt(sizeTmp[bigPId].trim());
			}
			
			
//			int[][] groundTruth = new int[numOfBigPartition][numOfSmallPartition];
			int[][] resultFound = new int[numOfBigPartition][numOfSmallPartition];
			int[] total = new int[numOfBigPartition];

			for (int i = 0; i < numOfBigPartition; i++) {
				BufferedReader reader = readers[i];
				reader.readLine();
				String ds1Line = reader.readLine();
				String ds2Line = reader.readLine();
				String resultLine = reader.readLine();
				String resultTotalLine = reader.readLine();
				String[] resultTmp = resultLine.replace("[", "").replace("]", "").split(",");
//				String[] resultTotalTmp = resultTotalLine.replace("[", "").replace("]", "").split(",");
				int t = 0;
				for (int smallId = 0; smallId < resultTmp.length; smallId++) {
					int numOfR = Integer.parseInt(resultTmp[smallId].trim());
//					int numOfTotal = Integer.parseInt(resultTotalTmp[smallId].trim());
					resultFound[i][smallId] = numOfR;
//					groundTruth[i][smallId] = numOfTotal;
					t += numOfR;
				}
				total[i] = t;

			}

			UniformSampling model = new UniformSampling(numOfOutputBigPartition, resultFound, numOfOutputBigPartitionPre);

			for (int bigPId = 0; bigPId < sizeTmp.length; bigPId++) {
				numOfOutputBigPartitionPre[bigPId] = numOfOutputBigPartition[bigPId];
			}
			
			int[][] samples = model.sampling(baseResult);
//			double[][] GTDist = ComputeDist(total, groundTruth);
			double[][] sampleDist = ComputeDist(total, samples);
			for (int i=0; i<numOfBigPartition; i++) {
				System.out.println(Arrays.toString(sampleDist[i]));
			}
			System.out.println();
			
			for (int i=0; i<GTDist.length; i++) {
				System.out.println(Arrays.toString(GTDist[i]));
			}
			System.out.println();
			
//			for (int i = 0; i < numOfBigPartition; i++) {
//				System.out.println(Arrays.toString(samples[i]));
//				int total = 0;
//				for (int smallId = 0; smallId < numOfSmallPartition; smallId++) {
//					total += samples[i][smallId];
//				}
//				System.out.println(total + ": " + Arrays.toString(samples[i]));
//			}
//			System.out.println();
			System.out.println("batch-" + batch + ", score = " + ComputeMAPE(GTDist, sampleDist));
			System.out.println();
		}
	}
	
	public static void WeightTest(String folderPath) throws Exception {
		int numOfBigPartition = 10;
		int numOfSmallPartition = 10;
		
		int[][] baseResult = new int[numOfBigPartition][numOfSmallPartition];
		int[] numOfOutputBigPartitionPre = new int[numOfBigPartition];

		BufferedReader[] readers = new BufferedReader[numOfBigPartition];
		String outputFile = folderPath + "outputSize.txt";
		File fileOutput = new File(outputFile);
		BufferedReader readerOutput = new BufferedReader(new FileReader(fileOutput));

		for (int i = 0; i < numOfBigPartition; i++) {
			String freFile = folderPath + "freP" + i + ".txt";

			File file = new File(freFile);

			BufferedReader reader = new BufferedReader(new FileReader(file));
			readers[i] = reader;

		}

		for (int batch = 0; batch < 30; batch++) {
			int[] numOfOutputBigPartition = new int[numOfBigPartition];
			readerOutput.readLine();
			String outputSizeLine = readerOutput.readLine();
			String[] sizeTmp = outputSizeLine.replace("[", "").replace("]", "").split(",");
			for (int bigPId = 0; bigPId < sizeTmp.length; bigPId++) {
				numOfOutputBigPartition[bigPId] = Integer.parseInt(sizeTmp[bigPId].trim());
			}
			
			
			int[][] groundTruth = new int[numOfBigPartition][numOfSmallPartition];
			int[][] resultFound = new int[numOfBigPartition][numOfSmallPartition];
			int[] total = new int[numOfBigPartition];

			for (int i = 0; i < numOfBigPartition; i++) {
				BufferedReader reader = readers[i];
				reader.readLine();
				String ds1Line = reader.readLine();
				String ds2Line = reader.readLine();
				String resultLine = reader.readLine();
				String resultTotalLine = reader.readLine();
				String[] resultTmp = resultLine.replace("[", "").replace("]", "").split(",");
				String[] resultTotalTmp = resultTotalLine.replace("[", "").replace("]", "").split(",");
				int t = 0;
				for (int smallId = 0; smallId < resultTmp.length; smallId++) {
					int numOfR = Integer.parseInt(resultTmp[smallId].trim());
					int numOfTotal = Integer.parseInt(resultTotalTmp[smallId].trim());
					resultFound[i][smallId] = numOfR;
					groundTruth[i][smallId] = numOfTotal;
					t += numOfR;
				}
				total[i] = t;
			}
//			System.out.println("batch " + batch);
//			for (int i = 0; i < numOfBigPartition; i++) {
//				System.out.println(Arrays.toString(resultFound[i]));
//			}
			// int[] numOfOutputBigPartition, int[][] numOfFound, int[] foundBefore,
//			int[][] numOfOutputBigPartitionTotal
			WeightedSampling model = new WeightedSampling(numOfOutputBigPartition, resultFound, numOfOutputBigPartitionPre,
					groundTruth);

			for (int bigPId = 0; bigPId < sizeTmp.length; bigPId++) {
				numOfOutputBigPartitionPre[bigPId] = numOfOutputBigPartition[bigPId];
			}
			
			int[][] samples = model.sampling(baseResult);
			
			double[][] GTDist = ComputeDist(total, groundTruth);
			double[][] sampleDist = ComputeDist(total, samples);
			
//			for (int i = 0; i < numOfBigPartition; i++) {
////				System.out.println(Arrays.toString(samples[i]));
//				int total = 0;
//				for (int smallId = 0; smallId < numOfSmallPartition; smallId++) {
//					total += samples[i][smallId];
//				}
//				System.out.println(total + ": " + Arrays.toString(samples[i]));
//			}
//			System.out.println();
			System.out.println("batch-" + batch + ", score = " + ComputeMAPE(GTDist, sampleDist));
//			System.out.println();
		}
	}
	
	public static double[][] ComputeDist(int[] sum, int[][] data) {
		double[][] distribution = new double[data.length][data[0].length];
		
		for (int i=0; i<data.length; i++) {
			for (int j=0; j<data[0].length; j++) {
				distribution[i][j] = data[i][j] / (double)sum[i];
			}
		}
		return distribution;
	}
	
	public static double ComputeMAPE(double[][] groundTruth, double[][] results) {
		double score = 0.0;
		
		for (int bigParId = 0; bigParId < groundTruth.length; bigParId++) {
			for (int smallParId = 0; smallParId < groundTruth[0].length; smallParId++) {
				if (groundTruth[bigParId][smallParId] > 0) {
					score += Math.abs(groundTruth[bigParId][smallParId] - results[bigParId][smallParId])
							/ groundTruth[bigParId][smallParId];
				} else {
					score += results[bigParId][smallParId];
				}
			}
		}

		int total = groundTruth.length * groundTruth[0].length;
		for (int bigParId = 0; bigParId < groundTruth.length; bigParId++) {
			for (int smallParId = 0; smallParId < groundTruth[0].length; smallParId++) {
				if (groundTruth[bigParId][smallParId] > 0) {
					score += Math.abs(groundTruth[bigParId][smallParId] - results[bigParId][smallParId])
							/ (double) groundTruth[bigParId][smallParId];
				} else {
					score += results[bigParId][smallParId];
				}
			}
		}
//		System.out.println();

		score = score / total * 100;

		return score;
	}

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		String folderPath = "/Users/xin_aurora/Downloads/Work/2019/UCR/"
				+ "Research/Spatial/Progressive_Join/ssdbm/10/";
//		String folderPath = "/Users/xin_aurora/Downloads/Work/2019/UCR/"
//				+ "Research/Spatial/Progressive_Join/ssdbm/spatial/round30/6/";
		UniformTest(folderPath);
//		WeightTest(folderPath);
	}

}
