package framework;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import dataStructure.DoubleTuple4;
import dataStructure.StringTuple2;
import io.CSVReader;
import operator.Estimation;
import operator.EstimationResult;
import partition.GridPartition;
import partition.HashPartition;
import partition.MultiHashPartition;
import utils.Configuration;
import utils.UtilsFunction;

public class Driver {

	// progressive control
	// by default is 100, each round returns 1%
	private int numBatch = 100;
	// configuration
	Configuration conf;

	// worker control
	private Worker[] workers;
	private MachineInfo[] machinesInfo;
	private boolean[] readDataWorker;

	// result
	private EstimationResult estimationResult;
	private int[][] partitionSize;
	private int[] curResultSize;
	private int[] selectivityEstimation;
	
	String statPath = "";
	int[] outputSizePre;

	public Driver() {

	}

	public Driver(int numBatch) {
		this.numBatch = numBatch;
	}

	public void run(Configuration conf) {
		this.conf = conf;
		System.out.println("Driver");
		System.out.println("LOG INFO: operator is " + conf.getOperatorName() + ", num partition = "
				+ conf.getNumPartition() + ", number of split = " + this.numBatch);
		System.out.println();

		FileWriter myWriter = null;
		if (conf.doEvaluation) {
			String logName = "./" + conf.getOperatorName() + "-" + conf.getSplitMethod() + "-P" + conf.getNumPartition()
					+ "-B" + numBatch + "-E" + conf.getErrorBound() + ".csv";
			try {
				myWriter = new FileWriter(logName, false);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// 0: initial parameters
		this.curResultSize = new int[conf.getNumPartition()];
		this.estimationResult = new EstimationResult(conf.getNumPartition());
		this.selectivityEstimation = new int[conf.getNumPartition()];
		this.readDataWorker = new boolean[conf.getNumPartition()];

		// 1: partition the data based on the partitionMethod
		int[][] fileTotalLines = null;
		if (conf.getPartitionMethod().toLowerCase().equals("hashpartition")) {
			if (conf.getDoLowPar()) {
				MultiHashPartition partition = CSVReader.readMultiHashPartitionKey(conf.getDatasetPaths(), 
						conf.getNumPartition(), conf.getNumLowPar(),
						conf.getKeyIdxs(), conf.getRegex());
				int[][] multiSelecEst = partition.getSelectivityEstimaiton();
				System.out.println("Multi-Level selectivity estimation: ");
				for (int parId = 0; parId < conf.getNumPartition(); parId++) {
					System.out.println("parId = " + Arrays.toString(multiSelecEst[parId]));
				}
				System.out.println("------");
			}
			
			// hash partition
			HashPartition partition = CSVReader.readHashPartitionKey(conf.getDatasetPaths(), conf.getNumPartition(),
					conf.getKeyIdxs(), conf.getRegex());
			fileTotalLines = partition.getFileTotalLines();
			partitionSize = partition.getPartitionFrequency();
			selectivityEstimation = partition.getSelectivityEstimaiton();
			System.out.println("selectivityEstimation = " + Arrays.toString(selectivityEstimation));
			
		} else if (conf.getPartitionMethod().toLowerCase().equals("gridpartition")) {
			// grid partition
			GridPartition partition = CSVReader.readGridPartitionKey(conf.getDatasetPaths(), conf.getRegex(), conf);
			fileTotalLines = partition.getFileTotalLines();
			partitionSize = partition.getPartitionFrequency();
//			selectivityEstimation = partition.getSelectivityEstimaiton();
//			System.out.println("selectivityEstimation = " + Arrays.toString(selectivityEstimation));
		}

		// 2: initial workers and broadcast machine ip and port
		setupWorker();
		boolean sentWorkerInfo = broadcastWorkersInfo(fileTotalLines);
		System.out.println("LOG INFO: worker read info: " + Arrays.toString(readDataWorker));

		// 3: run

		int curBatchId = 0;
		while (curBatchId < this.numBatch) {

			// 3.1: read and swap data
			for (int parId = 0; parId < conf.getDatasetPaths().length; parId++) {
				// # of worker more than # of files
				Worker worker = workers[parId];
				worker.loadAndSwap(curBatchId);
			}

			// 3.2: operation
			// for estimation
			int[][] processSizesPar = new int[2][conf.getNumPartition()];

			for (int parId = 0; parId < conf.getNumPartition(); parId++) {
				Worker worker = workers[parId];
				if (conf.getOperatorName().equals("hashjoin")) {
					// hash join
					this.curResultSize[parId] = worker.doComputation(curBatchId);

					// for estimation
					int[] processSize = worker.getPrePrev();
					processSizesPar[0][parId] = processSize[0];
					processSizesPar[1][parId] = processSize[1];

//					System.out.println("worker " + parId + ", processSize = " + Arrays.toString(processSize));
//					System.out.println("result size = " + curResultSize[parId]);

				} else if (conf.getOperatorName().equals("spatialjoin")) {
					// spatial join
					// hash join
					this.curResultSize[parId] = worker.doSpatialComputation(curBatchId);

					// for estimation
					int[] processSize = worker.getPrePrev();
					processSizesPar[0][parId] = processSize[0];
					processSizesPar[1][parId] = processSize[1];
				}
			}

			// 3.3: progressive output
			// our method
			int[] outputResultSize = new int[workers.length];
			if ((curBatchId + 1) == this.numBatch) {
				outputResultSize = curResultSize;
			} else {
				// selectivity estimation
				outputResultSize = computeOutputSize(processSizesPar, curBatchId);
			}
			
			if (conf.getDoLowPar()) {
				writeFrequencyFile(curBatchId, outputResultSize);
			}

			System.out.println("--------LOG INFO: Batch " + curBatchId + "--------");
			System.out.println("LOG INFO Output size = " + Arrays.toString(outputResultSize));
			System.out.println("LOG INFO Baseline Output size = " + Arrays.toString(curResultSize));
//			System.out.println("LOG INFO Accumulate result size = " + Arrays.toString(accuResultSize));
			System.out.println("----------------");

			if (conf.doEvaluation) {
				System.out.println("--------LOG INFO: DO EVALUATION--------");
				double[] disOutput = UtilsFunction.GetDisArrayFromNum(outputResultSize);
				double[] disBaseline = UtilsFunction.GetDisArrayFromNum(curResultSize);
				double[] disGT = conf.getDisGT();

				double outputMAPE = 0.0;
				double BaselineMAPE = 0.0;
				double outputKL = 0.0;
				double BaselineKL = 0.0;
				for (int parId = 0; parId < disGT.length; parId++) {
					outputMAPE += Math.abs((disGT[parId] - disOutput[parId]) / disGT[parId]);
					BaselineMAPE += Math.abs((disGT[parId] - disBaseline[parId]) / disGT[parId]);

					outputKL += Math.abs(disGT[parId] * Math.log(disGT[parId] / disOutput[parId]));
					BaselineKL += Math.abs(disGT[parId] * Math.log(disGT[parId] / disBaseline[parId]));
				}

				outputMAPE = outputMAPE / disGT.length * 100;
				BaselineMAPE = BaselineMAPE / disGT.length * 100;

				System.out.println("Output MAPE = " + outputMAPE + ", Baseline MAPE = " + BaselineMAPE);
				System.out.println("Output KL = " + outputKL + ", Baseline KL = " + BaselineKL);

				System.out.println("dis GT = " + Arrays.toString(disGT));
				System.out.println("dis Output = " + Arrays.toString(disOutput));
				System.out.println("dis Baseline = " + Arrays.toString(disBaseline));

				System.out.println("GT = " + Arrays.toString(conf.getGT()));
				System.out.println("Output = " + Arrays.toString(outputResultSize));
				System.out.println("Baseline = " + Arrays.toString(curResultSize));

				int total = 0;
				int totalOutput = 0;
				int totalComputation = 0;
				for (int i = 0; i < conf.getGT().length; i++) {
					total += conf.getGT()[i];
					totalOutput += outputResultSize[i];
					totalComputation += curResultSize[i];
				}
				System.out.println("Total GT = " + total + ", totalOutput = " + totalOutput + ", total computation = "
						+ totalComputation);
				System.out.println("----------------");

				// first: est, second: baseline
				double BaselineProcess = getProgress(false, true);
				double outputProcess = -1;
				if ((curBatchId + 1) == this.numBatch) {
					outputProcess = BaselineProcess;
				} else {
					outputProcess = getProgress(false, false);
				}
//				double BaselineRandomProcess = getProgress(false, true);
				String str = curBatchId + "," + outputProcess + "," + BaselineProcess + "," + outputMAPE + ","
						+ BaselineMAPE + "," + outputKL + "," + BaselineKL + "\n";
//				String strT = "time:" + duration * 1E-9 + " s\n";
				try {
					myWriter.write(str);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			curBatchId++;
		}
		if (conf.doEvaluation) {
			try {
				myWriter.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// close file
		for (int parId = 0; parId < conf.getDatasetPaths().length; parId++) {

			workers[parId].closeFile();

		}
	}

	// set up worker based on the worker size
	// set data to the worker
	private void setupWorker() {
		// create worker worker, with job name
		// for each worker, call fillData() to send the data
		// return true after all the sending finish
		workers = new Worker[conf.getNumPartition()];
		machinesInfo = new MachineInfo[conf.getNumPartition()];
		for (int parId = 0; parId < conf.getNumPartition(); parId++) {
			if (conf.getDoLowPar()) {
				Worker worker = new Worker(parId, numBatch, true, conf);
				workers[parId] = worker;
				MachineInfo machineInfo = new MachineInfo(worker.returnIP(), worker.returnPort(), parId);
				machinesInfo[parId] = machineInfo;
				if (conf.getOperatorName().toLowerCase().equals("spatialjoin")) {
					// pass range and lower-level partition size to worker
					int numLonParLow = conf.getNumLowPar();
					int numLatParLow = 1;
					// compute the range based on pId
					double lonUnit = (conf.getMaxLon() - conf.getMinLon()) / conf.getNumLonPar();
					double minLonLow = conf.getMinLon() + lonUnit*parId;
					double maxLonLow = minLonLow + lonUnit;
					double minLatLow = conf.getMinLat();
					double maxLatLow = conf.getMaxLat();
//					System.out.println("pId = " + parId + ": " + minLonLow + " - " + maxLonLow + ", " +
//					minLatLow + " - " + maxLatLow);
//					System.out.println();
					workers[parId].setSpatialRange(minLonLow, minLatLow, maxLonLow, maxLatLow, numLonParLow, numLatParLow);
				} 
				
			} else {
				Worker worker = new Worker(parId, numBatch, conf);
				workers[parId] = worker;
				MachineInfo machineInfo = new MachineInfo(worker.returnIP(), worker.returnPort(), parId);
				machinesInfo[parId] = machineInfo;
			}
			
		}
		if (conf.getDoLowPar()) {
			statPath = conf.getFrePath() + "outputSize.txt";
			outputSizePre = new int[conf.getNumPartition()];
		}
	}

	// TODO: broadcast by TCP
	// broadcast worker info
	public boolean broadcastWorkersInfo(int[][] fileTotalLines) {
		int filePathBound = conf.getDatasetPaths().length;
		for (int parId = 0; parId < conf.getNumPartition(); parId++) {
			// # of worker more than # of files
			if (parId < filePathBound) {
//				System.out.println(filePathBound + ", " + Arrays.toString(fileTotalLines[parId]));
				workers[parId].setWorkersInfo(machinesInfo, this, fileTotalLines[parId]);
				readDataWorker[parId] = true;
			} else {
				workers[parId].setWorkersInfoWithoutRead(machinesInfo, this);
				readDataWorker[parId] = false;
			}
		}
		return true;
	}

	public void temShipData(int parId, ArrayList<StringTuple2> firstData, ArrayList<StringTuple2> secondData) {
		this.workers[parId].reveiveData(firstData, secondData);
	}
	
	public void temShipSpatialData(int parId, ArrayList<DoubleTuple4> firstData, ArrayList<DoubleTuple4> secondData) {
		this.workers[parId].reveiveSpatialData(firstData, secondData);
	}

	// estimate the total result size
	// compute the output size for each worker
	private int[] computeOutputSize(int[][] processNum) {
		// compute process ratio array based
		double[][] ratio = new double[partitionSize.length][conf.getNumPartition()];
		for (int pId = 0; pId < conf.getNumPartition(); pId++) {
			if (processNum[0][pId] != 0) {
				ratio[0][pId] = partitionSize[0][pId] / (double) processNum[0][pId];
			}
			if (processNum[1][pId] != 0) {
				ratio[1][pId] = partitionSize[1][pId] / (double) processNum[1][pId];
			}

		}
		EstimationResult estimation = Estimation.EstimationWithoutSelectivity(conf.getNumPartition(), curResultSize,
				ratio, conf.getErrorBound());
		double minOutputRatio = estimation.getMinRatio();
//		System.out.println("In driver: output ratio = " + minOutputRatio + ".");
		System.out.println("Estimation = "+ Arrays.toString(estimation.getEstimation()));
		int[] estimationSize = estimation.getEstimation();
		int[] outputResultSize = new int[conf.getNumPartition()];
		for (int par = 0; par < conf.getNumPartition(); par++) {
			outputResultSize[par] = (int) (estimationSize[par] * minOutputRatio);
		}
		// refresh the estimation
		// TODO: what if the result estimation reduced
		estimationResult.refresh(estimationSize, estimation.getRatio());

		return outputResultSize;
	}

	private int[] computeOutputSize(int[][] processNum, int curBId) {
		// compute process ratio array based
		double[][] ratio = new double[partitionSize.length][conf.getNumPartition()];
		for (int pId = 0; pId < conf.getNumPartition(); pId++) {
			if (processNum[0][pId] != 0) {
				ratio[0][pId] = partitionSize[0][pId] / (double) processNum[0][pId];
			}
			if (processNum[1][pId] != 0) {
				ratio[1][pId] = partitionSize[1][pId] / (double) processNum[1][pId];
			}

		}
		EstimationResult estimation = Estimation.EstimationWithSelectivity(conf.getNumPartition(), curResultSize, ratio,
				conf.getErrorBound(), curBId, numBatch, selectivityEstimation);
		double minOutputRatio = estimation.getMinRatio();
//		System.out.println("In driver: output ratio = " + minOutputRatio + ".");
		System.out.println("Estimation = "+ Arrays.toString(estimation.getEstimation()));
		int[] estimationSize = estimation.getEstimation();
		int[] outputResultSize = new int[conf.getNumPartition()];
		for (int par = 0; par < conf.getNumPartition(); par++) {
			outputResultSize[par] = (int) (estimationSize[par] * minOutputRatio);
		}
		// refresh the estimation
		// TODO: what if the result estimation reduced
		estimationResult.refresh(estimationSize, estimation.getRatio());

		return outputResultSize;
	}

	private double getProgress(boolean est, boolean baseline) {
		int total = 0;
		int totalSize = 0;
		int[] GT = null;
		if (est) {
			GT = estimationResult.getEstimation();
		} else {
			GT = conf.getGT();
		}
		for (int i = 0; i < GT.length; i++) {
			total += GT[i];
			if (baseline) {
				totalSize += curResultSize[i];
			} else {
				if (estimationResult.getRatio()[i] < estimationResult.getMinRatio()) {
//					System.out.println("smaller than boost error, mEstimationResult.getRatio()[i] = " + mEstimationResult.getRatio()[i] +
//							", mEstimationResult.getMinRatio() = " + mEstimationResult.getMinRatio());
					totalSize += (int) (GT[i] * estimationResult.getRatio()[i]);
				} else {
//					System.out.println("not smaller than boost error, mEstimationResult.getRatio()[i] = " + mEstimationResult.getRatio()[i] +
//							", mEstimationResult.getMinRatio() = " + mEstimationResult.getMinRatio());
					totalSize += (int) (GT[i] * estimationResult.getMinRatio());
				}
			}
		}

		double progress = totalSize / (double) total;
		return progress;
	}
	
	private void writeFrequencyFile(int batchId, int[] outputSize) {
//		for (int i=0; i<outputSize.length; i++) {
//			outputSizePre[i] += outputSize[i];
//		}
		try {
			FileWriter myWriter = new FileWriter(statPath, true);
			myWriter.write("Batch " + batchId + "\n");
			myWriter.write(Arrays.toString(outputSize) + "\n");
			myWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
