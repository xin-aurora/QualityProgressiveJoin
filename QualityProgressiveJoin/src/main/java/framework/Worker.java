package framework;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import dataStructure.DoubleTuple4;
import dataStructure.StringTuple2;
import dataStructure.Tuple3;
import io.HashJoinFileReader;
import io.SpatialJoinFileReader;
import operator.HashJoin;
import operator.SpatialJoin;
import partition.GridPartition;
import partition.HashPartition;
import utils.Configuration;
import utils.UtilsFunction;

public class Worker {

	// identification
	private int id;
	private String ip;
	private String port;

	private Driver driver;
	private MachineInfo[] machinesInfo;

	// progressive control
	private int numBatch;
	Configuration conf;
	// join as the default
	private String joinName = "join";
	private int[] fileTotalLines;
	private int[][] readBatchSize;
	private int[] prevProcessedSize;

	// data
	private HashJoinFileReader firstHashJoinFile;
	private HashJoinFileReader secondHashJoinFile;
	private ArrayList<ArrayList<StringTuple2>> hashData = new ArrayList<ArrayList<StringTuple2>>();
	private SpatialJoinFileReader firstSpatialJoinFile;
	private SpatialJoinFileReader secondSpatialJoinFile;
	private ArrayList<ArrayList<DoubleTuple4>> spatialData = new ArrayList<ArrayList<DoubleTuple4>>();

	// result
	private int resultSize = 0;
	private int accuResultSize = 0;
	private int mOutputSize = 0;
	private Queue<Tuple3> HJResults = new LinkedList<Tuple3>();

	// low level partition
	boolean doMultiLevelPartition = false;
	int lowLevelNumPar = 0;
	String statPath = "";
	int freBatchId = 0;
	int[] resultFrequencyPreRounds;
	// spatial range
	double minLon = 0;
	double minLat = 0;
	double maxLon = 0;
	double maxLat = 0;
	int numLonPar = 0;
	int numLatPar = 0;

	public Worker(int workerId, int totalRound, Configuration conf) {
		this.id = workerId;
		this.numBatch = totalRound;

		// TODO: return machine's IP and available port
		this.ip = "IP" + id;
		this.port = "5000";

		this.conf = conf;
		this.joinName = conf.getOperatorName().toLowerCase();

	}

	public Worker(int workerId, int totalRound, boolean doMultiLevelPartition, Configuration conf) {
		this.id = workerId;
		this.numBatch = totalRound;

		// TODO: return machine's IP and available port
		this.ip = "IP" + id;
		this.port = "5000";

		this.conf = conf;
		this.joinName = conf.getOperatorName().toLowerCase();

		this.doMultiLevelPartition = true;
		this.lowLevelNumPar = conf.getNumLowPar();
		this.statPath = conf.getFrePath() + "freP" + id + ".txt";
		this.resultFrequencyPreRounds = new int[lowLevelNumPar];
	}

	public String returnIP() {
		return this.ip;
	}

	public String returnPort() {
		return this.port;
	}

	public void setWorkersInfo(MachineInfo[] machinesInfo, Driver driver, int[] fileTotalLines) {
		this.machinesInfo = machinesInfo;
		// TODO: remove later after swap data by TCP
		this.driver = driver;

		this.fileTotalLines = fileTotalLines;
		this.readBatchSize = new int[numBatch + 1][2];
//		System.out.println("numBatch = " + numBatch);

		// compute batch size
		if (conf.getSplitMethod().equals("equal")) {
			equalSplit();
		} else if (conf.getSplitMethod().equals("balance")) {
			workloadBalanceSplit();
		}

		if (joinName.equals("hashjoin")) {
			// open file
			firstHashJoinFile = new HashJoinFileReader(conf.getDatasetPaths()[id][0], conf.getRegex(),
					conf.getNumPartition(), conf.getKeyIdxs()[0]);
			secondHashJoinFile = new HashJoinFileReader(conf.getDatasetPaths()[id][1], conf.getRegex(),
					conf.getNumPartition(), conf.getKeyIdxs()[1]);

			firstHashJoinFile.openFile();
			secondHashJoinFile.openFile();

			// initial data
			hashData.add(new ArrayList<StringTuple2>());
			hashData.add(new ArrayList<StringTuple2>());
		} else if (joinName.equals("spatialjoin")) {
			// String filePath, String regex,
			// double minLon, double minLat, double maxLon, double maxLat,
			// int numLonBucket, int numLatBucket,
			// int numLonPar, int numLatPar
			firstSpatialJoinFile = new SpatialJoinFileReader(conf.getDatasetPaths()[id][0], conf.getRegex(),
					conf.getMinLon(), conf.getMinLat(), conf.getMaxLat(), conf.getMaxLat(), conf.getNumLonPar(),
					conf.getNumLatPar());
			secondSpatialJoinFile = new SpatialJoinFileReader(conf.getDatasetPaths()[id][1], conf.getRegex(),
					conf.getMinLon(), conf.getMinLat(), conf.getMaxLat(), conf.getMaxLat(), conf.getNumLonPar(),
					conf.getNumLatPar());

			firstSpatialJoinFile.openFile();
			secondSpatialJoinFile.openFile();
			// initial data
			spatialData.add(new ArrayList<DoubleTuple4>());
			spatialData.add(new ArrayList<DoubleTuple4>());
		}

		this.prevProcessedSize = new int[2];
//		System.out.println("worker " + id);
//		for (int i=0; i<readBatchSize.length; i++) {
//			System.out.println(Arrays.toString(readBatchSize[i]));
//		}
	}

