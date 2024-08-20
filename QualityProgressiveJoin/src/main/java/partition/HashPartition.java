package partition;

import java.util.HashSet;

import dataStructure.StringTuple2;
import dataStructure.Tuple3;
import operator.EuqiJoinSelectivity;

public class HashPartition {
	
	private EuqiJoinSelectivity[] mSelectivityEstimation;
	boolean mDoSelectivityEstimation = false;
	
	private int[][] partitionSize;
	private int numPar;
	private int[][] fileTotalLines;
	
	public HashPartition(int numPar) {
		this.numPar = numPar;
		partitionSize = new int[2][numPar];
	}
	
	
	public void setFileTotalLines(int[][] fileTotalLines) {
		this.fileTotalLines = fileTotalLines;
	}
	
	public int[][] getFileTotalLines(){
		return fileTotalLines;
	} 
	
	public void addKey(int dsId, StringTuple2 r, int keyIdx) {
		int parId = Math.abs(r.computeHashkey(keyIdx)) % numPar;
		partitionSize[dsId][parId] += 1;
		if (mDoSelectivityEstimation) {
			if (dsId ==0) {
				mSelectivityEstimation[parId].addRecord(r);
			} else {
				mSelectivityEstimation[parId].addRecordSecond(r);
			}
		}
	}
	
	public void addKey(int dsId, Tuple3 r, int keyIdx) {
		int parId = Math.abs(r.computeHashkey(keyIdx)) % numPar;
		partitionSize[dsId][parId] += 1;
	}
	
	public void addKeyMulti(int dsId, StringTuple2 r, int keyIdx, int BigPar) {
		int parId = Math.abs(r.computeHashkey(keyIdx)) / BigPar % numPar;
		partitionSize[dsId][parId] += 1;
	}
	
	public void addKeyMulti(int dsId, Tuple3 r, int keyIdx, int BigPar) {
		int parId = Math.abs(r.computeHashkey(keyIdx)) / BigPar % numPar;
		partitionSize[dsId][parId] += 1;
	}
	
	public int[][] getPartitionFrequency(){
		return this.partitionSize;
	}
	
	public void enableSelectivityEstimation(HashSet<String> filterStates) {
		mDoSelectivityEstimation = true;
		mSelectivityEstimation = new EuqiJoinSelectivity[numPar];
		for (int i=0; i<numPar; i++) {
			mSelectivityEstimation[i] = new EuqiJoinSelectivity(filterStates);
		}
		
	}
	
	public int[] getSelectivityEstimaiton() {
		int[] estimation = new int[numPar];
		for (int i=0; i<numPar; i++) {
			estimation[i] = mSelectivityEstimation[i].getSelectivity();
			mSelectivityEstimation[i].clear();
		}
		return estimation;
	}

}
