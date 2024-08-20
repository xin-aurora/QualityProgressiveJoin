package utils;

import java.util.HashSet;

public class Configuration {

	// data parameters
	private String[][] mdDatasetPaths;
	private String mRegex;
	private boolean mSkipHeaderLine;

	// partition parameters
	private String mPartitionMethod;
	// Hash Partition
	private int[] mKeyIdx;
	private int mNumPartition;
	private int mNumBucket;
	private HashSet<String> mFilterState; 

	// TODO: Grid Partition
	private double mMinLon;
	private double mMinLat;
	private double mMaxLon;
	private double mMaxLat;
	private int mNumLonBucket;
	private int mNumLatBucket;
	private int mNumLonPar;
	private int mNumLatPar;
	private int mEstLevel;
	
	// Multi-level partition 
	private boolean doMultiPartition;
	private int lowLevelNumPar;
	private String frequencyPath;
	
	// split method
	private String mSplitMethod;

	// operator parameters
	private String mOperatorName;

	// evaluation
	public boolean doEvaluation = false;
	private int[] GT;
	private double[] disGT;

	// input parameter
	private double mPostpondThreshold = Double.MAX_VALUE;
	private boolean mBoostProgress = false;
	private double mErrorBound = 0;

	public Configuration() {

	}

	public void setConf(String conName, String conValue) {
		switch (conName) {
		case "operator.name":
			mOperatorName = conValue.toLowerCase();
			break;
		case "partition.method":
			mPartitionMethod = conValue.toLowerCase();
			break;
		case "hashPartition.parNum":
			mNumPartition = Integer.parseInt(conValue);
			break;
		case "hashPartition.bucketNum":
			mNumBucket = Integer.parseInt(conValue);
			break;
		case "load.regex":
			mRegex = conValue;
			break;
		case "load.skipHead":
			mSkipHeaderLine = Boolean.parseBoolean(conValue);
			break;
		case "do.evaluation":
			doEvaluation = Boolean.parseBoolean(conValue);
			break;
		case "postponed.Threshold":
			mPostpondThreshold = Double.parseDouble(conValue);
			break;
		case "error.Bound":
			mBoostProgress = true;
			mErrorBound = Double.parseDouble(conValue);
			break;
		case "gridPartition.minLon":
			mMinLon = Double.parseDouble(conValue);
			break;
		case "gridPartition.minLat":
			mMinLat = Double.parseDouble(conValue);
			break;
		case "gridPartition.maxLon":
			mMaxLon = Double.parseDouble(conValue);
			break;
		case "gridPartition.maxLat":
			mMaxLat = Double.parseDouble(conValue);
			break;
		case "gridPartition.lonBucketNum":
			mNumLonBucket = Integer.parseInt(conValue);
			break;
		case "gridPartition.latBucketNum":
			mNumLatBucket = Integer.parseInt(conValue);
			break;
		case "gridPartition.lonParNum":
			mNumLonPar = Integer.parseInt(conValue);
			break;
		case "gridPartition.latParNum":
			mNumLatPar = Integer.parseInt(conValue);
			break;
		case "gridPartition.estLevel":
			mEstLevel = Integer.parseInt(conValue);
			break;
		case "split.method":
			mSplitMethod = conValue;
			break;
		case "multiPartition.lowLevelNumPar":
			doMultiPartition = true;
			lowLevelNumPar = Integer.parseInt(conValue);
			break;
		case "multiPartition.frequencyPath":
			frequencyPath = conValue;
			break;
		}
	}

	public void setConf(String conName, String[] conValues) {
		switch (conName) {
		case "hashPartition.keyIdx":
			mKeyIdx = new int[conValues.length];
			for (int i = 0; i < conValues.length; i++) {
				mKeyIdx[i] = Integer.parseInt(conValues[i]);
			}
			break;
		case "GT":
			GT = new int[conValues.length];
			for (int i = 0; i < conValues.length; i++) {
				GT[i] = Integer.parseInt(conValues[i]);
			}
			break;
		case "disGT":
			disGT = new double[conValues.length];
			for (int i = 0; i < conValues.length; i++) {
				disGT[i] = Double.parseDouble(conValues[i]);
			}
			break;
		case "gridPartition.bucketNum":
			mNumLonBucket = Integer.parseInt(conValues[0]);
			mNumLatBucket = Integer.parseInt(conValues[1]);
			mNumBucket = mNumLonBucket * mNumLatBucket;
			break;
		case "gridPartition.parNum":
			mNumLonPar = Integer.parseInt(conValues[0]);
			mNumLatPar = Integer.parseInt(conValues[1]);
			mNumPartition = mNumLonPar * mNumLatPar;
			break;
		case "hashPartition.filter":
			mFilterState = new HashSet<String>();
			for (String state : conValues) {
				mFilterState.add(state);
			}
			break;	
		}
	}

	public void setConf(String conName, String[][] conValues) {
		switch (conName) {
		case "data.paths":
			mdDatasetPaths = conValues;
			break;
		}
	}

	public String[][] getDatasetPaths() {
		return mdDatasetPaths;
	}

	public String getRegex() {
		return mRegex;
	}

	public boolean getSkipHead() {
		return mSkipHeaderLine;
	}

	public String getPartitionMethod() {
		return mPartitionMethod;
	}

	public int[] getKeyIdxs() {
		return mKeyIdx;
	}

	public int getNumPartition() {
		return mNumPartition;
	}

	public int getNumBukcet() {
		return mNumBucket;
	}
	
	public double getMinLon() {
		return mMinLon;
	}
	
	public double getMinLat() {
		return mMinLat;
	}
	
	public double getMaxLon() {
		return mMaxLon;
	}
	
	public double getMaxLat() {
		return mMaxLat;
	}
	
	public int getNumLonBucket() {
		return mNumLonBucket;
	}
	
	public int getNumLatBucket() {
		return mNumLatBucket;
	}
	
	public int getNumLonPar() {
		return mNumLonPar;
	}
	
	public int getNumLatPar() {
		return mNumLatPar;
	}

	public String getOperatorName() {
		return mOperatorName;
	}

	public double[] getDisGT() {
		return disGT;
	}

	public int[] getGT() {
		return GT;
	}
	
	public double getPostpondThreshold() {
		return mPostpondThreshold;
	}
	
	public double getErrorBound() {
		return mErrorBound;
	}
	
	public boolean boostProgress() {
		return mBoostProgress;
	}
	
	public HashSet<String> getFilterState() {
		return mFilterState;
	}
	public int getEstLevel() {
		return mEstLevel;
	}
	
	public String getSplitMethod() {
		return mSplitMethod;
	}
	
	public boolean getDoLowPar() {
		return doMultiPartition;
	}
	
	public int getNumLowPar() {
		return lowLevelNumPar;
	}
	
	public String getFrePath() {
		return frequencyPath;
	}
}
