package operator;

import java.util.HashSet;

import dataStructure.StringTuple2;

/**
 * From Database systems: the complete book.
 * 
 * Given input S and R, query Q
 * 
 * 1. filter the input with target state and get count 2. get distinct count for
 * join's predicate
 * 
 * @author xin_aurora
 *
 */
public class EuqiJoinSelectivity {

	private int mDSCountFirst = 0;
	private int mDSCountSecond = 0;
	private HashSet<String> mPredicateFirst = new HashSet<String>();
	private HashSet<String> mPredicateSecond = new HashSet<String>();

	private HashSet<String> mFilterStates = new HashSet<String>();

	private int mSelectivityEstimation = -1;

	public EuqiJoinSelectivity(HashSet<String> filterStates) {
		this.mFilterStates = filterStates;
	}

	// filter the target state for location set
	public void addRecord(StringTuple2 r) {
		if (mFilterStates.contains(r.toArray()[1])) {
			mDSCountFirst += 1;

			mPredicateFirst.add(r.toArray()[0]);
		} else if (mFilterStates.size() == 0) {
			mDSCountFirst += 1;

			mPredicateFirst.add(r.toArray()[0]);
		}
	}

	public void addRecordSecond(StringTuple2 r) {
		mDSCountSecond += 1;
		mPredicateSecond.add(r.toArray()[0]);
	}

	private void estimation() {
		System.out.println("mDSCountSecond = " + mDSCountSecond + ", mPredicateFirst.size() = " + mPredicateFirst.size() + 
				", mPredicateSecond.size() = " + mPredicateSecond.size());
		double predict = Math.max(mPredicateFirst.size(), mPredicateSecond.size());
		if (predict == 0) {
			mSelectivityEstimation = 0;
		} else {
			mSelectivityEstimation = mDSCountSecond	/ Math.max(mPredicateFirst.size(), mPredicateSecond.size());
			mSelectivityEstimation = mDSCountFirst * mSelectivityEstimation;
		}
//		mSelectivityEstimation = mDSCountSecond	/ Math.max(mPredicateFirst.size(), mPredicateSecond.size());
//		mSelectivityEstimation = mDSCountFirst * mSelectivityEstimation;
	}

	public int getSelectivity() {
		estimation();
		return mSelectivityEstimation;
	}
	
	public void clear() {
		mPredicateFirst = new HashSet<String>();
		mPredicateSecond = new HashSet<String>();
	}

}
