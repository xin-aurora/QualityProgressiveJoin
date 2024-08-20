package dataStructure;

public class CellCellOverlapInfo {

	public boolean overlap = false;
	public double mWo = 0.0;
	public double mHo = 0.0;
	public double moX1 = 0.0;
	public double moY1 = 0.0;
	public double mWs = 0.0;
	public double mHs = 0.0;
	public double mWm = 0.0;
	public double mHm = 0.0;
	public double msX1 = 0.0;
	public double msY1 = 0.0;
	public double mExpW = 0.0;
	public double mExpH = 0.0;
	public double mExpX1 = 0.0;
	public double mExpY1 = 0.0;
	
	public int mNumObj = 0;

	public double mComAreaSource = 0;
	public double mComAreaTarget = 0;
	public double mComHTarget = 0;
	public double mComVTarget = 0;
	
	// back flag
	public boolean backSrcDimension = false;

//	public CellCellOverlapInfo(double tMinLon, double tMinLat, double tMaxLon, double tMaxLat, double tLonUnit,
//			double tLatUnit, GHCellLearn src, double sLonUnit, double sLatUnit) {
//
////		double tMinLon = tar.mMinLon;
////		double tMaxLon = tar.mMaxLon;
////		double tMinLat = tar.mMinLat;
////		double tMaxLat = tar.mMaxLat;
//
//		double sMinLon = src.mMinLon;
//		double sMaxLon = src.mMaxLon;
//		double sMinLat = src.mMinLat;
//		double sMaxLat = src.mMaxLat;
//
//		double minLonInt = 0.0;
//		double maxLonInt = 0.0;
//		double minLatInt = 0.0;
//		double maxLatInt = 0.0;
//
//		if (tMinLon < sMinLon) {
//			if (tMaxLon <= sMinLon) {
//				// tMinLon < tMaxLon <= sMinLon, no overlap
//				return;
//			}
//			minLonInt = sMinLon;
//			if (tMaxLon < sMaxLon) {
//				maxLonInt = tMaxLon;
//				// lat
//				if (tMinLat < sMinLat) {
//					if (tMaxLat <= sMinLat) {
//						// tMinLat < tMaxLat <= sMinLat, no overlap
//						return;
//					}
//					overlap = true;
//					minLatInt = sMinLat;
//					if (tMaxLat < sMaxLat) {
//						maxLatInt = tMaxLat;
//					} else {
//						maxLatInt = sMaxLat;
//					}
//
//				} else {
//					// sMinLat < tMinLat
//					if (sMaxLat <= tMinLat) {
//						// sMinLat < sMaxLat <= tMinLat
//						return;
//					}
//					// sMinLat < tMinLat
//					overlap = true;
//					minLatInt = tMinLat;
//					if (tMaxLat < sMaxLat) {
//						maxLatInt = tMaxLat;
//					} else {
//						maxLatInt = sMaxLat;
//					}
//				}
//			} else {
//				// sMaxLon < tMaxLon
//				maxLonInt = sMaxLon;
//				// lat
//				if (tMinLat < sMinLat) {
//					if (tMaxLat <= sMinLat) {
//						// tMinLat < tMaxLat <= sMinLat, no overlap
//						return;
//					}
//					overlap = true;
//					minLatInt = sMinLat;
//					if (tMaxLat < sMaxLat) {
//						maxLatInt = tMaxLat;
//					} else {
//						maxLatInt = sMaxLat;
//					}
//
//				} else {
//					// sMinLat < tMinLat
//					if (sMaxLat <= tMinLat) {
//						// sMinLat < sMaxLat <= tMinLat
//						return;
//					}
//					// sMinLat < tMinLat
//					overlap = true;
//					minLatInt = tMinLat;
//					if (tMaxLat < sMaxLat) {
//						maxLatInt = tMaxLat;
//					} else {
//						maxLatInt = sMaxLat;
//					}
//				}
//			}
//
//		} else {
//			if (sMaxLon <= tMinLon) {
//				// sMinLon < sMaxLon <= tMinLon
//				return;
//			}
//			minLonInt = tMinLon;
//			if (tMaxLon < sMaxLon) {
//				maxLonInt = tMaxLon;
//				// lat
//				if (tMinLat < sMinLat) {
//					if (tMaxLat <= sMinLat) {
//						// tMinLat < tMaxLat <= sMinLat, no overlap
//						return;
//					}
//					overlap = true;
//					minLatInt = sMinLat;
//					if (tMaxLat < sMaxLat) {
//						maxLatInt = tMaxLat;
//					} else {
//						maxLatInt = sMaxLat;
//					}
//
//				} else {
//					// sMinLat < tMinLat
//					if (sMaxLat <= tMinLat) {
//						// sMinLat < sMaxLat <= tMinLat
//						return;
//					}
//					// sMinLat < tMinLat
//					overlap = true;
//					minLatInt = tMinLat;
//					if (tMaxLat < sMaxLat) {
//						maxLatInt = tMaxLat;
//					} else {
//						maxLatInt = sMaxLat;
//					}
//				}
//			} else {
//				// sMaxLon < tMaxLon
//				maxLonInt = sMaxLon;
//				// lat
//				if (tMinLat < sMinLat) {
//					if (tMaxLat <= sMinLat) {
//						// tMinLat < tMaxLat <= sMinLat, no overlap
//						return;
//					}
//					overlap = true;
//					minLatInt = sMinLat;
//					if (tMaxLat < sMaxLat) {
//						maxLatInt = tMaxLat;
//					} else {
//						maxLatInt = sMaxLat;
//					}
//
//				} else {
//					// sMinLat < tMinLat
//					if (sMaxLat <= tMinLat) {
//						// sMinLat < sMaxLat <= tMinLat
//						return;
//					}
//					// sMinLat < tMinLat
//					overlap = true;
//					minLatInt = tMinLat;
//					if (tMaxLat < sMaxLat) {
//						maxLatInt = tMaxLat;
//					} else {
//						maxLatInt = sMaxLat;
//					}
//				}
//			}
//
//		}
//
//		// compute values
//		mWo = maxLonInt - minLonInt;
//		mHo = maxLatInt - minLatInt;
//		if (mWo < 1.1102230246251565E-10 || mHo <  1.1102230246251565E-10) {
//			overlap = false;
//			return;
//		}
//		mWs = sLonUnit;
//		mHs = sLatUnit;
//		mWm = tLonUnit;
//		mHm = tLatUnit;
//		moX1 = minLonInt;
//		moY1 = minLatInt;
//		msX1 = src.mMinLon;
//		msY1 = src.mMinLat;
//		mNumObj = src.mNumObj;
////		System.out.println("Overlap area = " + minLonInt + "-" + maxLonInt + ", " + minLatInt + "-" + maxLatInt);
//		mComAreaSource = (mWo * mHo) / (sLonUnit * sLatUnit);
//		mExpW = (src.mHFull + src.mHPartial) / src.mNumObj;
//		mExpH = (src.mVFull + src.mVPartial) / src.mNumObj;
//		// if uniform distribution, then expX1 = (ox1 + sLonUnit - mExpW - ox1) / 2
//		mExpX1 = (sLonUnit - mExpW) / 2;
//		// if uniform distribution, then expY1 = (oy1 + sLatUnit - mExpH - ox2) / 2
//		mExpY1 = (sLatUnit - mExpH) / 2;
//		
//	}
	
