package partition;

import java.util.Arrays;
import java.util.HashSet;

import dataStructure.StringTuple2;
import dataStructure.Tuple3;
import operator.EuqiJoinSelectivity;

public class MultiHashPartition {
	
//	private EuqiJoinSelectivity[] mSelectivityEstimation;
	boolean mDoSelectivityEstimation = false;
	
	private int[][] partitionSizeDS1;
	private int[][] partitionSizeDS2;
	private int numPar;
	private int lowNumPar;
	private int[][] fileTotalLines;
	
	private EuqiJoinSelectivity[][] multiSelectivityEstimation;
	
	public MultiHashPartition(int numPar, int lowNumPar) {
		this.numPar = numPar;
		this.lowNumPar = lowNumPar;
		partitionSizeDS1 = new int[numPar][lowNumPar];
		partitionSizeDS2 = new int[numPar][lowNumPar];
	}
	
	
	public void setFileTotalLines(int[][] fileTotalLines) {
		this.fileTotalLines = fileTotalLines;
	}
	
	public int[][] getFileTotalLines(){
		return fileTotalLines;
	} 
	
	public void addKey(int dsId, StringTuple2 r, int keyIdx) {
		int parId = Math.abs(r.computeHashkey(keyIdx)) % numPar;
		int lowParId =Math.abs(r.computeHashkey(keyIdx)) / numPar % numPar;
		if (dsId == 0) {
			partitionSizeDS1[parId][lowParId] += 1;
			multiSelectivityEstimation[parId][lowParId].addRecord(r);
		} else {
			partitionSizeDS2[parId][lowParId] += 1;
			multiSelectivityEstimation[parId][lowParId].addRecordSecond(r);
		}
	}
	
	public void enableSelectivityEstimation(HashSet<String> filterStates) {
		mDoSelectivityEstimation = true;
		multiSelectivityEstimation = new EuqiJoinSelectivity[numPar][lowNumPar];
		for (int i=0; i<numPar; i++) {
			for (int j=0; j<lowNumPar; j++) {
				multiSelectivityEstimation[i][j] = new EuqiJoinSelectivity(filterStates);
			}
		}
		
	}
	
	public int[][] getSelectivityEstimaiton() {
		int[][] estimation = new int[numPar][lowNumPar];
		for (int i=0; i<numPar; i++) {
			for (int j=0; j<lowNumPar; j++) {
				estimation[i][j] = multiSelectivityEstimation[i][j].getSelectivity();
				System.out.print(estimation[i][j] + ", ");
				multiSelectivityEstimation[i][j].clear();
			}
			System.out.println();
			
		}
		return estimation;
	}

}
