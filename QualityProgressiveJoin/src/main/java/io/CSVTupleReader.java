package io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import dataStructure.DoubleTuple4;
import dataStructure.StringTuple2;
import dataStructure.Tuple;
import dataStructure.Tuple2;
import dataStructure.Tuple3;
//import estimation.histogram.GeometricHistogram;
//import estimation.histogram.GeometricHistogramLearn;
//import estimation.histogram.SimpleHistogram;
//import estimation.histogram.SimpleSpatialHistogram;
//import partition.ControlDisPartition;
import partition.GridPartition;
import partition.HashPartition;
import utils.SimpleHistogram;

public class CSVTupleReader extends CSVReaderTuple {

	private String mTupleType;

	private int mTupleSize;

	private int[] mSkipBytes = new int[2];

	public CSVTupleReader(String regex, boolean skipHeaderLine, String tupleType) {
		super(regex, skipHeaderLine);
		detectedTupleType(tupleType);
	}

	private void detectedTupleType(String tupleType) {
		switch (tupleType) {
		case "tuple2":
			this.mTupleType = "tuple2";
			this.mTupleSize = 2;
			break;
		case "tuple3":
			this.mTupleType = "tuple3";
			this.mTupleSize = 3;
			break;
		case "tuple5":
			this.mTupleType = "tuple5";
			this.mTupleSize = 5;
			break;
		case "tuple5r2":
			this.mTupleType = "tuple5r2";
			this.mTupleSize = 2;
			break;
		}
	}

	@SuppressWarnings("rawtypes")
	public ArrayList<Tuple2> readTuple2(String filePath) throws IOException {
		File file = new File(filePath);
		ArrayList<Tuple2> tuples = new ArrayList<Tuple2>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			if (mSkipHeaderLine) {
				reader.readLine();
			}
			String line = reader.readLine();
			while (line != null) {
				String[] tem = new String[mTupleSize];
				tem = line.split(mRegex);
				switch (this.mTupleType) {
				case "tuple2":
					Tuple2<String, String> tuple2 = new Tuple2<String, String>(tem[0], tem[1]);
					tuples.add(tuple2);
					break;
				case "tuple5r2":
					Tuple2<String, String> tuple5r2 = new Tuple2<String, String>(tem[3], tem[4]);
					tuples.add(tuple5r2);
					break;
				}
				line = reader.readLine();
			}
			reader.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return tuples;
	}