	public void setWorkersInfoWithoutRead(MachineInfo[] machinesInfo, Driver driver) {
		this.machinesInfo = machinesInfo;
		// TODO: remove later after swap data by TCP
		this.driver = driver;

		if (joinName.equals("hashjoin")) {
			// initial data
			hashData.add(new ArrayList<StringTuple2>());
			hashData.add(new ArrayList<StringTuple2>());
		} else if (joinName.equals("spatialjoin")) {
			// initial data
			spatialData.add(new ArrayList<DoubleTuple4>());
			spatialData.add(new ArrayList<DoubleTuple4>());
		}

		this.prevProcessedSize = new int[2];
	}
	
	public void setSpatialRange(double minLon, double minLat, double maxLon, double maxLat,
			int numLonPar, int numLatPar) {
		this.minLon = minLon;
		this.minLat = minLat;
		this.maxLon = maxLon;
		this.maxLat = maxLat;
		this.numLonPar = numLonPar;
		this.numLatPar = numLatPar;
	}

	public void closeFile() {
		if (joinName.equals("hashjoin")) {
			firstHashJoinFile.closeFile();
			secondHashJoinFile.closeFile();

		} else if (joinName.equals("spatialjoin")) {
			firstSpatialJoinFile.closeFile();
			secondSpatialJoinFile.closeFile();
		}
	}

	private void equalSplit() {
//		System.out.println("equal split, numBatch = " + numBatch);
		int batchUnit1 = fileTotalLines[0] / numBatch;
		int batchUnit2 = fileTotalLines[1] / numBatch;
		for (int batch = 1; batch < numBatch; batch++) {
			readBatchSize[batch][0] = batch * batchUnit1 - 1;
			readBatchSize[batch][1] = batch * batchUnit2 - 1;

		}
		readBatchSize[numBatch][0] = fileTotalLines[0];
		readBatchSize[numBatch][1] = fileTotalLines[1];
	}

	private void workloadBalanceSplit() {
//		System.out.println("workload balance split, numBatch = " + numBatch);
		int m1 = (int) (fileTotalLines[0] / Math.sqrt(numBatch));
		int n1 = (int) (fileTotalLines[1] / Math.sqrt(numBatch));

		readBatchSize[1][0] = m1;
		readBatchSize[1][1] = n1;

		for (int batch = 1; batch < numBatch; batch++) {
			readBatchSize[batch][0] = UtilsFunction.computeSegLength(batch, m1) + readBatchSize[batch - 1][0];
			readBatchSize[batch][1] = UtilsFunction.computeSegLength(batch, n1) + readBatchSize[batch - 1][1];
		}

		// last elements
		readBatchSize[numBatch][0] = fileTotalLines[0];
		readBatchSize[numBatch][1] = fileTotalLines[1];
	}

	public void loadAndSwap(int batchId) {
		if (joinName.equals("hashjoin")) {

			ArrayList<ArrayList<StringTuple2>> firstData = firstHashJoinFile.readFile(readBatchSize[batchId][0],
					readBatchSize[batchId + 1][0]);
			ArrayList<ArrayList<StringTuple2>> secondData = secondHashJoinFile.readFile(readBatchSize[batchId][1],
					readBatchSize[batchId + 1][1]);

			swapData(firstData, secondData);

		} else if (joinName.equals("spatialjoin")) {
			ArrayList<ArrayList<DoubleTuple4>> firstData = firstSpatialJoinFile.readFile(readBatchSize[batchId][0],
					readBatchSize[batchId + 1][0]);
			ArrayList<ArrayList<DoubleTuple4>> secondData = secondSpatialJoinFile.readFile(readBatchSize[batchId][1],
					readBatchSize[batchId + 1][1]);
			swapSpatialData(firstData, secondData);

		}
	}