	public CellCellOverlapInfo(double tMinLon, double tMinLat, double tMaxLon, double tMaxLat, double tLonUnit,
			double tLatUnit, double sMinLon, double sMinLat, double sMaxLon, double sMaxLat, double sLonUnit,
			double sLatUnit) {
		if (tMaxLon <= sMinLon || sMaxLon <= tMinLon || tMaxLat <= sMinLat || sMaxLat <= tMinLat) {
			return;
		} else {

			double overMinLon = Math.max(tMinLon, sMinLon);
			double overMaxLon = Math.min(tMaxLon, sMaxLon);
			
			double overMinLat = Math.max(tMinLat, sMinLat);
			double overMaxLat =  Math.min(tMaxLat, sMaxLat);
			// compute values
//			mWo = Math.min(tMaxLon, sMaxLon) - Math.max(tMinLon, sMinLon);
			mWo = overMaxLon - overMinLon;
//			mHo = Math.min(tMaxLat, sMaxLat) - Math.max(tMinLat, sMinLat);
			mHo = overMaxLat - overMinLat;
			if (mWo < 1.1102230246251565E-10 || mHo < 1.1102230246251565E-10) {
				return;
			}
			
			if (sMinLon <= tMaxLon && tMaxLon < sMaxLon) {
				backSrcDimension = true;
			}

			overlap = true;
			
			// compute values
			double tmp = mWo * mHo;
			mComAreaSource = tmp / sLonUnit / sLatUnit;
			mComAreaTarget = tmp / tLonUnit / tLatUnit;
			mComHTarget = tmp / tLonUnit / sLatUnit;
			mComVTarget = tmp / tLatUnit / sLonUnit;
		}
	}

	@Override
	public final String toString() {
		return "overlap = " + overlap + ", mComAreaSource = " + mComAreaSource + ", mComAreaTarget " + mComAreaTarget
				+ ", mComHTarget = " + mComHTarget + ", mComVTarget = " + mComVTarget;
	}
}