	@SuppressWarnings("rawtypes")
	public ArrayList<Tuple3> readTuple3(String filePath) throws IOException {
		File file = new File(filePath);
		ArrayList<Tuple3> tuples = new ArrayList<Tuple3>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			if (mSkipHeaderLine) {
				reader.readLine();
			}
			String line = reader.readLine();
			while (line != null) {
				String[] tem = new String[mTupleSize];
				tem = line.split(mRegex);
				Tuple3<String, String, String> tuple3 = new Tuple3<String, String, String>(tem[0], tem[1], tem[2]);
				tuples.add(tuple3);
				line = reader.readLine();
			}
			reader.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return tuples;
	}

//	public HashPartition readHashPartitionKey(String[][] filePaths, int[] keyIdxs, int numBucket, int numPar
////			,HashSet<String> filterStates
//	) {
//		HashPartition hashPartition = new HashPartition(numBucket, numPar);
////		hashPartition.enableSelectivityEstimation(filterStates);
//		// read the first dataset and do the partition
//		for (int i = 0; i < filePaths.length; i++) {
//			String[] filePath = filePaths[i];
//			File file = new File(filePath[0]);
//			BufferedReader reader;
//			try {
//				reader = new BufferedReader(new FileReader(file));
//				if (mSkipHeaderLine) {
//					reader.readLine();
//				}
//				String line = reader.readLine();
//				while (line != null) {
//					String[] tem = new String[mTupleSize];
//					tem = line.split(mRegex);
////					System.out.println(Arrays.toString(tem));
//					StringTuple2 tuple = new StringTuple2(tem[3], tem[4], 0);
//					hashPartition.addKey(tuple, 0);
//					line = reader.readLine();
//				}
//				reader.close();
////				hashPartition.partition();
//				// read the second dataset
//				file = new File(filePath[1]);
//				reader = new BufferedReader(new FileReader(file));
//				if (mSkipHeaderLine) {
//					reader.readLine();
//				}
//				line = reader.readLine();
//				while (line != null) {
////					System.out.println(line);
//					String[] tem = new String[mTupleSize];
//					tem = line.split(mRegex);
//					StringTuple2 tuple = new StringTuple2(tem[3], tem[4], 0);
//					hashPartition.addKeySecondDataset(tuple, 0);
//					line = reader.readLine();
//				}
//				reader.close();
//			} catch (FileNotFoundException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		return hashPartition;
//	}
	
//	public HashPartition readHashPartitionKey(String[][] filePaths, int numPar) {
//		HashPartition hashPartition = new HashPartition(numPar);
////		hashPartition.enableSelectivityEstimation(filterStates);
//		// read the first dataset and do the partition
//		for (int i = 0; i < filePaths.length; i++) {
//			String[] filePath = filePaths[i];
//			File file = new File(filePath[0]);
//			BufferedReader reader;
//			try {
//				reader = new BufferedReader(new FileReader(file));
//				if (mSkipHeaderLine) {
//					reader.readLine();
//				}
//				String line = reader.readLine();
//				while (line != null) {
//					String[] tem = new String[mTupleSize];
//					tem = line.split(mRegex);
////					System.out.println(Arrays.toString(tem));
//					StringTuple2 tuple = new StringTuple2(tem[2], tem[3], 0);
//					int pId = Integer.parseInt(tem[0]);
//					if (pId < numPar) {
//						hashPartition.addKeyById(tuple, pId);
//					}
//					line = reader.readLine();
//				}
//				reader.close();
////				hashPartition.partition();
//				// read the second dataset
//				file = new File(filePath[1]);
//				reader = new BufferedReader(new FileReader(file));
//				if (mSkipHeaderLine) {
//					reader.readLine();
//				}
//				line = reader.readLine();
//				while (line != null) {
////					System.out.println(line);
//					String[] tem = new String[mTupleSize];
//					tem = line.split(mRegex);
//					StringTuple2 tuple = new StringTuple2(tem[2], tem[3], 0);
//					int pId = Integer.parseInt(tem[0]);
//					if (pId < numPar) {
//						hashPartition.addKeySecondDatasetById(tuple, pId);
//					}
//					
//					line = reader.readLine();
//				}
//				reader.close();
//			} catch (FileNotFoundException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		return hashPartition;
//	}
//
//	public GridPartition readGridPartition(String[][] filePaths, double minLon, double minLat, double maxLon,
//			double maxLat, int numLonBucket, int numLatBucket, int numLonPar, int numLatPar, int level) {
//		GridPartition gridPartition = new GridPartition(minLon, minLat, maxLon, maxLat, numLonBucket, numLatBucket,
//				numLonPar, numLatPar);
////		gridPartition.enableSelectivityEstimation(level);
//		// read the first dataset and do the partition
//		for (int i = 0; i < filePaths.length; i++) {
//			String[] filePath = filePaths[i];
//			File file = new File(filePath[0]);
//			BufferedReader reader;
//			try {
//				reader = new BufferedReader(new FileReader(file));
//				if (mSkipHeaderLine) {
//					reader.readLine();
//				}
//				String line = reader.readLine();
//				while (line != null) {
//					String[] tem = line.split(mRegex);
////					System.out.println(Arrays.toString(tem));
//					gridPartition.addRecord(Double.parseDouble(tem[0]), Double.parseDouble(tem[1]),
//							Double.parseDouble(tem[2]), Double.parseDouble(tem[3]));
//					line = reader.readLine();
//				}
//				reader.close();
////				hashPartition.partition();
//				// read the second dataset
//				file = new File(filePath[1]);
//				reader = new BufferedReader(new FileReader(file));
//				if (mSkipHeaderLine) {
//					reader.readLine();
//				}
//				line = reader.readLine();
//				while (line != null) {
////					System.out.println(line);
//					String[] tem = line.split(mRegex);
//					gridPartition.addRecordSecondDataset(Double.parseDouble(tem[0]), Double.parseDouble(tem[1]),
//							Double.parseDouble(tem[2]), Double.parseDouble(tem[3]));
//					line = reader.readLine();
//				}
//				reader.close();
//			} catch (FileNotFoundException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		return gridPartition;
//	}

//	// bucket: <DS1 list, DS2 list>
//	public ArrayList<ArrayList<ArrayList<StringTuple2>>> readHashStringTuple2(String[] filePath, int numBucket,
//			int[] keyIdx, int[] startOffset, int[] endOffset) {
////		System.out.println(Arrays.toString(startOffset));
////		System.out.println(Arrays.toString(endOffset));
//		ArrayList<ArrayList<ArrayList<StringTuple2>>> partitionedDatas = new ArrayList<ArrayList<ArrayList<StringTuple2>>>();
//
//		for (int j = 0; j < numBucket; j++) {
//			ArrayList<ArrayList<StringTuple2>> partitionedData = new ArrayList<ArrayList<StringTuple2>>(
//					filePath.length);
//			for (int i = 0; i < filePath.length; i++) {
//				partitionedData.add(new ArrayList<StringTuple2>());
//			}
//			partitionedDatas.add(partitionedData);
//		}
//
//		for (int i = 0; i < filePath.length; i++) {
//			File file = new File(filePath[i]);
//			int start = startOffset[i];
//			int end = endOffset[i];
//			try {
//				BufferedReader reader = new BufferedReader(new FileReader(file));
//				if (mSkipHeaderLine) {
//					reader.readLine();
//				}
//				// skip the previous lines
//				reader.skip(mSkipBytes[i]);
//				String line = reader.readLine();
//				while (line != null && start < end) {
//					String[] tem = line.split(mRegex);
//					StringTuple2 tuple = new StringTuple2(tem[3], tem[4], 0);
//					partitionedDatas.get(tuple.getBucketId(numBucket)).get(i).add(tuple);
//					start++;
//					mSkipBytes[i] += line.length() + "\n".length();
//					line = reader.readLine();
//				}
//				reader.close();
//			} catch (FileNotFoundException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//
//		return partitionedDatas;
//	}
	