	public void swapData(ArrayList<ArrayList<StringTuple2>> firstData, ArrayList<ArrayList<StringTuple2>> secondData) {
		for (int parId = 0; parId < conf.getNumPartition(); parId++) {
//			System.out.println("work " + id + ", p = " + parId + ", " + firstData.get(parId));
			if (parId == id) {
				// keep the data
				hashData.get(0).addAll(firstData.get(parId));
				hashData.get(1).addAll(secondData.get(parId));

			} else {
				// swap the data
				// TODO: by TCP later
				this.driver.temShipData(parId, firstData.get(parId), secondData.get(parId));
			}
		}
	}

	// TODO: receive by TCP later
	public void reveiveData(ArrayList<StringTuple2> firstData, ArrayList<StringTuple2> secondData) {
//			System.out.println("In receive data, I am worker " + this.mId);
		hashData.get(0).addAll(firstData);
		hashData.get(1).addAll(secondData);
	}

	public int doComputation(int batchId) {
		Queue<Tuple3> hashJoinResults = new LinkedList<Tuple3>();
//		System.out.println("current batch id = " + batchId);
//		System.out.println("I am worker " + id + ", first data size = " + hashData.get(0).size()
//				+ ", second data size = " + hashData.get(1).size());

		List<StringTuple2> location = hashData.get(0).subList(prevProcessedSize[0], hashData.get(0).size());
		List<StringTuple2> twitter = hashData.get(1).subList(prevProcessedSize[1], hashData.get(1).size());

		if (batchId == 0) {
			hashJoinResults.addAll(HashJoin.HashJoinTuple(location, 0, twitter, 0));

		} else {
			// join existing data with new data
			List<StringTuple2> existLocation = hashData.get(0).subList(0, prevProcessedSize[0]);
			List<StringTuple2> existTwitter = hashData.get(1).subList(0, prevProcessedSize[1]);
//			System.out.println("existion location size = " + existLocation.size());
//			System.out.println("existing twitter size = " + existTwitter.size());
			// join old with new
			hashJoinResults.addAll(HashJoin.HashJoinTuple(existLocation, 0, twitter, 0));
			hashJoinResults.addAll(HashJoin.HashJoinTuple(location, 0, existTwitter, 0));
			// join new data
			hashJoinResults.addAll(HashJoin.HashJoinTuple(location, 0, twitter, 0));

		}

		if (doMultiLevelPartition) {
			// compute partition
			int[] locationFrequency = hashMultiLevelPartition(hashData.get(0), conf.getKeyIdxs()[0]);
			int[] twitterFrequency = hashMultiLevelPartition(hashData.get(1), conf.getKeyIdxs()[1]);
			Queue<Tuple3> hashJoinResultsTmp = new LinkedList<Tuple3>();
			hashJoinResultsTmp.addAll(hashJoinResults);
			int[] resultFrequency = hashMultiLevelPartition(hashJoinResultsTmp, 0);
			for (int i=0; i<lowLevelNumPar; i++) {
				resultFrequencyPreRounds[i] += resultFrequency[i];
			}
			// write frequency file
			writeFrequencyFile(locationFrequency, twitterFrequency, resultFrequency);
			freBatchId++;
		}

		prevProcessedSize[0] = hashData.get(0).size();
		prevProcessedSize[1] = hashData.get(1).size();

		this.resultSize = hashJoinResults.size();
		this.accuResultSize += resultSize;
//		System.out.println("In worker " + this.mId + ": current total join size = " + mResultSize);
		return this.accuResultSize;

	}

	public void swapSpatialData(ArrayList<ArrayList<DoubleTuple4>> firstData,
			ArrayList<ArrayList<DoubleTuple4>> secondData) {
		for (int parId = 0; parId < conf.getNumPartition(); parId++) {
//			System.out.println("work " + id + ", p = " + parId + ", " + firstData.get(parId));
			if (parId == id) {
				// keep the data
				spatialData.get(0).addAll(firstData.get(parId));
				spatialData.get(1).addAll(secondData.get(parId));

			} else {
				// swap the data
				// TODO: by TCP later
				this.driver.temShipSpatialData(parId, firstData.get(parId), secondData.get(parId));
			}
		}
	}

	// TODO: receive by TCP later
	public void reveiveSpatialData(ArrayList<DoubleTuple4> firstData, ArrayList<DoubleTuple4> secondData) {
//			System.out.println("In receive data, I am worker " + this.mId);
		spatialData.get(0).addAll(firstData);
		spatialData.get(1).addAll(secondData);
	}

