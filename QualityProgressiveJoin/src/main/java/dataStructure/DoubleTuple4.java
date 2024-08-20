package dataStructure;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import partition.GridPartition;

public class DoubleTuple4 implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8997499866092983118L;
	
	private HashSet<Integer> mParIds;
	private HashSet<Integer> mBucketIds;
//	private static final int SIZE = 4;
	private double[] mValue = new double[4];
	
	public DoubleTuple4(double minLon, double minLat, double maxLon, double maxLat) {
		this.mValue[0] = minLon;
		this.mValue[1] = minLat;
		this.mValue[2] = maxLon;
		this.mValue[3] = maxLat;
		
	}
	
	public DoubleTuple4(double minLon, double minLat, double maxLon, double maxLat, GridPartition gp) {
		this.mValue[0] = minLon;
		this.mValue[1] = minLat;
		this.mValue[2] = maxLon;
		this.mValue[3] = maxLat;
		
		HashSet<Integer> overlapBIdPIds = gp.overlapBIdsPIds(minLon, minLat, maxLon, maxLat);
//		this.mBucketIds = overlapBIdPIds.get(0);
		this.mParIds = overlapBIdPIds;
	}
	
	public double[] toArray() {
		return this.mValue;
	}
	
	public HashSet<Integer> getParId() {
		return this.mParIds;
	}
	
	public HashSet<Integer> getBucketId() {
		return this.mBucketIds;
	}

	// return size of the tuple
	public int getSize() {
		return 4;
	}

	@Override
	public final String toString() {
		return Arrays.toString(this.mValue);
	}
}