	// p0: <DS1 list, DS2 list>
	public ArrayList<ArrayList<ArrayList<StringTuple2>>> readHashStringTuple2NoBucket(String[] filePath, int numPar,
			int[] keyIdx, int[] startOffset, int[] endOffset) {
//		System.out.println(Arrays.toString(startOffset));
//		System.out.println(Arrays.toString(endOffset));
		ArrayList<ArrayList<ArrayList<StringTuple2>>> partitionedDatas = new ArrayList<ArrayList<ArrayList<StringTuple2>>>();

		for (int j = 0; j < numPar; j++) {
			ArrayList<ArrayList<StringTuple2>> partitionedData = new ArrayList<ArrayList<StringTuple2>>(
					filePath.length);
			for (int i = 0; i < filePath.length; i++) {
				partitionedData.add(new ArrayList<StringTuple2>());
			}
//			System.out.println("partitionedData size = " + partitionedData.size());
			partitionedDatas.add(partitionedData);
		}
		
//		System.out.println("partitionedDatas size = " + partitionedDatas.size());

		for (int i = 0; i < filePath.length; i++) {
			File file = new File(filePath[i]);
			int start = startOffset[i];
			int end = endOffset[i];
			try {
				BufferedReader reader = new BufferedReader(new FileReader(file));
				if (mSkipHeaderLine) {
					reader.readLine();
				}
				// skip the previous lines
				reader.skip(mSkipBytes[i]);
				String line = reader.readLine();
				while (line != null && start < end) {
					String[] tem = line.split(mRegex);
					StringTuple2 tuple = new StringTuple2(tem[2], tem[3], 0);
					int pId = Integer.parseInt(tem[0]);
					if (pId < numPar) {
						partitionedDatas.get(pId).get(i).add(tuple);
					}
					start++;
					mSkipBytes[i] += line.length() + "\n".length();
					line = reader.readLine();
				}
				reader.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return partitionedDatas;
	}

	// <DS1 list, DS2 list>
	public ArrayList<ArrayList<ArrayList<DoubleTuple4>>> readHashDoubleTuple4(GridPartition gp, String[] filePath,
			int[] startOffset, int[] endOffset) {
		ArrayList<ArrayList<ArrayList<DoubleTuple4>>> partitionedDatas = new ArrayList<ArrayList<ArrayList<DoubleTuple4>>>();

		for (int i = 0; i < filePath.length; i++) {
			ArrayList<ArrayList<DoubleTuple4>> tmp = new ArrayList<ArrayList<DoubleTuple4>>();
			for (int parId = 0; parId < gp.getPartitionFrequency()[0].length; parId++) {
				tmp.add(new ArrayList<DoubleTuple4>());
			}
			partitionedDatas.add(tmp);
		}

		for (int i = 0; i < filePath.length; i++) {
			File file = new File(filePath[i]);
			int start = startOffset[i];
			int end = endOffset[i];
			try {
				BufferedReader reader = new BufferedReader(new FileReader(file));
				if (mSkipHeaderLine) {
					reader.readLine();
				}
				// skip the previous lines
				reader.skip(mSkipBytes[i]);
				String line = reader.readLine();
				while (line != null && start < end) {
					String[] tem = line.split(mRegex);
					DoubleTuple4 tuple = new DoubleTuple4(Double.parseDouble(tem[0]), Double.parseDouble(tem[1]),
							Double.parseDouble(tem[2]), Double.parseDouble(tem[3]), gp);
					for (int parId : tuple.getParId()) {
						partitionedDatas.get(i).get(parId).add(tuple);
					}
					start++;
					mSkipBytes[i] += line.length() + "\n".length();
					line = reader.readLine();
				}
				reader.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return partitionedDatas;
	}

	public static int getTotalLine(String filePath) {
		int noOfLines = 0;
		try {
			LineNumberReader reader = new LineNumberReader(new FileReader(filePath));
			reader.skip(Integer.MAX_VALUE);
			noOfLines = reader.getLineNumber();
			reader.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return noOfLines;
	}

//	public ControlDisPartition readControlPartitionKey(String[][] filePaths, int[] keyIdxs, int numPar) {
//		ControlDisPartition partition = new ControlDisPartition(numPar);
//
//		// read the first dataset and do the partition
//		for (int i = 0; i < filePaths.length; i++) {
//			String[] filePath = filePaths[i];
//			File file = new File(filePath[0]);
//			BufferedReader reader;
//			try {
//				reader = new BufferedReader(new FileReader(file));
//				if (mSkipHeaderLine) {
//					reader.readLine();
//				}
//				String line = reader.readLine();
//				while (line != null) {
//					String[] tem = new String[mTupleSize];
//					tem = line.split(mRegex);
////							System.out.println(Arrays.toString(tem));
//					partition.addKey(tem[keyIdxs[0]], Integer.parseInt(tem[keyIdxs[0]]));
//					line = reader.readLine();
//				}
//				reader.close();
////						hashPartition.partition();
//				// read the second dataset
//				file = new File(filePath[1]);
//				reader = new BufferedReader(new FileReader(file));
//				if (mSkipHeaderLine) {
//					reader.readLine();
//				}
//				line = reader.readLine();
//				while (line != null) {
////							System.out.println(line);
//					String[] tem = new String[mTupleSize];
//					tem = line.split(mRegex);
//					partition.addKeySecondDataset(tem[keyIdxs[1]], Integer.parseInt(tem[keyIdxs[1]]));
//					line = reader.readLine();
//				}
//				reader.close();
//			} catch (FileNotFoundException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//
//		return partition;
//	}

	public ArrayList<ArrayList<ArrayList<StringTuple2>>> readHControlStringTuple2(String[] filePath, int numPar,
			int[] keyIdx, int[] startOffset, int[] endOffset) {
//		System.out.println(Arrays.toString(startOffset));
//		System.out.println(Arrays.toString(endOffset));
		ArrayList<ArrayList<ArrayList<StringTuple2>>> partitionedDatas = new ArrayList<ArrayList<ArrayList<StringTuple2>>>();

		for (int j = 0; j < numPar; j++) {
			ArrayList<ArrayList<StringTuple2>> partitionedData = new ArrayList<ArrayList<StringTuple2>>(
					filePath.length);
			for (int i = 0; i < filePath.length; i++) {
				partitionedData.add(new ArrayList<StringTuple2>());
			}
			partitionedDatas.add(partitionedData);
		}

		for (int i = 0; i < filePath.length; i++) {
			File file = new File(filePath[i]);
			int start = startOffset[i];
			int end = endOffset[i];
			try {
				BufferedReader reader = new BufferedReader(new FileReader(file));
				if (mSkipHeaderLine) {
					reader.readLine();
				}
				int cnt = 0;
				while (cnt < start) {
					reader.readLine();
					cnt++;
				}
				String line = reader.readLine();
				while (line != null && cnt < end) {
					String[] tem = new String[mTupleSize];
					tem = line.split(mRegex);
					StringTuple2 tuple = new StringTuple2(tem[3], tem[4], 0);
					int parId = Integer.parseInt(tem[keyIdx[i]]);
					partitionedDatas.get(parId).get(i).add(tuple);
					cnt++;
					line = reader.readLine();
				}
				reader.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return partitionedDatas;
	}

	public void getAreaBoundary(String[] filePaths, String regex, boolean skipHeaderLine, int coordinateD) {
		double MAX_LON = -Double.MAX_VALUE;
		double MAX_LAT = -Double.MIN_VALUE;
		double MIN_LON = Double.MAX_VALUE;
		double MIN_LAT = Double.MAX_VALUE;

		for (int i = 0; i < filePaths.length; i++) {
			File file = new File(filePaths[i]);

			try {
				BufferedReader reader = new BufferedReader(new FileReader(file));
				if (skipHeaderLine) {
					reader.readLine();
				}
				String line = reader.readLine();
				while (line != null) {
					String[] tem = line.split(regex);
//					System.out.println(Arrays.toString(tem));

					if (coordinateD == 2) {
						if (Double.parseDouble(tem[0]) < MIN_LON) {
							MIN_LON = Double.parseDouble(tem[0]);
						}

						if (Double.parseDouble(tem[0]) > MAX_LON) {
							MAX_LON = Double.parseDouble(tem[0]);
						}

						if (Double.parseDouble(tem[1]) < MIN_LAT) {
							MIN_LAT = Double.parseDouble(tem[1]);
						}

						if (Double.parseDouble(tem[1]) > MAX_LAT) {
							MAX_LAT = Double.parseDouble(tem[1]);
						}
					} else {
						if (Double.parseDouble(tem[0]) < MIN_LON) {
							MIN_LON = Double.parseDouble(tem[0]);
						}

						if (Double.parseDouble(tem[2]) > MAX_LON) {
							MAX_LON = Double.parseDouble(tem[2]);
						}

						if (Double.parseDouble(tem[1]) < MIN_LAT) {
							MIN_LAT = Double.parseDouble(tem[1]);
						}

						if (Double.parseDouble(tem[3]) > MAX_LAT) {
							MAX_LAT = Double.parseDouble(tem[3]);
						}
					}

					line = reader.readLine();
				}
				reader.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		System.out.println(
				"min lon: " + MIN_LON + ", max lon: " + MAX_LON + ", min lat: " + MIN_LAT + ", max lat: " + MAX_LAT);
	}

	public ArrayList<ArrayList<DoubleTuple4>> readDoubleTuple4(String[] paths) {
		ArrayList<ArrayList<DoubleTuple4>> data = new ArrayList<ArrayList<DoubleTuple4>>();
		for (int i = 0; i < paths.length; i++) {
			data.add(new ArrayList<DoubleTuple4>());
		}
		for (int i = 0; i < paths.length; i++) {
			File file = new File(paths[i]);

			try {
				BufferedReader reader = new BufferedReader(new FileReader(file));
				if (mSkipHeaderLine) {
					reader.readLine();
				}
				String line = reader.readLine();
				while (line != null) {
					String[] tem = line.split(mRegex);
					DoubleTuple4 tuple = new DoubleTuple4(Double.parseDouble(tem[0]), Double.parseDouble(tem[1]),
							Double.parseDouble(tem[2]), Double.parseDouble(tem[3]));
					data.get(i).add(tuple);
					line = reader.readLine();
				}
				reader.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		System.out.println("size of ds 1 = " + data.get(0).size() + ", " + "size of ds 2 = " + data.get(1).size());

		return data;
	}

//	public GeometricHistogram getSummaryDoubleTuple4(String[] paths, GeometricHistogram histogram) {
//
//		for (int i = 0; i < paths.length; i++) {
//			File file = new File(paths[i]);
//
//			try {
//				BufferedReader reader = new BufferedReader(new FileReader(file));
//				if (mSkipHeaderLine) {
//					reader.readLine();
//				}
//				String line = reader.readLine();
//				while (line != null) {
//					String[] tem = line.split(mRegex);
//
//					histogram.addRecord(Double.parseDouble(tem[0]), Double.parseDouble(tem[1]),
//							Double.parseDouble(tem[2]), Double.parseDouble(tem[3]));
//
//					line = reader.readLine();
//				}
//				reader.close();
//			} catch (FileNotFoundException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//
//		return histogram;
//	}
//
//	public SimpleSpatialHistogram getSummaryDoubleTuple2(String path, int lon, int lat) {
//
//		SimpleSpatialHistogram histogram = null;
//		File file = new File(path);
//
//		try {
//			BufferedReader reader = new BufferedReader(new FileReader(file));
//
//			String[] header = reader.readLine().split(",");
//			double minLon = 0;
//			double maxLon = 0;
//			double minLat = 0;
//			double maxLat = 0;
//			if (header.length > 2) {
//				minLon = Double.parseDouble(header[0].split(":")[1].trim());
//				maxLon = Double.parseDouble(header[1].split(":")[1].trim());
//				minLat = Double.parseDouble(header[2].split(":")[1].trim());
//				maxLat = Double.parseDouble(header[3].split(":")[1].trim());
//			} else {
//				minLon = Double.parseDouble(header[0]);
//				maxLon = Double.parseDouble(header[1]);
//				minLat = Double.parseDouble(header[0]);
//				maxLat = Double.parseDouble(header[1]);
//			}
//			histogram = new SimpleSpatialHistogram(minLon, minLat, maxLon, maxLat, lon, lat);
//			String line = reader.readLine();
//			while (line != null) {
//				String[] tem = line.split(mRegex);
//
//				histogram.addRecord(Double.parseDouble(tem[0]), Double.parseDouble(tem[1]));
//
//				line = reader.readLine();
//			}
//			reader.close();
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		return histogram;
//	}
//
//	public SimpleSpatialHistogram getSummaryDoubleTuple2(String[] paths, SimpleSpatialHistogram histogram
//			, boolean isNonUniform) {
//
//		for (int i = 0; i < paths.length; i++) {
//			File file = new File(paths[i]);
//
//			try {
//				BufferedReader reader = new BufferedReader(new FileReader(file));
//				if (mSkipHeaderLine) {
//					reader.readLine();
//				}
//				String line = reader.readLine();
//				while (line != null) {
//					String[] tem = line.split(mRegex);
//
//					if (isNonUniform) {
//						histogram.addRecord(Double.parseDouble(tem[0]), Double.parseDouble(tem[1]), isNonUniform);
//					}else {
//						histogram.addRecord(Double.parseDouble(tem[0]), Double.parseDouble(tem[1]));
//					}
//
//					line = reader.readLine();
//				}
//				reader.close();
//			} catch (FileNotFoundException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//
//		return histogram;
//	}

	public SimpleHistogram getSummaryDouble(String path, int srcNum) {

		SimpleHistogram histogram = null;
		File file = new File(path);

		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));

			String[] header = reader.readLine().split(",");
			double min = Double.parseDouble(header[0]);
			double max = Double.parseDouble(header[1]);
//			System.out.println(path);
//			System.out.println("min=" + min + ", max=" + max);
			histogram = new SimpleHistogram(min, max, srcNum);
			String line = reader.readLine();
			while (line != null) {

				histogram.addRecord(Double.parseDouble(line));
//				histogram.addRecordNonUniform(Double.parseDouble(line));

				line = reader.readLine();
			}
			reader.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return histogram;
	}

	public SimpleHistogram getSummaryDouble(String[] paths, int srcNum, double min, double max) {

		SimpleHistogram histogram = new SimpleHistogram(min, max, srcNum);
		for (int i = 0; i < paths.length; i++) {
			File file = new File(paths[i]);

			try {
				BufferedReader reader = new BufferedReader(new FileReader(file));
				// reader header
				reader.readLine();
				String line = reader.readLine();
				while (line != null) {

					histogram.addRecord(Double.parseDouble(line));

					line = reader.readLine();
				}
				reader.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return histogram;
	}
	
	public SimpleHistogram getSummaryDouble(String[] paths, SimpleHistogram histogram) {

		for (int i = 0; i < paths.length; i++) {
			File file = new File(paths[i]);
//			System.out.println(paths[i]);
			try {
				BufferedReader reader = new BufferedReader(new FileReader(file));
				if (mSkipHeaderLine) {
					reader.readLine();
				}
				String line = reader.readLine();
				while (line != null) {

					histogram.addRecordNonUniform(Double.parseDouble(line));

					line = reader.readLine();
				}
				reader.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return histogram;
	}

	public ArrayList<ArrayList<Double>> get1DRangeQuery(String path, int dimension) {

//		int GT = 0;
		ArrayList<ArrayList<Double>> data = new ArrayList<ArrayList<Double>>();
		for (int i=0; i<dimension; i++) {
			
			data.add(new ArrayList<Double>());
		}

		File file = new File(path);

		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			if (mSkipHeaderLine) {
				// reader header
				reader.readLine();
//				System.out.println("skip header");
			} 
			String line = reader.readLine();
			while (line != null) {

//					double pos = Double.parseDouble(line);
//					if (min <= pos & pos <= max) {
//						GT++;
//					}
				String[] tem = line.split(",");
				if (dimension == 1) {
					data.get(0).add(Double.parseDouble(tem[0]));
				}
				if (dimension == 2) {
					data.get(0).add(Double.parseDouble(tem[0]));
					data.get(1).add(Double.parseDouble(tem[1]));
				}
				line = reader.readLine();
			}
			reader.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return data;
	}

	public ArrayList<DoubleTuple4> getDoubleTuple4FromSingleFile(String path) {

		ArrayList<DoubleTuple4> data = new ArrayList<DoubleTuple4>();

		File file = new File(path);

		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			if (mSkipHeaderLine) {
				reader.readLine();
			}
			String line = reader.readLine();
			while (line != null) {
				String[] tem = line.split(mRegex);

				DoubleTuple4 tuple = new DoubleTuple4(Double.parseDouble(tem[0]), Double.parseDouble(tem[1]),
						Double.parseDouble(tem[2]), Double.parseDouble(tem[3]));
				data.add(tuple);

				line = reader.readLine();
			}
			reader.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return data;
	}
}