	public int doSpatialComputation(int batchId) {
		Queue<String> spatialJoinResults = new LinkedList<String>();
		List<DoubleTuple4> lake = spatialData.get(0).subList(prevProcessedSize[0], spatialData.get(0).size());
		List<DoubleTuple4> park = spatialData.get(1).subList(prevProcessedSize[1], spatialData.get(1).size());
//		System.out.println("current batch id = " + batchId);
//		System.out.println("I am worker " + id + ", first data size = " + spatialData.get(0).size()
//				+ ", second data size = " + spatialData.get(1).size());

		// TODO: multi-level partition for input
		if (batchId == 0) {
			spatialJoinResults = SpatialJoin.SpatialJoinTuple(lake, park);

		} else {
			List<DoubleTuple4> existLake = spatialData.get(0).subList(0, prevProcessedSize[0]);
			List<DoubleTuple4> existPark = spatialData.get(1).subList(0, prevProcessedSize[1]);

			// join old with new
			spatialJoinResults.addAll(SpatialJoin.SpatialJoinTuple(lake, existPark));
			spatialJoinResults.addAll(SpatialJoin.SpatialJoinTuple(park, existLake));
			// join new data
			spatialJoinResults.addAll(SpatialJoin.SpatialJoinTuple(lake, park));
		}

		if (doMultiLevelPartition) {
			// compute partition
			int[] lakeFrequency = spatialMultiLevelPartition(spatialData.get(0));
			int[] parkFrequency = spatialMultiLevelPartition(spatialData.get(1));
//			Queue<String> spatialJoinResultsTmp = new LinkedList<String>();
//			spatialJoinResultsTmp.addAll(spatialJoinResults);
			// TODO: change to result frequency
			int[] resultFrequency = parkFrequency;
			for (int i=0; i<lowLevelNumPar; i++) {
				resultFrequencyPreRounds[i] += resultFrequency[i];
			}
			// write frequency file
			writeFrequencyFile(lakeFrequency, parkFrequency, resultFrequency);
			freBatchId++;
		}

		prevProcessedSize[0] = spatialData.get(0).size();
		prevProcessedSize[1] = spatialData.get(1).size();

		this.resultSize = spatialJoinResults.size();
		this.accuResultSize += resultSize;
//		System.out.println("In worker " + this.mId + ": current total join size = " + mResultSize);
		return this.accuResultSize;
	}

	public ArrayList<Tuple3> doOutput(int popSize, boolean backToDriver) {
		ArrayList<Tuple3> output = new ArrayList<Tuple3>();
		while (mOutputSize < popSize) {
			output.add(HJResults.poll());
			mOutputSize++;
		}
		return output;
	}

	public int[] getPrePrev() {
//		System.out.println("processSize = " + Arrays.toString(mPrevProNum));
		return this.prevProcessedSize;
	}

	private int[] hashMultiLevelPartition(ArrayList<StringTuple2> tuples, int keyIdx) {
		// ArrayList<ArrayList<StringTuple2>>
		HashPartition hashPartition = new HashPartition(lowLevelNumPar);
		for (int i = 0; i < tuples.size(); i++) {
			hashPartition.addKeyMulti(0, tuples.get(i), keyIdx, conf.getNumPartition());
		}

		return hashPartition.getPartitionFrequency()[0];
	}

	private int[] hashMultiLevelPartition(Queue<Tuple3> tuples, int keyIdx) {
		// ArrayList<ArrayList<StringTuple2>>
		HashPartition hashPartition = new HashPartition(lowLevelNumPar);
		while (!tuples.isEmpty()) {
			hashPartition.addKeyMulti(0, tuples.poll(), keyIdx, conf.getNumPartition());
		}

		return hashPartition.getPartitionFrequency()[0];
	}

	private int[] spatialMultiLevelPartition(List<DoubleTuple4> tuples) {
		GridPartition gridPartition = new GridPartition(minLon, minLat, maxLon, maxLat, numLonPar, numLatPar);
		for (int i=0; i<tuples.size(); i++) {
			double[] tupleCoor = tuples.get(i).toArray();
			gridPartition.addRecordMulti(tupleCoor[0], tupleCoor[1], tupleCoor[2], tupleCoor[3]);
		}
		return gridPartition.getPartitionFrequency()[0];
	}

	private void writeFrequencyFile(int[] ds1Fre, int[] ds2Fre, int[] resultFrequency) {
		try {
			FileWriter myWriter = new FileWriter(statPath, true);
			myWriter.write("Batch " + freBatchId + "\n");
			myWriter.write(Arrays.toString(ds1Fre) + "\n");
			myWriter.write(Arrays.toString(ds2Fre) + "\n");
			myWriter.write(Arrays.toString(resultFrequency) + "\n");
			myWriter.write(Arrays.toString(resultFrequencyPreRounds) + "\n");
			myWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
